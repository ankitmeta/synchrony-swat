package com.synchrony.synchronyswat.driver.manager;

import com.synchrony.synchronyswat.driver.factory.Factory;
import com.synchrony.synchronyswat.utilities.PropertiesHelper;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.config.DriverManagerType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class ChromeDriverManager implements Factory {
    @Override
    public WebDriver createDriver() {
        WebDriverManager.getInstance(DriverManagerType.CHROME)
                .driverVersion(PropertiesHelper.getProperties().getProperty("browser.version"))
                .setup();
        return new ChromeDriver();
    }
}
