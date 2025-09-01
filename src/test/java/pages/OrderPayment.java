package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.junit.Assert;
import serializers.CardInfo;
import serializers.Products;
import serializers.ShippingDetails;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class OrderPayment extends BasePage {

    private final Locator user;
    private final Locator orderPayment;
    private final Locator address;
    private final Locator city;
    private final Locator country;
    private final Locator phone;
    private final Locator state;
    private final Locator zipcode;
    private final Locator nextButton;
    private final Locator masterCreditSection;
    private final Locator mmSelection;
    private final Locator yearSelection;
    private final Locator cardHoldName;
    private final Locator payNow;
    private final Locator userShippingName;
    private final Locator shippingAddress;
    private final Locator shippingCity;
    private final Locator shippingState;
    private final Locator shippingLastDigit;
    private final Locator shippingOrderedDate;
    private final Locator shippingSubTotal;
    private final Locator shippingTax;
    private final Locator shippingTotal;

    public OrderPayment(Page page) {
        super(page);
        this.user = this.page.locator("img[alt=\"user\"] + label");
        this.orderPayment = this.page.locator("h3").getByText("ORDER PAYMENT");
        this.address = this.page.locator("label[data-ng-show*=\"user.address\"]");
        this.city =  this.page.locator("label[data-ng-show*=\"user.city\"]");
        this.country =  this.page.locator("label[data-ng-show*=\"country.name\"]");
        this.phone = this.page.locator("div[icon-phone] + label").first();
        this.state = this.page.locator("label[data-ng-show*=\"user.stateProvince\"]");
        this.zipcode = this.page.locator("label[data-ng-show*=\"user.zipcode\"]");
        this.nextButton = this.page.locator("div#userSection button[id=\"next_btn\"]").first();
        this.masterCreditSection = this.page.locator("div.masterCreditSeccion");
        this.mmSelection = this.page.locator("select[name=\"mmListbox\"]");
        this.yearSelection = this.page.locator("select[name=\"yyyyListbox\"]");
        this.cardHoldName = this.page.locator("input[name=\"cardholder_name\"]");
        this.payNow = this.page.locator("button#pay_now_btn_ManualPayment");
        this.userShippingName = this.page.locator("div[icon-user] + label");
        this.shippingAddress = this.page.locator("div.innerSeccion div.iconCss.iconHome + label");
        this.shippingCity = this.page.locator("div.innerSeccion div.iconCss.iconHome + label + label");
        this.shippingState = this.page.locator("div.innerSeccion div.iconCss.iconHome + label + label + label");
        this.shippingLastDigit = this.page.locator("a[data-ng-show] span").last();
        this.shippingOrderedDate = this.page.locator("//label[contains(text(),'Date ordered')]/a");
        this.shippingSubTotal = this.page.locator("//label[contains(text(),'Subtotal')]/a");
        this.shippingTax = this.page.locator("//label[contains(text(),'shipping')]/a");
        this.shippingTotal = this.page.locator("//label[contains(text(),'TOTAL')]/a");
    };

    public void checkAddressDetails(
            String username,
            ShippingDetails shipping
    ){

        this.orderPayment.waitFor(
                new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE)
        );

        assertThat(this.user).containsText(username);
        assertThat(this.address).containsText(shipping.getAddress());
        assertThat(this.city).containsText(shipping.getCity());
        assertThat(this.country).containsText(shipping.getCountry());
        assertThat(this.phone).containsText(shipping.getPhone());
        assertThat(this.state).containsText(shipping.getState());
        assertThat(this.zipcode).containsText(shipping.getZip());
    }

    public void nextButton(){
        this.nextButton.click();
    }

    public void checkMasterCreditSection(CardInfo card){

        String[] parts = card.getExpirationDate().split("/");
        String mm = parts.length > 0 ? parts[0] : "";
        String year = parts.length > 1 ? parts[1] : "";

        Locator lastDigits = this.masterCreditSection.locator("span[data-ng-repeat]").last();
        Locator editBtn = this.masterCreditSection.locator("label.edit");
        Locator cvvNumber = this.page.locator("input[name=\"cvv_number\"]");

        editBtn.waitFor(
                new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE)
        );

        Assert.assertEquals(
                lastDigits.textContent().trim(),
                Arrays.stream(card.getCardNumber().split(" ")).toList().getLast()
        );

        editBtn.click();

        Assert.assertEquals(cvvNumber.inputValue(), card.getCvv());

        String selectedText = this.mmSelection.evaluate("el => el.options[el.selectedIndex].text").toString();
        String selectedYearText = this.yearSelection.evaluate("el => el.options[el.selectedIndex].text").toString();

        Assert.assertEquals(selectedText,mm);
        Assert.assertEquals(selectedYearText,year);
        Assert.assertEquals(
                this.cardHoldName.inputValue(),
                card.getCardHolderName()
        );

    }

    public void payNowBtn(){
        this.payNow.click();
    }

    public void checkSummary(
            String userName,
            ShippingDetails shipping,
            CardInfo card,
            List<Products> products
    ){

        Double sumProducts = products.stream().map(getProduto->getProduto.getValor()*getProduto.getQuantidade()).reduce(0.0,Double::sum);
        String lastDigits = Arrays.stream(card.getCardNumber().split(" ")).toList().getLast();
        String[] currentListDate = LocalDate.now().toString().split("/");

        LocalDate myObj = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
        String formattedDate = myObj.format(formatter);

        this.shippingTax.waitFor(
                new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE)
        );

        Double shippingPrice = Double.parseDouble(this.shippingTax.textContent().trim().replace("$",""));


        assertThat(this.userShippingName).hasText(userName);
        assertThat(this.shippingAddress).hasText(shipping.getAddress());
        assertThat(this.shippingCity).hasText(shipping.getCity());
        assertThat(this.shippingState).hasText(shipping.getState());
        assertThat(this.shippingOrderedDate).hasText(formattedDate);

        Assert.assertEquals(
                this.shippingLastDigit.textContent().trim(),
                lastDigits
        );

        Assert.assertEquals(
                this.shippingSubTotal.textContent().trim().replace("$",""),
                sumProducts.toString()
        );

        Assert.assertEquals(
                this.shippingTotal.textContent().trim().replace("$",""),
                Double.toString((shippingPrice + sumProducts))
        );

    }

    public void checkMessageOnPaymentPage(String msg){
        Locator msgLocator = this.page.locator(String.format("//label[text()=\"%s\"]",msg));

        try{
            msgLocator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(10000));
            Assert.assertTrue(msgLocator.isVisible());
        }catch (Exception e){
            System.out.println("Não foi possível verificar visibilidade do elemento: " + e.getMessage());
        }

    }


}
