package your.bank;

import java.math.BigDecimal;

import static java.math.BigDecimal.*;

import java.text.DecimalFormat;

public class Account {

    private BigDecimal amount;
    private String name;
    private String currency;
    private int succesfulTransac, unsuccesfullTransac = 0;
    private int transactionsProcessed;
    private int transactionsFailed;

    public Account() {
        name = "placeholder_name";
        amount = new BigDecimal(0);
        currency = "GBP";
        this.transactionsProcessed = 0;
        this.transactionsFailed = 0;
    }

    public Account(double amount) {
        name = "placeholder_name";
        this.amount = (amount >= 0) ? new BigDecimal(amount) : new BigDecimal(0);
        currency = "GBP";
        this.transactionsProcessed = 0;
        this.transactionsFailed = 0;
    }

    public Account(String name, double amount) {
        this.amount = (amount >= 0) ? new BigDecimal(amount) : new BigDecimal(0);
        this.name = name;
        currency = "GBP";
        this.transactionsProcessed = 0;
        this.transactionsFailed = 0;
    }

    public Account(String name, double amount, String currency) {
        this.amount = (amount >= 0) ? new BigDecimal(amount) : new BigDecimal(0);
        this.name = name;
        this.currency = currency;
        this.transactionsProcessed = 0;
        this.transactionsFailed = 0;
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

    public int getTransactionsFailed() {
        return transactionsFailed;
    }

    public int getTransactionsProcessed() {
        return transactionsProcessed;
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


    public void deposit(double amount) {
        this.amount = this.amount.add(valueOf(amount));
        this.transactionsProcessed++;
    }

    public void withdraw(double amount) {
        if (amount <= this.amount.doubleValue()) {
            this.amount = this.amount.subtract(valueOf(amount));
            this.succesfulTransac ++;
            this.transactionsProcessed++;
        } else{
            this.unsuccesfullTransac += 1;
            throw new ArithmeticException("can't withdraw amount greater than amount");
        }
    }

    public int getSuccesfulTransac(Account a){
        return a.succesfulTransac;
    }

    public int getUnSuccesfulTransac(Account a){
        return a.unsuccesfullTransac;
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

