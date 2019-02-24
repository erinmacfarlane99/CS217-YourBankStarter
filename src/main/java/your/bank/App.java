package your.bank;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import jooby.helpers.UnirestHelper;
import org.jooby.Jooby;
import org.jooby.Mutant;
import org.jooby.Results;
import org.jooby.hbs.Hbs;
import org.jooby.jdbc.Jdbc;
import org.jooby.json.Jackson;
import org.json.JSONObject;

import java.sql.*;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


/**
 * @author jooby generator
 */
public class App extends Jooby {

    private List<Account> accountList = new ArrayList<>();
    private List<Transaction> transactionList = new ArrayList<>();
    private BankingData bd;


    {
        // -- Start Boilerplate Setup --
        use(new UnirestHelper());
        use(new Hbs());
        use(new Jackson());
        use(new Jdbc("db"));


        assets("/bootstrap/**");
        assets("/static/**");

        // -- End Boilerplate Setup --

        // Simple GET Request
        get("/", () -> Results.redirect("/Team6Bank"));

        get("/Team6Bank", () -> Results.html("BankingHome"));


        // GET request which makes a call to another endpoint
        get("/hello", () -> {
            return "Hello " + Unirest.get("http://faker.hook.io/").asString().getBody();
        });

        // GET request with a template and a query parameter /dice?name=Jim
        get("/dice", (request) -> {
            String name = "Your";

            // Mutant is Jooby's representation of data which has not been cast to a type (and could be null/not set)
            Mutant query = request.param("name");
            if (query.isSet()) {
                name = query.value() + "'s";
            }

            // This renders public/dice.html with the model populated with random and name
            return Results.html("dice")
                    .put("random", new Random().nextInt(6))
                    .put("name", name);
        });

        get("/Team6Bank/accountDetailsJSON", () -> Results.json(bd.getAccountsFromDatabase()));

        get("/Team6Bank/accountDetailsTable", () -> Results.html("Accounts").put("accounts",bd.getAccountsFromDatabase()));

        // Perform actions on startup
        onStart(() -> {
            System.out.println("Starting Up...");
             bd = new BankingData (require (DataSource.class));

            accountList = bd.getAccountsFromApi();
            transactionList = bd.getTransactionsFromApi();
            bd.writeAccountsToDatabase(accountList);
        });

        // Perform actions after startup
        onStarted(() -> {
            System.out.println("Started!");
        });

    }

    public static void main(final String[] args) {
        run(App::new, args);
    }

}
