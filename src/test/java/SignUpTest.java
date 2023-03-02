import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.api_step.UserStep;
import org.example.constant.Browser;
import org.example.page_object.LoginPage;
import org.example.page_object.MainPage;
import org.example.page_object.SignUpPage;
import org.example.pojo.SignInRequest;
import org.example.pojo.SuccessSignInSignUpResponse;
import org.example.util.DriverInitializer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;


import java.time.Duration;

import static org.example.constant.BaseConstant.BASE_TEST_URL;

@RunWith(Parameterized.class)
public class SignUpTest {
    WebDriver driver;
    MainPage mainPage;
    LoginPage loginPage;
    SignUpPage signUpPage;
    Browser browserEnum;
    public SignUpTest(Browser browserEnum) {
        this.browserEnum = browserEnum;
    }

    @Before
    public void init() {
        driver = DriverInitializer.getDriver(browserEnum);
        driver.get(BASE_TEST_URL);
        this.mainPage = new MainPage(driver);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][]{
                {Browser.CHROME},
                {Browser.YANDEX}
        };
    }

    @After
    public void shutdown() {
        driver.quit();
    }

    @DisplayName("Успешная регистрация с корректными данными")
    @Test
    public void signUpWithValidDataSuccess() {
        String name = RandomStringUtils.randomAlphabetic(10);
        String email = RandomStringUtils.randomAlphanumeric(10, 12) + "@test.com";
        String password = RandomStringUtils.randomAlphanumeric(6, 12);

        mainPage.clickSignInButton();
        loginPage = new LoginPage(driver);
        loginPage.clickSignUpButton();
        signUpPage = new SignUpPage(driver);
        signUpPage.enterName(name);
        signUpPage.enterEmail(email);
        signUpPage.enterPassword(password);
        signUpPage.clickSignUpButton();

        boolean displayed = loginPage.getSignInButton().isDisplayed();
        Assert.assertTrue("Регистрация не выполнена", displayed);

        Response response = UserStep.signInWithSignInRequest(new SignInRequest(email, password));

        String accessToken = response
                .then()
                .statusCode(200)
                .extract()
                .as(SuccessSignInSignUpResponse.class).getAccessToken();

        UserStep.deleteUser(accessToken);
    }

    @DisplayName("Ошибка при регистрации - пароль меньше 6 символов")
    @Test
    public void signUpWithInvalidPasswordFails() {
        String name = RandomStringUtils.randomAlphabetic(10);
        String email = RandomStringUtils.randomAlphanumeric(10, 12) + "@test.com";
        String password = RandomStringUtils.randomAlphanumeric(5);

        mainPage.clickSignInButton();
        loginPage = new LoginPage(driver);
        loginPage.clickSignUpButton();
        signUpPage = new SignUpPage(driver);
        signUpPage.enterName(name);
        signUpPage.enterEmail(email);
        signUpPage.enterPassword(password);
        signUpPage.clickSignUpButton();

        boolean displayed = signUpPage.getPasswordErrorMessage().isDisplayed();

        if (!displayed) {
            Response response = UserStep.signInWithSignInRequest(new SignInRequest(email, password));

            if (response.getStatusCode() == 200) {
                String accessToken = response.then().extract().as(SuccessSignInSignUpResponse.class).getAccessToken();
                UserStep.deleteUser(accessToken);
            }
        }
        Assert.assertTrue("Не выведена ошибка о некорректном пароле", displayed);
    }
}