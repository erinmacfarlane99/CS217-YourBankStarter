package your.bank;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import jooby.helpers.UnirestHelper;
import org.jooby.Jooby;
import org.jooby.Mutant;
import org.jooby.Results;
import org.jooby.hbs.Hbs;
import org.jooby.jdbc.Jdbc;
import org.jooby.json.Jackson;
import org.json.JSONObject;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


/**
 * @author jooby generator
 */
public class App extends Jooby {

    private List<Account> accountList = new ArrayList<>();
    //private Account[] accountList;

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
        get("/", () -> "Hello World!");


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

        get("/accountDetailsJSON", () -> Results.json(accountList));

        get("/accountDetailsTable", () -> Results.html("Accounts").put("accounts",accountList));

        get("/accountDetails", () ->
                Results
                    .when("text/html", () -> Results.html("Accounts").put("accounts",accountList))
                    .when("application/json", () -> Results.json(accountList))
        );

        // Perform actions on startup
        onStart(() -> {
            System.out.println("Starting Up...");

//            DataSource ds = require(DataSource.class);
//            Connection cn = ds.getConnection();
//
//            Statement stmt = cn.createStatement();
//            String sqlTable = "CREATE TABLE IF NOT EXISTS accounts (\n"
//                        + " accName text NOT NULL, \n"
//                        + " balance decimal NOT NULL);";
//            stmt.execute(sqlTable);
//
//            String sqlData = "INSERT INTO accounts (accName, balance)"
//                        + "VALUES (?,?,?)";
//
//            PreparedStatement prep = cn.prepareStatement(sqlData);
//            prep.setString(1,"Rachel");
//            prep.setDouble(2, 50.00);
//            prep.setString(3,"USD");
//            prep.executeUpdate();
//
//            cn.close();


//            accountList.add(new Account("Rachel", 50.00));
//            accountList.add(new Account("Monica", 100.00));
//            accountList.add(new Account("Phoebe", 76.00));
//            accountList.add(new Account("Joey", 23.90));
//            accountList.add(new Account("Chandler", 3.00));
//            accountList.add(new Account("Ross", 54.32));

            HttpResponse<Account[]> accountsResponse =
                    Unirest.get("http://your-bank.herokuapp.com/api/Team6/accounts").asObject(Account[].class);
            accountList = Arrays.asList(accountsResponse.getBody());

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
