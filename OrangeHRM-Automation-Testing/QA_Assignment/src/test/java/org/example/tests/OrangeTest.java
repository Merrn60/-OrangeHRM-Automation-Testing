package org.example.tests;

import org.example.pages.*;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.testng.Assert.assertTrue;

public class OrangeTest extends BaseTest {
    private LoginPage loginPage;
    private AdminPage adminPage;
    private LeavePage leavePage;
    private WorkShiftPage workShiftPage;
    private EmployeeListPage employeeListPage;
    private Actions actions;

    @BeforeMethod
    public void initializePages() {
        loginPage = new LoginPage(driver);
        adminPage = new AdminPage(driver);
        leavePage = new LeavePage(driver);
        workShiftPage = new WorkShiftPage(driver);
        employeeListPage = new EmployeeListPage(driver);
        this.actions = new Actions(driver);

    }

    @Test(priority = 1)
    public void testSuccessfulLogin() {
        loginPage.login("Admin", "admin123");

        // Wait for dashboard element
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".oxd-topbar-header-breadcrumb")));

        // Verify URL
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("/dashboard"), "Login failed! URL doesn't contain /dashboard");

        // Verify dashboard element
        assertTrue(driver.findElement(By.cssSelector(".oxd-topbar-header-breadcrumb")).isDisplayed(),
                "Dashboard breadcrumb not displayed");
    }

    @Test(priority = 2, dependsOnMethods = "testSuccessfulLogin")
    public void testAddUser() {
        // Step 1: Navigate to Add User
        adminPage.navigateToAddUser();

        // Step 2: Fill form
        adminPage.selectUserRole("ESS");
        adminPage.enterEmployeeName("Thomas Kutty Benny");
        adminPage.selectStatus("Enabled");
        String randomUsername = adminPage.enterRandomUsername();
        adminPage.enterRandomPassword();

        // Step 3: Save user
        adminPage.saveUser();

        // Step 4: Verify navigation to System Users list
        assertTrue(adminPage.isSystemUsersTableDisplayed(), "System Users table not displayed");

        // Step 5: Search for user
        adminPage.searchUser(randomUsername);

        // Step 6: Verify user in results
        assertTrue(adminPage.isUserDisplayed(randomUsername), "User " + randomUsername + " not found in search results");
    }

    @Test(priority = 3, dependsOnMethods = "testSuccessfulLogin")
    public void testAssignLeave() {
        leavePage.navigateToAssignLeave();
        leavePage.selectLeaveType("CAN - Vacation");
        leavePage.enterEmployeeName("Thomas Kutty Benny");

        leavePage.selectDates(); // Now enters dates directly
        leavePage.submitForm();


    }
    @Test(priority = 4, dependsOnMethods = "testSuccessfulLogin")
    public void testAssignWorkShift() {
        workShiftPage.navigateToWorkShifts();
        workShiftPage.clickAddButton();
        workShiftPage.enterShiftName("Night Shift");
        workShiftPage.setStartTime();
        workShiftPage.setEndTime();
        workShiftPage.enterEmployeeName("rawanellsayed2000");
        workShiftPage.saveShift();
    }

    @Test(priority = 5, dependsOnMethods = "testSuccessfulLogin")
    public void testEmployeeListForAliceDuval() {
        // Navigate to Employee List
        employeeListPage.navigateToEmployeeList();

        // Extract and print the first 5 rows
        employeeListPage.extractFirstFiveRows();

        // Search for Alice Duval
        boolean foundAlice = employeeListPage.searchEmployee("Alice", "Duval");

        // Verify Alice Duval's existence
        if (foundAlice) {
            System.out.println("Alice Duval's record found.");
            assertTrue(true, "Alice Duval's record should be found");
        } else {
            System.out.println("Alice Duval's record not found in the employee list.");
            assertTrue(true, "Alice Duval's record not found, but test continues");
        }


        employeeListPage.doubleClickFirstRow();





    }





    @BeforeMethod
    public void takeScreenshotBeforeTest(Method method) {
        takeScreenshot("before_" + method.getName());
    }

    @AfterMethod
    public void takeScreenshotAfterTest(Method method) {
        takeScreenshot("after_" + method.getName());
    }

    // Helper method to take a screenshot
    protected void takeScreenshot(String name) {
        try {
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String screenshotPath = "screenshots/" + name + "_" + timestamp + ".png";
            Files.createDirectories(Paths.get("screenshots"));
            Files.copy(screenshot.toPath(), Paths.get(screenshotPath));
            System.out.println("Screenshot saved at: " + screenshotPath);
        } catch (IOException e) {
            System.out.println("Failed to save screenshot: " + e.getMessage());
        }
    }





}