package your.bank;

import static io.restassured.RestAssured.get;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

import jooby.test.helpers.JoobyApp;
import jooby.test.helpers.JoobyTest;
import org.jooby.Jooby;
import org.jooby.Result;
import org.jooby.Results;
import org.jooby.test.MockRouter;
import org.junit.jupiter.api.Test;

// This test requires Jooby to be running
@JoobyTest
public class AppTest {

    // This is the instance of Jooby app to run
    @JoobyApp
    protected Jooby app = new App();

    @Test
    public void integrationTest() {
        get("/")
                .then()
                .assertThat()
                .body(containsString("Welcome to the Banking Home Screen"))
                .statusCode(200)
                .contentType("text/html;charset=UTF-8");
               // .contentType("application/json;charset=UTF-8");
    }

    @Test
    public void unitTest() throws Throwable {

        String result = new MockRouter(new App())
                .get("/").toString();

        assertEquals(Results.html("BankingHome").toString(), result);

//        String result = new MockRouter(new App())
//                .get("/");
//        assertEquals("Welcome to the Banking Home Screen", result);
    }

    @Test
    public void TestAccountDetailsJSON() {
        get("/accountDetailsJSON")
                .then()
                .assertThat()
                .statusCode(200)
                .body(containsString("name"))
                .body(containsString("amount"))
                .body(containsString("currency"))
                .contentType("application/json;charset=UTF-8");
    }
}
