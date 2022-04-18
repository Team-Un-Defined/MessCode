package JDBC;

import JDBC.DBConn.DatabaseConnection;
import com.messcode.transferobjects.AccountManager;
import com.messcode.transferobjects.Group;
import com.messcode.transferobjects.User;
import com.messcode.transferobjects.messages.PublicMessage;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class ImportData {

    private Character character;
    private Connection c;
    private DatabaseConnection conn;
    public ImportData() {
        /**
         * Constructor method for LoadCharacter. Gets the PostgreSQL connection.
         */
        conn = new DatabaseConnection();
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection(conn.getConn(), conn.getName(),
                            conn.getPass()); //use your own password here

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates an SQL statement that will create an account in the Database
     *
     * @param fname String containing the first name
     * @param lname String containing the last name
     * @param pwd_hash   Byte array containing the pass hash
     * @param pwd_salt String containing the salt
     * @param email String containing the email
     * @param type    String containing the user type
     * @throws SQLException an exception that provides information on a database
     *                      access error or other errors.
     */
    public void createAccount(String fname,String lname, Byte[] pwd_hash,String pwd_salt ,String email,String type)
            throws SQLException {
        boolean done = false;
        Statement st = c.createStatement();
        String query =
                "INSERT INTO  \"Users\".\"Users\" VALUES( '" +fname + "', '"
                        +email+ "', null, '" + email + "') ";

        st.executeUpdate(query);
    }




    public void addUsersToProject(Group grp)
            throws SQLException {
        boolean done = false;
        Statement st = c.createStatement();
        String query ="";

        st.executeUpdate(query);
    }

    public void saveMessage(PublicMessage pm)
            throws SQLException {
        boolean done = false;
        Statement st = c.createStatement();
        String query ="";


        st.executeUpdate(query);
    }
    public void saveDataOnExit(ArrayList<PublicMessage> msgs)
            throws SQLException {
        boolean done = false;
        Statement st = c.createStatement();
        String query ="";


        st.executeUpdate(query);
    }
    public boolean test(String password, String email) throws SQLException {
        boolean unique = false;
        Statement st = c.createStatement();
        String query =
                "SELECT * FROM Account WHERE  email = '" + email + "'";


        ResultSet rs = st.executeQuery(query);

        String ema = null;
        String salt = null;
        String hashedPassword = null;
        byte[] pass = null;

        while (rs.next()) {

            hashedPassword = rs.getString("pwd_hash");
            ema = rs.getString("email");
            salt = rs.getString("pwd_salt");


            System.out.println("salt = " + salt);
            System.out.println("password =  " +hashedPassword);
            AccountManager am = new AccountManager();
            pass = am.hashPassword(password, salt);
            System.out.println("cli:"+ Arrays.toString(pass));


            break;
        }


        String query2 =
                " update public.account\n" +
                        "set pwd_hash='" + Arrays.toString(pass) +
                        "' where email='" + email + "' ;";

        System.out.println("EXECUTED QUERY");
         st.executeUpdate(query2);

        return unique;
    }
}
