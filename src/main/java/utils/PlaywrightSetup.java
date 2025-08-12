package utils;

import com.microsoft.playwright.*;

public class PlaywrightSetup {
    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
            // Triggers browser download
            playwright.chromium().launch();
        }
    }
}
