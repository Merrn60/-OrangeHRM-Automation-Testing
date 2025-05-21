# OrangeHRM Automation Testing

## Overview
This is a **Quality Assurance project** developed as part of the Software Engineering curriculum (Year 4) at **Ahram Canadian University**. It automates testing for the OrangeHRM web application using **Selenium WebDriver** and **TestNG**. The project follows the **Page Object Model (POM)** design pattern to create maintainable and scalable test automation scripts for key functionalities.

## Features
- **Login Verification**: Validates successful login with correct credentials.
- **User Management**: Automates adding a new user and verifying their presence in the system.
- **Leave Assignment**: Automates assigning leave to an employee with specific dates and leave type.
- **Work Shifts**: Creates and configures new work shifts.
- **Employee List**: Extracts data from the employee table and verifies specific employee records.
- **Element State Checks**: Verifies UI elements like buttons and menus.
- **Double-Click Actions**: Implements interactions like double-clicking table rows to view details.

## Technologies Used
- **Java**: Core programming language for test scripts.
- **Selenium WebDriver**: For browser automation.
- **TestNG**: For test case management and assertions.
- **WebDriverManager**: For managing browser drivers.
- **Page Object Model (POM)**: For structured and reusable test code.

## Setup Instructions
1. Clone the repository: `git clone https://github.com/Merrn60/-OrangeHRM-Automation-Testing.git`
2. Ensure **Java JDK** and **Maven** are installed.
3. Install dependencies: `mvn clean install`
4. Run tests: `mvn test`
5. Screenshots are saved in the `screenshots` folder for each test execution.

## Project Structure
- **src/main/java/org/example/pages**: Page Object classes for each module (Login, Admin, Leave, WorkShift, EmployeeList).
- **src/test/java/org/example/tests**: TestNG test cases and base test setup.
- **screenshots**: Stores test execution screenshots.

## Usage
- Tests are organized in `OrangeTest.java` with prioritized test cases.
- Each test case logs actions and takes screenshots before and after execution.
- Update the `BaseTest.java` URL if testing on a different environment.

## Notes
- Ensure Chrome browser is installed for WebDriver compatibility.
- Tests are designed for the OrangeHRM demo site: [OrangeHRM Live](https://opensource-demo.orangehrmlive.com).
- This project was developed as a university assignment to demonstrate Quality Assurance and test automation skills.
