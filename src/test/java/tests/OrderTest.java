package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.HomePage;
import pages.OrderPage;
import utils.OrderData;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class OrderTest {
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
                // Chrome(верхняя кнопка, первый тестовый вариант)
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

                // Chrome(нижняя кнопка, второй тестовый вариант)
                {"chrome", new OrderData(
                        "Тестов",
                        "Тестер",
                        "Москва, ул. Ленина, 2",
                        "Лихоборы",
                        "+79179170917",
                        "03.06.2025",
                        "семеро суток",
                        "чёрный жемчуг",
                        "test2"
                ), false},

                // Firefox(верхняя кнопка, первый тестовый вариант)
                {"firefox", new OrderData(
                        "Тестов",
                        "Тестер",
                        "Москва, ул. Ленина, 3а",
                        "Окружная",
                        "+79179170917",
                        "03.06.2025",
                        "трое суток",
                        "чёрный жемчуг",
                        "test3"
                ), true},

                // Firefox(нижняя кнопка, второй тестовый вариант)
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
                ), false}
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
        driver.get("https://qa-scooter.praktikum-services.ru/");
        new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[contains(text(), 'да все привыкли')]")
                )).click();
        homePage = new HomePage(driver);
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
