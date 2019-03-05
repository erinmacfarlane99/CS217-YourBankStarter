package your.bank;

import java.math.BigDecimal;
import static java.math.BigDecimal.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Account {

    private BigDecimal amount;
    private ArrayList<Transaction> successfulTransactions;
    private ArrayList<Transaction> failedTransactions;
    private String name;
    private String currency;

    public Account() { this(0); }

    public Account(double amount) { this("placeholder",amount); }

    public Account(String name, double amount) { this(name, amount, "GBP"); }

    public Account(String name, double amount, String currency) {
        this.amount = (amount >= 0) ? new BigDecimal(amount) : new BigDecimal(0);
        this.name = name;
        this.currency = currency;
        successfulTransactions = new ArrayList<>();
        failedTransactions = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getNumberTransactionsFailed() {
        return failedTransactions.size();
    }

    public int getNumberTransactionsProcessed() {
        return successfulTransactions.size() + failedTransactions.size();
    }

    public double getInitialAmount() {
        if (successfulTransactions.size() > 0) {
            return successfulTransactions.get(0).getStartingAmount(this.getName());
        }
        return this.amount.doubleValue();
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount.doubleValue();
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void addFailedTransaction(Transaction failedTransaction) {
        failedTransactions.add(failedTransaction);
    }

    public void deposit(double amount) {
        Transaction t = new Transaction(null, amount, null, this.name);
        t.setToStartingAmount(this.amount.doubleValue());
        this.amount = this.amount.add(valueOf(amount));
        successfulTransactions.add(t);
    }

    public void deposit(Transaction t) {
        t.setToStartingAmount(this.amount.doubleValue());
        successfulTransactions.add(t);
        this.amount = this.amount.add(valueOf(t.getAmount()));
    }

    public void withdraw(Transaction t) {
        if (t.getAmount() <= this.amount.doubleValue()) {
            t.setFromStartingAmount(this.amount.doubleValue());
            successfulTransactions.add(t);
            this.amount = this.amount.subtract(valueOf(t.getAmount()));
        } else {
            failedTransactions.add(t);
            throw new ArithmeticException("can't withdraw amount greater than amount");
        }
    }

    @Override
    public String toString() {
            return "Account Name: " + this.getName() +
                    ", amount: " + new DecimalFormat("#.00").format(this.getAmount()) +
                    ", currency: " + this.getCurrency() +
                    ", numberTransactionsProcessed: " + this.getNumberTransactionsProcessed() +
                    ", numberTransactionsFailed: " + this.getNumberTransactionsFailed() +
                    ", initialAmount: " + getInitialAmount();
    }
}

