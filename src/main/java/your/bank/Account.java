package your.bank;

import java.math.BigDecimal;

import static java.math.BigDecimal.*;

import java.text.DecimalFormat;

public class Account {

    private BigDecimal amount;
    private String name;
    private String currency;
    private int totalTransactions;
    private int transactionsProcessed;
    private int transactionsFailed;
    private double balanceBefore;
    private double balanceAfter;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
    
    public Account() {
        name = "placeholder_name";
        amount = new BigDecimal(0);
        currency = "GBP";
    }

    public Account(double amount) {
        name = "placeholder_name";
        this.amount = (amount >= 0) ? new BigDecimal(amount) : new BigDecimal(0);
        currency = "GBP";
    }

    public Account(String name, double amount) {
        this.amount = (amount >= 0) ? new BigDecimal(amount) : new BigDecimal(0);
        this.name = name;
        currency = "GBP";
    }

    public Account(String name, double amount, String currency) {
        this.amount = (amount >= 0) ? new BigDecimal(amount) : new BigDecimal(0);
        this.name = name;
        this.currency = currency;
    }

    public Account(String name, double amount, String currency, int transactionsProcessed, int transactionsFailed) {
        this.amount = (amount >= 0) ? new BigDecimal(amount) : new BigDecimal(0);
        this.name = name;
        this.currency = currency;
        this.transactionsProcessed = transactionsProcessed;
        this.transactionsFailed = transactionsFailed;
    }

    public String getName() {
        return name;
    }

    public double getBalanceBefore() {
        return balanceBefore;
    }

    public double getBalanceAfter() {
        return balanceAfter;
    }

    public int getTransactionsFailed() {
        return transactionsFailed;
    }

    public int getTransactionsProcessed() {
        return transactionsProcessed;
    }

    public int getTotalTransactions() {
        return totalTransactions;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount.doubleValue();
    }

    public void deposit(double amount) {
        this.balanceBefore = this.getAmount();
        this.amount = this.amount.add(valueOf(amount));
        this.balanceAfter = this.getAmount();
        this.transactionsProcessed++;
        totalTransactions++;
    }

    public void withdraw(double amount) {
        if (amount <= this.amount.doubleValue()) {
            this.balanceBefore = this.getAmount();
            this.amount = this.amount.subtract(valueOf(amount));
            this.balanceAfter = this.getAmount();
            this.transactionsProcessed++;
            totalTransactions++;
        } else {
            this.transactionsFailed++;
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