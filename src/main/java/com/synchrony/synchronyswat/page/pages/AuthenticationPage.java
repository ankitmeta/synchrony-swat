package com.synchrony.synchronyswat.page.pages;

import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

@Log4j2
public class AuthenticationPage extends HomePage{
    public AuthenticationPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(id = "email")
    private WebElement txtEmail;

    @FindBy(id = "passwd")
    private WebElement txtPassword;

    @FindBy(xpath = "//span[normalize-space()='Sign in']")
    private WebElement btnSignIn;

    @FindBy(id = "email_create")
    private WebElement txtEmailCreate;

    @FindBy(xpath = "//span[normalize-space()='Create an account']")
    private WebElement btnCreateAnAccount;

    private final String pageTitle = "Login - My Store";

    public boolean checkPageTitle(){
        String title = driver.getTitle();
        return title.equalsIgnoreCase(pageTitle);
    }

    public void createAnAccount(String newEmailAddress){
        this.type(txtEmailCreate, newEmailAddress);
        this.click(btnCreateAnAccount);
    }

    public void logIn(String emailAddress, String password){
        this.type(txtEmail, emailAddress);
        this.type(txtPassword, password);
        this.click(btnSignIn);
    }
}
