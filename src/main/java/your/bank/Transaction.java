package your.bank;

public class Transaction {
    private String id;
    private double fromStartingAmount;
    private double toStartingAmount;
    private double amount;
    private String from;
    private String to;

    //this constructor is needed for json to obj mapping
    public Transaction (){
        this.id = "UNSET";
        this.amount = 0;
        this.from = "NOBODY";
        this.from = "NOBODY";
    }

    public Transaction(String id, double amount, String from, String to) {
        this.id = id;
        fromStartingAmount = 0;
        toStartingAmount = 0;
        this.amount = amount;
        this.from = from;
        this.to = to;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setFromStartingAmount(double startingAmount) {
        fromStartingAmount = startingAmount;
    }

    public void setToStartingAmount(double startingAmount) {
        toStartingAmount = startingAmount;
    }

    public double getStartingAmount(String name) {
        if (name.equals(from)) {
            return fromStartingAmount;
        }
        else if (name.equals(to)) {
            return toStartingAmount;
        }
        return 0;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }


    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + id + '\'' +
                ", amount=" + amount +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                '}';
    }


}
