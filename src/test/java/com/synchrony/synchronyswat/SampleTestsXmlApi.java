package com.synchrony.synchronyswat;

import com.github.javafaker.Faker;
import com.synchrony.synchronyswat.api.enums.HttpMethod;
import com.synchrony.synchronyswat.api.enums.ValidatorOperation;
import com.synchrony.synchronyswat.api.services.ApiImpl;
import com.synchrony.synchronyswat.pojo.books.Authentication;
import com.synchrony.synchronyswat.repository.ContactDataProvider;
import com.synchrony.synchronyswat.utilities.CsvHelper;
import com.synchrony.synchronyswat.utilities.IoHelper;
import com.synchrony.synchronyswat.utilities.PropertiesHelper;
import com.synchrony.synchronyswat.utilities.StringHelper;
import io.qameta.allure.Description;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;


public class SampleTestsXmlApi {

    ApiImpl api;

    private String apiDomain;
    private int port;
    private String getbooksEndPoint;

    @BeforeTest
    public void setup() throws IOException {
        api = new ApiImpl();
        apiDomain = PropertiesHelper.getProperties().getProperty("bookxmlapi.domain");
        port = Integer.parseInt(PropertiesHelper.getProperties().getProperty("bookxmlapi.port"));
        getbooksEndPoint = PropertiesHelper.getProperties().getProperty("bookxmlapi.endpoint.getstatus");
    }

    @Description("Test for contains")
    @Test
    public void testBookContain() {
        api.init(apiDomain, getbooksEndPoint, port, HttpMethod.GET);
        api.callService();
        api.assertIt(200);
        api.assertIt("bookstore.book.title[0]", "The Nightingale", ValidatorOperation.EQUALS);
    }

    @Description("Test for Xpath")
    @Test
    public void testBookHasXpath() {
        api.init(apiDomain, getbooksEndPoint, port, HttpMethod.GET);
        api.callService();
        api.assertIt(200);
        api.assertIt("/bookstore/book[@category='children']/title ", "Harry Potter", ValidatorOperation.HAS_XPATH);
    }

    @Description("Test for Count")
    @Test
    public void testBooksCount() {
        api.init(apiDomain, getbooksEndPoint, port, HttpMethod.GET);
        api.callService();
        api.assertIt(200);
        api.assertIt("bookstore.book.title.size()", 2, ValidatorOperation.EQUALS);
    }

    @Description("Test for query")
    @Test
    public void testBookBasedOnSpecificValue() {
        api.init(apiDomain, getbooksEndPoint, port, HttpMethod.GET);
        api.callService();
        api.assertIt(200);
        api.assertIt("bookstore.book.findAll { it.@category == 'cooking' }.year", "2015", ValidatorOperation.EQUALS);
    }

    @Description("Test for relative query")
    @Test
    public void testBookBasedOnSpecificValueWithDeepSearch() {
        api.init(apiDomain, getbooksEndPoint, port, HttpMethod.GET);
        api.callService();
        api.assertIt(200);
        api.assertIt("**.findAll { it.@category == 'cooking' }.year", "2015", ValidatorOperation.EQUALS);
    }

    @Description("Test for Node")
    @Test
    public void testBookWorkOnXMLNode() {
        api.init(apiDomain, getbooksEndPoint, port, HttpMethod.GET);
        api.callService();
        api.assertIt(200);

        System.out.println("just single string: "+ api.getXmlNodeFromResponse("bookstore.book.title"));
        System.out.println("spcific index : "+ api.getXmlNodeFromResponse("bookstore.book.title").get(0));
        System.out.println("is empty : "+ api.getXmlNodeFromResponse("bookstore.book.title").isEmpty());
        System.out.println("size : "+ api.getXmlNodeFromResponse("bookstore.book.title").size());
        System.out.println("list : "+ api.getXmlNodeFromResponse("bookstore.book.title").list());
    }

    @AfterTest
    public void tearDown() {
    }

}
