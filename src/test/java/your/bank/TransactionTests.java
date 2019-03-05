package your.bank;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

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
    public void test_TotalTransactions_using_single_transactions(){
        Account a = new Account( "Bill", 20);
        Account b = new Account( "Fred", 30);
        Account c = new Account( "Rose", 80);
        Transaction t1 = new Transaction("transaction1", 30, "Bill", "Fred");
        Transaction t2 = new Transaction("transaction2", 20, "Bill", "Fred");
        Transaction t3 = new Transaction("transaction3", 50, "Fred", "Rose");

        TransactionProcessor tp = new TransactionProcessor();
        tp.processTransaction(t1, a, b);
        tp.processTransaction(t2, a, b);
        tp.processTransaction(t3, b, c);

        assertEquals(tp.getTotalTransactions(), 3);
        assertEquals(tp.getSuccessfulTransactions(), 2);
        assertEquals(tp.getFailedTransactions(), 1);
    }

    @Test
    public void test_TotalTransactions_using_list_of_transactions() {
        TransactionProcessor tp = new TransactionProcessor();
        ArrayList<Transaction> transactions = new ArrayList<>();
        ArrayList<Account> accounts = new ArrayList<>();

        accounts.add(new Account("Greg", 80));
        accounts.add(new Account("Jill", 30));
        accounts.add(new Account("Jamie", 10));
        transactions.add(new Transaction("1", 10, "Greg", "Jill"));
        transactions.add(new Transaction("2", 10, "Greg", "Jill"));
        transactions.add(new Transaction("3", 30, "Jamie", "Jill"));
        transactions.add(new Transaction("4", 20, "Jamie", "Greg"));
        transactions.add(new Transaction("5", 18, "Jill", "Fred"));

        tp.processTransactionList(transactions, accounts);
        assertEquals(tp.getTotalTransactions(), 5);
    }

    @Test
    public void test_successful_transactions() {
        TransactionProcessor tp = new TransactionProcessor();

        Account a = new Account("Kim", 8);
        Account b = new Account("John", 12);
        Transaction t1 = new Transaction("1", 4, "Kim", "John");
        Transaction t2 = new Transaction("2", 5, "Kim", "John");

        tp.processTransaction(t1, a, b);
        tp.processTransaction(t2, a, b);

        assertEquals(tp.getSuccessfulTransactions(), 1);
        assertEquals(tp.getTotalTransactions(), 2);
    }

    @Test
    public void test_failing_transactions() {
        TransactionProcessor tp = new TransactionProcessor();
        ArrayList<Transaction> transactions = new ArrayList<>();
        ArrayList<Account> accounts = new ArrayList<>();

        accounts.add(new Account("Greg", 80));
        accounts.add(new Account("Jill", 30));
        accounts.add(new Account("Jamie", 10));
        transactions.add(new Transaction("1", 200, "Greg", "Jill"));
        transactions.add(new Transaction("2", 200, "John", "Jill"));
        transactions.add(new Transaction("3", 30, "Jill", "Jamie"));

        tp.processTransactionList(transactions, accounts);

        assertEquals(tp.getFailedTransactions(), 2);
        assertEquals(tp.getTotalTransactions(), 3);
    }


}
