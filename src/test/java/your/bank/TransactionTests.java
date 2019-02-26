package your.bank;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class TransactionTests {

    @Test
    public void test_2ProcessedTransactions(){

        Account a = new Account(40);
        a.deposit(20);
        a.withdraw(20);
        assertEquals(a.getNumberTransactionsProcessed(), 2);
    }

    @Test
    public void test_1FailedTransaction(){
        Account a = new Account(20);
        assertThrows( ArithmeticException.class, () -> a.withdraw(100));
        assertEquals(a.getNumberTransactionsFailed(), 1);
    }
}
