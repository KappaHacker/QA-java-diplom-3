import io.restassured.RestAssured;
import org.example.constant.Browser;
import org.example.page_object.MainPage;
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
public class ConstructorTest {
    WebDriver driver;
    MainPage mainPage;
    Browser browserEnum;


    public ConstructorTest(Browser browserEnum) {
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
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        this.driver = DriverInitializer.getDriver(browserEnum);

        driver.get(BASE_TEST_URL);
        this.mainPage = new MainPage(driver);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
    }

    @After
    public void shutdown() {
        driver.quit();
    }

    @Test
    public void clickOnBunsSectionButtonAutoScroll() {
        mainPage.clickOnFillingsSectionButton();
        mainPage.clickOnBunsSectionButton();
        boolean isSelected = mainPage.isSectionButtonSelected(mainPage.getBunsSectionButton());
        Assert.assertTrue("Переход к разделу Булки не выполнен", isSelected);
    }

    @Test
    public void clickOnSousesSectionButtonAutoScroll() {
        mainPage.clickOnSousesSectionButton();
        boolean isSelected = mainPage.isSectionButtonSelected(mainPage.getSousesSectionButton());
        Assert.assertTrue("Переход к разделу Соусы не выполнен", isSelected);
    }

    @Test
    public void clickOnFillingsSectionButtonAutoScroll() {
        mainPage.clickOnFillingsSectionButton();
        boolean isSelected = mainPage.isSectionButtonSelected(mainPage.getFillingsSectionButton());
        Assert.assertTrue("Переход к разделу Начинки не выполнен", isSelected);
    }
}

