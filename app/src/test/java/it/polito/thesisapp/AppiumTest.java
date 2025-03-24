package it.polito.thesisapp;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
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
import java.util.List;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

public class AppiumTest {

    private AndroidDriver driver;

    @BeforeClass
    public void setup() throws URISyntaxException, MalformedURLException {
        UiAutomator2Options options = new UiAutomator2Options()
                .setDeviceName("emulator-5554")
                .setApp("C:/Users/pole9/Desktop/Poli/Tesi/App_tesi/ThesisApp/app/build/intermediates/apk/debug/app-debug.apk")
                .setAutomationName("UiAutomator2");

        driver = new AndroidDriver(new URI("http://127.0.0.1:4723").toURL(), options);

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @Test
    public void testWelcomeTitleExists() {
        try {
            WebElement text = driver.findElement(AppiumBy.xpath("//android.widget.TextView[contains(@text, 'Welcome, ')]"));
            Assert.assertTrue(text.isDisplayed(), "'Welcome' title is not visible!");
            System.out.println("'Welcome' title exists!");
        } catch (NoSuchElementException e) {
            Assert.fail("'Welcome' title does not exist!");
        }
    }

    @Test
    public void testMyTeamsTitleExists() {
        try {
            WebElement text = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='My Teams']"));
            Assert.assertTrue(text.isDisplayed(), "'My Teams' title is not visible!");
            System.out.println("'My Teams' title exists!");
        } catch (NoSuchElementException e) {
            Assert.fail("'My Teams' title does not exist!");
        }
    }

