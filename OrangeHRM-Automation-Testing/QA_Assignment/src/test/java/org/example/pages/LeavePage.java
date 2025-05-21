package org.example.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

import java.time.Duration;

public class LeavePage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Language-agnostic locator for Leave menu
    @FindBy(xpath = "//a[contains(@href, '/web/index.php/leave/viewLeaveModule')]")
    private WebElement leaveMenu;

    // Locator for Assign Leave link
    @FindBy(xpath = "//a[contains(@href, '/assignLeave') or contains(text(), 'Assign') or contains(text(), 'Asignar')]")
    private WebElement assignLeaveLink;

    // Leave Type dropdown
    @FindBy(xpath = "//div[contains(@class, 'oxd-input-group') and .//label[contains(text(), 'Leave Type') or contains(text(), 'Tipo de Licencia')]]//div[contains(@class, 'oxd-select-text')]")
    private WebElement leaveTypeDropdown;

    // Employee Name autocomplete
    @FindBy(xpath = "//div[contains(@class, 'oxd-input-group') and .//label[contains(text(), 'Employee Name') or contains(text(), 'Nombre del Empleado')]]//input[@placeholder='Type for hints...']")
    private WebElement employeeNameInput;



    // From Date input
    @FindBy(xpath = "//div[contains(@class, 'oxd-input-group') and .//label[contains(text(), 'From Date') or contains(text(), 'Fecha Desde')]]//input[@placeholder='yyyy-dd-mm']")
    private WebElement fromDateInput;

    // To Date input
    @FindBy(xpath = "//div[contains(@class, 'oxd-input-group') and .//label[contains(text(), 'To Date') or contains(text(), 'Fecha Hasta')]]//input[@placeholder='yyyy-dd-mm']")
    private WebElement toDateInput;

    // Submit button
    @FindBy(xpath = "//button[@type='submit' and contains(@class, 'oxd-button--secondary orangehrm-left-space')]")
    private WebElement submitButton;
    // أضف هذا الـ locator للزر OK في البوب أب
    @FindBy(xpath = "//button[contains(@class, 'oxd-button') and contains(text(), 'Ok')]")
    private WebElement okButtonPopup;

    // أو استخدم هذا إذا كان الزر يحتوي على class محدد

    public LeavePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        PageFactory.initElements(driver, this);
    }

    public void navigateToAssignLeave() {
        Reporter.log("Navigating to Leave > Assign Leave");
        wait.until(ExpectedConditions.elementToBeClickable(leaveMenu)).click();
        wait.until(ExpectedConditions.elementToBeClickable(assignLeaveLink)).click();
        wait.until(ExpectedConditions.visibilityOf(leaveTypeDropdown));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".oxd-loading-spinner")));
    }

    public void selectLeaveType(String leaveType) {
        Reporter.log("Selecting Leave Type: " + leaveType);
        wait.until(ExpectedConditions.elementToBeClickable(leaveTypeDropdown)).click();
        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@role='option']//span[contains(text(), '" + leaveType + "')]")));
        option.click();
    }

    public void enterEmployeeName(String name) {
        Reporter.log("Entering Employee Name: " + name);
        wait.until(ExpectedConditions.visibilityOf(employeeNameInput)).sendKeys(name);
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(@class, 'oxd-autocomplete-option')]")));
        WebElement suggestion = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(@class, 'oxd-autocomplete-option')]//span[contains(text(), '" + name + "')]")));
        suggestion.click();
    }
    public void selectDates() {
        Reporter.log("Entering From Date and To Date directly into fields");

        // Define dates in correct format (yyyy-dd-mm)
        String fromDateStr = "2025-10-01"; // October 1, 2025
        String toDateStr = "2025-12-12";   // December 12, 2025

        // Enter From Date
        try {
            WebElement fromInput = wait.until(ExpectedConditions.visibilityOf(fromDateInput));
            fromInput.click(); // Focus the field first
            fromInput.sendKeys(Keys.chord(Keys.CONTROL, "a")); // Select all text
            fromInput.sendKeys(Keys.DELETE); // Clear the field
            fromInput.sendKeys(fromDateStr);
            Reporter.log("Entered From Date: " + fromDateStr);
        } catch (Exception e) {
            Reporter.log("Failed to enter From Date: " + e.getMessage());
            throw new RuntimeException("From Date input failed", e);
        }

        // Enter To Date
        try {
            WebElement toInput = wait.until(ExpectedConditions.visibilityOf(toDateInput));
            toInput.click(); // Focus the field first
            toInput.sendKeys(Keys.chord(Keys.CONTROL, "a")); // Select all text
            toInput.sendKeys(Keys.DELETE); // Clear the field
            toInput.sendKeys(toDateStr);
            Reporter.log("Entered To Date: " + toDateStr);
        } catch (Exception e) {
            Reporter.log("Failed to enter To Date: " + e.getMessage());
            throw new RuntimeException("To Date input failed", e);
        }

        // Optional: Blur the last field
        toDateInput.sendKeys(Keys.TAB);
    }


    public void submitForm() {
        Reporter.log("Submitting Assign Leave form");

        try {

            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".oxd-form-loader")));


            WebElement submitBtn = wait.until(ExpectedConditions.elementToBeClickable(submitButton));


            try {
                submitBtn.click();
            } catch (ElementClickInterceptedException e) {
                ((JavascriptExecutor)driver).executeScript("arguments[0].click();", submitBtn);
                Reporter.log("Used JavaScript click due to interception");
            }


            handleConfirmationPopup();

        } catch (Exception e) {
            Reporter.log("Failed to submit form: " + e.getMessage());
            throw new RuntimeException("Form submission failed", e);
        }
    }

    private void handleConfirmationPopup() {
        try {

            WebElement popup = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[contains(@class,'oxd-dialog-container')]")));


            WebElement confirmBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(@class,'oxd-button') and contains(text(),'Ok')]")));


            try {
                confirmBtn.click();
            } catch (ElementClickInterceptedException e) {
                ((JavascriptExecutor)driver).executeScript("arguments[0].click();", confirmBtn);
            }

            Reporter.log("Handled confirmation popup successfully");

        } catch (TimeoutException e) {
            Reporter.log("No confirmation popup appeared");
        }
    }

}