import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.example.api_step.UserStep;
import org.example.constant.Browser;
import org.example.page_object.*;
import org.example.pojo.RegisterRequest;
import org.example.pojo.SignInRequest;
import org.example.pojo.SuccessSignInSignUpResponse;
import org.example.util.DriverInitializer;
import org.example.util.UserUtils;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;


import java.time.Duration;

import static org.example.constant.BaseConstant.BASE_TEST_URL;

@RunWith(Parameterized.class)
public class LoginTest {
    WebDriver driver;
    MainPage mainPage;
    LoginPage loginPage;
    AccountPage accountPage;
    Browser browserEnum;
    RegisterRequest testUser;
    String accessToken;
    SignInRequest signInRequest;

    public LoginTest(Browser browserEnum) {
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

        this.driver = DriverInitializer.getDriver(browserEnum);

        driver.get(BASE_TEST_URL);
        this.mainPage = new MainPage(driver);
        this.loginPage = new LoginPage(driver);
        this.accountPage = new AccountPage(driver);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
    }


    @After
    public void closeDriver() {
        driver.quit();
        UserStep.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Вход по кнопке Войти в аккаунт на главной")
    public void signInWithValidDataWithSignInButtonSuccess() {
        mainPage.clickSignInButton();
        loginPage.loginWithCredentials(signInRequest);
        mainPage.clickAccountButton();

        boolean displayed = accountPage.getProfileButton().isDisplayed();
        Assert.assertTrue("Вход в личный кабинет не выполнен", displayed);
    }

    @Test
    @DisplayName("Вход через кнопку Личный кабинет")
    public void signInWithValidDataWithAccountButtonSuccess() {
        mainPage.clickAccountButton();
        loginPage.loginWithCredentials(signInRequest);
        mainPage.clickAccountButton();

        boolean displayed = accountPage.getProfileButton().isDisplayed();
        Assert.assertTrue("Вход в личный кабинет не выполнен", displayed);
    }

    @Test
    @DisplayName("Вход через кнопку в форме регистрации")
    public void signInWithValidDataFromSignUpFormSuccess() {
        mainPage.clickSignInButton();
        loginPage.clickSignUpButton();
        SignUpPage signUpPage = new SignUpPage(driver);
        signUpPage.clickSignInButton();
        loginPage.loginWithCredentials(signInRequest);
        mainPage.clickAccountButton();

        boolean displayed = accountPage.getProfileButton().isDisplayed();
        Assert.assertTrue("Вход в личный кабинет не выполнен", displayed);
    }

    @Test
    @DisplayName("Вход через кнопку в форме восстановления пароля")
    public void signInWithValidDataFromPasswordRecoverFormSuccess() {
        mainPage.clickSignInButton();
        loginPage.clickRecoverPasswordButton();
        ForgotPasswordPage forgotPasswordPage = new ForgotPasswordPage(driver);
        forgotPasswordPage.clickSignInButton();
        loginPage.loginWithCredentials(signInRequest);
        mainPage.clickAccountButton();

        boolean displayed = accountPage.getProfileButton().isDisplayed();
        Assert.assertTrue("Вход в личный кабинет не выполнен", displayed);
    }
}
