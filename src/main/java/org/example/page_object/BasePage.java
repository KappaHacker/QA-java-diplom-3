package org.example.page_object;

import org.openqa.selenium.WebDriver;

public class BasePage {
    public WebDriver driver;
    public static final String urlDestination = "https://stellarburgers.nomoreparties.site";

    public BasePage(WebDriver webDriver) {
        this.driver = webDriver;
    }
    public BasePage() {
    }
}
