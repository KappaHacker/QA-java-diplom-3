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
public class LogoutTest {
    WebDriver driver;
    MainPage mainPage;
    LoginPage loginPage;
    AccountPage accountPage;
    Browser browserEnum;
    RegisterRequest testUser;
    String accessToken;
    SignInRequest signInRequest;

    public LogoutTest(Browser browserEnum) {
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
        testUser = UserUtils.getUniqueUser();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        SuccessSignInSignUpResponse signUpResponse = UserStep.createUniqueUser(testUser)
                .then()
                .statusCode(200)
                .extract()
                .as(SuccessSignInSignUpResponse.class);
        accessToken = signUpResponse.getAccessToken();
        signInRequest = new SignInRequest(testUser.getEmail(), testUser.getPassword());

        driver = DriverInitializer.getDriver(browserEnum);
        mainPage = new MainPage(driver);
        loginPage = new LoginPage(driver);
        accountPage = new AccountPage(driver);
        driver.get(BASE_TEST_URL + "/login");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
    }

    @After
    public void shutdown() {
        driver.quit();
    }

    @Test
    @DisplayName("Выход по кнопке Выйти в личном кабинете")
    public void logoutWithLogoutButtonSuccess() {
        loginPage.loginWithCredentials(signInRequest);
        mainPage.clickAccountButton();
        accountPage.clickLogoutButton();
        mainPage.clickAccountButton();

        boolean displayed = loginPage.getSignInButton().isDisplayed();
        Assert.assertTrue("Выход из личного кабинета не выполнен", displayed);
    }
}
