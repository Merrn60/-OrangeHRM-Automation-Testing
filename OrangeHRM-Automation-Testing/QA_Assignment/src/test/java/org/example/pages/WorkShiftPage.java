package org.example.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

import java.time.Duration;

public class WorkShiftPage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final Actions actions;

    @FindBy(xpath = "//a[contains(@href, '/web/index.php/admin/viewAdminModule')]")
    private WebElement adminMenu;

    @FindBy(xpath = "//li[contains(@class, 'oxd-topbar-body-nav-tab') and .//span[contains(text(), 'Job') or contains(text(), 'Trabajo')]]")
    private WebElement jobDropdown;

    @FindBy(xpath = "//a[contains(@class, 'oxd-topbar-body-nav-tab-link') and contains(text(), 'Work Shifts') or contains(text(), 'Turnos de Trabajo')]")
    private WebElement workShiftsLink;

    @FindBy(xpath = "//button[contains(@class, 'oxd-button--secondary') and .//i[contains(@class, 'bi-plus')]]")
    private WebElement addButton;

    @FindBy(xpath = "//div[contains(@class, 'oxd-input-group') and .//label[contains(text(), 'Shift Name') or contains(text(), 'Nombre del Turno')]]//input[contains(@class, 'oxd-input')]")
    private WebElement shiftNameInput;

    @FindBy(xpath = "(//i[contains(@class, 'bi-clock oxd-time-input--clock')])[1]")
    private WebElement startTimeClockIcon;

    @FindBy(xpath = "(//input[contains(@class, 'oxd-time-hour-input-text')])[1]")
    private WebElement startTimeHourInput;

    @FindBy(xpath = "//input[@name='am' and @value='AM']")
    private WebElement amRadioButton;

    @FindBy(xpath = "(//i[contains(@class, 'bi-clock oxd-time-input--clock')])[2]")
    private WebElement endTimeClockIcon;

    @FindBy(xpath = "(//input[@placeholder='hh:mm'])[2]")
    private WebElement endTimeHourInput;



    @FindBy(xpath = "//input[@name='pm' and @value='PM']")
    private WebElement pmRadioButton;

    @FindBy(xpath = "//input[@placeholder='Type for hints...']")
    private WebElement assignedEmployeeInput;

    @FindBy(xpath = "//button[@type='submit' and contains(@class, 'oxd-button--medium oxd-button--secondary orangehrm-left-space')]")
    private WebElement saveButton;

    public WorkShiftPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.actions = new Actions(driver);
        PageFactory.initElements(driver, this);
    }

    public void navigateToWorkShifts() {
        Reporter.log("Navigating to Admin > Job > Work Shifts");
        wait.until(ExpectedConditions.elementToBeClickable(adminMenu)).click();
        wait.until(ExpectedConditions.elementToBeClickable(jobDropdown)).click();
        wait.until(ExpectedConditions.elementToBeClickable(workShiftsLink)).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".oxd-loading-spinner")));
        Reporter.log("Navigation completed");
    }

    public void clickAddButton() {
        Reporter.log("Clicking Add button");
        wait.until(ExpectedConditions.elementToBeClickable(addButton)).click();
        wait.until(ExpectedConditions.visibilityOf(shiftNameInput));
        Reporter.log("Add button clicked, form visible");
    }

    public void enterShiftName(String name) {
        Reporter.log("Entering Shift Name: " + name);
        wait.until(ExpectedConditions.visibilityOf(shiftNameInput)).clear();
        shiftNameInput.sendKeys(name);
        Reporter.log("Shift Name entered");
    }

    public void setStartTime() {
        Reporter.log("Setting start time to 10");

        // Click clock icon
        wait.until(ExpectedConditions.elementToBeClickable(startTimeClockIcon)).click();

        // Clear and enter hour value
        wait.until(ExpectedConditions.visibilityOf(startTimeHourInput));
        startTimeHourInput.click();
        startTimeHourInput.clear();
        actions.moveToElement(startTimeHourInput).perform();
        startTimeHourInput.sendKeys(Keys.chord(Keys.CONTROL, "a")); // Select all text
        startTimeHourInput.sendKeys(Keys.DELETE);
        startTimeHourInput.sendKeys("10");

        // فقط نحرك الفوكس من غير ما نختار AM/PM
        actions.moveToElement(shiftNameInput).click().perform();
        Reporter.log("Start time set to 10 (no AM/PM selected)");
    }


    public void setEndTime() {
        Reporter.log("Setting end time to 5");

        // Click clock icon
        wait.until(ExpectedConditions.elementToBeClickable(endTimeClockIcon)).click();

        // Clear and enter hour value
        wait.until(ExpectedConditions.visibilityOf(endTimeHourInput));
        endTimeHourInput.click();
        endTimeHourInput.clear();
        actions.moveToElement(endTimeHourInput).perform();
        endTimeHourInput.sendKeys(Keys.chord(Keys.CONTROL, "a")); // Select all text
        endTimeHourInput.sendKeys(Keys.DELETE);
        endTimeHourInput.sendKeys("5");


        actions.moveToElement(assignedEmployeeInput).click().perform();

        Reporter.log("End time set to 5 (no AM/PM selected)");
    }


    public void enterEmployeeName(String employeeName) {
        Reporter.log("Entering employee name: " + employeeName);
        wait.until(ExpectedConditions.visibilityOf(assignedEmployeeInput)).click();
        assignedEmployeeInput.clear();
        assignedEmployeeInput.sendKeys(employeeName);
        Reporter.log("Employee name entered");
    }

    public void saveShift() {
        Reporter.log("Saving Work Shift");
        wait.until(ExpectedConditions.elementToBeClickable(saveButton)).click();
        Reporter.log("Save button clicked");
    }
}