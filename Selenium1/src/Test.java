import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;

public class Test {
    public static void main(String[] args) throws Exception {
        //variable section
        String expectedTitle = "Stock Images, Royalty-Free Illustrations, Vectors, & Stock Video Clips - iStock";
        String expectedURL = "https://www.istockphoto.com/stock-photos";
        String toSearch = "Petar Mulaj";
        String expectedButtonText = "Download this image";

        System.setProperty("webdriver.driver.chrome", "C:\\Program Files\\Web Drivers\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();

        //go to web site
        driver.get("https://www.istockphoto.com/");

        //take a title of web site
        String title = driver.getTitle();

        //check if title is correct
        Assert.assertTrue(title.equals(expectedTitle));

        //click on Photos
        driver.findElement(By.xpath("//a[contains(text(),'Photos')]")).click();

        //get the current url
        String url = driver.getCurrentUrl();

        //check if url is correct
        Assert.assertTrue(url.equals(expectedURL));

        //enter text in search input
        driver.findElement(By.id("search-phrase")).sendKeys(toSearch);

        //click on search button
        driver.findElement(By.xpath("//button[@gi-track='track.ui-element.search-bar-default']")).click();

        //wait for photos with explicit wait
        WebDriverWait d = new WebDriverWait(driver, 30);
        d.until(ExpectedConditions.visibilityOfElementLocated(By.className("image-section")));

        //count all found photos
        int count = driver.findElements(By.className("image-section")).size();

        //Assert if the photos are not found (%s is format specifier)
        if(count > 0) {
            System.out.println(String.format("Photos found for search %s on iStock web site.", toSearch));
        } else {
            Assert.fail(String.format("Photos did not found for search %s on iStock web site.", toSearch));
        }

        //click on first photo
        driver.findElements(By.className("image-section")).get(0).click();

        //check radio button for image price
        driver.findElement(By.xpath("//input[@value='checkout']/parent::div[@class='radio-button']")).click();

        //Assert in case radio check box is not checked
        String buttonText = driver.findElement(By.xpath("//span[@ng-if='!calc.ctaInProgress && step.ctaTitle']")).getText();
        Assert.assertEquals(buttonText, expectedButtonText);

        //click on button "Download this image"
        driver.findElement(By.xpath("//span[@ng-if='!calc.ctaInProgress && step.ctaTitle']")).click();

        //Enter checkout data

        //Enter email - every time different email using Math.random()
        int random = (int)(Math.random() * 50 + 1);
        driver.findElement(By.id("create_order_form_easy_first_purchase_email")).sendKeys("test" + random + "@gmail.com");
        //Enter country from dropdown
        Select s = new Select(driver.findElement(By.id("create_order_form_contact_info_address_country_code")));
        s.selectByValue("AUT");
        //Enter first name
        driver.findElement(By.name("create_order_form[contact_info][first_name]")).sendKeys("Testni_Name");
        //Enter last name
        driver.findElement(By.name("create_order_form[contact_info][last_name]")).sendKeys("Testerovic");
        //Enter company
        driver.findElement(By.id("create_order_form_contact_info_organization_name")).sendKeys("Organization_Testing");
        //Check checkbox
        driver.findElement(By.xpath("//span[@data-fn='self_employed']")).click();
        //Enter address
        driver.findElement(By.id("create_order_form_contact_info_address_line1")).sendKeys("Adddrrrreeeeeesssssssssssssssss");
        //Enter postal code
        driver.findElement(By.id("create_order_form_contact_info_address_postal_code")).sendKeys("1234567");
        //Enter city
        driver.findElement(By.id("create_order_form_contact_info_address_city")).sendKeys("Town_Test");
        //Enter phone number
        driver.findElement(By.id("create_order_form_contact_info_phone_number")).sendKeys("123235566734235346");
        //Enter password
        driver.findElement(By.id("create_order_form_easy_first_purchase_password")).sendKeys("password123435");
        //Check paypal check box
        driver.findElement(By.xpath("//label[contains(text(),'PayPal')]")).click();
        //Click on button submit
        driver.findElement(By.id("paypal_create_order_button")).click();

        //Wait to PayPal page load - explicit wait
        WebDriverWait waitPayPal = new WebDriverWait(driver, 60);
        waitPayPal.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[contains(text(),'Pay with PayPal')]")));

        String payPalUrl = driver.getCurrentUrl();
        if(payPalUrl.contains("www.paypal.com")) {
            System.out.println("We are on the PayPal page.");
        } else {
            Assert.fail("Something went wrong.");
        }

        //Take screenshot
        File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(src,new File("C:\\Users\\Petar\\Pictures/screenshot" + random +".png"));

        //close web browser
        driver.close();
    }
}
