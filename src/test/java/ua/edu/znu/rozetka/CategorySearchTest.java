package ua.edu.znu.rozetka;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.WebElement;
/**
 * Test to check the correctness of categories when searching.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CategorySearchTest {

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
     * Test case to perform a basic search.
     */
    @Test
    @Order(1)
    public void basicSearchTest() {
        WebElement searchButton = driver.findElement(AppiumBy.id("ua.com.rozetka.shop:id/view_search_tv"));
        searchButton.click();
        /*Close IV Scan the first time search field is used*/
        CloseIVScan();
        WebElement homeButton = driver.findElement(AppiumBy.id("ua.com.rozetka.shop:id/tab_home"));
        homeButton.click();
    }

    /**
     * Test case to search for a product and verify the category of the first search result.
     *
     * @param searchableProduct The product to search for.
     * @param expectedCategory  The expected category of the search result.
     */
    @ParameterizedTest (name = "Product: {0}, Category: {1}")
    @Order(2)
    @CsvSource({
            "Відеокарта RTX 3060 Ti, Відеокарти",
            "Монітор 144 гц, Монітори",
            "Блок живлення 500 вт, Блоки живлення"
    })
    void categoryCorrectnessTest (String searchableProduct, String expectedCategory ) {
        WebElement searchButton = driver.findElement(AppiumBy.id("ua.com.rozetka.shop:id/view_search_tv"));
        searchButton.click();
        WebElement searchField = driver.findElement(AppiumBy.id("ua.com.rozetka.shop:id/et_query"));
        searchField.sendKeys(searchableProduct);
        /*Check first searched result and write to searchedCategory*/
        WebElement firstSearchedElement = driver.findElement(AppiumBy.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.widget.FrameLayout[1]/android.view.ViewGroup/androidx.recyclerview.widget.RecyclerView/androidx.cardview.widget.CardView[1]/android.widget.RelativeLayout/android.widget.TextView"));
        String searchedCategory = firstSearchedElement.getText();
        /*If the category in the search matches the expected category, the test will continue*/
        Assertions.assertEquals(expectedCategory, searchedCategory);
        firstSearchedElement.click();
        WebElement homeButton = driver.findElement(AppiumBy.xpath("//android.widget.FrameLayout[@content-desc=\"Головна\"]/android.widget.FrameLayout/android.widget.ImageView"));
        homeButton.click();
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
     * This method is used to close the first banner that appears on the first app launch.
     */
    private static void closeFirstBanner() {
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        if (!driver.findElements(AppiumBy.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/androidx.appcompat.widget.LinearLayoutCompat/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.ImageView[2]")).isEmpty()) {
            driver.navigate().back();
        }
    }

    @AfterAll public static void tearDown() {
        driver.quit();
    }
}
