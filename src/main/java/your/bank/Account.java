package your.bank;

import java.math.BigDecimal;

import static java.math.BigDecimal.*;

import java.text.DecimalFormat;

public class Account {

    private BigDecimal balance;
    private String accName;

    public Account() {
        accName = "placeholder_name";
        balance = new BigDecimal(0);
    }

    public Account(double amount) {
        accName = "placeholder_name";
        balance = new BigDecimal(amount);
    }

    public Account(String name, double amount) {
        balance = new BigDecimal(amount);
        accName = name;
    }

    public String getAccountName() {
        return accName;
    }

    public void setAccountName(String accName) {
        this.accName = accName;
    }

    public double getBalance() {
        return balance.doubleValue();
    }

    public void deposit(double amount) {
        balance = balance.add(valueOf(amount));
    }

    public void withdraw(double amount) {
        if (amount <= balance.doubleValue()) {
            balance = balance.subtract(valueOf(amount));
        } else {
            throw new ArithmeticException("can't withdraw amount greater than balance");
        }

    }

    @Override
    public String toString() {
        return "name: " + this.getAccountName() + " balance: " + new DecimalFormat("#.00").format(this.getBalance());
    }


}
