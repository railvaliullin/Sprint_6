package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.HomePage;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;

import static junit.framework.TestCase.assertEquals;

@RunWith(Parameterized.class)
public class FaqTest {
    private WebDriver driver;
    private static final String URL = "https://qa-scooter.praktikum-services.ru/";

    // Параметры теста
    private final int questionIndex;
    private final String expectedAnswer;

    public FaqTest(int questionIndex, String expectedAnswer) {
        this.questionIndex = questionIndex;
        this.expectedAnswer = expectedAnswer;
    }

    @Parameterized.Parameters(name = "Вопрос {0}: проверяем ответ")
    public static Collection<Object[]> faqData() {
        return Arrays.asList(new Object[][]{
                {0, "Сутки — 400 рублей. Оплата курьеру — наличными или картой."},
                {1, "Пока что у нас так: один заказ — один самокат. Если хотите покататься с друзьями, можете просто сделать несколько заказов — один за другим."},
                {2, "Допустим, вы оформляете заказ на 8 мая. Мы привозим самокат 8 мая в течение дня. Отсчёт времени аренды начинается с момента, когда вы оплатите заказ курьеру. Если мы привезли самокат 8 мая в 20:30, суточная аренда закончится 9 мая в 20:30."},
                {3, "Только начиная с завтрашнего дня. Но скоро станем расторопнее."},
                {4, "Пока что нет! Но если что-то срочное — всегда можно позвонить в поддержку по красивому номеру 1010."},
                {5, "Самокат приезжает к вам с полной зарядкой. Этого хватает на восемь суток — даже если будете кататься без передышек и во сне. Зарядка не понадобится."},
                {6, "Да, пока самокат не привезли. Штрафа не будет, объяснительной записки тоже не попросим. Все же свои."},
                {7, "Да, обязательно. Всем самокатов! И Москве, и Московской области."}
        });
    }

    @Before
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver(options);

        // Закрываем баннер для куки
        driver.get(URL);
        new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[contains(text(), 'да все привыкли')]")
                )).click();
        HomePage homePage = new HomePage(driver);
        homePage.scrollToFaq();
    }

    @Test
    public void checkFaqAnswers() {
        // Через JavascriptExecutor делаем прокрутку и кликаем
        WebElement question = driver.findElement(By.id("accordion__heading-" + questionIndex));
        ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", question);
        ((JavascriptExecutor)driver).executeScript("arguments[0].click();", question);

        // Локатор ответа
        By answerLocator = By.xpath(String.format(
                "//div[@id='accordion__panel-%d']",
                questionIndex
        ));

        // Ожидание с доп. условиями
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.and(
                        ExpectedConditions.visibilityOfElementLocated(answerLocator),
                        ExpectedConditions.textToBe(answerLocator, expectedAnswer)
                ));

        // Доп.проверка
        String actualText = driver.findElement(answerLocator).getText().trim();
        assertEquals(expectedAnswer, actualText);
    }

    @After
    public void tearDown() {
        driver.quit();
    }
}
