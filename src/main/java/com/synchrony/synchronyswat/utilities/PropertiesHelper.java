package com.synchrony.synchronyswat.utilities;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Log4j2
public class PropertiesHelper {

    private static PropertiesHelper _instance = null;

    private Properties loadProperties(){
        Properties properties = new Properties();
        try(InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("config.properties")){
            properties.load(resourceAsStream);
        }catch (IOException e){
           log.error("Unable to load properties file from resources");
        }
        return properties;
    }

    public static Properties getProperties(){
        if(_instance==null){
            _instance = new PropertiesHelper();
        }
        return _instance.loadProperties();
    }
}
