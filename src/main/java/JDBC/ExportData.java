package JDBC;

import JDBC.DBConn.DatabaseConnection;
import com.messcode.transferobjects.ClassName;
import com.messcode.transferobjects.Container;
import com.messcode.transferobjects.User;
import com.messcode.transferobjects.messages.PrivateMessage;
import com.messcode.transferobjects.messages.PublicMessage;

import java.nio.charset.StandardCharsets;
import java.sql.DriverManager;

import java.sql.*;
import java.util.ArrayList;

public class ExportData {

    private Connection c;
    private DatabaseConnection conn;

    public ExportData() {
        /**
         * Constructor method for Loading stuff from database. Gets the PostgreSQL connection.
         */
        conn = new DatabaseConnection();
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection(conn.getConn(), conn.getName(),
                            conn.getPass()); //use your own password here
            System.out.println("hello success");


        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates an SQL statement, if the  email is found in the database
     * the method will return a boolean false. If they are not in the database the
     * credentials are unique and the method will return a boolean true
     *
     * @param email String containing the email
     * @return boolean true if the account is unique and false if it is not
     * @throws SQLException an exception that provides information on a database
     *                      access error or other errors.
     */
    public boolean checkAccountUniqueness(String username, String email) throws SQLException {
        boolean unique = false;
        Statement st = c.createStatement();
        String query =
                "SELECT * FROM Accounts WHERE email ='" + email + "' ;";

        ResultSet rs = st.executeQuery(query);

        String ema = null;
        while (rs.next()) {

            ema = rs.getString("email");

            System.out.println("email = " + ema);
            if (ema != null) {
                System.out.println(unique);
                break;
            }
            System.out.println(unique);
        }
        if (ema == null) {
            unique = true;
            System.out.println(unique);
        }
        return unique;
    }

    /**
     * Creates an SQL statement that checks if the username and password are found in
     * the database. If the credentials are found a Container will be created containing the
     * answer(true), otherwise the answer will be false
     *
     * @param email    String containing the username
     * @param password String containing the password
     * @return a container that has the answer(boolean) from the database
     * @throws SQLException An exception that provides information on a database
     *                      access error or other errors.
     */
    public Container checkLogin(String email, String password) throws SQLException {
        System.out.println("hello why not?");
        boolean answer = false;

        System.out.println("HELLO ");
        Statement st = c.createStatement();
        String query =
                "SELECT * FROM Account WHERE  email = '" + email
                        + "' AND pwd_hash ='" + password + "' ;";

        ResultSet rs = st.executeQuery(query);
        System.out.println("HELLO2 ");
        String ema = null;
        String pass = null;

        while (rs.next()) {

            pass = rs.getString("pwd_hash");
            ema = rs.getString("email");

            System.out.println("pass= " + password);
            System.out.println("email = " + ema);

            if (email != null && password != null) {
                answer = true;
                System.out.println("ans1" + answer);
                break;
            }

        }
        System.out.println("ans3" + answer);

        Container datapack = new Container(answer, ClassName.LOGIN_RESPONSE);
        return datapack;
    }

    /**
     * Creates an SQL statement that will get the account information and the groups, that
     * the user is part of , from the database.
     *
     * @param email    String containing the username
     * @param password String containing the password
     * @throws SQLException an exception that provides information on a database
     *                      access error or other errors.
     * @returns Container that contains a boolean true stating that the login was successfull, the account of the user and an ArrayList of groups.
     */
    public Container acceptLogin(String email, String password) throws SQLException {
        Statement st = c.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        System.out.println("db: "+ email + " "+ password);
        String query = "SELECT * from public.account as a   WHERE a.email = '" + email + "' AND a.pwd_hash= '" + password + "'  ";


        ResultSet rs = st.executeQuery(query);

        boolean doesAccountHaveGroups = true;
        String fname = null;
        String lname = null;
        String ema = null;
        // hash = password;
        String salt =null;
        int id = 0;
        String type = null;
        int publicMID = 0;

        ArrayList<PublicMessage> allMessages = new ArrayList<>();
        ArrayList<PublicMessage> lastSeen = new ArrayList<>();


        rs.beforeFirst();

        while (rs.next()) {
            id = rs.getInt("id");
            fname = rs.getString("fname");
            lname = rs.getString("lname");
            ema = rs.getString("email");

            salt = rs.getString("pwd_salt");
            type = rs.getString("type");


        }
        rs.beforeFirst();
        String query2 = "select m.public_message_id from last_seen as m where m.user_id =" + id + "and m.public_message_id is not null";
        rs = st.executeQuery(query2);

        while (rs.next()) {
            publicMID = rs.getInt("public_message_id");


        }
        String query3 = "select * from public.public_messages as s join public.account as pa\n" +
                "on s.sender_id=pa.id";
        rs = st.executeQuery(query3);
        rs.beforeFirst();
        int cid = 0;
        while (rs.next()) {
            cid = rs.getInt("id");
            User us = new User(rs.getString("email"),"a");
            us.setSurname(rs.getString("lname"));
            us.setName(rs.getString("fname"));
            us.setType(rs.getString("type"));
PublicMessage pubm =new PublicMessage(us, rs.getString("message"));
pubm.setTime(rs.getTimestamp("date"));
            allMessages.add(pubm);

        }
        if (cid > publicMID) {
            lastSeen.add(new PublicMessage(null, "PublicMessageTrue"));
        }
        String query4= "select\n" +
                "la.user_id,\n" +
                "la.private_message_id,\n" +
                "p.reciever_id,\n" +
                "p.sender_id,\n" +
                "p.message,\n" +
                "p.date,\n" +
                "a.fname,\n" +
                "a.lname,\n" +
                "a.email\n" +
                "from last_seen as la\n" +
                "join private_messages as p\n" +
                "on p.id = la.private_message_id\n" +
                "join account as a\n" +
                "on (a.id = p.reciever_id or a.id = p.sender_id) and a.id != "+id +
                "where la.user_id = "+id ;
        ArrayList<Object> objs = new ArrayList<>();
        User use = new User(fname,lname,ema,password.getBytes(StandardCharsets.UTF_8),salt,type);
        System.out.println("database: "+ use.getSurname() +" "+ use.getName());
        rs = st.executeQuery(query4);
        rs.beforeFirst();

        while (rs.next()) {
            User u =new User(rs.getString("email"),"a");
            u.setName(rs.getString("fname"));
            u.setSurname(rs.getString("lname"));
            PrivateMessage pm;
            if(rs.getInt("sender_id")==id)
            {
                pm= new PrivateMessage(use,u,rs.getString("message"));
            }else {
                pm= new PrivateMessage(u,use,rs.getString("message"));
            }

            pm.setTime(rs.getTimestamp("date"));
            lastSeen.add(pm);

        }
        String query5="select\n" +
                "m.sender_id,\n" +
                "m.reciever_id,\n" +
                "m.message,\n" +
                "m.date,\n" +
                "a.fname,\n" +
                "a.lname,\n" +
                "a.email\n" +
                "from private_messages as m\n" +
                "join account as a\n" +
                "on (a.id = m.reciever_id or a.id = m.sender_id) and a.id != "+id+"\n" +
                "where m.reciever_id = "+id+" or m.sender_id="+id;
                rs = st.executeQuery(query5);
        rs.beforeFirst();

        while (rs.next()) {
            User u =new User(rs.getString("email"),"a");
            u.setName(rs.getString("fname"));
            u.setSurname(rs.getString("lname"));
            PrivateMessage pm;
            if(rs.getInt("sender_id")==id)
            {
                pm= new PrivateMessage(use,u,rs.getString("message"));
            }else {
                pm= new PrivateMessage(u,use,rs.getString("message"));
            }


            pm.setTime(rs.getTimestamp("date"));
           allMessages.add(pm);

        }
        String query6= "select* from account as a where a.id!="+id;
        rs = st.executeQuery(query6);
        rs.beforeFirst();
        ArrayList<User> users=new ArrayList<>();
        while (rs.next()) {
            User u =new User(rs.getString("email"),"a");
            u.setName(rs.getString("fname"));
            u.setSurname(rs.getString("lname"));
            u.setType(rs.getString("type"));
            u.setSalt("");

        users.add(u);
        }

        objs.add(allMessages);
        objs.add(lastSeen);
        objs.add(use);
        objs.add(users);
        Container dataPack = new Container(objs, ClassName.LOGIN_DATA);
        return dataPack;

    }

    /**
     * Just a parser to change sql Arrays to ArrayList of integers.
     *
     * @param ar String
     * @return ArrayList<Integer>
     */
    public ArrayList<Integer> sqlArrayToArrayListInteger(String ar) {
        ArrayList<Integer> temp = new ArrayList<>();
        String[] ara = ar.split("\\{");
        String part2 = ara[1];
        String[] h = part2.split("}");
        String o = h[0];
        String[] l = o.split(",");

        for (int i = 0; i < l.length; i++) {
            if (l[i].equals("NULL")) {
                l[i] = "0";
            }
            temp.add(Integer.parseInt(l[i]));

        }
        return temp;
    }

    /**
     * Just a parser to change sql Arrays to ArrayList of Strings.
     *
     * @param ar String
     * @return ArrayList<String>
     */
    public static ArrayList<String> sqlArrayToArrayListString(String ar) {
        ArrayList<String> temp = new ArrayList<>();
        System.out.println("This is the array of usernames:" + ar);

        String[] ara = ar.split("\\{");
        String part2 = ara[1];
        String[] h = part2.split("}");
        String o = h[0];
        String[] l = o.split(",");

        for (int i = 0; i < l.length; i++) {
            temp.add(l[i]);

        }
        return temp;
    }
    /**
     * Creates an SQL statement that will search for a group and return whether it exists or not.
     * @param id int ID to search for the group
     * @throws SQLException an exception that provides information on a database
     *                      access error or other errors.
     * @returns boolean stating true if the group exists, and false if otherwise.
     */

}
