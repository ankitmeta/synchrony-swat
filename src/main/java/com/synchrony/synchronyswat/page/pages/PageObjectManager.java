package com.synchrony.synchronyswat.page.pages;

import com.synchrony.synchronyswat.driver.factory.DriverFactory;
import org.openqa.selenium.WebDriver;


public class PageObjectManager {
    private WebDriver driver;
    private HomePage homePage;
    private AuthenticationPage authenticationPage;

    public PageObjectManager(){
        this.driver = DriverFactory.getDriver();
    }

    public HomePage getHomePage() {
        return (homePage == null) ? homePage = new HomePage(driver) : homePage;
    }

    public AuthenticationPage getAuthenticationPage() {
        return (authenticationPage == null) ? authenticationPage = new AuthenticationPage(driver) : authenticationPage;
    }
}
