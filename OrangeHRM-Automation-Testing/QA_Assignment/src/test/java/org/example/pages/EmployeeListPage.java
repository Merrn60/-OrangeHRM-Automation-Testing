package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class EmployeeListPage {
    private WebDriver driver;
    private WebDriverWait wait;
    private Actions actions;

    // Locators
    private final By pimMenu = By.xpath("//a[@href='/web/index.php/pim/viewPimModule']");
    private final By employeeListTab = By.cssSelector("a.oxd-topbar-body-nav-tab-item");
    private final By searchInput = By.cssSelector("input[data-v-75e744cd]");
    private final By searchButton = By.cssSelector("button.oxd-button--secondary[type='submit']");
    private final By nextPageButton = By.cssSelector("button.oxd-pagination-page-item--next");
    private final By tableBody = By.cssSelector("div.oxd-table-body");
    private final By tableRows = By.cssSelector("div.oxd-table-row.oxd-table-row--clickable");

    // Constructor
    public EmployeeListPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.actions = new Actions(driver);
    }

    // Navigate to PIM > Employee List
    public void navigateToEmployeeList() {
        wait.until(ExpectedConditions.elementToBeClickable(pimMenu)).click();
        List<WebElement> navTabs = driver.findElements(employeeListTab);
        for (WebElement tab : navTabs) {
            if (tab.getText().trim().equals("Employee List")) {
                wait.until(ExpectedConditions.elementToBeClickable(tab)).click();
                break;
            }
        }
        wait.until(ExpectedConditions.visibilityOfElementLocated(tableBody));
    }

    // Search for an employee by name
    public void searchForEmployee(String employeeName) {
        WebElement searchField = wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput));
        searchField.clear();
        searchField.sendKeys(employeeName);
        wait.until(ExpectedConditions.elementToBeClickable(searchButton)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(tableBody));
    }

    // Extract data from the first 5 rows
    public void extractFirstFiveRows() {
        try {
            List<WebElement> allRows = new ArrayList<>();
            int rowsNeeded = 5;

            while (allRows.size() < rowsNeeded) {
                List<WebElement> currentPageRows = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(tableRows));
                allRows.addAll(currentPageRows);

                if (allRows.size() >= rowsNeeded) break;

                WebElement nextButton = driver.findElement(nextPageButton);
                if (!nextButton.isEnabled()) break;

                nextButton.click();
                wait.until(ExpectedConditions.visibilityOfElementLocated(tableBody));
            }

            int rowsToPrint = Math.min(allRows.size(), rowsNeeded);
            System.out.println("Extracted data from " + rowsToPrint + " rows:");
            for (int i = 0; i < rowsToPrint; i++) {
                WebElement row = allRows.get(i);
                List<WebElement> cells = row.findElements(By.cssSelector("div.oxd-table-cell"));
                String id = cells.get(0).getText();
                String firstName = cells.get(1).getText();
                String lastName = cells.get(2).getText();
                System.out.println("Row " + (i + 1) + ": Id=" + id + ", First Name=" + firstName + ", Last Name=" + lastName);
            }
        } catch (Exception e) {
            System.out.println("Error extracting rows: " + e.getMessage());
        }
    }

    // Search for an employee by first and last name across all pages
    public boolean searchEmployee(String firstName, String lastName) {
        searchForEmployee(firstName + " " + lastName);

        try {
            if (isEmployeeInCurrentPage(firstName, lastName)) return true;

            // Reset and search manually
            WebElement searchField = wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput));
            searchField.clear();
            driver.findElement(searchButton).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(tableBody));

            while (true) {
                if (isEmployeeInCurrentPage(firstName, lastName)) return true;

                WebElement nextButton = driver.findElement(nextPageButton);
                if (!nextButton.isEnabled()) break;
                nextButton.click();
                wait.until(ExpectedConditions.visibilityOfElementLocated(tableBody));
            }
        } catch (Exception e) {
            System.out.println("Error while searching employee: " + e.getMessage());
        }

        return false;
    }

    // Helper to check current page for employee
    private boolean isEmployeeInCurrentPage(String firstName, String lastName) {
        List<WebElement> rows = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(tableRows));
        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.cssSelector("div.oxd-table-cell"));
            if (cells.size() >= 3) {
                String rowFirstName = cells.get(1).getText().trim();
                String rowLastName = cells.get(2).getText().trim();
                if (rowFirstName.equalsIgnoreCase(firstName) && rowLastName.equalsIgnoreCase(lastName)) {
                    return true;
                }
            }
        }
        return false;
    }

    // Double-click the first clickable row
    public void doubleClickFirstRow() {
        try {
            List<WebElement> rows = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(tableRows));
            if (rows.isEmpty()) {
                System.out.println("No clickable rows found.");
                return;
            }
            WebElement firstRow = rows.get(0);
            actions.doubleClick(firstRow).perform();

            List<WebElement> cells = firstRow.findElements(By.cssSelector("div.oxd-table-cell"));
            String id = cells.get(0).getText();
            System.out.println("Double-clicked row ID: " + id);

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.oxd-layout-context")));
        } catch (Exception e) {
            System.out.println("Error in doubleClickFirstRow: " + e.getMessage());
        }
    }
}
