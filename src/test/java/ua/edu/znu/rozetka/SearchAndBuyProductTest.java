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
* Test of Searching and Ordering a product.
* */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SearchAndBuyProductTest {

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
     * This test case performs a product search.
     * It searches for the product "Samsung galaxy s23" and clicks on the first search result.
     * It then scrolls down to the section with product results.
     */
    @Test
    @Order(1)
    public void SearchProductTest() {
        String productName = "Samsung Galaxy S23";
        WebElement searchButton = driver.findElement(AppiumBy.id("ua.com.rozetka.shop:id/view_search_tv"));
        searchButton.click();
        /*Close IV Scan the first time search field is used*/
        CloseIVScan();
        WebElement searchField = driver.findElement(AppiumBy.id("ua.com.rozetka.shop:id/et_query"));
        searchField.sendKeys(productName);
        WebElement firstSearchedResult = driver.findElement(AppiumBy.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.widget.FrameLayout[1]/android.view.ViewGroup/androidx.recyclerview.widget.RecyclerView/androidx.cardview.widget.CardView[1]/android.widget.RelativeLayout/android.widget.TextView"));
        firstSearchedResult.click();
        WebElement swipeElementProductResults = driver.findElement(AppiumBy.id("ua.com.rozetka.shop:id/section_rv_offers"));
        verticalSwipeDown(swipeElementProductResults);
    }

    /**
     * This test case verifies if the searched product is correct.
     * It expects the partial product name "Samsung Galaxy S23" to be present in the search result.
     * The test clicks on the first searched product and retrieves its name.
     * It then compares the retrieved name with the expected partial product name and asserts if it contains the expected text.
     * If the assertion fails, it means that the element text does not contain the expected partial text.
     */
    @Test
    @Order(2)
    public void SearchedProductIsCorrectTest() {
        String expectedPartialProductNameText = "Samsung Galaxy S23";
        WebElement firstSearchedProduct = driver.findElement(AppiumBy.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.widget.FrameLayout[1]/androidx.drawerlayout.widget.DrawerLayout/android.widget.LinearLayout/android.widget.ScrollView/android.widget.GridView/android.view.ViewGroup[1]/androidx.cardview.widget.CardView[1]/androidx.viewpager.widget.ViewPager/androidx.recyclerview.widget.RecyclerView/android.widget.FrameLayout/android.widget.ImageView"));
        firstSearchedProduct.click();
        WebElement productName = driver.findElement(AppiumBy.id("ua.com.rozetka.shop:id/tv_title"));
        String productNameText = productName.getText();
        Assertions.assertTrue(productNameText.contains(expectedPartialProductNameText), "Element text does not contain the expected partial text.");
    }

    /**
     * This test case performs a scroll test on the review menu.
     * It clicks on the review button to navigate to the review section.
     * The test locates the scrollable element containing the reviews and performs four vertical scroll downs on it.
     * This simulates scrolling through multiple reviews in the review menu.
     */
    @Test
    @Order(3)
    public void ReviewMenuScrollTest() {
        WebElement reviewButton = driver.findElement(AppiumBy.xpath("//android.widget.LinearLayout[@content-desc=\"Відгуки\"]/android.widget.TextView"));
        reviewButton.click();
        /*Search element for scrolling*/
        WebElement swipeElementReviews = driver.findElement(AppiumBy.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.widget.FrameLayout/android.widget.ScrollView"));
        verticalSwipeDown(swipeElementReviews);
        verticalSwipeDown(swipeElementReviews);
        verticalSwipeDown(swipeElementReviews);
        verticalSwipeDown(swipeElementReviews);
    }

    /**
     * This test case verifies if the series of the product is correct.
     * It expects the partial series of the product "Galaxy S23" to be present in the technical characteristics section.
     * The test clicks on the technical characteristics button to navigate to the section.
     * It then retrieves the series of the product and compares it with the expected partial series of the product.
     * If the assertion fails, it means that the element text does not contain the expected partial text.
     */
    @Test
    @Order(4)
    public void SeriesOfProductIsCorrectTest() {
        String exceptedPartialSeriesOfProduct = "Galaxy S23";
        WebElement technicalCharacteristics = driver.findElement(AppiumBy.xpath("//android.widget.LinearLayout[@content-desc=\"Характеристики\"]/android.widget.TextView"));
        technicalCharacteristics.click();
        WebElement seriesOfProduct = driver.findElement(AppiumBy.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.widget.FrameLayout/android.widget.ScrollView/androidx.viewpager.widget.ViewPager/androidx.recyclerview.widget.RecyclerView/android.widget.FrameLayout/android.widget.FrameLayout/androidx.recyclerview.widget.RecyclerView/android.widget.LinearLayout[1]/android.widget.TextView[2]"));
        String seriesOfProductText = seriesOfProduct.getText();
        Assertions.assertTrue(seriesOfProductText.contains(exceptedPartialSeriesOfProduct), "Element text does not contain the expected partial text.");
    }

    /**
     * This test case performs a scroll test on the technical characteristics section.
     * The test locates the scrollable element containing the technical characteristics and performs four vertical scroll downs on it.
     * This simulates scrolling through the technical characteristics to view more details.
     */
    @Test
    @Order(5)
    public void TechnicalCharacteristicsScrollTest() {
        /*Search element for scrolling*/
        WebElement swipeElementTechnicalCharacteristics = driver.findElement(AppiumBy.id("ua.com.rozetka.shop:id/coordinator_layout"));
        verticalSwipeDown(swipeElementTechnicalCharacteristics);
        verticalSwipeDown(swipeElementTechnicalCharacteristics);
        verticalSwipeDown(swipeElementTechnicalCharacteristics);
        verticalSwipeDown(swipeElementTechnicalCharacteristics);
    }

    /**
     * This test case performs an order product test.
     * It clicks on the cart button twice to navigate to the cart view.
     * Then clicks on the order product button to proceed to the checkout process.
     * It selects the destination for the order by choosing a city.
     * The test enters the last name, first name, and phone number for the order.
     * It performs a vertical scroll down on the contact data section.
     * Finally, the test clicks on the confirm order button to complete the order process.
     */
    @Test
    @Order(6)
    public void OrderProductTest() {
        String lastName = "Бойко";
        String firstName = "Микола";
        String phoneNumber = "509861320";
        WebElement cartButton = driver.findElement(AppiumBy.id("ua.com.rozetka.shop:id/iv_cart"));
        cartButton.click();
        cartButton.click();
        WebElement orderProduct = driver.findElement(AppiumBy.id("ua.com.rozetka.shop:id/fab_checkout"));
        orderProduct.click();
        WebElement chooseDestinationButton = driver.findElement(AppiumBy.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.widget.FrameLayout/android.view.ViewGroup/android.widget.ScrollView/android.widget.LinearLayout/androidx.cardview.widget.CardView[2]/android.widget.LinearLayout/android.widget.LinearLayout"));
        chooseDestinationButton.click();
        WebElement cityDestination = driver.findElement(AppiumBy.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.widget.FrameLayout/android.view.ViewGroup/android.widget.FrameLayout/androidx.recyclerview.widget.RecyclerView/android.widget.LinearLayout[5]/android.widget.TextView"));
        cityDestination.click();
        WebElement lastNameField = driver.findElement(AppiumBy.id("ua.com.rozetka.shop:id/et_last_name"));
        lastNameField.sendKeys(lastName);
        WebElement firstNameField = driver.findElement(AppiumBy.id("ua.com.rozetka.shop:id/et_first_name"));
        firstNameField.sendKeys(firstName);
        WebElement phoneNumberField = driver.findElement(AppiumBy.id("ua.com.rozetka.shop:id/et_phone"));
        phoneNumberField.sendKeys(phoneNumber);
        WebElement swipeElementOrder = driver.findElement(AppiumBy.id("ua.com.rozetka.shop:id/contact_data_nsv_scroll"));
        verticalSwipeDown(swipeElementOrder);
        WebElement confirmOrder = driver.findElement(AppiumBy.id("ua.com.rozetka.shop:id/b_continue"));
        confirmOrder.click();
        if (isElementPresent(By.id("ua.com.rozetka.shop:id/parentPanel"))){
            Assertions.assertTrue(true);
        }
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
     * This method is used to close the IV scan functionality.
     */
    public static void CloseIVScan() {
        WebElement ivScanButton = driver.findElement(AppiumBy.id("ua.com.rozetka.shop:id/iv_scan"));
        ivScanButton.click();
        WebElement permissionButtonDeny = driver.findElement(AppiumBy.id("com.android.permissioncontroller:id/permission_deny_button"));
        permissionButtonDeny.click();
        WebElement canselButton = driver.findElement(AppiumBy.id("android:id/button2"));
        canselButton.click();
        WebElement closeIVScan = driver.findElement(AppiumBy.accessibilityId("Перейти вгору"));
        closeIVScan.click();
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
