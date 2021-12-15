package com.synchrony.synchronyswat;

import com.github.javafaker.Faker;
import com.synchrony.synchronyswat.api.services.ApiImpl;
import com.synchrony.synchronyswat.driver.factory.DriverFactory;
import com.synchrony.synchronyswat.page.pages.PageObjectManager;

import com.synchrony.synchronyswat.utilities.PropertiesHelper;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


public class SampleTests {

    PageObjectManager pageObjectManager;
    ApiImpl api;
    Faker faker;

    @BeforeTest
    public void setup(){
        pageObjectManager = new PageObjectManager();
        api = new ApiImpl();
        boolean isPageExist = pageObjectManager.getHomePage().navigateTo(PropertiesHelper.getProperties().getProperty("app.searchurl"));
        Assert.assertTrue(isPageExist);
    }

    @Test
    public void signInTest(){
        pageObjectManager.getHomePage().clickSignIn();
        Assert.assertTrue(pageObjectManager.getAuthenticationPage().checkPageTitle());
        faker = new Faker();

        pageObjectManager.getAuthenticationPage().createAnAccount(faker.internet().emailAddress());

    }

    @AfterTest
    public void tearDown(){
        DriverFactory.getDriver().close();
        DriverFactory.getDriver().quit();
    }

}
