package your.bank;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class TransactionTests {

    @Test
        public void test_ProcessedTransactions(){

        Account a = new Account(40);
        Account b = new Account(10);
        a.deposit(20);
        a.withdraw(20);
        b.deposit(10);
        b.withdraw(20);
        b.deposit(10);
        assertEquals(a.getNumberTransactionsProcessed(), 2);
        assertEquals(b.getNumberTransactionsProcessed(), 3);
    }

    @Test
    public void test_FailedTransactions(){
        Account a = new Account(20);
        Account b = new Account(30);
        assertThrows( ArithmeticException.class, () -> a.withdraw(100));
        assertThrows( ArithmeticException.class, () -> a.withdraw(30));
        assertThrows( ArithmeticException.class, () -> b.withdraw(30.01));
        assertEquals(a.getNumberTransactionsFailed(), 2);
        assertEquals(b.getNumberTransactionsFailed(), 1);
    }

    @Test
    public void test_TotalTransactions(){
        Account a = new Account(20);
        Account b = new Account(30);
        Account c = new Account(80);
        assertThrows( ArithmeticException.class, () -> a.withdraw(21));
        assertThrows( ArithmeticException.class, () -> b.withdraw(30.01));
        a.deposit(20);
        b.deposit(10);
        c.withdraw(70);
        c.deposit(10);
        a.deposit(10);
        //(.getTotalTransactions(), 5);
    }


}
