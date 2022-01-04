package com.synchrony.synchronyswat.driver.factory;

import com.synchrony.synchronyswat.driver.manager.ChromeDriverManager;
import com.synchrony.synchronyswat.driver.manager.FirefoxDriverManager;
import com.synchrony.synchronyswat.driver.enums.DriverType;
import com.synchrony.synchronyswat.utilities.PropertiesHelper;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebDriver;

import java.time.Duration;
import java.util.Optional;
import java.util.Properties;

@Log4j2
public class DriverFactory {
    private static WebDriver _driver;
    private DriverFactory(){}

    private static WebDriver createInstance() {
        String browser = Optional.of(System.getProperty("browser")).orElse("Chrome");
        log.debug("Get driver for browser: {}", browser);
        DriverType driverType = DriverType.valueOf(browser.toUpperCase());
            switch (driverType) {
                case CHROME:
                    _driver  = new ChromeDriverManager().createDriver();
                    break;
                case FIREFOX:
                    _driver  = new FirefoxDriverManager().createDriver();
                    break;
                default:
                    throw new IllegalArgumentException(browser);
            }
        _driver.manage().window().maximize();
        _driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(
                Long.parseLong(PropertiesHelper.getProperties().getProperty("browser.default.timeout"))));
        return _driver ;
    }

    public static WebDriver getDriver(){
        return (_driver  == null) ? createInstance(): _driver ;
    }
}
