package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import pages.HomePage;
import pages.OrderPage;
import utils.OrderData;

import java.util.Arrays;
import java.util.Collection;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class OrderTest {
    private static final String URL = "https://qa-scooter.praktikum-services.ru/";
    private WebDriver driver;
    private final String browser;
    private final OrderData orderData;
    private final boolean useUpperButton;
    private HomePage homePage;
    private OrderPage orderPage;

    public OrderTest(String browser, OrderData orderData, boolean useUpperButton) {
        this.browser = browser;
        this.orderData = orderData;
        this.useUpperButton = useUpperButton;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                // Chrome(верхняя кнопка)
                {"chrome", new OrderData(
                        "Тестов",
                        "Тестер",
                        "Москва, ул. Ленина, 2",
                        "Черкизовская",
                        "+79179170917",
                        "03.06.2025",
                        "сутки",
                        "серая безысходность",
                        "test1"
                ), true},

                // Firefox(нижняя кнопка)
                {"firefox", new OrderData(
                        "Тестов",
                        "Тестер",
                        "Москва, ул. Ленина, д.6",
                        "Борисово",
                        "+79179170917",
                        "03.02.2025",
                        "шестеро суток",
                        "серая безысходность",
                        "test4"
                ), true}
        });
    }

    @Before
    public void setUp() {
        switch (browser) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                break;
            default:
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--remote-allow-origins=*");
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver(options);
        }
        // Закрываем баннер для куки
        driver.get(URL);
        homePage = new HomePage(driver);
        homePage.closeCookieBanner();
        orderPage = new OrderPage(driver);
    }

    @Test
    public void testScooterOrder() {
        // Нажимаем на кнопку заказа
        homePage.clickOrderButton(useUpperButton);

        // Заполняем форму
        orderPage.fillFirstPage(orderData);
        orderPage.fillSecondPage(orderData);
        orderPage.confirmOrder();

        // Проверка успешного оформления
        assertTrue("Не удалось оформить заказ", orderPage.isOrderSuccess());
    }

    @After
    public void tearDown() {
        driver.quit();
    }
}
