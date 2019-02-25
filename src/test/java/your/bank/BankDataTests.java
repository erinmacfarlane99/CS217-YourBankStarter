package your.bank;

import com.mashape.unirest.http.exceptions.UnirestException;
import jooby.test.helpers.JoobyApp;
import jooby.test.helpers.JoobyTest;
import org.jooby.Jooby;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JoobyTest
public class BankDataTests {

    BankingData bd;

    @JoobyApp
    protected Jooby app = new App();

    public void init() throws SQLException {
        bd = new BankingData(app.require (DataSource.class));
        bd.clearAccountsFromDatabase();
    }

    @Test
    public void accountsFromDatabaseTest() throws UnirestException, SQLException {
        init();
        List<Account> acl = new ArrayList<>();
        acl.add(new Account("bill",50));
        acl.add(new Account("bob",345.53));
        acl.add(new Account("ben",99.99));
        bd.writeAccountsToDatabase(acl);
        long length = bd.getAccountsFromDatabase().size();
        assertEquals(3,length);
    }

    @Test
    public void accountsFromApiTest() throws UnirestException, SQLException {
        init();
        long length1 = bd.getAccountsFromApi().size();
        bd.writeAccountsToDatabase(bd.getAccountsFromApi());
        long length2 = bd.getAccountsFromDatabase().size();
        assertEquals(length1,length2);
    }
}
