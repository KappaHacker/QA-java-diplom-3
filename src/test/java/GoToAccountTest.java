import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.example.api_step.UserStep;
import org.example.constant.Browser;
import org.example.page_object.AccountPage;
import org.example.page_object.LoginPage;
import org.example.page_object.MainPage;
import org.example.pojo.RegisterRequest;
import org.example.pojo.SignInRequest;
import org.example.pojo.SuccessSignInSignUpResponse;
import org.example.util.DriverInitializer;
import org.example.util.UserUtils;
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
public class GoToAccountTest {
    WebDriver driver;
    MainPage mainPage;
    LoginPage loginPage;
    AccountPage accountPage;
    Browser browserEnum;

    public GoToAccountTest(Browser browserEnum) {
        this.browserEnum = browserEnum;
    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][]{
                {Browser.CHROME},
                {Browser.YANDEX}
        };
    }

    @Before
    public void init() {
        driver = DriverInitializer.getDriver(browserEnum);
        driver.get(BASE_TEST_URL);
        mainPage = new MainPage(driver);
        accountPage = new AccountPage(driver);
        loginPage = new LoginPage(driver);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
    }

    @After
    public void shutdown() {
        driver.quit();
    }

    @Test
    @DisplayName("Успешный переход по клику на Личный кабинет")
    public void goToAccountWithAccountButtonSuccess() {
       RegisterRequest user = UserUtils.getUniqueUser();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        String accessToken = UserStep.createUniqueUser(user)
                .then()
                .statusCode(200)
                .extract()
                .as(SuccessSignInSignUpResponse.class)
                .getAccessToken();

        mainPage.clickAccountButton();
        loginPage.loginWithCredentials(new SignInRequest(user.getEmail(), user.getPassword()));
        mainPage.clickAccountButton();

        boolean displayed = accountPage.getProfileButton().isDisplayed();
        Assert.assertTrue("Личный кабинет не открыт", displayed);

        UserStep.deleteUser(accessToken);
    }
}

