package com.synchrony.synchronyswat.page.pages;

import com.synchrony.synchronyswat.utilities.PropertiesHelper;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

@Log4j2
public class HomePage extends BasePage{
    public HomePage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(id = "search_query_top")
    private WebElement txtSearchBox;

    @FindBy(xpath = "//a[normalize-space()='Sign in']")
    private WebElement btnSignIn;

    @FindBy(xpath = "//img[@alt='My Store']")
    private WebElement imgLogo;


    @FindAll(@FindBy(xpath = "//div[@class='solarsearch-searchresults-list']//a"))
    private List<WebElement> searchResults;


    public boolean navigateTo(String  url){
        this.navigate(url);
        return this.checkPageTitle();
    }

    public boolean checkPageTitle(){
        String title = driver.getTitle();
        return title.equalsIgnoreCase("My Store");
    }

    public void clickSignIn(){
        this.click(btnSignIn);
    }
}
