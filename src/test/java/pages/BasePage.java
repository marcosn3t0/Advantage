package pages;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.Cookie;
import com.microsoft.playwright.options.WaitForSelectorState;
import utils.Config;

import java.nio.file.Paths;
import java.util.List;

public class BasePage {

    protected Page page;

    protected BrowserContext browserContext;

    public BasePage(Page page) {
        this.page = page;
        this.browserContext = null;
    }

    public BasePage(Page page, BrowserContext context) {
        this.page = page;
        this.browserContext = context;
    };

    protected void waitForLoaderPopUp(){
        this.page.locator("div.loader").first().waitFor(
                new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN)
                        .setTimeout(10000)

        );
    };

    public void storeState(){
        this.page.context().storageState(
                new BrowserContext.StorageStateOptions().setPath(Paths.get(Config.getAuth()))
        );
    };



}
