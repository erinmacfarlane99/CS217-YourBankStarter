package your.bank;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.jooby.jdbc.Jdbc;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class BankingData {

    private DataSource db;

    public BankingData (DataSource db){
        this.db = db;
    }

    public List<Account> getAccountsFromApi() throws UnirestException {
        HttpResponse<Account[]> accountsResponse =
                Unirest.get("http://your-bank.herokuapp.com/api/Team6/accounts").asObject(Account[].class);
        return Arrays.asList(accountsResponse.getBody());
    }

    public List<Transaction> getTransactionsFromApi () throws UnirestException {
        HttpResponse<Transaction[]> accountsResponse =
                Unirest.get("http://your-bank.herokuapp.com/api/Team6/auth/transaction")
                        .basicAuth("Team6","xi35QJzheP")
                        .asObject(Transaction[].class);
        return Arrays.asList(accountsResponse.getBody());
    }

    public void writeAccountsToDatabase (List<Account> accountList) throws SQLException {

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

    public List<Account> getAccountsFromDatabase () throws SQLException {
        Connection connection = db.getConnection();

        //retrieving data
        Statement stmt2 = connection.createStatement();
        String sql3 = "SELECT * FROM bankAccount";
        ResultSet rs = stmt2.executeQuery(sql3);

        //creating accounts from results
        List<Account> accountList = new ArrayList<>();
        while (rs.next()){
            String name = rs.getString("name");
            double amount = rs.getDouble("amount");
            String currency = rs.getString("currency");
            int transactionsProcessed = rs.getInt("transactionsProcessed");
            int transactionsFailed = rs.getInt("transactionsFailed");
            accountList.add(new Account(name, amount, currency, transactionsProcessed, transactionsFailed));
        }

        rs.close();
        connection.close();
        return accountList;
    }




}
