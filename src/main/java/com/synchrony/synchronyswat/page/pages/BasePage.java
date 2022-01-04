package com.synchrony.synchronyswat.page.pages;

import com.synchrony.synchronyswat.utilities.PropertiesHelper;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

@Log4j2
public class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected WebDriverWait shortWait;

    public BasePage(WebDriver driver){
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(Long.parseLong(PropertiesHelper.getProperties().getProperty("browser.normal.timeout"))));
        shortWait = new WebDriverWait(driver, Duration.ofSeconds(Long.parseLong(PropertiesHelper.getProperties().getProperty("browser.short.timeout"))));
        PageFactory.initElements(driver, this);
    }

    public void navigate(String url) {
        log.debug("Navigate to the {}.", url);
        driver.get(url);
    }

    public void clear(WebElement element){
        waitForElementPresent(element);
        log.info("Clear Element: {}", element);
        element.clear();
    }

    public void click(WebElement element) {
        log.debug("Clicking element: {}.", element);
        waitForElementEnable(element);
        element.click();
    }

    public void type(WebElement element, String string) {
        log.debug("Type {} into element: {}.", string, element);
        waitForElementPresent(element);
        element.sendKeys(string);
    }

    public void pressKeys(WebElement element, Keys keys) {
        log.debug("Press {} into element: {}.", keys, element);
        waitForElementPresent(element);
        element.sendKeys(keys);
    }

    public String getText(WebElement element) {
        log.debug("Get text from element {}.", element);
        String text = null;
        try {
            WebElement elem = wait.until(ExpectedConditions.visibilityOf(element));
            text = elem.getText();
        } catch (NoSuchElementException e) {
            log.error("Element {} is not visible ", element);
        }
        return text;
    }

    public void waitForElementPresent(WebElement element) {
        log.debug("Checking presence of element: {}.", element);
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    public void waitForElementEnable(WebElement element) {
        log.debug("Checking presence of element: {}.", element);
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public void shortWaitForElementPresent(WebElement element) {
        log.debug("Checking presence of element: {}.", element);
        shortWait.until(ExpectedConditions.visibilityOf(element));
    }

}
