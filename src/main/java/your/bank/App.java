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
    private List<String> fraudTransactionList = new ArrayList<>();
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

            getAccountsFromApi();
            getTransactionsFromApi();
            getFraudTransactionsFromApi();
            bd.writeAccountsToDatabase(accountList);
            bd.getAccountsFromDatabase();

            //stoping transactions
           for(String s: fraudTransactionList){
               for (int i =0; i < transactionList.size(); i++){
                   if(s.equals(transactionList.get(i).getId())){
                       transactionList.remove(i);
                   }
               }
           }

            //test
            for ( Account a: accountList) {
                System.out.println(a.getName());
                System.out.println(a.getAmount());
                System.out.println(a.getCurrency());
                System.out.println(a.getNumberTransactionsProcessed());
                System.out.println(a.getNumberTransactionsFailed());
            }

            tp.processTransactionList(transactionList, accountList);
            totals[0] = tp.getTotalTransactions();
            totals[1] = tp.getFailedTransactions();
        });

        // Perform actions after startup
        onStarted(() -> {
            System.out.println("Started!");
        });
    }
//    public void searchDataBase(){
//        PreparedStatement sqlStatement = db.prepare
//    }

    private void getAccountsFromApi() throws UnirestException {
        HttpResponse<Account[]> accountsResponse =
                Unirest.get("http://your-bank.herokuapp.com/api/Team6/accounts").asObject(Account[].class);
        accountList = new ArrayList<>(Arrays.asList(accountsResponse.getBody()));
    }

    private void getTransactionsFromApi () throws UnirestException {
        HttpResponse<Transaction[]> accountsResponse =
                Unirest.get("http://your-bank.herokuapp.com/api/Team6/auth/transaction")
                        .basicAuth("Team6","xi35QJzheP")
                        .asObject(Transaction[].class);
        transactionList = new ArrayList<>(Arrays.asList(accountsResponse.getBody()));
    }

    private void getFraudTransactionsFromApi () throws UnirestException {
        HttpResponse<String[]> IDResponse =
                Unirest.get("http://your-bank.herokuapp.com/api/Team6/secure/fraud")
                        .queryString("token","IlFwG0Zmvbhi6Hb72L2tkxttg")
                        .header("accept", "application/json")
                        .asObject(String[].class);
        fraudTransactionList = new ArrayList<>(Arrays.asList(IDResponse.getBody()));
    }

    public static void main(final String[] args) {
        run(App::new, args);
    }

}
