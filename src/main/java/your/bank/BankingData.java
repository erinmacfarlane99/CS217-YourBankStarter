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

    public void clearAccountsFromDatabase() throws SQLException {
        //opens a connection
        Connection connection = db.getConnection();

        //create a table
        Statement stmt = connection.createStatement();
        String sql = "DELETE FROM bankAccount";
        stmt.execute(sql);

        connection.close();
    }

    public void writeAccountsToDatabase (List<Account> accountList) throws SQLException {

        //opens a connection
        Connection connection = db.getConnection();

        //create a table
        Statement stmt = connection.createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS bankAccount (\n"
                +" name text, \n"
                + " amount decimal, \n"
                + " currency text);";
        stmt.execute(sql);

        //insert data
        String sql2 = "INSERT INTO bankAccount (name, amount, currency) " + "VALUES (?,?,?)";
        PreparedStatement prep = connection.prepareStatement(sql2);
        for ( Account a: accountList) {
            prep.setString(1, a.getName());
            prep.setDouble(2, a.getAmount());
            prep.setString(3, a.getCurrency());
            //prep.setInt(4, a.getNumberTransactionsProcessed());
            //prep.setInt(5, a.getNumberTransactionsFailed());
            prep.executeUpdate();
        }

        connection.close();
    }

    public void writeAccountToDatabase (Account account) throws SQLException {
        List<Account> accs = new ArrayList<>();
        accs.add(account);
        writeAccountsToDatabase(accs);
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
            accountList.add(new Account(name, amount, currency));
        }

        rs.close();
        connection.close();
        return accountList;
    }




}
