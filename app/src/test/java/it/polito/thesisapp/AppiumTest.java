package it.polito.thesisapp;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
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
import java.util.Collections;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

public class AppiumTest {

    private AndroidDriver driver;

    // Utility function to swipe horizontally (the finger goes from right to left)
    public void swipeLeft(String containerXPath) {
        WebElement container = driver.findElement(AppiumBy.xpath(containerXPath));

        int startY = container.getLocation().getY() + (container.getSize().getHeight() / 2);
        int startX = container.getLocation().getX() + (int) (container.getSize().getWidth() * 0.8);
        int endX = container.getLocation().getX() + (int) (container.getSize().getWidth() * 0.2);

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger1");
        Sequence swipe = new Sequence(finger, 1);

        swipe.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY)); // Start
        swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(1000), PointerInput.Origin.viewport(), endX, startY)); // Move
        swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(swipe));
    }

    // Utility function to swipe horizontally (the finger goes from left to right)
    public void swipeRight(String containerXPath) {
        WebElement container = driver.findElement(AppiumBy.xpath(containerXPath));

        int startY = container.getLocation().getY() + (container.getSize().getHeight() / 2);
        int startX = container.getLocation().getX() + (int) (container.getSize().getWidth() * 0.2);
        int endX = container.getLocation().getX() + (int) (container.getSize().getWidth() * 0.8);

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger1");
        Sequence swipe = new Sequence(finger, 1);

        swipe.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY)); // Start
        swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(1000), PointerInput.Origin.viewport(), endX, startY)); // Move
        swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(swipe));
    }

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
            // Search for message 'Welcome, Alessandro' and verify it is displayed
            WebElement welcome_message = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='Welcome, Alessandro']"));
            Assert.assertTrue(welcome_message.isDisplayed(), "Welcome message is not visible!");
        } catch (NoSuchElementException e) {
            Assert.fail("Element not found: " + e.getMessage());
        } catch (Exception e) {
            Assert.fail("Unexpected error: " + e.getMessage());
        }
    }

    @Test
    public void teamSection_displaysTeams() {
        try {
            // Search for 'Team A' and verify it is displayed
            WebElement team_name = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='Team A']"));
            Assert.assertTrue(team_name.isDisplayed(), "Team name is not visible!");

            // Search for the number of team members of 'Team A' and verify it is displayed
            WebElement team_members = driver.findElement(AppiumBy.xpath("(//android.widget.TextView[@text='3 members'])[1]"));
            Assert.assertTrue(team_members.isDisplayed(), "Team members is not visible!");
        } catch (NoSuchElementException e) {
            Assert.fail("Element not found: " + e.getMessage());
        } catch (Exception e) {
            Assert.fail("Unexpected error: " + e.getMessage());
        }
    }

    @Test
    public void taskSection_displaysTasks() {
        try {
            // 'Team A' is selected
            // Search for the first task name and verify it is displayed
            WebElement task_name1 = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='erwrwrw']"));
            Assert.assertTrue(task_name1.isDisplayed(), "First task name is not visible!");

            // Search for the first task description and verify it is displayed
            WebElement task_description1 = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='ewrwrw']"));
            Assert.assertTrue(task_description1.isDisplayed(), "First task description is not visible!");

            // Search for the second task name and verify it is displayed
            WebElement task_name2 = driver.findElement(AppiumBy.xpath("(//android.widget.TextView[@text='fwefwf'])[1]"));
            Assert.assertTrue(task_name2.isDisplayed(), "Second task name is not visible!");

            // Search for the second task description and verify it is displayed
            WebElement task_description2 = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='fwefewf']"));
            Assert.assertTrue(task_description2.isDisplayed(), "Second task description is not visible!");
        } catch (NoSuchElementException e) {
            Assert.fail("Element not found: " + e.getMessage());
        } catch (Exception e) {
            Assert.fail("Unexpected error: " + e.getMessage());
        }
    }

    @Test
    public void taskStatusChips_areCorrectlyDisplayed() {
        try {
            // Search for the status of the first task and verify it is displayed
            WebElement task_status1 = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='To Do']"));
            Assert.assertTrue(task_status1.isDisplayed(), "First task status is not visible!");

            // Search for the status of the second task and verify it is displayed
            WebElement task_status2 = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='Done']"));
            Assert.assertTrue(task_status2.isDisplayed(), "Second task status is not visible!");
        } catch (NoSuchElementException e) {
            Assert.fail("Element not found: " + e.getMessage());
        } catch (Exception e) {
            Assert.fail("Unexpected error: " + e.getMessage());
        }
    }

    @Test
    public void teamCard_clickTriggersNavigation() {
        try {
            // Search for 'Team A' and open it (by clicking on it)
            WebElement team_name = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='Team A']"));
            Assert.assertTrue(team_name.isDisplayed(), "Team name is not visible!");
            team_name.click();

            // Wait for the page to load (the title 'Team A' should be displayed)
            // TODO: verify if it is necessary
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    AppiumBy.xpath("//android.widget.TextView[@text='Team A']")
            ));

            // Search for the title 'Team A' and the create task fab, and verify they are displayed
            WebElement team_name_title = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='Team A']"));
            Assert.assertTrue(team_name_title.isDisplayed(), "Team name (title) is not visible!");
            WebElement create_task_button = driver.findElement(AppiumBy.xpath("//android.view.View[@content-desc='Create Task']"));
            Assert.assertTrue(create_task_button.isDisplayed(), "Create task button is not visible!");

            // Click the back button of the device to return to the home page
            driver.navigate().back();
        } catch (NoSuchElementException e) {
            Assert.fail("Element not found: " + e.getMessage());
        } catch (Exception e) {
            Assert.fail("Unexpected error: " + e.getMessage());
        }
    }

    @Test
    public void taskCard_clickTriggersNavigation_homePage() {
        try {
            // Click the home button for a "stable" task list
            // TODO: verify if it is necessary
            WebElement homeButton = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='Home']"));
            homeButton.click();

            // Wait for the first task to be displayed
            // TODO: verify if it is necessary
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    AppiumBy.xpath("//android.widget.TextView[@text='erwrwrw']")
            ));

            // Search for the first task, verify it is displayed and open it (by clicking on it)
            WebElement task = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='erwrwrw']"));
            Assert.assertTrue(task.isDisplayed(), "Task is not visible!");
            task.click();

            // Search for the task name (title) and verify it is displayed
            WebElement task_name = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='erwrwrw']"));
            Assert.assertTrue(task_name.isDisplayed(), "Task name (task page) is not visible!");

            // Search for the task description under the title and verify it is displayed
            WebElement task_description = driver.findElement(AppiumBy.xpath("(//android.widget.TextView[@text='ewrwrw'])[1]"));
            Assert.assertTrue(task_description.isDisplayed(), "Task description (task page) is not visible!");

            // Search for the task status and verify it is displayed
            WebElement task_status = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='To Do']"));
            Assert.assertTrue(task_status.isDisplayed(), "Task status (task page) is not visible!");

            // Search for the description part and verify it is displayed
            WebElement description_title = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='Description']"));
            Assert.assertTrue(description_title.isDisplayed(), "Description title is not visible!");
            WebElement task_description2 = driver.findElement(AppiumBy.xpath("(//android.widget.TextView[@text='ewrwrw'])[2]"));
            Assert.assertTrue(task_description2.isDisplayed(), "Task description 2 (task page) is not visible!");

            // Search for the assigned members part and verify it is displayed
            WebElement members_title = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='Assigned Members']"));
            Assert.assertTrue(members_title.isDisplayed(), "Assigned Members title is not visible!");
            WebElement task_members = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='0 members assigned']"));
            Assert.assertTrue(task_members.isDisplayed(), "Task members (task page) is not visible!");

            // Click the back button of the device to return to the home page
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
            // Open 'Team A'
            WebElement team_name = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='Team A']"));
            Assert.assertTrue(team_name.isDisplayed(), "Team name is not visible!");
            team_name.click();

            // Open the first task
            WebElement task = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='erwrwrw']"));
            Assert.assertTrue(task.isDisplayed(), "Task is not visible!");
            task.click();

            // Verify everything is present in the task page
            WebElement task_name = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='erwrwrw']"));
            Assert.assertTrue(task_name.isDisplayed(), "Task name (task page) is not visible!");

            WebElement task_description = driver.findElement(AppiumBy.xpath("(//android.widget.TextView[@text='ewrwrw'])[1]"));
            Assert.assertTrue(task_description.isDisplayed(), "Task description (task page) is not visible!");

            WebElement task_status = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='To Do']"));
            Assert.assertTrue(task_status.isDisplayed(), "Task status (task page) is not visible!");

            WebElement description_title = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='Description']"));
            Assert.assertTrue(description_title.isDisplayed(), "Description title is not visible!");

            WebElement task_description2 = driver.findElement(AppiumBy.xpath("(//android.widget.TextView[@text='ewrwrw'])[2]"));
            Assert.assertTrue(task_description2.isDisplayed(), "Task description 2 (task page) is not visible!");

            WebElement members_title = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='Assigned Members']"));
            Assert.assertTrue(members_title.isDisplayed(), "Assigned Members title is not visible!");

            WebElement task_members = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='0 members assigned']"));
            Assert.assertTrue(task_members.isDisplayed(), "Task members (task page) is not visible!");

            // Click the back button of the device 2 times to return to the home page
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
            // Wait for the view that contains the first task to be visible
            // TODO: verify if it is necessary
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    AppiumBy.xpath("//androidx.compose.ui.platform.ComposeView/android.view.View/android.view.View/android.view.View[1]/android.view.View[3]/android.view.View[1]")
            ));

            // Search for the sort button, verify it is displayed and click on it (tasks now ordered a-z)
            WebElement sort_tasks_button = driver.findElement(AppiumBy.xpath("//android.view.View[@content-desc='Sort tasks']"));
            Assert.assertTrue(sort_tasks_button.isDisplayed(), "Sort Tasks button is not visible!");
            sort_tasks_button.click();

            // Wait for the view that contains the first task to be visible
            // TODO: verify if it is necessary
            WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait1.until(ExpectedConditions.visibilityOfElementLocated(
                    AppiumBy.xpath("//androidx.compose.ui.platform.ComposeView/android.view.View/android.view.View/android.view.View[1]/android.view.View[3]/android.view.View[1]")
            ));

            // Search for the view that contains the first task and verify it contains the first task alphabetically
            WebElement first_task_view = driver.findElement(AppiumBy.xpath("//androidx.compose.ui.platform.ComposeView/android.view.View/android.view.View/android.view.View[1]/android.view.View[3]/android.view.View[1]"));
            Assert.assertTrue(first_task_view.isDisplayed(), "View is not visible!");
            WebElement task_name1 = first_task_view.findElement(AppiumBy.xpath("//android.widget.TextView[@text='dfdfs']"));
            Assert.assertTrue(task_name1.isDisplayed(), "Task name (1) is not visible!");

            // Click the sort button (tasks now ordered z-a)
            sort_tasks_button.click();

            // Wait for the view that contains the first task to be visible
            // TODO: verify if it is necessary
            WebDriverWait wait2 = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait2.until(ExpectedConditions.visibilityOfElementLocated(
                    AppiumBy.xpath("//androidx.compose.ui.platform.ComposeView/android.view.View/android.view.View/android.view.View[1]/android.view.View[3]/android.view.View[1]")
            ));

            // Search for the view that contains the first task and verify it contains the last task alphabetically
            WebElement last_task_view = driver.findElement(AppiumBy.xpath("//androidx.compose.ui.platform.ComposeView/android.view.View/android.view.View/android.view.View[1]/android.view.View[3]/android.view.View[1]"));
            Assert.assertTrue(last_task_view.isDisplayed(), "View is not visible!");
            WebElement task_name2 = last_task_view.findElement(AppiumBy.xpath("//android.widget.TextView[@text='task Aa']"));
            Assert.assertTrue(task_name2.isDisplayed(), "Task name (2) is not visible!");

            // Click the sort button (tasks now not ordered)
            sort_tasks_button.click();

            // Wait for the view that contains the first task to be visible
            // TODO: verify if it is necessary
            WebDriverWait wait3 = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait3.until(ExpectedConditions.visibilityOfElementLocated(
                    AppiumBy.xpath("//androidx.compose.ui.platform.ComposeView/android.view.View/android.view.View/android.view.View[1]/android.view.View[3]/android.view.View[1]")
            ));

            // Search for the view that contains the first task and verify it contains the expected task
            WebElement task_view = driver.findElement(AppiumBy.xpath("//androidx.compose.ui.platform.ComposeView/android.view.View/android.view.View/android.view.View[1]/android.view.View[3]/android.view.View[1]"));
            Assert.assertTrue(task_view.isDisplayed(), "View is not visible!");
            WebElement task_name3 = task_view.findElement(AppiumBy.xpath("//android.widget.TextView[@text='erwrwrw']"));
            Assert.assertTrue(task_name3.isDisplayed(), "Task name (3) is not visible!");
        } catch (NoSuchElementException e) {
            Assert.fail("Element not found: " + e.getMessage());
        } catch (Exception e) {
            Assert.fail("Unexpected error: " + e.getMessage());
        }
    }

    @Test
    public void fabButton_navigatesToCreateTeam() {
        try {
            // Search for the create team fab, verify it is displayed and click on it (open create team page)
            WebElement create_team_button = driver.findElement(AppiumBy.xpath("//android.view.View[@content-desc='Create Team']"));
            Assert.assertTrue(create_team_button.isDisplayed(), "Create Team button is not visible!");
            create_team_button.click();

            // Search for the 'Create New Team' title and verify it is displayed
            WebElement create_team_title = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='Create New Team']"));
            Assert.assertTrue(create_team_title.isDisplayed(), "Create New Team title is not visible!");

            // Click the back button of the device to return to the home page
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
            // Search for 'Welcome, Alessandro', 'My Teams' title and 'Tasks' title, and verify they are displayed
            WebElement welcome_text = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='Welcome, Alessandro']"));
            Assert.assertTrue(welcome_text.isDisplayed(), "Welcome title is not visible!");

            WebElement teams_text = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='My Teams']"));
            Assert.assertTrue(teams_text.isDisplayed(), "My Teams title is not visible!");

            WebElement tasks_text = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='Tasks']"));
            Assert.assertTrue(tasks_text.isDisplayed(), "Tasks title is not visible!");

            // Verify they are correctly stacked one under the other
            int welcomeY = welcome_text.getLocation().getY();
            int teamsY = teams_text.getLocation().getY();
            int tasksY = tasks_text.getLocation().getY();

            Assert.assertTrue(welcomeY < teamsY, "Welcome text is not above My Teams text!");
            Assert.assertTrue(teamsY < tasksY, "My Teams text is not above Tasks text!");
        } catch (NoSuchElementException e) {
            Assert.fail("Element not found: " + e.getMessage());
        } catch (Exception e) {
            Assert.fail("Unexpected error: " + e.getMessage());
        }
    }

    @Test
    public void taskCard_showsCorrectAssignedMembersCount() {
        try {
            // Wait for the view that contains the first task to be visible
            // TODO: verify if it is necessary
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    AppiumBy.xpath("//androidx.compose.ui.platform.ComposeView/android.view.View/android.view.View/android.view.View[1]/android.view.View[3]/android.view.View[1]")
            ));

            // Search for the sort button, verify it is displayed and click on it (tasks now ordered a-z)
            WebElement sort_tasks_button = driver.findElement(AppiumBy.xpath("//android.view.View[@content-desc='Sort tasks']"));
            Assert.assertTrue(sort_tasks_button.isDisplayed(), "Sort Tasks button is not visible!");
            sort_tasks_button.click();

            // Wait for the view that contains the first task to be visible
            // TODO: verify if it is necessary
            WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait1.until(ExpectedConditions.visibilityOfElementLocated(
                    AppiumBy.xpath("//androidx.compose.ui.platform.ComposeView/android.view.View/android.view.View/android.view.View[1]/android.view.View[3]/android.view.View[1]")
            ));

            // Search for the view that contains the first task and verify it contains the first task alphabetically
            WebElement first_task_view = driver.findElement(AppiumBy.xpath("//androidx.compose.ui.platform.ComposeView/android.view.View/android.view.View/android.view.View[1]/android.view.View[3]/android.view.View[1]"));
            Assert.assertTrue(first_task_view.isDisplayed(), "View is not visible!");
            WebElement task_name1 = first_task_view.findElement(AppiumBy.xpath("//android.widget.TextView[@text='dfdfs']"));
            Assert.assertTrue(task_name1.isDisplayed(), "Task name (1) is not visible!");

            // Search for the number of assigned members on the task and verify it is displayed and it is correct
            WebElement task_members1 = first_task_view.findElement(AppiumBy.xpath("(//android.widget.TextView[@text='0 assigned members'])[1]"));
            Assert.assertTrue(task_members1.isDisplayed(), "Task members (1) is not visible!");

            // Click the sort button (tasks now ordered z-a)
            sort_tasks_button.click();

            // Wait for the view that contains the first task to be visible
            // TODO: verify if it is necessary
            WebDriverWait wait2 = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait2.until(ExpectedConditions.visibilityOfElementLocated(
                    AppiumBy.xpath("//androidx.compose.ui.platform.ComposeView/android.view.View/android.view.View/android.view.View[1]/android.view.View[3]/android.view.View[1]")
            ));

            // Search for the view that contains the first task and verify it contains the last task alphabetically
            WebElement last_task_view = driver.findElement(AppiumBy.xpath("//androidx.compose.ui.platform.ComposeView/android.view.View/android.view.View/android.view.View[1]/android.view.View[3]/android.view.View[1]"));
            Assert.assertTrue(last_task_view.isDisplayed(), "View is not visible!");
            WebElement task_name2 = last_task_view.findElement(AppiumBy.xpath("//android.widget.TextView[@text='task Aa']"));
            Assert.assertTrue(task_name2.isDisplayed(), "Task name (2) is not visible!");

            // Search for the number of assigned members on the task and verify it is displayed and it is correct
            WebElement task_members2 = last_task_view.findElement(AppiumBy.xpath("//android.widget.TextView[@text='2 assigned members']"));
            Assert.assertTrue(task_members2.isDisplayed(), "Task members (2) is not visible!");

            // Click the sort button (tasks now not ordered)
            sort_tasks_button.click();

            // Wait for the view that contains the first task to be visible
            // TODO: verify if it is necessary
            WebDriverWait wait3 = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait3.until(ExpectedConditions.visibilityOfElementLocated(
                    AppiumBy.xpath("//androidx.compose.ui.platform.ComposeView/android.view.View/android.view.View/android.view.View[1]/android.view.View[3]/android.view.View[1]")
            ));

            // Search for the view that contains the first task and verify it contains the expected task
            WebElement task_view = driver.findElement(AppiumBy.xpath("//androidx.compose.ui.platform.ComposeView/android.view.View/android.view.View/android.view.View[1]/android.view.View[3]/android.view.View[1]"));
            Assert.assertTrue(task_view.isDisplayed(), "View is not visible!");
            WebElement task_name3 = task_view.findElement(AppiumBy.xpath("//android.widget.TextView[@text='erwrwrw']"));
            Assert.assertTrue(task_name3.isDisplayed(), "Task name (3) is not visible!");

            // Search for the number of assigned members on the task and verify it is displayed and it is correct
            WebElement task_members3 = task_view.findElement(AppiumBy.xpath("(//android.widget.TextView[@text='0 assigned members'])[1]"));
            Assert.assertTrue(task_members3.isDisplayed(), "Task members (3) is not visible!");
        } catch (NoSuchElementException e) {
            Assert.fail("Element not found: " + e.getMessage());
        } catch (Exception e) {
            Assert.fail("Unexpected error: " + e.getMessage());
        }
    }

    @Test
    public void teamCard_showsCorrectMemberCount() {
        try {
            // Search for the team members of the first team, verify in is displayed and correct
            WebElement team_members = driver.findElement(AppiumBy.xpath("(//android.widget.TextView[@text='3 members'])[1]"));
            Assert.assertTrue(team_members.isDisplayed(), "Team members is not visible!");

            // Search for the team members of the second team, verify in is displayed and correct
            WebElement team_members2 = driver.findElement(AppiumBy.xpath("(//android.widget.TextView[@text='3 members'])[2]"));
            Assert.assertTrue(team_members2.isDisplayed(), "Team members (2) is not visible!");
        } catch (NoSuchElementException e) {
            Assert.fail("Element not found: " + e.getMessage());
        } catch (Exception e) {
            Assert.fail("Unexpected error: " + e.getMessage());
        }
    }

    @Test
    public void sectionHeaders_areDisplayedCorrectly() {
        try {
            // Verify 'My Teams' header is displayed
            WebElement teams_text = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='My Teams']"));
            Assert.assertTrue(teams_text.isDisplayed(), "My Teams title is not visible!");

            //Verify 'Tasks' header is displayed
            WebElement tasks_text = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='Tasks']"));
            Assert.assertTrue(tasks_text.isDisplayed(), "Tasks title is not visible!");
        } catch (NoSuchElementException e) {
            Assert.fail("Element not found: " + e.getMessage());
        } catch (Exception e) {
            Assert.fail("Unexpected error: " + e.getMessage());
        }
    }

    @Test
    public void teamPager_swipeUpdatesSelectedTeam() {
        int maxSwipes = 10;
        int swipes = 0;

        try {
            // Wait for the teams to be loaded
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    AppiumBy.xpath("//android.widget.TextView[@text='Team A']")
            ));

            // Swipe until the first task of the searched team is visible
            String teams_view_xpath = "//androidx.compose.ui.platform.ComposeView/android.view.View/android.view.View/android.view.View[1]/android.view.View[1]";
            while (driver.findElements(AppiumBy.xpath("//android.widget.TextView[@text=\"sdada\"]")).isEmpty() && swipes < maxSwipes) {
                swipeLeft(teams_view_xpath);
                swipes++;
            }

            // Verify the first task of the searched team is visible
            WebElement task_searched = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text=\"sdada\"]"));
            Assert.assertTrue(task_searched.isDisplayed(), "The task is not displayed!");

            // Swipe until the first task of the first team is visible
            swipes = 0;
            while (driver.findElements(AppiumBy.xpath("//android.widget.TextView[@text=\"erwrwrw\"]")).isEmpty() && swipes < maxSwipes) {
                swipeRight(teams_view_xpath);
                swipes++;
            }

            // Verify the first task of the first team is visible
            WebElement task_first = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text=\"erwrwrw\"]"));
            Assert.assertTrue(task_first.isDisplayed(), "The task is not displayed!");
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
