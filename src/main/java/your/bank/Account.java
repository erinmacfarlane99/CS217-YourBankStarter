package your.bank;

import java.math.BigDecimal;

import static java.math.BigDecimal.*;

public class Account {

    private BigDecimal balance;


    public Account() {
        balance = new BigDecimal(0);
    }

    public Account(double amount) {
        balance = new BigDecimal(amount);
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

    public double getBalance() {
        return balance.doubleValue();
    }
}
