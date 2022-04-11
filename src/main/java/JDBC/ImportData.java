package JDBC;

import java.sql.*;

public class ImportData {

    private Character character;
    private Connection c;

    public ImportData() {
        /**
         * Constructor method for LoadCharacter. Gets the PostgreSQL connection.
         */
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/MessCode", "postgres",
                            "chickenattack777"); //use your own password here

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates an SQL statement that will create an account in the Database
     *
     * @param username String containing the username
     * @param password String containing the password
     * @param email    String containing the email
     * @throws SQLException an exception that provides information on a database
     *                      access error or other errors.
     */
    public void createAccount(String username, String password, String email)
            throws SQLException {
        boolean done = false;
        Statement st = c.createStatement();
        String query =
                "INSERT INTO  \"Users\".\"Users\" VALUES( '" + username + "', '"
                        + password + "', null, '" + email + "') ";

        st.executeUpdate(query);
    }
}
