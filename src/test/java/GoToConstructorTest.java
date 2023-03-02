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
public class GoToConstructorTest {
    WebDriver driver;
    MainPage mainPage;
    LoginPage loginPage;
    AccountPage accountPage;
    Browser browserEnum;

    public GoToConstructorTest(Browser browserEnum) {
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

        mainPage = new MainPage(driver);
        loginPage = new LoginPage(driver);
        accountPage = new AccountPage(driver);
        driver.get(BASE_TEST_URL);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
    }

    @After
    public void shutdown() {
        driver.quit();
    }

    @Test
    @DisplayName("Успешный переход в конструктор из аккаунта")
    public void goToConstructorFromAccountSuccess() {
        RegisterRequest user = UserUtils.getUniqueUser();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        SuccessSignInSignUpResponse signUpResponse = UserStep.createUniqueUser(user)
                .then()
                .statusCode(200)
                .extract()
                .as(SuccessSignInSignUpResponse.class);

        mainPage.clickAccountButton();
        loginPage.loginWithCredentials(new SignInRequest(user.getEmail(), user.getPassword()));
        mainPage.clickAccountButton();
        accountPage.clickGoToConstructorButton();

        boolean displayed = mainPage.getBurgerConstructorHeader().isDisplayed();
        Assert.assertTrue("Конструктор не открыт", displayed);

        UserStep.deleteUser(signUpResponse.getAccessToken());
    }
}

