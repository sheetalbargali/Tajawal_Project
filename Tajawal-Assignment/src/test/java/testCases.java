import com.opencsv.CSVWriter;
import org.junit.*;
import org.junit.rules.TestName;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.junit.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import static org.junit.Assert.assertThat;
import java.io.FileWriter;
import java.util.List;
import java.util.concurrent.TimeUnit;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;


public class testCases {

    public WebDriver driver;

    @Rule
    public TestName name = new TestName();

    @Before
    public void before() {
        System.out.println("************" + name.getMethodName() + "************");
    }
  //  ExtentReports Extreport = new ExtentReports("D:/"+name.getMethodName()+"-"+System.currentTimeMillis()+".html", true);

    @Test
    public void firstScenario() throws Exception {

        // Create an instance of the driver
        System.setProperty("webdriver.gecko.driver", "C:\\SeleniumDriver\\geckodriver.exe");
        driver = new FirefoxDriver();

//      System.setProperty("webdriver.chrome.driver", "C:\\SeleniumDriver\\chromedriver.exe");
//      WebDriver driver = new ChromeDriver();

        // launch browser and direct it to tajawal website
        activator.launchBrowser(driver, "http://www.tajawal.ae");

        // Select value of origin
        String originText = "DXB Dubai, United Arab Emirates - Dubai Airport";
        activator.setOriginText(driver, originText);

        // Select value of desination
        String destinationText = "DEL New Delhi, India - Indira Gandhi International Airport";
        activator.setDestinationText(driver, destinationText);

        // Select departure date
        activator.setDepartureDate(driver, "10");

        // Select return date
        activator.setReturnDate(driver,"20");

        // Select ecomomy class
        activator.setTravellersClass(driver,"economy");

        // Select passengers
        activator.setAdultPassengersCount(driver,2);

        // Click search button
        activator.clickSearchButton(driver);

        // Wait until search summary module appears
        WebElement myDynamicElement = (new WebDriverWait(driver, 30))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='desktop-summary__city-group pull-leading']")));

        // Set variables
        String originText1 = driver.findElement(By.xpath("//div[@title='Dubai']/div[@class='desktop-summary__label desktop-summary__label--highlighted']")).getText();
        String originText2 = driver.findElement(By.xpath("//div[@title='Dubai']/div[@class='desktop-summary__value']")).getText();
        String destinationText1 = driver.findElement(By.xpath("//div[@title='New Delhi']/div[@class='desktop-summary__label desktop-summary__label--highlighted']")).getText();
        String destinationText2 = driver.findElement(By.xpath("//div[@title='New Delhi']/div[@class='desktop-summary__value']")).getText();
        String depatureDate = driver.findElement(By.xpath("//div[@class='desktop-summary__block-group pull-leading']/div[@class='desktop-summary__block pull-leading'][1]/div[@class='desktop-summary__value desktop-summary__value--highlighted']")).getText();
        String returnDate = driver.findElement(By.xpath("//div[@class='desktop-summary__block-group pull-leading']/div[@class='desktop-summary__block pull-leading'][2]/div[@class='desktop-summary__value desktop-summary__value--highlighted']")).getText();
        String passengersCount = driver.findElement(By.xpath("//div[@class='desktop-summary__block-group pull-leading']/div[@class='desktop-summary__block pull-leading'][3]/div[@class='desktop-summary__value desktop-summary__value--highlighted']")).getText();
        String cabin = driver.findElement(By.xpath("//div[@class='desktop-summary__block-group pull-leading']/div[@class='desktop-summary__block pull-leading'][4]/div[@class='desktop-summary__value desktop-summary__value--highlighted']")).getText();

        // Assert search summary module
        assertThat(originText1, is("DXB"));
        assertThat(originText2, is("Dubai"));
        assertThat(destinationText1, is("DEL"));
        assertThat(destinationText2, is("New Delhi"));
        assertThat(depatureDate, is("Sun, 10/02"));
        assertThat(returnDate, is("Wed, 20/02"));
        assertThat(passengersCount, is("2"));
        assertThat(cabin, is("Economy"));

        // Filter results to emirates carrier
        WebElement Element = driver.findElement(By.id("flights-filters-airline-leg-0-check_2"));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView();", Element);
        Element.click();
        driver.findElement(By.id("flights-filters-airline-leg-0-check-exclude-2")).click();
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

        // Find total elements present on search page
        List carrierCount = driver.findElements(By.className("card-airline__details"));
        System.out.println("Total flight details present on search page - "+carrierCount.size());

        // Assert listing results are only the emirates carrier
        for (int i=1;i<=carrierCount.size(); i++)
        {
            String carrier = driver.findElement(By.xpath("(//div[@class='card-airline__details'])["+i+"]/div[1]")).getText();
            assertThat(carrier, is("Emirates"));
        }

