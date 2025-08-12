package pages;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Response;
import com.microsoft.playwright.options.WaitForSelectorState;
import serializers.CartResponse;
import utils.Config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HomePage extends BasePage {

    private final Locator cartIcon;

    private final Locator loginIcon;

    private final Locator username;

    private final Locator pass;

    private final Locator loginBtn;

    private final Locator searchIcon;

    private final Locator placeholder;

    private final Locator userLink;

    public HomePage(Page page) {
        super(page);
        this.cartIcon = this.page.locator("a#shoppingCartLink");
        this.loginIcon = this.page.locator("svg#menuUser");
        this.username = this.page.locator("input[name='username']");
        this.pass = this.page.locator("input[name='password']");
        this.loginBtn = this.page.locator("button#sign_in_btn");
        this.searchIcon = this.page.getByTitle("SEARCH");
        this.placeholder = this.page.getByPlaceholder("Search AdvantageOnlineShopping.com");
        this.userLink = this.page.locator("a#menuUserLink");
    };

    public void homePage(){
        this.page.navigate(Config.getBaseUrl());
        this.waitForLoaderPopUp();
    };

    public void searchProduct(String productName){
        this.searchIcon.click();
        this.placeholder.fill(productName);
        this.placeholder.press("Enter");

    };

    public void selectProduct(String productName){
        Locator p = this.page.locator(String.format("//p[text()='%s']", productName.toUpperCase()));
        p.click();
    };

    public void cartButton(){
        this.cartIcon.click();
    };

    public String login(
            String username,
            String password,
            Integer id
    ){
        String sessionId;

        this.loginIcon.click();

        this.username.waitFor(
                new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE)
        );

        this.username.fill(username);
        this.pass.fill(password);

        sessionId = this.getSessionId(id);

        this.waitForLoaderPopUp();

        this.userLink.filter(
                new Locator.FilterOptions().setHasText(username)
        ).waitFor(
                new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE)
                        .setTimeout(10000)
        );

        return sessionId;
    };

    public String getSessionId(Integer userId) {

        Gson gson = new Gson();

        Map<String,String> map = new HashMap<>();

        Response response = this.page.waitForResponse(
                res -> res.url().contains("/order/api/v1/carts/" + userId) && res.status() == 200,
                this.loginBtn::click
        );

        String jsonString = response.text();

        CartResponse cart = gson.fromJson(jsonString, CartResponse.class);

        return cart.getSessionId();
    }

}
