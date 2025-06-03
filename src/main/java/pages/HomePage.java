package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class HomePage {
    private final WebDriver driver;

    // Локаторы
    private final By faqSection = By.className("Home_FAQ__3uVm4"); // Вопросы о важном
    private final By faqQuestions = By.className("accordion__button"); // Выпадающий список со всеми вопросами
    private final By upperOrderButton = By.className("Button_Button__ra12g"); // Верхняя кнопка "Заказать"
    private final By lowerOrderButton = By.xpath("//button[contains(@class, 'Button_Middle')]"); // Нижняя кнопка "Заказать"

    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    // Метод для проскролла к разделу FAQ
    public void scrollToFaq() {
        WebElement element = driver.findElement(faqSection);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
    }

    // Клик по вопросу
    public void clickQuestion(int index) {
        List<WebElement> questions = driver.findElements(faqQuestions);
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(questions.get(index))).click();

    }

    // Клик по кнопке заказа (верхней или нижней)
    public void clickOrderButton(boolean isUpperButton) {
        WebElement button = isUpperButton ?
                driver.findElement(upperOrderButton) :
                driver.findElement(lowerOrderButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", button);
        button.click();
    }
}
