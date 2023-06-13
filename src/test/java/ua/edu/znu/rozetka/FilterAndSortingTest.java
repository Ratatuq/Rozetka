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
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
/**
 * Test of filter and sorting functionality.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FilterAndSortingTest {

    private static AndroidDriver driver;


    @BeforeAll
    public static void setUp() throws MalformedURLException {
        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName("Android")
                .setPlatformVersion("11.0")
                .setDeviceName("CustomPhone")
                .setAutomationName("UiAutomator2")
                .setAppPackage("ua.com.rozetka.shop")
                .setAppActivity("ua.com.rozetka.shop.screen.MainActivity")
                .eventTimings();
        URL remoteUrl = new URL("http://127.0.0.1:4723/wd/hub/");
        driver = new AndroidDriver(remoteUrl, options);

        /*Close banner on the launch*/
        closeFirstBanner();
    }

    /**
     * This test case performs a filter test.
     * It applies filters to a catalog of sport and hobbies items, checks the initial quantity of items,
     * applies another filter by changing the maximum price, and checks the quantity of items after filtering.
     */
    @Test
    @Order(1)
    public void FilterTest() {
        WebElement catalogButton = driver.findElement(AppiumBy.xpath("//android.widget.FrameLayout[@content-desc=\"Каталог\"]/android.widget.FrameLayout/android.widget.ImageView"));
        catalogButton.click();
        WebElement catalogElementSportAndHobbies = driver.findElement(AppiumBy.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.widget.FrameLayout[1]/android.view.ViewGroup/android.widget.GridView/android.widget.LinearLayout[9]/android.widget.TextView"));
        catalogElementSportAndHobbies.click();
        /*Search element for scrolling*/
        WebElement swipeElementSportAndHobbies = driver.findElement(AppiumBy.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.widget.FrameLayout[1]/android.view.ViewGroup/android.widget.GridView"));
        verticalSwipeDown(swipeElementSportAndHobbies);
        WebElement bicycleButton = driver.findElement(AppiumBy.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.widget.FrameLayout[1]/android.view.ViewGroup/android.widget.GridView/android.widget.TextView[1]"));
        bicycleButton.click();
        /*Apply first filter by pressing on checkbox*/
        WebElement filterButton = driver.findElement(AppiumBy.id("ua.com.rozetka.shop:id/section_iv_filter"));
        filterButton.click();
        WebElement filterCheckBox = driver.findElement(AppiumBy.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.widget.FrameLayout[1]/androidx.drawerlayout.widget.DrawerLayout/android.widget.RelativeLayout/android.view.ViewGroup/androidx.recyclerview.widget.RecyclerView/android.widget.CheckBox[1]"));
        filterCheckBox.click();
        /*Check first quantity of items after filter for comparing*/
        WebElement firstQuantityOfItemsElement = driver.findElement(AppiumBy.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.widget.FrameLayout[1]/androidx.drawerlayout.widget.DrawerLayout/android.widget.RelativeLayout/android.view.ViewGroup/android.widget.LinearLayout/android.view.ViewGroup/android.widget.TextView[2]"));
        int firstQuantityOfItems = extractIntFromElement(firstQuantityOfItemsElement);
        /*Search element for scrolling*/
        WebElement swipeFilterElement = driver.findElement(AppiumBy.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.widget.FrameLayout[1]/androidx.drawerlayout.widget.DrawerLayout/android.widget.RelativeLayout/android.view.ViewGroup/androidx.recyclerview.widget.RecyclerView"));
        verticalSwipeDown(swipeFilterElement);
        verticalSwipeDown(swipeFilterElement);
        /*Applying another filter for changing quantity of items in field*/
        WebElement maxPriceFilter = driver.findElement(AppiumBy.id("ua.com.rozetka.shop:id/item_filter_slider_data_et_to"));
        maxPriceFilter.sendKeys("13000");
        WebElement filterApplyButton = driver.findElement(AppiumBy.id("ua.com.rozetka.shop:id/view_filters_tv_apply"));
        filterApplyButton.click();
        if (isElementPresent(By.id("ua.com.rozetka.shop:id/section_iv_filter"))){
            filterButton.click();
        }
        /*Check second quantity of items after filtering maximum price for comparing*/
        WebElement secondQuantityOfItemsElement = driver.findElement(AppiumBy.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.widget.FrameLayout[1]/androidx.drawerlayout.widget.DrawerLayout/android.widget.RelativeLayout/android.view.ViewGroup/android.widget.LinearLayout/android.view.ViewGroup/android.widget.TextView[2]"));
        int secondQuantityOfItems = extractIntFromElement(secondQuantityOfItemsElement);
        /*Comparison of the first and second filter values, second quantity have to be lower, because of applied filters*/
        Assertions.assertTrue(secondQuantityOfItems < firstQuantityOfItems);
    }

    /**
     * This test case performs a sorting test.
     * It checks the minimum and maximum prices in the filter, closes the filter tab,
     * opens the sort menu, sorts the items from cheapest to expensive, checks the price of the cheapest item,
     * compares the minimum filter price with the minimum item price,
     * sorts the items from expensive to cheapest, checks the price of the most expensive item,
     * and compares the maximum filter price with the maximum item price.
     */
    @Test
    @Order(2)
    public void SortingTest() {
        /*Check minimal price in filter for comparing*/
        WebElement minPriceFilterField = driver.findElement(AppiumBy.id("ua.com.rozetka.shop:id/item_filter_slider_data_et_from"));
        int minFilterPrice = extractIntFromElement(minPriceFilterField);
        /*Check maximum price in filter for comparing*/
        WebElement maxPriceFilterField = driver.findElement(AppiumBy.id("ua.com.rozetka.shop:id/item_filter_slider_data_et_to"));
        int maxFilterPrice = extractIntFromElement(maxPriceFilterField);
        /*Close tab with filters*/
        WebElement filterApply = driver.findElement(AppiumBy.id("ua.com.rozetka.shop:id/view_filters_tv_apply"));
        filterApply.click();
        /*Open sort menu for sorting from cheapest to expensive*/
        WebElement sortMenu = driver.findElement(AppiumBy.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.widget.FrameLayout[1]/androidx.drawerlayout.widget.DrawerLayout/android.widget.LinearLayout/android.widget.ScrollView/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.LinearLayout[1]/android.widget.LinearLayout/android.widget.TextView[1]"));
        sortMenu.click();
        WebElement sortFromCheapestToExpensiveRadioButton = driver.findElement(AppiumBy.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.widget.FrameLayout[1]/androidx.drawerlayout.widget.DrawerLayout/android.widget.RelativeLayout/android.view.ViewGroup/android.widget.ScrollView/android.widget.RadioGroup/android.widget.RadioButton[1]"));
        sortFromCheapestToExpensiveRadioButton.click();
        /*Check price of cheapest bicycle*/
        WebElement minBicyclePriceField = driver.findElement(AppiumBy.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.widget.FrameLayout[1]/androidx.drawerlayout.widget.DrawerLayout/android.widget.LinearLayout/android.widget.ScrollView/android.widget.GridView/android.view.ViewGroup[1]/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.TextView[1]"));
        int minBicyclePrice = extractIntFromElement(minBicyclePriceField);

        /*Comparing minimal filter price and minimal bicycle price, minimal filter price should be less than price of bicycle*/
        Assertions.assertTrue(minFilterPrice <= minBicyclePrice);

        /*Open sort menu for sorting from expensive to cheapest*/
        sortMenu.click();
        WebElement sortFromExpensiveToCheapestRadioButton = driver.findElement(AppiumBy.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.widget.FrameLayout[1]/androidx.drawerlayout.widget.DrawerLayout/android.widget.RelativeLayout/android.view.ViewGroup/android.widget.ScrollView/android.widget.RadioGroup/android.widget.RadioButton[2]"));
        sortFromExpensiveToCheapestRadioButton.click();
        /*Check price of the most expensive bicycle*/
        WebElement maxBicyclePriceField = driver.findElement(AppiumBy.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.widget.FrameLayout[1]/androidx.drawerlayout.widget.DrawerLayout/android.widget.LinearLayout/android.widget.ScrollView/android.widget.GridView/android.view.ViewGroup[1]/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.TextView[1]"));
        int maxBicyclePrice = extractIntFromElement(maxBicyclePriceField);

        /*Comparing maximum filter price and maximum bicycle price, max filter price should be higher than price of bicycle*/
        Assertions.assertTrue(maxFilterPrice >= maxBicyclePrice);
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
     * This method is used to make vertical swipe down on element.
     *
     * @param element element that used for swipe
     */
    private static void verticalSwipeDown(WebElement element) {
        int centerX = element.getRect().x + (element.getSize().width/2);
        double startY = element.getRect().y + (element.getSize().height * 0.9);
        double endY = element.getRect().y + (element.getSize().height * 0.1);

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipe = new Sequence(finger, 1);
        swipe.addAction(finger.createPointerMove(Duration.ofSeconds(0), PointerInput.Origin.viewport(), centerX, (int)startY));
        swipe.addAction(finger.createPointerDown(0));
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(700),
                PointerInput.Origin.viewport(), centerX, (int)endY));
        swipe.addAction(finger.createPointerUp(0));

        driver.perform(Arrays.asList(swipe));
    }

    /**
     * Extracts an integer value from the text of the given WebElement.
     *
     * @param element the WebElement from which to extract the integer value
     * @return the extracted integer value
     */
    public int extractIntFromElement(WebElement element) {
        String elementText = element.getText();
        String numericText = elementText.replaceAll("[^0-9]", "");
        return Integer.parseInt(numericText);
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

    @AfterAll public static void tearDown() {
        driver.quit();
    }
}
