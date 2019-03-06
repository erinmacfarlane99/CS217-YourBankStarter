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
import java.awt.print.Book;
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
    private int[] totals = new int[2];
    private DataSource db;
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

        get("/Team6Bank/transactionInfo", () ->
                Results.html("Transactions").put("accounts",accountList).put("totalProcessed", totals[0]).put("totalFailed", totals[1]));

        // Perform actions on startup
        onStart(() -> {
            System.out.println("Starting Up...");

            db = require(DataSource.class);
            TransactionProcessor tp = new TransactionProcessor();

            bd = new BankingData (require (DataSource.class));

            accountList = bd.getAccountsFromApi();
            transactionList = bd.getTransactionsFromApi();
            bd.writeAccountsToDatabase(accountList);

//            getAccountsFromApi();
//            getTransactionsFromApi();
//            writeAccountsToDatabase(accountList);
//            getAccountsFromDatabase();

//            //test
//            for ( Account a: accountList) {
//                System.out.println(a.getName());
//                System.out.println(a.getAmount());
//                System.out.println(a.getCurrency());
//                System.out.println(a.getNumberTransactionsProcessed());
//                System.out.println(a.getNumberTransactionsFailed());
//            }
            tp.processTransactionList(transactionList, accountList);
            totals[0] = tp.getTotalTransactions();
            totals[1] = tp.getFailedTransactions();

            HttpResponse<Account> accountResponse = Unirest.get("http://your-bank.herouapp.com/api/Team6/accounts").asObject(Account.class);
            Account accountObject = accountResponse.getBody();

        });

        // Perform actions after startup
        onStarted(() -> {
            System.out.println("Started!");
        });

    }

    private void getAccountsFromApi() throws UnirestException {
        HttpResponse<Account[]> accountsResponse =
                Unirest.get("http://your-bank.herokuapp.com/api/Team6/accounts").asObject(Account[].class);
        accountList = Arrays.asList(accountsResponse.getBody());
    }

    private void getTransactionsFromApi () throws UnirestException {
        HttpResponse<Transaction[]> accountsResponse =
                Unirest.get("http://your-bank.herokuapp.com/api/Team6/auth/transaction")
                        .basicAuth("Team6","xi35QJzheP")
                        .asObject(Transaction[].class);
        transactionList = Arrays.asList(accountsResponse.getBody());
    }

    private void writeAccountsToDatabase (List<Account> accountList) throws SQLException {

        //opens a connection
        Connection connection = db.getConnection();

        //create a table
        Statement stmt = connection.createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS bankAccount (\n"
                +" name text, \n"
                + " amount decimal, \n"
                + " currency text, \n"
                + " transactionsProcessed int, \n"
                + " transactionsFailed int);";
        stmt.execute(sql);

        //insert data
        String sql2 = "INSERT INTO bankAccount (name, amount, currency, transactionsProcessed, transactionsFailed) " + "VALUES (?,?,?,?,?)";
        PreparedStatement prep = connection.prepareStatement(sql2);
        for ( Account a: accountList) {
            prep.setString(1, a.getName());
            prep.setDouble(2, a.getAmount());
            prep.setString(3, a.getCurrency());
            prep.setInt(4, a.getTransactionsProcessed());
            prep.setInt(5, a.getTransactionsFailed());
            prep.executeUpdate();
        }

        connection.close();
    }

    private void getAccountsFromDatabase () throws SQLException {

        Connection connection = db.getConnection();

        //retrieving data
        Statement stmt2 = connection.createStatement();
        String sql3 = "SELECT * FROM bankAccount";
        ResultSet rs = stmt2.executeQuery(sql3);

        //creating accounts from results
        accountList = new ArrayList<>();
        while (rs.next()){
            String name = rs.getString("name");
            int amount = rs.getInt("amount");
            String currency = rs.getString("currency");
            int transactionsProcessed = rs.getInt("transactionsProcessed");
            int transactionsFailed = rs.getInt("transactionsFailed");
            accountList.add(new Account(name, amount, currency, transactionsProcessed, transactionsFailed));
        }
        rs.close();
        connection.close();
    }

//    public void searchDataBase(){
//        PreparedStatement sqlStatement = db.prepare
//    }

    public static void main(final String[] args) {

        run(App::new, args);
    }

}
