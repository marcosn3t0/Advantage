package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

public class ProductDetails extends BasePage {

    private final Locator quantityField;

    private final Locator plusQuantity;

    private final Locator addCartButton;

    private final Locator cartToolTip;

    public ProductDetails(Page page) {
        super(page);
        quantityField = this.page.locator("input[name='quantity']");
        plusQuantity = this.page.locator("div.plus");
        addCartButton = this.page.locator("button[name='save_to_cart']");
        cartToolTip = this.page.locator("tool-tip-cart[id]");
    };

    public void selectColor(String color){
        Locator selectedColor = this.page.locator(String.format("div[ng-show=\"firstImageToShow\"] span[style*=\"%s\"]",color));
        selectedColor.click();
    };

    public void editColor(String color){
        Locator selectedColor = this.page.locator(String.format("div[ng-show=\"!firstImageToShow\"] span[style*=\"%s\"]",color));
        selectedColor.click();
    };

    public void selectQuantity(Integer quantity){

        for(int i=Integer.parseInt(quantityField.inputValue());i<quantity;i++){
            plusQuantity.click();
        }
    }

    public void addCart(){
        this.addCartButton.click();
        this.cartToolTip.waitFor(
                new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE)
        );
    }


}
