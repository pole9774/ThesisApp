package it.polito.thesisapp;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

public class AppiumTest {

    private AndroidDriver driver;

    @BeforeClass
    public void setup() throws URISyntaxException, MalformedURLException {
        UiAutomator2Options options = new UiAutomator2Options()
                .setDeviceName("emulator-5554")
                .setApp("C:/Users/pole9/Desktop/Poli/Tesi/App/ThesisApp/app/build/intermediates/apk/debug/app-debug.apk")
                .setAutomationName("UiAutomator2");

        driver = new AndroidDriver(new URI("http://127.0.0.1:4723").toURL(), options);

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @Test
    public void welcomeMessage_displaysUserFirstName() {
        try {
            WebElement welcome_message = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='Welcome, Alessandro']"));
            Assert.assertTrue(welcome_message.isDisplayed(), "Welcome message is not visible!");
            System.out.println("Welcome message exists!");
        } catch (NoSuchElementException e) {
            Assert.fail("Element not found: " + e.getMessage());
        } catch (Exception e) {
            Assert.fail("Unexpected error: " + e.getMessage());
        }
    }

    @Test
    public void teamSection_displaysTeams() {
        try {
            WebElement team_name = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='Team A']"));
            Assert.assertTrue(team_name.isDisplayed(), "Team name is not visible!");
            System.out.println("Team name exists!");

            WebElement team_members = driver.findElement(AppiumBy.xpath("(//android.widget.TextView[@text='3 members'])[1]"));
            Assert.assertTrue(team_members.isDisplayed(), "Team members is not visible!");
            System.out.println("Team members exists!");
        } catch (NoSuchElementException e) {
            Assert.fail("Element not found: " + e.getMessage());
        } catch (Exception e) {
            Assert.fail("Unexpected error: " + e.getMessage());
        }
    }

    @Test
    public void taskSection_displaysTasks() {
        try {
            WebElement task_name1 = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='erwrwrw']"));
            Assert.assertTrue(task_name1.isDisplayed(), "First task name is not visible!");
            System.out.println("First task name exists!");

            WebElement task_description1 = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='ewrwrw']"));
            Assert.assertTrue(task_description1.isDisplayed(), "First task description is not visible!");
            System.out.println("First task description exists!");

            WebElement task_name2 = driver.findElement(AppiumBy.xpath("(//android.widget.TextView[@text='fwefwf'])[1]"));
            Assert.assertTrue(task_name2.isDisplayed(), "Second task name is not visible!");
            System.out.println("Second task name exists!");

            WebElement task_description2 = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='fwefewf']"));
            Assert.assertTrue(task_description2.isDisplayed(), "Second task description is not visible!");
            System.out.println("Second task description exists!");
        } catch (NoSuchElementException e) {
            Assert.fail("Element not found: " + e.getMessage());
        } catch (Exception e) {
            Assert.fail("Unexpected error: " + e.getMessage());
        }
    }

    @Test
    public void taskStatusChips_areCorrectlyDisplayed() {
        try {
            WebElement task_status1 = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='To Do']"));
            Assert.assertTrue(task_status1.isDisplayed(), "First task status is not visible!");
            System.out.println("First task status exists!");

            WebElement task_status2 = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='Done']"));
            Assert.assertTrue(task_status2.isDisplayed(), "Second task status is not visible!");
            System.out.println("Second task status exists!");
        } catch (NoSuchElementException e) {
            Assert.fail("Element not found: " + e.getMessage());
        } catch (Exception e) {
            Assert.fail("Unexpected error: " + e.getMessage());
        }
    }

    @Test
    public void teamCard_clickTriggersNavigation() {
        try {
            WebElement team_name = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='Team A']"));
            Assert.assertTrue(team_name.isDisplayed(), "Team name is not visible!");
            System.out.println("Team name exists!");

            team_name.click();

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    AppiumBy.xpath("//android.widget.TextView[@text='Team A']")
            ));

            WebElement team_name_title = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='Team A']"));
            Assert.assertTrue(team_name_title.isDisplayed(), "Team name (title) is not visible!");
            System.out.println("Team name (title) exists!");

            WebElement create_task_button = driver.findElement(AppiumBy.xpath("//android.view.View[@content-desc='Create Task']"));
            Assert.assertTrue(create_task_button.isDisplayed(), "Create task button is not visible!");
            System.out.println("Create task button exists!");

            driver.navigate().back();
        } catch (NoSuchElementException e) {
            Assert.fail("Element not found: " + e.getMessage());
        } catch (Exception e) {
            Assert.fail("Unexpected error: " + e.getMessage());
        }
    }

    @Test
    public void taskCard_clickTriggersNavigation_mainPage() {
        try {
            WebElement homeButton = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='Home']"));
            homeButton.click();

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    AppiumBy.xpath("//android.widget.TextView[@text='erwrwrw']")
            ));

            WebElement task = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='erwrwrw']"));
            Assert.assertTrue(task.isDisplayed(), "Task is not visible!");
            System.out.println("Task exists!");

            task.click();

            WebElement task_name = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='erwrwrw']"));
            Assert.assertTrue(task_name.isDisplayed(), "Task name (task page) is not visible!");
            System.out.println("Task name exists!");

            WebElement task_description = driver.findElement(AppiumBy.xpath("(//android.widget.TextView[@text='ewrwrw'])[1]"));
            Assert.assertTrue(task_description.isDisplayed(), "Task description (task page) is not visible!");
            System.out.println("Task description (task page) exists!");

            WebElement task_status = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='To Do']"));
            Assert.assertTrue(task_status.isDisplayed(), "Task status (task page) is not visible!");
            System.out.println("Task status (task page) exists!");

            WebElement description_title = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='Description']"));
            Assert.assertTrue(description_title.isDisplayed(), "Description title is not visible!");
            System.out.println("Description title exists!");

            WebElement task_description2 = driver.findElement(AppiumBy.xpath("(//android.widget.TextView[@text='ewrwrw'])[2]"));
            Assert.assertTrue(task_description2.isDisplayed(), "Task description 2 (task page) is not visible!");
            System.out.println("Task description 2 (task page) exists!");

            WebElement members_title = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='Assigned Members']"));
            Assert.assertTrue(members_title.isDisplayed(), "Assigned Members title is not visible!");
            System.out.println("Assigned Members title exists!");

            WebElement task_members = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='0 members assigned']"));
            Assert.assertTrue(task_members.isDisplayed(), "Task members (task page) is not visible!");
            System.out.println("Task members (task page) exists!");

            driver.navigate().back();
        } catch (NoSuchElementException e) {
            Assert.fail("Element not found: " + e.getMessage());
        } catch (Exception e) {
            Assert.fail("Unexpected error: " + e.getMessage());
        }
    }

    @Test
    public void taskCard_clickTriggersNavigation_teamPage() {
        try {
            WebElement team_name = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='Team A']"));
            Assert.assertTrue(team_name.isDisplayed(), "Team name is not visible!");
            System.out.println("Team name exists!");

            team_name.click();

            WebElement task_name2 = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='erwrwrw']"));
            Assert.assertTrue(task_name2.isDisplayed(), "Task name (team page) is not visible!");
            System.out.println("Task name (team page) exists!");

            task_name2.click();

            WebElement task_name3 = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='erwrwrw']"));
            Assert.assertTrue(task_name3.isDisplayed(), "Task name (task page) is not visible!");
            System.out.println("Task name exists!");

            WebElement task_description = driver.findElement(AppiumBy.xpath("(//android.widget.TextView[@text='ewrwrw'])[1]"));
            Assert.assertTrue(task_description.isDisplayed(), "Task description (task page) is not visible!");
            System.out.println("Task description (task page) exists!");

            WebElement task_status = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='To Do']"));
            Assert.assertTrue(task_status.isDisplayed(), "Task status (task page) is not visible!");
            System.out.println("Task status (task page) exists!");

            WebElement description_title = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='Description']"));
            Assert.assertTrue(description_title.isDisplayed(), "Description title is not visible!");
            System.out.println("Description title exists!");

            WebElement task_description2 = driver.findElement(AppiumBy.xpath("(//android.widget.TextView[@text='ewrwrw'])[2]"));
            Assert.assertTrue(task_description2.isDisplayed(), "Task description 2 (task page) is not visible!");
            System.out.println("Task description 2 (task page) exists!");

            WebElement members_title = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='Assigned Members']"));
            Assert.assertTrue(members_title.isDisplayed(), "Assigned Members title is not visible!");
            System.out.println("Assigned Members title exists!");

            WebElement task_members = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='0 members assigned']"));
            Assert.assertTrue(task_members.isDisplayed(), "Task members (task page) is not visible!");
            System.out.println("Task members (task page) exists!");

            driver.navigate().back();
            driver.navigate().back();
        } catch (NoSuchElementException e) {
            Assert.fail("Element not found: " + e.getMessage());
        } catch (Exception e) {
            Assert.fail("Unexpected error: " + e.getMessage());
        }
    }

    @Test
    public void sortButton_callsToggleSortMode() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    AppiumBy.xpath("//androidx.compose.ui.platform.ComposeView/android.view.View/android.view.View/android.view.View[1]/android.view.View[3]/android.view.View[1]")
            ));

            WebElement sort_tasks_button = driver.findElement(AppiumBy.xpath("//android.view.View[@content-desc='Sort tasks']"));
            Assert.assertTrue(sort_tasks_button.isDisplayed(), "Sort Tasks button is not visible!");
            System.out.println("Sort Tasks button exists!");

            sort_tasks_button.click();

            WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(10));

            wait1.until(ExpectedConditions.visibilityOfElementLocated(
                    AppiumBy.xpath("//androidx.compose.ui.platform.ComposeView/android.view.View/android.view.View/android.view.View[1]/android.view.View[3]/android.view.View[1]")
            ));

            WebElement first_task_view = driver.findElement(AppiumBy.xpath("//androidx.compose.ui.platform.ComposeView/android.view.View/android.view.View/android.view.View[1]/android.view.View[3]/android.view.View[1]"));
            Assert.assertTrue(first_task_view.isDisplayed(), "View is not visible!");
            System.out.println("View exists!");

            WebElement task_name1 = first_task_view.findElement(AppiumBy.xpath("//android.widget.TextView[@text='dfdfs']"));
            Assert.assertTrue(task_name1.isDisplayed(), "Task name (1) is not visible!");
            System.out.println("Task name (1) exists!");

            sort_tasks_button.click();

            WebDriverWait wait2 = new WebDriverWait(driver, Duration.ofSeconds(10));

            wait2.until(ExpectedConditions.visibilityOfElementLocated(
                    AppiumBy.xpath("//androidx.compose.ui.platform.ComposeView/android.view.View/android.view.View/android.view.View[1]/android.view.View[3]/android.view.View[1]")
            ));

            WebElement last_task_view = driver.findElement(AppiumBy.xpath("//androidx.compose.ui.platform.ComposeView/android.view.View/android.view.View/android.view.View[1]/android.view.View[3]/android.view.View[1]"));
            Assert.assertTrue(last_task_view.isDisplayed(), "View is not visible!");
            System.out.println("View exists!");

            WebElement task_name2 = last_task_view.findElement(AppiumBy.xpath("//android.widget.TextView[@text='task Aa']"));
            Assert.assertTrue(task_name2.isDisplayed(), "Task name (2) is not visible!");
            System.out.println("Task name (2) exists!");

            sort_tasks_button.click();

            WebDriverWait wait3 = new WebDriverWait(driver, Duration.ofSeconds(10));

            wait3.until(ExpectedConditions.visibilityOfElementLocated(
                    AppiumBy.xpath("//androidx.compose.ui.platform.ComposeView/android.view.View/android.view.View/android.view.View[1]/android.view.View[3]/android.view.View[1]")
            ));

            WebElement task_view = driver.findElement(AppiumBy.xpath("//androidx.compose.ui.platform.ComposeView/android.view.View/android.view.View/android.view.View[1]/android.view.View[3]/android.view.View[1]"));
            Assert.assertTrue(task_view.isDisplayed(), "View is not visible!");
            System.out.println("View exists!");

            WebElement task_name3 = task_view.findElement(AppiumBy.xpath("//android.widget.TextView[@text='erwrwrw']"));
            Assert.assertTrue(task_name3.isDisplayed(), "Task name (3) is not visible!");
            System.out.println("Task name (3) exists!");
        } catch (NoSuchElementException e) {
            Assert.fail("Element not found: " + e.getMessage());
        } catch (Exception e) {
            Assert.fail("Unexpected error: " + e.getMessage());
        }
    }

    @Test
    public void fabButton_navigatesToCreateTeam() {
        try {
            WebElement create_team_button = driver.findElement(AppiumBy.xpath("//android.view.View[@content-desc='Create Team']"));
            Assert.assertTrue(create_team_button.isDisplayed(), "Create Team button is not visible!");
            System.out.println("Create Team button exists!");

            create_team_button.click();

            WebElement create_team_title = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='Create New Team']"));
            Assert.assertTrue(create_team_title.isDisplayed(), "Create New Team title is not visible!");
            System.out.println("Create New Team title exists!");

            driver.navigate().back();
        } catch (NoSuchElementException e) {
            Assert.fail("Element not found: " + e.getMessage());
        } catch (Exception e) {
            Assert.fail("Unexpected error: " + e.getMessage());
        }
    }

    @Test
    public void homeScreen_hasCorrectVerticalLayoutStructure() {
        try {
            WebElement welcome_text = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='Welcome, Alessandro']"));
            Assert.assertTrue(welcome_text.isDisplayed(), "Welcome title is not visible!");
            System.out.println("Welcome title exists!");

            WebElement teams_text = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='My Teams']"));
            Assert.assertTrue(teams_text.isDisplayed(), "My Teams title is not visible!");
            System.out.println("My Teams title exists!");

            WebElement tasks_text = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='Tasks']"));
            Assert.assertTrue(tasks_text.isDisplayed(), "Tasks title is not visible!");
            System.out.println("Tasks title exists!");

            int welcomeY = welcome_text.getLocation().getY();
            int teamsY = teams_text.getLocation().getY();
            int tasksY = tasks_text.getLocation().getY();

            Assert.assertTrue(welcomeY < teamsY, "Welcome text is not above My Teams text!");
            Assert.assertTrue(teamsY < tasksY, "My Teams text is not above Tasks text!");

            System.out.println("Elements are correctly stacked vertically!");
        } catch (NoSuchElementException e) {
            Assert.fail("Element not found: " + e.getMessage());
        } catch (Exception e) {
            Assert.fail("Unexpected error: " + e.getMessage());
        }
    }

    @AfterClass
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
