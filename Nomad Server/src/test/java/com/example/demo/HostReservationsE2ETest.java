package com.example.demo;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.springframework.stereotype.Component;

@Component
public class HostReservationsE2ETest {

    private WebDriver driver;


    public HostReservationsE2ETest(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }
}
