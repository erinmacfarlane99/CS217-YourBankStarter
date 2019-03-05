package your.bank;

import java.sql.PreparedStatement;

public class AccountSearch {
    private String account_first_name;
    private String account_second_name;


    public AccountSearch(String firstname, String lastname){
        account_first_name = firstname;
        account_second_name = lastname;
    }

    private void generateSQLQuery(){
        PreparedStatement sqlQuery =


        String newSQLquery = "SELECT * FROM TABLE WHERE firstname = " + account_first_name +
                "AND lastname = " + account_second_name + ";";

    }

}
