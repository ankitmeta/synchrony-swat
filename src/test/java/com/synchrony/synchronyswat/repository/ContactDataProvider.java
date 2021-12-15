package com.synchrony.synchronyswat.repository;

import com.synchrony.synchronyswat.utilities.CsvHelper;
import org.testng.annotations.DataProvider;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ContactDataProvider {


    private static Object[][] contactsCsvData;


    public void getContactsDataFromCsv(String filePath) throws IOException {
        contactsCsvData = CsvHelper.getCSVDataForDataProvider(filePath, ',');
    }

    @DataProvider(name="ContactsData", parallel = true)
    public Object[][] ContactsData(Method m) throws IOException {
        return contactsCsvData;
    }

}
