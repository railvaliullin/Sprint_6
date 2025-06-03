package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.OrderData;
import java.time.Duration;

public class OrderPage {
    private final WebDriver driver;

    // Локаторы для формы заказа
    private final By nameInput = By.cssSelector("input[placeholder='* Имя']");
    private final By lastNameInput = By.cssSelector("input[placeholder='* Фамилия']");
    private final By addressInput = By.cssSelector("input[placeholder='* Адрес: куда привезти заказ']");
    private final By metroInput = By.cssSelector("input[placeholder='* Станция метро']");
    private final By phoneInput = By.cssSelector("input[placeholder='* Телефон: на него позвонит курьер']");
    private final By nextButton = By.xpath("//button[text()='Далее']");
    private final By dateInput = By.cssSelector("input[placeholder='* Когда привезти самокат']");
    private final By rentalPeriod = By.className("Dropdown-placeholder");
    private final By colorCheckbox = By.id("black"); // Для черного цвета
    private final By commentInput = By.cssSelector("input[placeholder='Комментарий для курьера']");
    private final By orderButton = By.xpath("//button[contains(@class, 'Button_Middle') and text()='Заказать']");
    private final By confirmButton = By.xpath("//button[text()='Да']");
    private final By successMessage = By.className("Order_ModalHeader__3FDaJ");

    public OrderPage(WebDriver driver) {
        this.driver = driver;
    }

    // Заполнение первой страницы заказа
    public void fillFirstPage(OrderData orderData) {
        driver.findElement(nameInput).sendKeys(orderData.getName());
        driver.findElement(lastNameInput).sendKeys(orderData.getLastName());
        driver.findElement(addressInput).sendKeys(orderData.getAddress());

        driver.findElement(metroInput).sendKeys(orderData.getMetroStation());
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//div[text()='" + orderData.getMetroStation() + "']")
                )).click();

        driver.findElement(phoneInput).sendKeys(orderData.getPhone());
        driver.findElement(nextButton).click();
    }

    // Заполнение второй страницы заказа
    public void fillSecondPage(OrderData orderData) {
        WebElement datePicker = driver.findElement(dateInput);
        datePicker.sendKeys(orderData.getDeliveryDate());
        datePicker.sendKeys(Keys.ENTER);

        driver.findElement(rentalPeriod).click();
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//div[text()='" + orderData.getRentalPeriod() + "']")
                )).click();

        if (orderData.getColor().equals("black")) {
            driver.findElement(colorCheckbox).click();
        }

        driver.findElement(commentInput).sendKeys(orderData.getComment());
        driver.findElement(orderButton).click();
    }

    // Подтверждение заказа
    public void confirmOrder() {
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(confirmButton)).click();
    }

    // Проверка успешного оформления заказа
    public boolean isOrderSuccess() {
        WebElement successModal = new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//div[contains(@class, 'Order_Modal__')]")
                ));

             // Проверяем и заголовок, и текст
        return successModal.isDisplayed()
                && successModal.getText().contains("Заказ оформлен")
                && driver.findElement(By.className("Order_Text__2broi"))
                .getText().contains("Номер заказа");
    }
}
