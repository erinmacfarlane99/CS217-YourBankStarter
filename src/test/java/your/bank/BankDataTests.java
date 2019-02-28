package your.bank;

import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DatabaseTestsExt.DatabaseTest
public class BankDataTests {

    DataSource ds;
    BankingData bd;

    @BeforeEach
    void setUp(DataSource dataSource) {
        this.ds = dataSource;

        this.bd = new BankingData(dataSource);
    }

    @Test
    public void insertingAccountListTest() throws UnirestException, SQLException {
        List<Account> acl = new ArrayList<>();
        acl.add(new Account("bill",50));
        acl.add(new Account("bob",345.53));
        acl.add(new Account("ben",99.99));
        bd.writeAccountsToDatabase(acl);
        long length = bd.getAccountsFromDatabase().size();
        assertEquals(3,length);
    }

    @Test
    public void insertingSingleAccountTest() throws UnirestException, SQLException {
        bd.writeAccountToDatabase(new Account("bill",50));
        long length = bd.getAccountsFromDatabase().size();
        assertEquals(1,length);
    }

    @Test
    public void insertingMultipleAccountTest() throws UnirestException, SQLException {
        for (int i = 0; i < 20; i++){
            bd.writeAccountToDatabase(new Account("bill",50));

        }

        long length = bd.getAccountsFromDatabase().size();
        assertEquals(20,length);
    }

}
