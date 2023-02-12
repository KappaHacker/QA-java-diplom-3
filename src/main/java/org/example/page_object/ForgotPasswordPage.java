package org.example.page_object;

import lombok.Data;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

@Data
public class ForgotPasswordPage extends BasePage{
    WebDriver driver;
    @FindBy(xpath = "//input[@name='name']")
    WebElement emailField;
    @FindBy(xpath = "//button[text()='Восстановить']")
    WebElement recoverButton;
    @FindBy(xpath = "//a[@href='/login']")
    WebElement signInButton;

    public ForgotPasswordPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    public void enterEmail(String email) {
        emailField.sendKeys(email);
    }

    public void clickRecoverButton() {
        recoverButton.click();
    }

    public void clickSignInButton() {
        signInButton.click();
    }
}