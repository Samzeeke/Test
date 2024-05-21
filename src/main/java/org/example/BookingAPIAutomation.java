package org.example;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;


public class BookingAPIAutomation {

    private String baseUrl = "https://restful-booker.herokuapp.com";
    private int bookingId;
    private String token;

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = baseUrl;

        // Here assuming token is "abc123" 
        this.token = "abc123";
    }

    @Test(priority = 1)
    public void createBooking() {
        String requestBody = "{\n" +
                "    \"firstname\" : \"Jim\",\n" +
                "    \"lastname\" : \"Brown\",\n" +
                "    \"totalprice\" : 111,\n" +
                "    \"depositpaid\" : true,\n" +
                "    \"bookingdates\" : {\n" +
                "        \"checkin\" : \"2018-01-01\",\n" +
                "        \"checkout\" : \"2019-01-01\"\n" +
                "    },\n" +
                "    \"additionalneeds\" : \"Breakfast\"\n" +
                "}";

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/booking")
                .then()
                .statusCode(200)
                .extract().response();

        bookingId = response.jsonPath().getInt("bookingid");
        System.out.println("Booking ID: " + bookingId);
    }

    @Test(priority = 2)
    public void getBooking() {
        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/booking/" + bookingId)
                .then()
                .statusCode(200)
                .extract().response();

        String firstname = response.jsonPath().getString("firstname");
        String lastname = response.jsonPath().getString("lastname");
        int totalprice = response.jsonPath().getInt("totalprice");
        boolean depositpaid = response.jsonPath().getBoolean("depositpaid");
        String checkin = response.jsonPath().getString("bookingdates.checkin");
        String checkout = response.jsonPath().getString("bookingdates.checkout");
        String additionalneeds = response.jsonPath().getString("additionalneeds");

        assertEquals(firstname, "Jim");
        assertEquals(lastname, "Brown");
        assertEquals(totalprice, 111);
        assertEquals(depositpaid, true);
        assertEquals(checkin, "2018-01-01");
        assertEquals(checkout, "2019-01-01");
        assertEquals(additionalneeds, "Breakfast");
    }

    @Test(priority = 3)
    public void updateBooking() {
        String updateRequestBody = "{\n" +
                "    \"firstname\" : \"James\",\n" +
                "    \"lastname\" : \"Brown\",\n" +
                "    \"totalprice\" : 111,\n" +
                "    \"depositpaid\" : true,\n" +
                "    \"bookingdates\" : {\n" +
                "        \"checkin\" : \"2018-01-01\",\n" +
                "        \"checkout\" : \"2019-01-01\"\n" +
                "    },\n" +
                "    \"additionalneeds\" : \"Breakfast\"\n" +
                "}";

        Response response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("Cookie", "token=" + token)
                .body(updateRequestBody)
                .when()
                .put("/booking/" + bookingId)
                .then()
                .statusCode(200)
                .extract().response();

        String firstname = response.jsonPath().getString("firstname");
        String lastname = response.jsonPath().getString("lastname");
        String additionalneeds = response.jsonPath().getString("additionalneeds");

        assertEquals(firstname, "James");
        assertEquals(lastname, "Brown");
        assertEquals(additionalneeds, "Breakfast");
    }
}
