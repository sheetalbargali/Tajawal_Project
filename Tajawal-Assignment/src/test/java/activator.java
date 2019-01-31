import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;

public class activator {

    public static void launchBrowser(WebDriver driver, String website) {

        driver.get(website);
    }

    public static void setOriginText(WebDriver driver, String originText) {

        // Select value of origin
        driver.findElement(By.id("flights-search-origin-1")).click();
        WebElement origin = driver.findElement(By.xpath("//div[@class = 'awesomplete']/descendant::li[text() = '" + originText + "']"));
        origin.click();
    }

    public static void setDestinationText(WebDriver driver, String destinationText) {

        // Select value of origin
        driver.findElement(By.id("flights-search-destination-1")).click();
        WebElement desination = driver.findElement(By.xpath("//div[@class = 'awesomplete']/descendant::li[text() = '" + destinationText + "']"));
        desination.click();
    }

    // for now this function will onlu select value from feb month as it is hardcoded, only dates can be varied
    public static void setDepartureDate(WebDriver driver, String day) {

        driver.findElement(By.id("flights-search-departureDate-1")).click();
        WebElement departureDate = (new WebDriverWait(driver, 30))
                .until(ExpectedConditions.visibilityOf(driver.findElements(By.cssSelector(".pika-table")).get(0)));
        driver.findElements(By.cssSelector
                ("button[data-pika-year='2019'][data-pika-month='1'][data-pika-day='"+day+"']")).get(0).click();
    }

    // for now this function will onlu select value from feb month as it is hardcoded, only dates can be varied
    public static void setReturnDate(WebDriver driver, String day) {

        driver.findElement(By.id("flights-search-returnDate-1")).click();
//        WebElement returnDate = (new WebDriverWait(driver, 30))
//                .until(ExpectedConditions.visibilityOf(driver.findElements(By.cssSelector(".pika-table")).get(1)));
        driver.findElements(By.cssSelector
                ("button[data-pika-year='2019'][data-pika-month='1'][data-pika-day='"+day+"']")).get(1).click();
    }

    public static void setTravellersClass(WebDriver driver, String travellersClass) {
        driver.findElement(By.className("js-fs-active-cabin")).click();

        if(travellersClass.equalsIgnoreCase("economy")) {
            driver.findElement(By.id("flights-search-cabin-Economy-btn")).click();
        }
        else if(travellersClass.equalsIgnoreCase("premium economy")) {
            driver.findElement(By.id("flights-search-cabin-Premium-Economy-btn")).click();
        }
        else if(travellersClass.equalsIgnoreCase("business")) {
            driver.findElement(By.id("flights-search-cabin-Business-btn")).click();
        }
        else if(travellersClass.equalsIgnoreCase("first")) {
            driver.findElement(By.id("flights-search-cabin-First-btn")).click();
        }
    }

    public static void setAdultPassengersCount(WebDriver driver, int count) {
        driver.findElement(By.className("js-fs-pax-title")).click();
        WebElement button = driver.findElement(By.xpath("//a[@class='js-fs-pax-action-btn']/i[@class='tj-icon tj-icon--add-c']"));

        for(int i=1; i<count;i++) {
            button.click();
        }
    }

    public static void clickSearchButton(WebDriver driver) {
        driver.findElement(By.id("flights-search-cta")).click();
    }

    public static void takeSnapShot(WebDriver webdriver,String fileWithPath) throws Exception{

        //Convert web driver object to TakeScreenshot
        TakesScreenshot scrShot =((TakesScreenshot)webdriver);

        //Call getScreenshotAs method to create image file
        File SrcFile=scrShot.getScreenshotAs(OutputType.FILE);

        //Move image file to new destination
        File DestFile=new File(fileWithPath);

        //Copy file at destination
        FileUtils.copyFile(SrcFile, DestFile);
    }
}