        // Fetch price details of first flight
        String currency = driver.findElement(By.xpath("//div[@class='search-result-card__price-details']/div[@class='search-result-card__currency']")).getText();
        String price = driver.findElement(By.xpath("//div[@class='search-result-card__price-details']/div[@class='search-result-card__price']")).getText();
        double serviceFee= 30.00;
        double actualPrice = Integer.parseInt(price.replaceAll(",", ""))+serviceFee;

        // Select first flight and navigate to detail page
        driver.findElement(By.id("select-flight-0-0")).click();

        // Assert cart price
        String cartPrice = driver.findElement(By.xpath("//div[@class='total-payment__title total-payment__title_wrapper  clearfix']/span/h2[@class='pull-right']")).getText();
        assertThat(cartPrice, is(currency+" " + String.format("%,.2f", actualPrice)));

        List carrierCount1 = driver.findElements(By.className("title-container__radio"));
        System.out.println(carrierCount1.size());

        // Fill travellers detail
        // NOTE----Sometimes travellers detail page is displayed different so case is failing here
        driver.findElement(By.xpath("//label[@class='radio-inline']/input[@value='Mrs']")).click();
        driver.findElement((By.id("flights-summary-travelers-form-first-name-0"))).sendKeys("TestUserOne");
        driver.findElement((By.id("flights-summary-travelers-form-middle-name-0"))).sendKeys("TestUserOne");
        driver.findElement((By.id("flights-summary-travelers-form-last-name-0"))).sendKeys("TestUserOne");

        // driver.findElement(By.xpath("//div[@class='pax-form-container__section']/label[@class='radio-inline']/input[@value='Ms']")).click();
        driver.findElement((By.id("flights-summary-travelers-form-first-name-1"))).sendKeys("TestUserTwo");
        driver.findElement((By.id("flights-summary-travelers-form-middle-name-1"))).sendKeys("TestUserTwo");
        driver.findElement((By.id("flights-summary-travelers-form-last-name-1"))).sendKeys("TestUserTwo");

        // Take screenshot
        activator.takeSnapShot(driver,"D://Flight_Details_"+System.currentTimeMillis()+".png");

        // Close browser
        driver.close();
    }

    @Test
    public void secondScenario() throws Exception {

        // Create an instance of the driver
        System.setProperty("webdriver.gecko.driver", "C:\\SeleniumDriver\\geckodriver.exe");
        driver = new FirefoxDriver();

        // launch browser and direct it to tajawal website
        activator.launchBrowser(driver, "http://www.tajawal.ae");

        // Select value of origin
        String originText = "DXB Dubai, United Arab Emirates - Dubai Airport";
        activator.setOriginText(driver, originText);

        // Select value of desination
        String destinationText = "DEL New Delhi, India - Indira Gandhi International Airport";
        activator.setDestinationText(driver, destinationText);

        // Select departure date
        activator.setDepartureDate(driver, "12");

        // Select return date
        activator.setReturnDate(driver,"15");

        // Select first class
        activator.setTravellersClass(driver,"first");

        // Select passengers
        activator.setAdultPassengersCount(driver,3);

        // Take screenshot
        activator.takeSnapShot(driver,"D://homepage_"+System.currentTimeMillis()+".png");

        // Click search button
        activator.clickSearchButton(driver);

        // Fetch price details of first flight
        WebElement myDynamicElement = (new WebDriverWait(driver, 30))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='search-result-card__price-details']")));
        String price1 = driver.findElement(By.xpath("//div[@class='search-result-card__price-details']/div[@class='search-result-card__price']")).getText();
        long firstFlightPrice = Integer.parseInt(price1.replaceAll(",", ""));

        // Find total prices displayed on page
        List totalPrice = driver.findElements(By.className("search-result-card__price"));
        System.out.println(totalPrice.size());

        // Create .csv file
        CSVWriter csvWriter = new CSVWriter(new FileWriter("D://Search_Price_"+System.currentTimeMillis()+".csv", true));
        csvWriter.writeNext(new String[]{price1});

        for (int i=2;i<=totalPrice.size(); i++)
        {
            WebElement Element = (new WebDriverWait(driver, 30))
                    .until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//div[@class='search-result-card__price'])["+i+"]")));
            JavascriptExecutor js = (JavascriptExecutor) driver;
            String listedPrice = Element.getText();
            System.out.println(i +"---"+listedPrice);

            // Assert that first price is the lowest comparing to other prices
            assertTrue(firstFlightPrice  <=  Long.valueOf(listedPrice.replaceAll(",","")));

            // Save listed price in a .CSV file
            csvWriter.writeNext(new String[]{listedPrice});
        }
        csvWriter.close();

        // Take screenshot
        activator.takeSnapShot(driver,"D://searchPage_"+System.currentTimeMillis()+".png");

        // Close browser
        driver.close();
    }
}
