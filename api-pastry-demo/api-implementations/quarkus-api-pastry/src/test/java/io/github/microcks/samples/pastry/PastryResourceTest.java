package io.github.microcks.samples.pastry;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class PastryResourceTest {

    @Test
    public void testGesPastriesEndpoint() {
        given()
          .when().get("/pastry")
          .then()
             .statusCode(200);
    }

}