package org.example.page_object;

import lombok.Data;
import org.example.pojo.SignInRequest;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


@Data
public class LoginPage extends BasePage{
    WebDriver driver;
    @FindBy(xpath = "//input[@name='name']")
    WebElement emailField;
    @FindBy(xpath = "//input[@type='password']")
    WebElement passwordField;
    @FindBy(xpath = "//button[text()='Войти']")
    WebElement signInButton;
    @FindBy(xpath = "//a[@href='/register']")
    WebElement signUpButton;
    @FindBy(xpath = "//a[@href='/forgot-password']")
    WebElement recoverPasswordButton;

    public LoginPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    public void clickSignUpButton() {
        signUpButton.click();
    }

    public void clickSignInButton() {
        signInButton.click();
    }

    public void enterPassword(String password) {
        passwordField.sendKeys(password);
    }

    public void enterEmail(String email) {
        emailField.sendKeys(email);
    }

    public void clickRecoverPasswordButton() {
        recoverPasswordButton.click();
    }

    public void loginWithCredentials(SignInRequest signInRequest) {
        enterEmail(signInRequest.getEmail());
        enterPassword(signInRequest.getPassword());
        clickSignInButton();
    }
}
