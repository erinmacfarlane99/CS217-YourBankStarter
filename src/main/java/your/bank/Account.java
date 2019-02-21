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

    public Account() {
        name = "placeholder_name";
        amount = new BigDecimal(0);
        currency = "GBP";
        successfulTransactions = new ArrayList<>();
        failedTransactions = new ArrayList<>();
    }

    public Account(double amount) {
        name = "placeholder_name";
        this.amount = (amount >= 0) ? new BigDecimal(amount) : new BigDecimal(0);
        currency = "GBP";
        successfulTransactions = new ArrayList<>();
        failedTransactions = new ArrayList<>();
    }

    public Account(String name, double amount) {
        this.amount = (amount >= 0) ? new BigDecimal(amount) : new BigDecimal(0);
        this.name = name;
        currency = "GBP";
        successfulTransactions = new ArrayList<>();
        failedTransactions = new ArrayList<>();
    }

    public Account(String name, double amount, String currency) {
        this.amount = (amount >= 0) ? new BigDecimal(amount) : new BigDecimal(0);
        this.name = name;
        this.currency = currency;
        successfulTransactions = new ArrayList<>();
        failedTransactions = new ArrayList<>();
    }

    public Account(String name, double amount, String currency, int transactionsProcessed, int transactionsFailed) {
        this.amount = (amount >= 0) ? new BigDecimal(amount) : new BigDecimal(0);
        this.name = name;
        this.currency = currency;
        successfulTransactions = new ArrayList<>();
        failedTransactions = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getTransactionsFailed() {
        return failedTransactions.size();
    }

    public int getTransactionsProcessed() { return successfulTransactions.size() + failedTransactions.size(); }

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

    public void addSuccessfulTransaction(Transaction successfulTransaction) {
        successfulTransactions.add(successfulTransaction);
    }

    public void addFailedTransaction(Transaction failedTransaction) {
        failedTransactions.add(failedTransaction);
    }

    public void deposit(double amount) {
        this.amount = this.amount.add(valueOf(amount));
    }

    public void withdraw(double amount) {
        if (amount <= this.amount.doubleValue()) {
            this.amount = this.amount.subtract(valueOf(amount));
        } else {
            throw new ArithmeticException("can't withdraw amount greater than amount");
        }

    }

    @Override
    public String toString() {
        return "Account Name: " + this.getName() +
                ", amount: " + new DecimalFormat("#.00").format(this.getAmount()) +
                ", currency: " + this.getCurrency() +
                ", transactionsProcessed: " + this.getTransactionsProcessed() +
                ", transactionsFailed: " + this.getTransactionsFailed();
    }


}