    @Test
    public void testTasksTitleExists() {
        try {
            WebElement text = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='Tasks']"));
            Assert.assertTrue(text.isDisplayed(), "'Tasks' title is not visible!");
            System.out.println("'Tasks' title exists!");
        } catch (NoSuchElementException e) {
            Assert.fail("'Tasks' title does not exist!");
        }
    }

    @Test
    public void testCreateTeamButton() {
        try {
            WebElement createTeamButton = driver.findElement(AppiumBy.xpath("//android.view.View[@content-desc='Create Team']"));
            Assert.assertTrue(createTeamButton.isDisplayed(), "'Create Team' button is not visible!");
            System.out.println("'Create Team' button exists!");
        } catch (NoSuchElementException e) {
            Assert.fail("'Create Team' button does not exist!");
        }
    }

    @Test
    public void testHomeButton() {
        try {
            WebElement homeButton = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='Home']"));
            Assert.assertTrue(homeButton.isDisplayed(), "'Home' button is not visible!");
            System.out.println("'Home' button exists!");
        } catch (NoSuchElementException e) {
            Assert.fail("'Home' button does not exist!");
        }
    }

    @Test
    public void testProfileButton() {
        try {
            WebElement profileButton = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='Profile']"));
            Assert.assertTrue(profileButton.isDisplayed(), "'Profile' button is not visible!");
            System.out.println("'Profile' button exists!");
        } catch (NoSuchElementException e) {
            Assert.fail("'Profile' button does not exist!");
        }
    }

    @Test
    public void testProfileButtonSwitch() {
        try {
            WebElement profileButton = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='Profile']"));
            profileButton.click();

            WebElement nameInitials = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='AF']"));
            Assert.assertTrue(nameInitials.isDisplayed(), "Initials are not visible!");
            System.out.println("Initials exists!");

            WebElement homeButton = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='Home']"));
            homeButton.click();
        } catch (NoSuchElementException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testSortTasksButton() {
        try {
            WebElement createTeamButton = driver.findElement(AppiumBy.xpath("//android.view.View[@content-desc='Sort tasks']"));
            Assert.assertTrue(createTeamButton.isDisplayed(), "'Sort Tasks' button is not visible!");
            System.out.println("'Sort Tasks' button exists!");
        } catch (NoSuchElementException e) {
            Assert.fail("'Sort Tasks' button does not exist!");
        }
    }

    @Test
    public void testCreateTeamButtonSwitch() {
        try {
            WebElement createTeamButton = driver.findElement(AppiumBy.xpath("//android.view.View[@content-desc='Create Team']"));
            createTeamButton.click();
            WebElement createTeamTitle = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='Create New Team']"));
            Assert.assertTrue(createTeamTitle.isDisplayed(), "'Create New Team' title is not visible!");
            System.out.println("'Create New Team' title exists!");
            driver.navigate().back();
        } catch (NoSuchElementException e) {
            Assert.fail("'Create New Team' title does not exist!");
        }
    }

    @Test
    public void testFindTeamScrolling() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    AppiumBy.xpath("//android.widget.TextView[@text='Team A']")
            ));

            WebElement teamView = driver.findElement(AppiumBy.xpath("//androidx.compose.ui.platform.ComposeView/android.view.View/android.view.View/android.view.View[1]/android.view.View[1]"));

            Dimension size = teamView.getSize();
            Point location = teamView.getLocation();

            int startX = location.getX() + (int) (size.width * 0.6);
            int endX = location.getX() + (int) (size.width * 0.2);
            int y = location.getY() + size.height / 2;

            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence scrollSequence = new Sequence(finger, 0);
            scrollSequence.addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), startX, y));
            scrollSequence.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
            scrollSequence.addAction(finger.createPointerMove(Duration.ofMillis(1000), PointerInput.Origin.viewport(), endX, y));
            scrollSequence.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

            driver.perform(List.of(scrollSequence));

            WebElement team = teamView.findElement(AppiumBy.xpath("//android.widget.TextView[@text='vsdfsd']"));

            Assert.assertTrue(team.isDisplayed(), "The team searched is not visible!");
            System.out.println("The team exists!");

            WebElement homeButton = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='Home']"));
            homeButton.click();
        } catch (NoSuchElementException e) {
            Assert.fail("The team does not exist!");
        }
    }

    @Test
    public void testTeamClickSwitch() {
        try {
            WebElement teamName = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='Team A']"));
            Assert.assertTrue(teamName.isDisplayed(), "Team name is not visible!");
            System.out.println("Team name exists!");

            teamName.click();

            WebElement teamNameTitle = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='Team A']"));
            Assert.assertTrue(teamNameTitle.isDisplayed(), "Team name (title) is not visible!");
            System.out.println("Team name (title) exists!");

            WebElement teamMembers = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='3 members']"));
            Assert.assertTrue(teamMembers.isDisplayed(), "Team members is not visible!");
            System.out.println("Team members exists!");

            WebElement createTaskButton = driver.findElement(AppiumBy.xpath("//android.view.View[@content-desc='Create Task']"));
            Assert.assertTrue(createTaskButton.isDisplayed(), "'createTaskButton' is not visible!");
            System.out.println("'createTaskButton' exists!");

            driver.navigate().back();
        } catch (NoSuchElementException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testCreateTaskButton() {
        try {
            WebElement teamName = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='Team A']"));
            teamName.click();

            WebElement createTaskButton = driver.findElement(AppiumBy.xpath("//android.view.View[@content-desc='Create Task']"));
            createTaskButton.click();

            WebElement createTaskTitle = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='Create New Task']"));
            Assert.assertTrue(createTaskTitle.isDisplayed(), "Title is not visible!");
            System.out.println("Title exists!");

            WebElement taskName = driver.findElement(AppiumBy.xpath("//androidx.compose.ui.platform.ComposeView/android.view.View/android.view.View/android.view.View[1]/android.widget.EditText[1]"));
            Assert.assertTrue(taskName.isDisplayed(), "Task Name box is not visible!");
            System.out.println("Task Name box exists!");

            WebElement taskNameText = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='Task Name']"));
            Assert.assertTrue(taskNameText.isDisplayed(), "Task Name text is not visible!");
            System.out.println("Task Name text exists!");

            WebElement taskDescription = driver.findElement(AppiumBy.xpath("//androidx.compose.ui.platform.ComposeView/android.view.View/android.view.View/android.view.View[1]/android.widget.EditText[2]"));
            Assert.assertTrue(taskDescription.isDisplayed(), "Task Description box is not visible!");
            System.out.println("Task Description box exists!");

            WebElement taskDescriptionText = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='Task Description']"));
            Assert.assertTrue(taskDescriptionText.isDisplayed(), "Task Description text is not visible!");
            System.out.println("Task Description text exists!");

            WebElement saveTaskButton = driver.findElement(AppiumBy.xpath("//android.view.View[@content-desc='Save Task']"));
            Assert.assertTrue(saveTaskButton.isDisplayed(), "Save Task Button is not visible!");
            System.out.println("Save Task Button exists!");

            driver.navigate().back();
            driver.navigate().back();
        } catch (NoSuchElementException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testOpenTaskFromMainPage() {
        try {
            WebElement homeButton = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='Home']"));
            homeButton.click();

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    AppiumBy.xpath("//android.widget.TextView[@text='erwrwrw']")
            ));

            WebElement view = driver.findElement(AppiumBy.xpath("//androidx.compose.ui.platform.ComposeView/android.view.View/android.view.View/android.view.View[1]/android.view.View[3]/android.view.View[1]"));
            Assert.assertTrue(view.isDisplayed(), "View is not visible!");
            System.out.println("View exists!");

            WebElement task = view.findElement(AppiumBy.xpath("//android.widget.TextView[@text='erwrwrw']"));
            Assert.assertTrue(task.isDisplayed(), "Task is not visible!");
            System.out.println("Task exists!");

            WebElement taskStatus = view.findElement(AppiumBy.xpath("//android.widget.TextView[@text='To Do']"));
            Assert.assertTrue(taskStatus.isDisplayed(), "Task status is not visible!");
            System.out.println("Task status exists!");

            WebElement taskDescription = view.findElement(AppiumBy.xpath("//android.widget.TextView[@text='ewrwrw']"));
            Assert.assertTrue(taskDescription.isDisplayed(), "Task description is not visible!");
            System.out.println("Task description exists!");

            WebElement taskMembers = view.findElement(AppiumBy.xpath("(//android.widget.TextView[@text='0 assigned members'])[1]"));
            Assert.assertTrue(taskMembers.isDisplayed(), "Task members is not visible!");
            System.out.println("Task members exists!");

            task.click();

            WebElement taskName = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='erwrwrw']"));
            Assert.assertTrue(taskName.isDisplayed(), "Task name is not visible!");
            System.out.println("Task name exists!");

            WebElement taskDescription2 = driver.findElement(AppiumBy.xpath("(//android.widget.TextView[@text='ewrwrw'])[1]"));
            Assert.assertTrue(taskDescription2.isDisplayed(), "Task description (task page) is not visible!");
            System.out.println("Task description (task page) exists!");

            WebElement taskStatus2 = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='To Do']"));
            Assert.assertTrue(taskStatus2.isDisplayed(), "Task status (task page) is not visible!");
            System.out.println("Task status (task page) exists!");

            WebElement description = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='Description']"));
            Assert.assertTrue(description.isDisplayed(), "'Description' is not visible!");
            System.out.println("'Description' exists!");

            WebElement taskDescription3 = driver.findElement(AppiumBy.xpath("(//android.widget.TextView[@text='ewrwrw'])[2]"));
            Assert.assertTrue(taskDescription3.isDisplayed(), "Task description 2 (task page) is not visible!");
            System.out.println("Task description 2 (task page) exists!");

            WebElement members = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='Assigned Members']"));
            Assert.assertTrue(members.isDisplayed(), "'Assigned Members' is not visible!");
            System.out.println("'Assigned Members' exists!");

            WebElement taskMembers2 = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='0 members assigned']"));
            Assert.assertTrue(taskMembers2.isDisplayed(), "Task members (task page) is not visible!");
            System.out.println("Task members (task page) exists!");

            driver.navigate().back();
        } catch (NoSuchElementException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testOpenTaskFromTeamPage() {
        try {
            WebElement teamName = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='Team A']"));
            Assert.assertTrue(teamName.isDisplayed(), "Team name is not visible!");
            System.out.println("Team name exists!");

            teamName.click();

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    AppiumBy.xpath("//androidx.compose.ui.platform.ComposeView/android.view.View/android.view.View/android.view.View[1]/android.view.View[2]/android.view.View[1]")
            ));

            WebElement view = driver.findElement(AppiumBy.xpath("//androidx.compose.ui.platform.ComposeView/android.view.View/android.view.View/android.view.View[1]/android.view.View[2]/android.view.View[1]"));
            Assert.assertTrue(view.isDisplayed(), "View is not visible!");
            System.out.println("View exists!");

            WebElement task = view.findElement(AppiumBy.xpath("//android.widget.TextView[@text='erwrwrw']"));
            Assert.assertTrue(task.isDisplayed(), "Task is not visible!");
            System.out.println("Task exists!");

            WebElement taskStatus = view.findElement(AppiumBy.xpath("//android.widget.TextView[@text='To Do']"));
            Assert.assertTrue(taskStatus.isDisplayed(), "Task status is not visible!");
            System.out.println("Task status exists!");

            WebElement taskDescription = view.findElement(AppiumBy.xpath("//android.widget.TextView[@text='ewrwrw']"));
            Assert.assertTrue(taskDescription.isDisplayed(), "Task description is not visible!");
            System.out.println("Task description exists!");

            WebElement taskMembers = view.findElement(AppiumBy.xpath("(//android.widget.TextView[@text='0 assigned members'])[1]"));
            Assert.assertTrue(taskMembers.isDisplayed(), "Task members is not visible!");
            System.out.println("Task members exists!");

            task.click();

            WebElement taskName = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='erwrwrw']"));
            Assert.assertTrue(taskName.isDisplayed(), "Task name is not visible!");
            System.out.println("Task name exists!");

            WebElement taskDescription2 = driver.findElement(AppiumBy.xpath("(//android.widget.TextView[@text='ewrwrw'])[1]"));
            Assert.assertTrue(taskDescription2.isDisplayed(), "Task description (task page) is not visible!");
            System.out.println("Task description (task page) exists!");

            WebElement taskStatus2 = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='To Do']"));
            Assert.assertTrue(taskStatus2.isDisplayed(), "Task status (task page) is not visible!");
            System.out.println("Task status (task page) exists!");

            WebElement description = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='Description']"));
            Assert.assertTrue(description.isDisplayed(), "'Description' is not visible!");
            System.out.println("'Description' exists!");

            WebElement taskDescription3 = driver.findElement(AppiumBy.xpath("(//android.widget.TextView[@text='ewrwrw'])[2]"));
            Assert.assertTrue(taskDescription3.isDisplayed(), "Task description 2 (task page) is not visible!");
            System.out.println("Task description 2 (task page) exists!");

            WebElement members = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='Assigned Members']"));
            Assert.assertTrue(members.isDisplayed(), "'Assigned Members' is not visible!");
            System.out.println("'Assigned Members' exists!");

            WebElement taskMembers2 = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='0 members assigned']"));
            Assert.assertTrue(taskMembers2.isDisplayed(), "Task members (task page) is not visible!");
            System.out.println("Task members (task page) exists!");

            driver.navigate().back();
            driver.navigate().back();
        } catch (NoSuchElementException e) {
            Assert.fail(e.getMessage());
        }
    }

    @AfterClass
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
