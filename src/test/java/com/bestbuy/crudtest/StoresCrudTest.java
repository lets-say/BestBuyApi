package com.bestbuy.crudtest;

import com.bestbuy.StoresPojo;
import com.bestbuy.testbase.TestBase;
import com.bestbuy.utils.TestUtils;
import io.restassured.response.Response;
import org.junit.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class StoresCrudTest extends TestBase {
    static String name = "Abcd" + TestUtils.getRandomValue();
    static String type = "Bigstore" + TestUtils.getRandomValue();
    static String address = TestUtils.getRandomValue() + ", 16 Road";
    static String address2 = "My Street";
    static String city = "London";
    static String state = "England";
    static String zip = "123456";
    static int storeId;

    @Test
    public void test001() {

        StoresPojo storesPojo = new StoresPojo();
        storesPojo.setName(name);
        storesPojo.setType(type);
        storesPojo.setAddress(address);
        storesPojo.setAddress2(address2);
        storesPojo.setCity(city);
        storesPojo.setState(state);
        storesPojo.setZip(zip);


        Response response = given()
                .basePath("/stores")
                .header("Content-Type", "application/json")
                .body(storesPojo)
                .when()
                .post();
        response.then().statusCode(201);
        response.prettyPrint();
    }

    @Test
    public void test002() {

        String p1 = "data.findAll{it.name='";
        String p2 = "'}.get(0)";

        HashMap<String, Object> value =
                given()
                        .when()
                        .get()
                        .then()
                        .statusCode(200)
                        .extract()
                        .path(p1 + name + p2);
        storeId = (int) value.get("id");
        System.out.println(value);

    }

    @Test
    public void test003() {
        String p1 = "data.findAll{it.id='";
        String p2 = "'}.get(0)";

        name = name + "_updated";
        type = type + "_updated";
        address = address2 + "_updated";
        address2 = address2 + "_updated";

        StoresPojo storesPojo = new StoresPojo();
        storesPojo.setName(name);
        storesPojo.setType(type);
        storesPojo.setAddress(address);
        storesPojo.setAddress2(address2);
        storesPojo.setCity(city);
        storesPojo.setState(state);
        storesPojo.setZip(zip);

        Response response = given()
                .basePath("/stores")
                .header("Content-Type", "application/json")
                .pathParam("sId", storeId)
                .body(storesPojo)
                .when()
                .put("/{sId}");
        response.then().log().all().statusCode(200);

        HashMap<String, Object> value =
                given()
                        .when()
                        .get()
                        .then()
                        .statusCode(200)
                        .extract()
                        .path(p1 + storeId + p2);

        System.out.println(value);
    }

    @Test
    public void test004() {

        Response response = given()
                .pathParam("storeID", storeId)
                .when()
                .delete("/{storeID}");
        response.then().statusCode(200);
        response.prettyPrint();


        Response response1 =
                given()
                        .pathParam("storeID", storeId)
                        .when()
                        .get("/{storeID}");
        response1.then().statusCode(404);

    }
}
