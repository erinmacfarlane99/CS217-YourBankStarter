package your.bank;

import org.checkerframework.checker.units.qual.A;

import java.math.BigDecimal;

import static java.math.BigDecimal.*;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Account {

    private BigDecimal amount;
    private String name;
    private String currency;
    private int transactionsProcessed;
    private int transactionsFailed;

    public Account() { this(0); }

    public Account(double amount) { this("placeholder",amount); }

    public Account(String name, double amount) { this(name, amount, "GBP"); }

    public Account(String name, double amount, String currency) {
        this(name,amount,currency,0,0);
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
            this.transactionsProcessed++;
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