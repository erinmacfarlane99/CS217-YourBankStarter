package your.bank;

import static org.junit.jupiter.api.Assertions.*;
//import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AccountTests {

    @Test
    public void createAccount(){
        Account a = new Account();
        assertTrue(a != null);
    }

    @Test
    public void test1_newAccountZero(){
        Account a = new Account();
        assertEquals(a.getBalance(),0);
    }


    @Test
    public void test2_deposits(){
        Account a = new Account(50);
        a.deposit(20);
        assertEquals(a.getBalance(),70);
    }

    @Test
    public void test3_withdrawal(){
        Account a = new Account(40);
        a.withdraw(20);
        assertEquals(a.getBalance(),20);
    }

    @Test
    public void test4_Overdraft(){
        Account a = new Account(30);
        assertThrows( ArithmeticException.class, () -> a.withdraw(100));
    }

    @Test
    public void test5_DepositsAndWithdrawals () {
        Account a = new Account(20);
        for (int i = 0; i<5; i++){
            a.deposit(10);
        }
        for (int i = 0; i<3; i++){
            a.withdraw(20);
        }
        assertEquals(a.getBalance(),10);
    }

    @Test
    public void test6_BigDecimals () {
        Account a = new Account(5.45);
        a.deposit(17.56);
        assertEquals(a.getBalance(),23.01);
    }

    @Test
    public void test_noNegativeBalance(){
        Account a = new Account(-50);
        assertEquals(a.getBalance(),0);
    }


}
