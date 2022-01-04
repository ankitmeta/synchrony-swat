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
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;


public class SampleTestsApi {

    ApiImpl api;

    private String apiDomain;
    private int port;
    private String getStatusEndPoint;
    private String getbooksEndPoint;
    private String getAuthenticateEndPoint;
    private String createOrdersEndPoint;

    private String dynamicOrderJson;
    private String staticOrderJson;
    private String contactJson;

    private String contactCSV;

    static Faker faker;
    static String accessToken;

    ContactDataProvider contactDataProvider;

    @BeforeTest
    public void setup() throws IOException {
        api = new ApiImpl();
        faker = new Faker();

        apiDomain = PropertiesHelper.getProperties().getProperty("bookapi.domain");
        port = Integer.parseInt(PropertiesHelper.getProperties().getProperty("bookapi.port"));
        getStatusEndPoint = PropertiesHelper.getProperties().getProperty("bookapi.endpoint.getstatus");
        getbooksEndPoint = PropertiesHelper.getProperties().getProperty("bookapi.endpoint.getbooks");
        getAuthenticateEndPoint = PropertiesHelper.getProperties().getProperty("bookapi.endpoint.authenticate");
        createOrdersEndPoint = PropertiesHelper.getProperties().getProperty("bookapi.endpoint.createorders");

        dynamicOrderJson = PropertiesHelper.getProperties().getProperty("dynamicorderjson.path");
        staticOrderJson = PropertiesHelper.getProperties().getProperty("staticorderjson.path");
        contactJson = PropertiesHelper.getProperties().getProperty("contactjson.path");
        contactCSV = PropertiesHelper.getProperties().getProperty("contactcsv.path");


        Authentication authentication = new Authentication();
        authentication.setClientName("Ankit");
        authentication.setClientEmail(faker.internet().emailAddress());

        api.init(apiDomain, getAuthenticateEndPoint, port, HttpMethod.POST);
        api.setHeader("Content-Type", "application/json");
        api.setBody(authentication);
        api.callService();
        api.assertIt(201);

        accessToken = api.extractString("accessToken");

        contactDataProvider = new ContactDataProvider();
        contactDataProvider.getContactsDataFromCsv(contactCSV);

    }

    @Test
    public void testStatus() {
        api.init(apiDomain, getStatusEndPoint, port, HttpMethod.GET);
        api.callService();
        api.assertIt(200);
    }

    @Test
    public void testBooksNonEmpty() {
        api.init(apiDomain, getbooksEndPoint, port, HttpMethod.GET);
        api.callService();
        api.assertIt(200);
        api.assertIt("id", "", ValidatorOperation.NOT_EMPTY);
    }

    @Test
    public void testBooksCount() {
        api.init(apiDomain, getbooksEndPoint, port, HttpMethod.GET);
        api.callService();
        api.assertIt(200);
        api.assertIt("id.size()", 6, ValidatorOperation.EQUALS);
    }

    @Test
    public void testSubmitStaticOrder() {
        api.init(apiDomain, createOrdersEndPoint, port, HttpMethod.POST);
        api.setHeader("Authorization", "Bearer " + accessToken);
        api.setHeader("Content-Type", "application/json");
        api.setBody("{\n" +
                "  \"bookId\": 1,\n" +
                "  \"customerName\": \"Mohan\"\n" +
                "}");
        api.callService();
        api.assertIt(201);
        System.out.println(api.getResponseBody());
    }

    @Test
    public void testSubmitDynamicOrder() throws IOException {
        final var jsonPayload = IoHelper.getFileData(IoHelper.getFile(dynamicOrderJson))
                .replaceAll("@customerName@", faker.name().name());

        api.init(apiDomain, createOrdersEndPoint, port, HttpMethod.POST);
        api.setHeader("Authorization", "Bearer " + accessToken);
        api.setHeader("Content-Type", "application/json");
        api.setBody(jsonPayload);
        api.callService();
        api.assertIt(201);
        System.out.println(api.getResponseBody());
    }

    @Test
    public void testSubmitStaticOrderWithAsFile() throws IOException {
        api.init(apiDomain, createOrdersEndPoint, port, HttpMethod.POST);
        api.setHeader("Authorization", "Bearer " + accessToken);
        api.setHeader("Content-Type", "application/json");
        api.setBody(IoHelper.getFile(staticOrderJson));
        api.callService();
        api.assertIt(201);
        System.out.println(api.getResponseBody());
    }

    @Test(priority = 0)
    public void testCSV() throws IOException {
        var records = CsvHelper
                .getCSVData(IoHelper.getFile(contactCSV), ',');
        for (var data : records) {
            String payLoad = StringHelper
                    .populateJSONSchemaWithData
                            (IoHelper.getFileData(IoHelper.getFile(contactJson))
                                    , data);
            System.out.println(payLoad);
        }
    }

    @Test(dataProvider = "ContactsData", dataProviderClass = ContactDataProvider.class, priority = 1)
    public void testCSVUsingDataProvider(String firstName, String lastName, String gender, String age, String streetAddress,
                                         String city, String state, String postalCode, String type1, String number1, String type2,
                                         String number2) throws IOException {

        final var jsonPayload = IoHelper.getFileData(IoHelper.getFile(contactJson))
                .replaceAll("@firstName@", firstName)
                .replaceAll("@lastName@", lastName)
                .replaceAll("@gender@", gender)
                .replaceAll("@age@",age)
                .replaceAll("@streetAddress@", streetAddress)
                .replaceAll("@city@", city)
                .replaceAll("@state@", state)
                .replaceAll("@postalCode@", postalCode)
                .replaceAll("@type1@", type1)
                .replaceAll("@number1@", number1)
                .replaceAll("@type2@", type2)
                .replaceAll("@number2@", number2);

        System.out.println(jsonPayload);
    }

    @AfterTest
    public void tearDown() {
    }

}
