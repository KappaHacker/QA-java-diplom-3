package org.example.util;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.example.constant.Browser;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;


public class DriverInitializer {
    public static WebDriver getDriver(Browser browser) {
        switch (browser) {
            case CHROME:
                WebDriverManager.chromedriver().setup();
                return new ChromeDriver();

            case YANDEX:
                System.setProperty("webdriver.chrome.driver",
                        "src/main/resources/yandexdriver.exe");
                return new ChromeDriver();

            default:
                throw new EnumConstantNotPresentException(Browser.class, "BROWSER");
        }
    }
}
