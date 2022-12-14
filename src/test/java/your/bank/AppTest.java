package your.bank;

import jooby.test.helpers.JoobyApp;
import jooby.test.helpers.JoobyTest;
import org.jooby.Jooby;
import org.jooby.Results;
import org.jooby.test.MockRouter;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.get;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
    }

    @Test
    public void homepageUnitTest() throws Throwable {

        String result = new MockRouter(new App())
                .get("/Team6Bank").toString();

        assertEquals(Results.html("BankingHome").toString(), result);

    }

    @Test
    public void TestAccountDetailsJSON() {
        get("/Team6Bank/accountDetailsJSON")
                .then()
                .assertThat()
                .statusCode(200)
                .body(containsString("name"))
                .body(containsString("amount"))
                .body(containsString("currency"))
                .contentType("application/json;charset=UTF-8");
    }

    @Test
    public void TestAccountDetails() {
        get("/Team6Bank/accountDetailsTable")
                .then()
                .assertThat()
                .statusCode(200)
                .body(containsString("Account Name"))
                .body(containsString("Account Balance"))
                .body(containsString("Currency"))
                .contentType("text/html;charset=UTF-8");
    }
}
