package ua.edu.znu.rozetka;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
/**
 * Tests for the registration functionality.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RegistrationTest {

    private static AndroidDriver driver;

    @BeforeAll
    public static void setUp() throws MalformedURLException {
        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName("Android")
                .setPlatformVersion("11.0")
                .setDeviceName("CustomPhone")
                .setAutomationName("UiAutomator2")
                .setApp("C:\\Users\\nikol\\Appium\\Rozetka\\apps\\rozetka_5.40.1.apk")
                .eventTimings();
        URL remoteUrl = new URL("http://127.0.0.1:4723/wd/hub/");
        driver = new AndroidDriver(remoteUrl, options);

        /*Close banner on the launch*/
        closeFirstBanner();
    }

    /**
     * Test case to verify opening the registration menu.
     */
    @Test
    @Order(1)
    public void openRegistrationMenuTest() {
        WebElement moreOptionsButton = driver.findElement(AppiumBy.xpath("//android.widget.FrameLayout[@content-desc=\"Ще\"]/android.widget.FrameLayout/android.widget.ImageView"));
        moreOptionsButton.click();
        WebElement signUpButton = driver.findElement(AppiumBy.id("ua.com.rozetka.shop:id/item_menu_auth_tv_sign_up"));
        signUpButton.click();
    }

    /**
     * Test case to perform registration with parameterized data.
     *
     * @param lastName     the last name of the user
     * @param firstName    the first name of the user
     * @param phoneNumber  the phone number of the user
     * @param mail         the email address of the user
     * @param password     the password of the user
     */
    @ParameterizedTest (name = "last name: {0}, first name: {1}, phoneNumber: {2}, mail: {3}, password: {4}")
    @Order(2)
    @CsvSource({
            "Бойко, Микола, 509871350, yourusername1@gmail.com, Password1",
            "Ігнатенко, Данило, 966047217, yourusername1@hotmail.com, Password2",
            "Шевченко, Сергій, 683180433, yourusername1@outlook.com, Password3",
            "Шостак, Олександр, 639571063, yourusername1@icloud.com, Password4"
    })
    void registrationTest(String lastName, String firstName, String phoneNumber, String mail, String password) {
        WebElement lastNameField = driver.findElement(AppiumBy.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.widget.FrameLayout/android.view.ViewGroup/androidx.viewpager.widget.ViewPager/androidx.recyclerview.widget.RecyclerView/android.widget.FrameLayout/android.widget.ScrollView/android.widget.LinearLayout/android.widget.LinearLayout[1]/android.widget.FrameLayout/android.widget.EditText"));
        lastNameField.sendKeys(lastName);
        WebElement firstNameField = driver.findElement(AppiumBy.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.widget.FrameLayout/android.view.ViewGroup/androidx.viewpager.widget.ViewPager/androidx.recyclerview.widget.RecyclerView/android.widget.FrameLayout/android.widget.ScrollView/android.widget.LinearLayout/android.widget.LinearLayout[2]/android.widget.FrameLayout/android.widget.EditText"));
        firstNameField.sendKeys(firstName);
        WebElement phoneNumberField = driver.findElement(AppiumBy.id("ua.com.rozetka.shop:id/et_phone"));
        phoneNumberField.sendKeys(phoneNumber);
        WebElement mailField = driver.findElement(AppiumBy.id("ua.com.rozetka.shop:id/et_email"));
        mailField.sendKeys(mail);
        WebElement passwordField = driver.findElement(AppiumBy.id("ua.com.rozetka.shop:id/et_password"));
        passwordField.sendKeys(password);
        /*Search element for scrolling*/
        WebElement swipeElement = driver.findElement(AppiumBy.id("ua.com.rozetka.shop:id/sv_container"));
        verticalSwipe(swipeElement, SwipeDirection.DOWN);
        WebElement singUpButton = driver.findElement(AppiumBy.id("ua.com.rozetka.shop:id/b_sign_up"));
        singUpButton.click();
        /*Close window if appears after confirm*/
        if (isElementPresent(By.id("ua.com.rozetka.shop:id/parentPanel"))){
            Assertions.assertTrue(true);
            driver.navigate().back();
        }
        verticalSwipe(swipeElement, SwipeDirection.UP);
    }

    /**
     * This method is used to close the first banner that appears on the first app launch.
     */
    private static void closeFirstBanner() {
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        if (!driver.findElements(AppiumBy.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/androidx.appcompat.widget.LinearLayoutCompat/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.ImageView[2]")).isEmpty()) {
            driver.navigate().back();
        }
    }

    /**
     * Checks if an element is present on the page based on the given locator.
     *
     * @param locator the locator used to find the element
     * @return true if the element is present, false otherwise
     */
    private boolean isElementPresent(By locator) {
        try {
            Thread.sleep(3000);
            driver.findElement(locator);
            return true;
        } catch (NoSuchElementException | InterruptedException ex) {
            return false;
        }
    }

    /**
     * Enum representing the direction of a swipe gesture.
     */
    enum SwipeDirection {
        UP,
        DOWN
    }

    /**
     * Performs a vertical swipe gesture on the given element.
     *
     * @param element the element on which to perform the swipe
     * @param direction the direction of the swipe (up or down)
     */
    private void verticalSwipe(WebElement element, SwipeDirection direction) {
        int centerX = element.getRect().x + (element.getSize().width / 2);
        double startY, endY;

        if (direction == SwipeDirection.DOWN) {
            startY = element.getRect().y + (element.getSize().height * 0.9);
            endY = element.getRect().y + (element.getSize().height * 0.1);
        } else {
            startY = element.getRect().y + (element.getSize().height * 0.1);
            endY = element.getRect().y + (element.getSize().height * 0.9);
        }

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipe = new Sequence(finger, 1);
        swipe.addAction(finger.createPointerMove(Duration.ofSeconds(0), PointerInput.Origin.viewport(), centerX, (int) startY));
        swipe.addAction(finger.createPointerDown(0));
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(700), PointerInput.Origin.viewport(), centerX, (int) endY));
        swipe.addAction(finger.createPointerUp(0));

        driver.perform(Arrays.asList(swipe));
    }

    @AfterAll public static void tearDown() {
        driver.quit();
    }

}
