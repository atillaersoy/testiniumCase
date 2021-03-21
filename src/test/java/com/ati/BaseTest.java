import com.ati.setup.Setup;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import static java.util.concurrent.TimeUnit.SECONDS;

    public class BaseTest {
    public static WebDriver driver;
    private static Logger log = Logger.getLogger(BaseTest.class);
    public static Setup setup;


    @Before
    public void setUp()  {
        //set lo4j appender, chrome options and start a new chromedriver
        PropertyConfigurator.configure("src/test/java/log4j.properties");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications"); //disable notification prompt of gittigidiyor
        System.setProperty("webdriver.chrome.driver", setup.webdriverExec);
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, SECONDS);
        driver.manage().timeouts().pageLoadTimeout(10, SECONDS);
        setup = new Setup(driver);
    }

    @Test
    public void BaseTest() {
        //Check if Home Page Loads Correctly, if any error occurs fail the test.
        driver.get(setup.homePage);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        if (((JavascriptExecutor) js).executeScript("return document.readyState").equals("complete")) {
            log.info("Homepage Loading Was Successful");
            logIn();
        } else {
            log.error("Something Went Wrong While Loading Homepage");
            Assert.fail("Homepage Loading Failed, Test Failed");
        }
    }
    public static void logIn() {
        try {
            //login process
            WebElement loginhover = setup.hoverLogin();
            Actions builder = new Actions(driver);
            builder.moveToElement(loginhover).perform();
            setup.buttonLogin().click();
            setup.mailField().sendKeys(setup.uMail);
            setup.passwordField().sendKeys(setup.uPass);
            setup.logIn().click();
            //Check if the username displayed on header matches with our credentials
            if (driver.findElements( By.xpath("//span[contains(.,'testusertt799970')]") ).size() != 0) {
                //check if username exist and login successful
                log.info("Logging In Successful");
                log.info("Username found on header");
                searchproduct();
            }
            else{
                log.error("Log In Failed, Login process executed but username wasn't found in header");
                Assert.fail("Login process executed but username wasn't found in header, Test Failed, May be you're using wrong credentials?");
            }
        } catch (Exception e1) {
            log.error(e1);
            Assert.fail("Login Failed, Test Failed");
        }
    }

    //Product search and moving next page function
    public static void searchproduct() {
        //Search for "Bilgisayar", Go to 2nd Page than select a random product.
        try {
            setup.searchField().sendKeys("Bilgisayar");
            setup.searchButton().click();
            log.info("Searching for 'bilgisayar'");
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollBy(0,30000)");
            setup.secondPage().click();
            if (driver.getCurrentUrl().contains("sf=2")){
                log.info("Successfully switched to Page 2");
            }
            else
            {
                log.error("There was an error while switching Page 2");
                Assert.fail("Page Switching Fail, Test Failed");
            }
            js.executeScript("window.scrollBy(0,10000)"); //scroll down to the end of the page to load all product grid
            WebDriverWait wait = new WebDriverWait(driver, 10);
            wait.until(ExpectedConditions.elementToBeClickable(setup.randomProduct()));
            setup.randomProduct().click();
            log.info("Random Product Selected");
        addToCart();
        } catch (Exception e2) {
            log.error(e2);
            Assert.fail(e2.getMessage());

        }


    }
    public static void addToCart() {
        //Product page, check if gittigidiyor notification pop-up presents if present close it than add product to the cart and go to cart
        try {
            WebDriverWait wait = new WebDriverWait(driver, 10);
            setup.setProductPrice(setup.productPrice().getText());
            if (driver.findElements( By.cssSelector(".wis-clsbtn-82204") ).size() != 0)
                log.info("GittiGidiyor Notification Found, Closing to avoid any interference");
                setup.annyoingNotification().click();
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView(true);", setup.addToCart()); //scroll to the add to cart button for avoiding any screen resolution faults.
            wait.until(ExpectedConditions.elementToBeClickable(setup.addToCart()));
            setup.addToCart().click();
            wait.until(ExpectedConditions.elementToBeClickable(setup.goToCart()));
            setup.goToCart().click();
            wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.cssSelector(".gg-input-select > .amount"))));
            driver.findElement(By.cssSelector(".gg-input-select > .amount")).click();
            setup.setCartPrice(setup.cartPrice().getText());
            log.info("Successfully Added To Cart");
            comparePrices();
        }
        catch (Exception e3) {
            log.error(e3);
            Assert.fail(e3.getMessage());
        }

    }
    public static void comparePrices() {
        //Compare the listing price with cart price (Please Note that, while creating project there was 150TL discount on cart)
        try {
            String productPrice, cartPrice;
            productPrice = setup.getProductPrice();
            cartPrice = setup.getCartPrice();
            if (cartPrice.equals(productPrice)) {
                log.info("Product Price is " + productPrice + ", Cart Price is " + cartPrice + "They Equal!");
            } else {
                log.info("The Product price and cart price is not equal! May be an automatic discount applied on the cart?");
                log.warn("Product Price is " + cartPrice + ",Discounted Product Cart Price is " + productPrice);
            }
            increaseQty();
        }
        catch (Exception e4) {
            log.error(e4);
            Assert.fail(e4.getMessage());
        }
    }
    public static void increaseQty(){
        //Increase the quantity of the product on the cart
        try {
            WebDriverWait wait = new WebDriverWait(driver, 10);
            WebElement dropdown = driver.findElement(By.cssSelector(".gg-input-select > .amount"));
            wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//option[@value='2']"))));
            dropdown.findElements(By.xpath("//option[@value='2']"));
            wait.until(ExpectedConditions.elementToBeClickable(setup.increaseQty()));
            setup.increaseQty().click();
            int piece = Integer.parseInt(setup.productCount().getAttribute("value"));
            log.info(piece);
            if (piece == 2) {
                log.info("Product Quantity Successfully Switched to 2 Pcs");
            } else {
                log.info("There was something wrong with increasing the Quantity, Is stock available?");
            }
            emptyCart();
        }
        catch (Exception e5) {
            log.error(e5);
            Assert.fail(e5.getMessage());
        }
        }
    public static void emptyCart()  {
        //empty the cart and check 
        try{
            WebDriverWait wait = new WebDriverWait(driver, 10);
            setup.deleteItem().click();
            wait.until(ExpectedConditions.elementToBeClickable(setup.cartEmpty()));
            if (setup.cartEmpty().isDisplayed()) {
                log.info("Successfully emptied the cart");
            } else {
                log.error("There was error emptying cart, Is there any other product on the cart?");
            }
            driver.close();
            }
        catch (Exception e6) {
            log.error(e6);
            Assert.fail(e6.getMessage());
        }
    }

}
