package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage {
    private final WebDriver driver;

    // Локаторы
    private final By cookieBanner = By.xpath("//button[contains(text(), 'да все привыкли')]");
    private final String answerLocatorTemplate = "accordion__panel-%d";
    private final By faqSection = By.className("Home_FAQ__3uVm4"); // Вопросы о важном
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

    // Клик по кнопке заказа (верхней или нижней)
    public void clickOrderButton(boolean isUpperButton) {
        WebElement button = isUpperButton ?
                driver.findElement(upperOrderButton) :
                driver.findElement(lowerOrderButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", button);
        button.click();
    }

    //Закрытие куки баннера
    public void closeCookieBanner() {
        new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.elementToBeClickable(cookieBanner))
                .click();
    }

    // Кликаем по вопросу
    public void clickQuestion(int questionIndex) {
        String questionLocatorTemplate = "accordion__heading-%d";
        By questionLocator = By.id(String.format(questionLocatorTemplate, questionIndex));
        WebElement question = driver.findElement(questionLocator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", question);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", question);
    }

    // Ожидаем появление ответа
    public void waitForAnswerWithText(int questionIndex, String expectedText) {
        By answerLocator = By.id(String.format(answerLocatorTemplate, questionIndex));
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.and(
                        ExpectedConditions.visibilityOfElementLocated(answerLocator),
                        ExpectedConditions.textToBe(answerLocator, expectedText)
                ));
    }

    // Получаем текст ответа по индексу
    public String getAnswerText(int questionIndex) {
        By answerLocator = By.id(String.format(answerLocatorTemplate, questionIndex));
        return driver.findElement(answerLocator).getText().trim();
    }
}
