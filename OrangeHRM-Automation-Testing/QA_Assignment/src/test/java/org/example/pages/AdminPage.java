package org.example.pages;

import org.openqa.selenium.By;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

import java.time.Duration;
import java.util.Random;

public class AdminPage {
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(xpath = "//a[contains(@class, 'oxd-main-menu-item') and contains(., 'Admin')]")
    private WebElement adminMenu;

    @FindBy(xpath = "//button[contains(@class, 'oxd-button--secondary') and contains(., 'Add')]")
    private WebElement addButton;

    // Updated XPath for User Role dropdown
    @FindBy(xpath = "//div[contains(@class, 'oxd-input-group') and .//label[contains(text(), 'User Role')]]//div[contains(@class, 'oxd-select-text')]")
    private WebElement userRoleDropdown;

    @FindBy(xpath = "//div[contains(@class, 'oxd-input-group') and .//label[contains(text(), 'Employee Name')]]//input[@placeholder='Type for hints...']")
    private WebElement employeeNameInput;

    // Updated XPath for Status dropdown
    @FindBy(xpath = "//div[contains(@class, 'oxd-input-group') and .//label[contains(text(), 'Status')]]//div[contains(@class, 'oxd-select-text')]")
    private WebElement statusDropdown;

    @FindBy(xpath = "//div[contains(@class, 'oxd-input-group') and .//label[contains(text(), 'Username')]]//input[contains(@class, 'oxd-input')]")
    private WebElement usernameInput;

    @FindBy(xpath = "//div[contains(@class, 'oxd-input-group') and .//label[contains(text(), 'Password')]]//input[@type='password']")
    private WebElement passwordInput;

    @FindBy(xpath = "//div[contains(@class, 'oxd-input-group') and .//label[contains(text(), 'Confirm Password')]]//input[@type='password']")
    private WebElement confirmPasswordInput;

    @FindBy(xpath = "//button[@type='submit' and contains(@class, 'oxd-button--secondary') and contains(., 'Save')]")
    private WebElement saveButton;

    @FindBy(xpath = "//div[contains(@class, 'oxd-table')]")
    private WebElement systemUsersTable;

    @FindBy(xpath = "//div[contains(@class, 'oxd-input-group') and .//label[contains(text(), 'Username')]]//input[contains(@class, 'oxd-input')]")
    private WebElement searchUsernameInput;

    @FindBy(xpath = "//button[@type='submit' and contains(@class, 'oxd-button--secondary') and contains(., 'Search')]")
    private WebElement searchButton;

    public AdminPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20)); // Increased timeout
        PageFactory.initElements(driver, this);
    }

    public void navigateToAddUser() {
        Reporter.log("Navigating to Admin > Add User");
        wait.until(ExpectedConditions.elementToBeClickable(adminMenu)).click();
        wait.until(ExpectedConditions.elementToBeClickable(addButton)).click();
        // Wait for form to load
        wait.until(ExpectedConditions.visibilityOf(userRoleDropdown));
    }

    public void selectUserRole(String role) {
        Reporter.log("Selecting User Role: " + role);
        wait.until(ExpectedConditions.elementToBeClickable(userRoleDropdown)).click();
        WebElement roleOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@role='option']//span[contains(text(), '" + role + "')]")));
        roleOption.click();
    }

    public void enterEmployeeName(String name) {
        Reporter.log("Entering Employee Name: " + name);
        wait.until(ExpectedConditions.visibilityOf(employeeNameInput)).sendKeys(name);
        // Wait for autocomplete suggestions
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(@class, 'oxd-autocomplete-option')]")));
        WebElement suggestion = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(@class, 'oxd-autocomplete-option')]//span[contains(text(), '" + name + "')]")));
        suggestion.click();
    }

    public void selectStatus(String status) {
        Reporter.log("Selecting Status: " + status);
        wait.until(ExpectedConditions.elementToBeClickable(statusDropdown)).click();
        WebElement statusOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@role='option']//span[contains(text(), '" + status + "')]")));
        statusOption.click();
    }

    public String enterRandomUsername() {
        String randomUsername = "xrawanx" + new Random().ints(97, 123)
                .limit(5)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        Reporter.log("Entering Username: " + randomUsername);
        wait.until(ExpectedConditions.visibilityOf(usernameInput)).sendKeys(randomUsername);
        return randomUsername;
    }

    public String enterRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String randomPassword = new Random().ints(10, 0, chars.length())
                .mapToObj(chars::charAt)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
        Reporter.log("Entering Password");
        wait.until(ExpectedConditions.visibilityOf(passwordInput)).sendKeys(randomPassword);
        wait.until(ExpectedConditions.visibilityOf(confirmPasswordInput)).sendKeys(randomPassword);
        return randomPassword;
    }

    public void saveUser() {
        Reporter.log("Saving User");
        wait.until(ExpectedConditions.elementToBeClickable(saveButton)).click();
    }

    public boolean isSystemUsersTableDisplayed() {
        Reporter.log("Verifying System Users table");
        return wait.until(ExpectedConditions.visibilityOf(systemUsersTable)).isDisplayed();
    }

    public void searchUser(String username) {
        Reporter.log("Searching for User: " + username);
        wait.until(ExpectedConditions.visibilityOf(searchUsernameInput)).sendKeys(username);
        wait.until(ExpectedConditions.elementToBeClickable(searchButton)).click();
    }

    public boolean isUserDisplayed(String username) {
        Reporter.log("Verifying User in results: " + username);
        try {
            WebElement userRow = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[contains(@class, 'oxd-table-row')]//div[contains(text(), '" + username + "')]")));
            return userRow.isDisplayed();
        } catch (Exception e) {
            Reporter.log("Error verifying user: " + e.getMessage());
            return false;
        }
    }
}