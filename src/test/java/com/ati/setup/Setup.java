package com.ati.setup;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class Setup {
    public static String uMail= "testusermail@test.com";
    public static String uPass= "TestCase*2021";
    public static String homePage= "https://www.gittigidiyor.com/";
    public static String webdriverExec= "webdriver/chromedriver89_macm1";
    public static WebDriver driver;
    public String productPrice, cartPrice;
    public Setup(WebDriver webDriver) {
        driver = webDriver;
    }
    public WebElement hoverLogin() {
        return driver.findElement(By.cssSelector(".gekhq4-5 > .gekhq4-6 path"));
    }
    public WebElement buttonLogin() {
        return driver.findElement(By.cssSelector(".kNKwwK"));
    }
    public WebElement mailField() {
        return driver.findElement(By.id("L-UserNameField"));
    }
    public WebElement passwordField() {
        return driver.findElement(By.id("L-PasswordField"));
    }
    public WebElement logIn() {
        return driver.findElement(By.xpath("//*[@id=\"gg-login-enter\"]"));
    }
    public WebElement searchField() {
        return driver.findElement(By.cssSelector(".sc-4995aq-0"));
    }
    public WebElement searchButton() {
        return driver.findElement(By.cssSelector(".hKfdXF > span"));
    }
    public WebElement secondPage() {
        return driver.findElement(By.xpath("//a[contains(text(),'2')]"));
    }
    public WebElement randomProduct() {
        int min = 1;
        int max = 48;
        int random_int = (int)(Math.random() * (max - min + 1) + min);
        return driver.findElement(By.xpath("//li[" + random_int + "]/a/div/div/div/div/h3/span"));
    }
    public WebElement productPrice() {
        return driver.findElement(By.xpath("//div[2]/div[2]/div/div/div/div/div/div/div[2]"));
    }
    public WebElement annyoingNotification() {
        return driver.findElement(By.cssSelector(".wis-clsbtn-82204"));
    }
    public WebElement cartPrice() {
        return driver.findElement(By.xpath("//*[@id=\"submit-cart\"]/div/div[2]/div[3]/div/div[1]/div/div[5]/div[1]/div/ul/li[1]/div[2]"));
    }
    public WebElement addToCart() {
        return driver.findElement(By.cssSelector("#add-to-basket"));
    }
    public WebElement goToCart() {
        return driver.findElement(By.xpath("//a[@href='https://www.gittigidiyor.com/sepetim']"));
    }
    public WebElement increaseQty() {
        return driver.findElement(By.cssSelector("option:nth-child(2)"));
    }
    public WebElement deleteItem() {
        return driver.findElement(By.cssSelector(".row > .btn-delete > .gg-icon"));
    }
    public WebElement cartEmpty() {
        return driver.findElement(By.cssSelector("#empty-cart-container > div.gg-d-24 > div:nth-child(1) > div > div.gg-w-22.gg-d-22.gg-t-21.gg-m-18 > h2"));
    }
    public WebElement productCount() {
        return driver.findElement(By.xpath("//select"));
    }
    public String getProductPrice() {
        return productPrice;
    }
    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }
    public String getCartPrice() {
        return cartPrice;
    }
    public void setCartPrice(String cartPrice) {
        this.cartPrice = cartPrice;
    }

}