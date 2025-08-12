package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.junit.Assert;
import serializers.Products;
import utils.LogHelper;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import java.util.List;

public class Cart extends BasePage {

    private final Locator prodCart;

    private final Locator totalCart;

    private final Locator actions;

    private final Locator emptyCartMsg;

    private final Locator checkoutBtn;

    public Cart(Page page) {
        super(page);
        this.prodCart = this.page.locator("div#shoppingCart tr[class=\"ng-scope\"]");
        this.totalCart = this.page.locator("div#mobileShoppingCart tfoot span.cart-total");
        this.actions = this.page.locator("span.actions");
        this.emptyCartMsg = this.page.locator("label[translate=\"Your_shopping_cart_is_empty\"]").first();
        this.checkoutBtn = this.page.locator("button[id=\"checkOutButton\"]");
    };

    public void cart(){
        this.page.navigate("/#/shoppingCart");

        this.page.getByText("SHOPPING CART", new Page.GetByTextOptions().setExact(true)).first().waitFor(
                new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE)
        );

        try{
            this.prodCart.first().waitFor(
                    new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(5000)
            );
        }catch (Exception e){
            LogHelper.info(e.getMessage());
        }
    };

    public void removeAllProducts(List<Products> products){

        for(Products p : products){
            this.prodCart.getByText(
                    p.getProduto()
            ).waitFor(
                    new Locator.WaitForOptions().setState(
                            WaitForSelectorState.VISIBLE
                    )
            );
        }

        this.actions.last().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));

        List<Locator> removeBtns = this.actions.locator("a[data-ng-click*=\"remove\"]").all();

        removeBtns.getLast().last().waitFor(
                new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(10000)
        );

        for(Locator removeBtn : removeBtns.stream().toList().reversed()){
            removeBtn.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            removeBtn.click();
        };

    };

    public void editProduct(String productName){
       Locator editButton =  this.prodCart.filter(
                new Locator.FilterOptions().setHasText(productName)
        ).locator("span.actions a[class*=\"edit\"]");

       System.out.println(editButton);

        editButton.click();
    }

    public void verifyProductsOnCart(List<Products> expectedProducts){

        for(Products product: expectedProducts){
            String expectedName = product.getProduto().toUpperCase();
            String expectedColor = product.getCor();
            Integer expectedQuantity = product.getQuantidade();
            String valor = Double.toString(product.getQuantidade()*product.getValor());

            Locator matchingItem = this.prodCart.filter(new Locator.FilterOptions().setHasText(expectedName));
            assertThat(matchingItem).hasCount(1);

            Locator productRow = matchingItem.first();

            //Verifica o Nome do Produto
            assertThat(productRow.locator(".productName")).hasText(expectedName);

            //verifica a quantidade do Produto
            assertThat(productRow.locator("td.smollCell.quantityMobile label.ng-binding")).hasText(Integer.toString(expectedQuantity));

            Assert.assertEquals(
                    productRow.locator("p.price").textContent().trim().replace("$",""),
                    valor
            );

            //verifica a Cor do Produto
            Locator colorSpan = productRow.locator("span.productColor");
            Assert.assertTrue(colorSpan.getAttribute("style").contains(expectedColor));
        }

    };

    public void verifySumProducts(List<Products> expectedProducts){
        Double sumProducts = expectedProducts.stream().map(getProduto->getProduto.getValor()*getProduto.getQuantidade()).reduce(0.0,Double::sum);
        String totalCart = this.totalCart.textContent().trim().replace("$","");

        assertThat(this.totalCart).containsText(sumProducts.toString());

    };

    public void checkEmptyCardMessage(String message){
        this.emptyCartMsg.waitFor(
                new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE)
        );

        assertThat(this.emptyCartMsg).hasText(message);
    }

    public void checkout(){
        this.checkoutBtn.waitFor(
                new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE)
        );
        this.checkoutBtn.click();
    }
}
