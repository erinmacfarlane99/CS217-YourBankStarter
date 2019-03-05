package your.bank;

import java.util.List;

public class TransactionProcessor {
    int totalTransactions;
    int successfulTransactions;
    int failedTransactions;

    public TransactionProcessor(){
        totalTransactions = 0;
        failedTransactions = 0;
    }

    public void processTransactionList(List<Transaction> transactionList, List<Account> accountList) {
        for (Transaction t : transactionList) {
            boolean foundName = false;
            for (Account a : accountList) {
                if (a.getName().equals(t.getFrom())) {
                    foundName = true;
                    try {
                        boolean found = false;
                        for (Account b : accountList) {
                            if (b.getName().equals(t.getTo())) {
                                found = true;
                                a.withdraw(t);
                                b.deposit(t);
                                successfulTransactions++;
                            }
                        }
                        if (!found) {
                            a.addFailedTransaction(t);
                            failedTransactions++;
                        }
                    } catch (ArithmeticException e){
                        failedTransactions++;
                        for (Account b : accountList) {
                            if (b.getName().equals(t.getTo())) {
                                b.addFailedTransaction(t);
                            }
                        }
                    }
                }
            }
            if (!foundName) {
                failedTransactions++;
            }
        }
    }

    public void processTransaction(Transaction t, Account fromAccount, Account toAccount) {
        if (fromAccount.getName().equals(t.getFrom())) {
            try {
                if (toAccount.getName().equals(t.getTo())) {
                    fromAccount.withdraw(t);
                    toAccount.deposit(t);
                    successfulTransactions++;
                }
                else {
                    fromAccount.addFailedTransaction(t);
                    toAccount.addFailedTransaction(t);
                    failedTransactions++;
                }
            } catch (ArithmeticException e){
                if (toAccount.getName().equals(t.getTo())) {
                    toAccount.addFailedTransaction(t);
                }
                failedTransactions++;
            }
        }
        else {
            failedTransactions++;
        }
    }

    public int getTotalTransactions() {
        return this.successfulTransactions + this.failedTransactions;
    }
    public int getSuccessfulTransactions() {
        return this.successfulTransactions;
    }
    public int getFailedTransactions() {
        return this.failedTransactions;
    }
}
