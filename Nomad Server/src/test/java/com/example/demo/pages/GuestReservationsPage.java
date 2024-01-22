package com.example.demo.pages;

import com.example.demo.TestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Component
public class GuestReservationsPage {

    private WebDriver driver;

    @FindBy(xpath = "//mat-snack-bar-container//span")
    WebElement snackBar;

    public GuestReservationsPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public boolean clickCancelButton(Long id){
        try {
            WebElement cancelButton = driver.findElement(By.id("cancelButton-" + id));
            (new WebDriverWait(driver, Duration.of(10, ChronoUnit.SECONDS)))
                    .until(ExpectedConditions.elementToBeClickable(cancelButton)).click();
            return true;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }

    }

    public boolean doesStatusMatch(Long id, String status) {
        WebElement statusParagraph = driver.findElement(By.id("status-" + id));
        return (new WebDriverWait(driver, Duration.of(10, ChronoUnit.SECONDS)))
                .until(ExpectedConditions.textToBePresentInElementLocated(By.id("status-" + id), status));
    }

    public boolean doesStatusMatchWithoutWaiting(Long id, String status) {
        WebElement statusParagraph = driver.findElement(By.id("status-" + id));
        if(statusParagraph.getText().equals(status)) {
            return true;
        }
        return false;
    }

    public boolean isSuccess(){
        return (new WebDriverWait(driver, Duration.of(10, ChronoUnit.SECONDS)))
                .until(ExpectedConditions.textToBePresentInElement(snackBar, "SUCCESS"));
    }
    public boolean isFail(){
        return (new WebDriverWait(driver, Duration.of(10, ChronoUnit.SECONDS)))
                .until(ExpectedConditions.textToBePresentInElement(snackBar, "WARNING"));
    }


}
