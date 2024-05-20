import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class OnafriqTest {


public static void main(String[] args) {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();

        try {
            // Step 1: Go to "https://www.automationexercise.com/" and click on Sign-In
            driver.get("https://www.automationexercise.com/");
            driver.findElement(By.linkText("Signup / Login")).click();

            // Step 2: Sign-In using provided credentials
            driver.findElement(By.name("email")).sendKeys("qat@mailinator.com");
            driver.findElement(By.name("password")).sendKeys("123456");
            driver.findElement(By.cssSelector("button[data-qa='login-button']")).click();

            // Step 3: Fetch and sort featured items by price
            List<WebElement> items = driver.findElements(By.cssSelector(".features_items .col-sm-4"));
            List<Item> itemList = new ArrayList<>();

            for (WebElement item : items) {
                String label = item.findElement(By.cssSelector(".productinfo h2")).getText();
                String priceString = item.findElement(By.cssSelector(".productinfo h2")).getText().replaceAll("[^\\d.]", "");
                double price = Double.parseDouble(priceString);
                itemList.add(new Item(label, price));
            }

            Collections.sort(itemList, Comparator.comparingDouble(Item::getPrice));

            // Print sorted items
            for (Item item : itemList) {
                System.out.println("Label: " + item.getLabel() + ", Price: $" + item.getPrice());
            }

            // Step 4: Navigate to Women >> Dress >> Women – Tops Products
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
            driver.findElement(By.linkText("Women")).click();
            driver.findElement(By.linkText("Dress")).click();
            driver.findElement(By.linkText("Tops Products")).click();

            // Add Fancy Green Top and Summer White Top to cart
            driver.findElement(By.partialLinkText("Fancy Green Top")).click();
            driver.findElement(By.cssSelector("button[type='button']")).click();
            driver.navigate().back();

            driver.findElement(By.partialLinkText("Summer White Top")).click();
            driver.findElement(By.cssSelector("button[type='button']")).click();

            // Step 5: View cart and proceed to checkout
            driver.findElement(By.linkText("Cart")).click();
            driver.findElement(By.linkText("Proceed To Checkout")).click();
            driver.findElement(By.name("message")).sendKeys("Order placed.");
            driver.findElement(By.cssSelector("a.btn.btn-default.check_out")).click();

            // Enter card details and place order
            driver.findElement(By.name("name_on_card")).sendKeys("Test Card");
            driver.findElement(By.name("card_number")).sendKeys("4100 0000 0000");
            driver.findElement(By.name("cvc")).sendKeys("123");
            driver.findElement(By.name("expiry_month")).sendKeys("01");
            driver.findElement(By.name("expiry_year")).sendKeys("1900");
            driver.findElement(By.cssSelector("button[type='submit']")).click();

            // Step 6: Confirm order has been placed
            String confirmationMessage = driver.findElement(By.cssSelector(".alert-success")).getText();
            System.out.println("Order Confirmation: " + confirmationMessage);

        } finally {
            driver.quit();
        }
    }

    static class Item {
        private final String label;
        private final double price;

        public Item(String label, double price) {
            this.label = label;
            this.price = price;
        }

        public String getLabel() {
            return label;
        }

        public double getPrice() {
            return price;
        }
    }
}
