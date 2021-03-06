package JDBC;

import JDBC.DBConn.DatabaseConnection;
import com.messcode.transferobjects.*;
import com.messcode.transferobjects.messages.GroupMessages;
import com.messcode.transferobjects.messages.PrivateMessage;
import com.messcode.transferobjects.messages.PublicMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 */
public class ExportData {

    private static final Logger log4j = LogManager.getLogger(ExportData.class);
    private Connection c;
    private DatabaseConnection conn;

    /**
     * Constructor method for Loading stuff from database. Gets the PostgreSQL connection.
     */
    public ExportData() {
        conn = new DatabaseConnection();
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(conn.getConn(), conn.getName(),
                    conn.getPass()); //use your own password here
            System.out.println("hello success");


        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            log4j.error(e.getMessage(), e);
        }
    }

    /**
     * Creates a sql statement that will create a superuser automatically if the database contains no accounts.
     *
     * @throws SQLException an exception that provides information on a database access error or other errors.
     */
     public void checkDatabaseState() throws SQLException {
         System.out.println("IS THIS RUNNING?" +
                 "");
         Statement st = c.createStatement();
         String query = "SELECT * FROM account ";

         ResultSet rs = st.executeQuery(query);
         if(!rs.next())
         { User u = new User("Admin","Admin","admin@gmail.com","admin","superuser");

          query= "INSERT INTO ACCOUNT(id,fname,lname,pwd_hash,pwd_salt,type,email) values(default,'Admin','Admin','"+ Arrays.toString(u.getHashedPassword()) +"','"+u.getSalt()+"','superuser','admin@gmail.com')";
         }
         st.execute(query);


     }

    /**
     * Creates an SQL statement that checks if the username and password are found in
     * the database and whether the password is correct. If the credentials are found and are correct a Container will be created containing the
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
        String answer = null;

        System.out.println("HELLO ");

        String query = "SELECT * FROM account WHERE email = ?";
        PreparedStatement myPreparedStatement = c.prepareStatement(query);
        myPreparedStatement.setString(1, email);

        ResultSet rs = myPreparedStatement.executeQuery();

        String ema = null;
        String salt = null;
        String passw = null;
        byte[] pass = null;

        String hashedPassword = null;
        byte[] finalpa = null;

        while (rs.next()) {
            ema = rs.getString("email");
            salt = rs.getString("pwd_salt");
            hashedPassword = rs.getString("pwd_hash");

            System.out.println("salt = " + salt);
            System.out.println("pwd from db = " + hashedPassword);

            AccountManager am = new AccountManager();
            pass = am.hashPassword(password, salt);
            System.out.println("pwd from client = " + Arrays.toString(pass));

            if (ema != null) {
                if (Arrays.toString(pass).equals(hashedPassword)) {
                    System.out.println("THEY ARE THE SAME OMG");
                    answer = hashedPassword;
                }
                break;
            }
        }
        System.out.println("ans3" + answer);

        return new Container(answer, ClassName.LOGIN_RESPONSE);
    }

    /**
     * Creates an SQL statement that will get the account information,groups and all messages and users that
     * the user is associated with from the database.
     *
     * @param email String containing the email
     * @param password String containing the password
     * @throws SQLException an exception that provides information on a database
     *                      access error or other errors.
     * @returns Container that contains an Arraylist of objects
     */
    public Container acceptLogin(String email, String password) throws SQLException {
        PreparedStatement myPreparedStatement;

        System.out.println("db: " + email + " " + password);

        String query = "SELECT * FROM public.account AS a WHERE a.email = ? AND a.pwd_hash = ?";
        myPreparedStatement = c.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        myPreparedStatement.setString(1, email);
        myPreparedStatement.setString(2, password);
        ResultSet rs = myPreparedStatement.executeQuery();
        ResultSet rs0;

        boolean doesAccountHaveGroups = true;
        String fname = null;
        String lname = null;
        String ema = null;
        String salt = null;
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
        User use = new User(fname, lname, ema, password.getBytes(StandardCharsets.UTF_8), salt, type);   // fix this too
        String query2 = "SELECT m.public_message_id FROM last_seen AS m " +
                "WHERE m.user_id = ? AND m.public_message_id IS NOT NULL";
        myPreparedStatement = c.prepareStatement(query2, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        myPreparedStatement.setInt(1, id);
        rs = myPreparedStatement.executeQuery();

        rs.beforeFirst();
        while (rs.next()) {
            publicMID = rs.getInt("public_message_id");
        }

        String query3 = "SELECT * FROM public.public_messages AS s " +
                "JOIN public.account AS pa ON s.sender_id = pa.id ORDER BY date";
        myPreparedStatement = c.prepareStatement(query3, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        rs = myPreparedStatement.executeQuery();

        int cid = 0;
        rs.beforeFirst();
        while (rs.next()) {
            cid = rs.getInt("id");
            User us = new User(rs.getString("email"), "a");
            us.setSurname(rs.getString("lname"));
            us.setName(rs.getString("fname"));
            us.setType(rs.getString("type"));

            PublicMessage pubm = new PublicMessage(us, rs.getString("message"), rs.getTimestamp("date"));
            allMessages.add(pubm);
        }

        if (cid > publicMID) {
            lastSeen.add(new PublicMessage(null, "PublicMessageTrue"));
        }

        String query4 = "SELECT la.user_id, la.private_message_id, p.receiver_id,p.sender_id, p.message, p.date, a.fname, " +
                "a.lname, a.email FROM last_seen AS la JOIN private_messages as p ON p.id = la.private_message_id " +
                "JOIN account AS a ON (a.id = p.receiver_id OR a.id = p.sender_id) AND a.id != ? WHERE la.user_id = ? " +
                "ORDER BY DATE";
        myPreparedStatement = c.prepareStatement(query4, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        myPreparedStatement.setInt(1, id);
        myPreparedStatement.setInt(2, id);
        rs = myPreparedStatement.executeQuery();


        System.out.println("database: " + use.getSurname() + " " + use.getName());

        rs.beforeFirst();
        while (rs.next()) {
            User u = new User(rs.getString("email"), "a");
            u.setName(rs.getString("fname"));
            u.setSurname(rs.getString("lname"));

            PrivateMessage pm;
            if (rs.getInt("sender_id") == id) {
                pm = new PrivateMessage(use, u, rs.getString("message"), rs.getTimestamp("date"));
            } else {
                pm = new PrivateMessage(u, use, rs.getString("message"), rs.getTimestamp("date"));
            }

            lastSeen.add(pm);
        }
        String query45 = "SELECT distinct  la.user_id, la.group_message_id, p.sender_id, p.message, p.date, a.fname, a.type,\n" +
                "a.lname, a.email,  pps.name,pps.description,pps.leader_id FROM last_seen AS la JOIN group_messages as p ON p.id = la.group_message_id \n" +
                "jOIN account AS a ON (a.id = p.sender_id) \n" +
                "join project_members as gms on gms.account_id = a.id \n" +
                "join projects as pps on pps.id = p.project_id\n" +
                "WHERE la.user_id = ? \n" +
                "ORDER BY DATE";
        myPreparedStatement = c.prepareStatement(query45, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        myPreparedStatement.setInt(1, id);

        rs = myPreparedStatement.executeQuery();
        rs.beforeFirst();
        while (rs.next()) {
            User u = new User(rs.getString("email"), "a");
            u.setName(rs.getString("fname"));
            u.setSurname(rs.getString("lname"));
            u.setType(rs.getString("type"));
            int leaderid= rs.getInt("leader_id");
            User leader= new User("asd----","asd") ;
            String query46 = " select * from  projects as o join account as a on a.id=o.leader_id where o.leader_id=? ";
            myPreparedStatement = c.prepareStatement(query46, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            myPreparedStatement.setInt(1, leaderid);

            rs0 = myPreparedStatement.executeQuery();
            while(rs0.next())
            {
                leader=new User(rs0.getString("fname"),rs0.getString("lname"),rs0.getString("email"),"asda",rs0.getString("type"));
            }
            if(leader.getEmail().equals("asd----"))
            {
                System.out.println("SYSTEM ERROR, IMMIDIATE SHUTDOWN !");
            }

           Group g = new Group(rs.getString("name"),rs.getString("description"),leader);
            GroupMessages pm = new GroupMessages(u, rs.getString("message"),g, rs.getTimestamp("date"));

            lastSeen.add(pm);


        }



        String query5 = "SELECT m.sender_id, m.receiver_id, m.message, m.date, a.fname, a.lname, a.email " +
                "FROM private_messages AS m JOIN account AS a ON (a.id = m.receiver_id OR a.id = m.sender_id) " +
                "AND a.id != ? WHERE m.receiver_id = ? OR m.sender_id = ? ORDER BY DATE";
        myPreparedStatement = c.prepareStatement(query5, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        myPreparedStatement.setInt(1, id);
        myPreparedStatement.setInt(2, id);
        myPreparedStatement.setInt(3, id);
        rs = myPreparedStatement.executeQuery();

        rs.beforeFirst();
        while (rs.next()) {
            User u = new User(rs.getString("email"), "a");
            u.setName(rs.getString("fname"));
            u.setSurname(rs.getString("lname"));
            PrivateMessage pm;
            if (rs.getInt("sender_id") == id) {
                pm = new PrivateMessage(use, u, rs.getString("message"), rs.getTimestamp("date"));
            } else {
                pm = new PrivateMessage(u, use, rs.getString("message"), rs.getTimestamp("date"));
            }

            allMessages.add(pm);
        }

        String query6 = "SELECT * FROM account AS a WHERE a.id != ?";
        myPreparedStatement = c.prepareStatement(query6, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        myPreparedStatement.setInt(1, id);
        rs = myPreparedStatement.executeQuery();

        ArrayList<User> users = new ArrayList<>();
        rs.beforeFirst();
        while (rs.next()) {
            User u = new User(rs.getString("email"), "a");

            u.setName(rs.getString("fname"));
            u.setSurname(rs.getString("lname"));
            u.setType(rs.getString("type"));
            if (rs.getString("pwd_hash").equals("deleted")) {

                u.setSalt(" - deleted");
                System.out.println("this guy should be delted: " + u.getEmail() + " : " + u.getSalt());
            } else {
                u.setSalt("");
            }

            users.add(u);
        }

        ArrayList<Group> groups = updateGroups(use);
        if (groups != null) {
            for (Group group : groups) {
                String query7 = "SELECT g.message, g.date, a.fname, a.lname, a.type, a.email FROM group_messages AS g " +
                        "JOIN account AS a ON a.id = g.sender_id JOIN projects AS p ON p.id = g.project_id " +
                        "WHERE p.name = ? ORDER BY DATE";
                myPreparedStatement = c.prepareStatement(query7, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                myPreparedStatement.setString(1, group.getName());
                rs = myPreparedStatement.executeQuery();

                rs.beforeFirst();
                while (rs.next()) {
                    User member = new User(rs.getString("email"), "a");
                    member.setName(rs.getString("fname"));
                    member.setSurname(rs.getString("lname"));
                    member.setType(rs.getString("type"));
                    GroupMessages g = new GroupMessages(member, rs.getString("message"), group, rs.getTimestamp("date"));
                    allMessages.add(g);
                }
            }
        }
        ArrayList<Object> objs = new ArrayList<>();
        objs.add(allMessages);
        use.setUnreadMessages(lastSeen);
        use.setSalt(" - online");
        objs.add(use);
        objs.add(users);
        objs.add(groups);

        return new Container(objs, ClassName.LOGIN_DATA);
    }


    /**
     * Creates a sql statement that will update the groups and return them to the user they are associated with.
     *
     * @param current User object
     * @return ArrayList<Group> Object containing the groups that the user is associated with
     * @throws SQLException an exception that provides information on a database access error or other errors.
     */
    public ArrayList<Group> updateGroups(User current) throws SQLException {
        ArrayList<Group> groups = new ArrayList<Group>();
        PreparedStatement myPreparedStatement;
        PreparedStatement myPreparedStatement1;
        PreparedStatement myPreparedStatement2;
        ResultSet rs0 = null;
        ResultSet rs;

        if (current.isEmployee() || current.isProjectLeader()) {
            String query0 = "SELECT p.name FROM projects AS p JOIN project_members as pm ON pm.project_id = p.id " +
                    "JOIN account AS a ON a.id = pm.account_id WHERE a.email = ?";
            myPreparedStatement = c.prepareStatement(query0, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            myPreparedStatement.setString(1, current.getEmail());
            rs0 = myPreparedStatement.executeQuery();
            if (!rs0.next()) {
                return null;
            }
            //rs0.beforeFirst();

        }

        do {
            if (!(rs0 == null)) {
                String query = "SELECT p.name, p.description,p.leader_id, a.fname, a.lname, a.email, a.type FROM projects AS p " +
                        " JOIN account AS a ON a.id = p.leader_id WHERE p.name = ?";
                myPreparedStatement1 = c.prepareStatement(query);
                myPreparedStatement1.setString(1, rs0.getString("name"));
            } else {
                String query = "SELECT p.name, p.description, p.leader_id, a.fname, a.lname, a.email, a.type FROM projects AS p " +
                        "LEFT JOIN account AS a ON a.id = p.leader_id";
                myPreparedStatement1 = c.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            }
            rs = myPreparedStatement1.executeQuery();


            while (rs.next()) {
                Group g;
                if (rs.getObject("leader_id") != null) {
                    User lead = new User(rs.getString("email"), rs.getString("fname") + rs.getString("lname"));
                    lead.setName(rs.getString("fname"));
                    lead.setSurname(rs.getString("lname"));
                    lead.setType(rs.getString("type"));
                    g = new Group(rs.getString("name"), rs.getString("description"), lead);
                } else {

                    g = new Group(rs.getString("name"), rs.getString("description"), null);

                }

                String query2 = "SELECT a.fname, a.lname, a.email, a.type FROM project_members AS pm " +
                        "JOIN account AS a ON a.id = pm.account_id JOIN projects AS p ON p.id = pm.project_id " +
                        "WHERE p.name = ?";
                myPreparedStatement2 = c.prepareStatement(query2);
                myPreparedStatement2.setString(1, rs.getString("name"));
                ResultSet rs2 = myPreparedStatement2.executeQuery();

                while (rs2.next()) {
                    User u = new User(rs2.getString("email"), rs2.getString("fname") + rs2.getString("lname"));
                    u.setName(rs2.getString("fname"));
                    u.setSurname(rs2.getString("lname"));
                    u.setType(rs2.getString("type"));
                    g.addMember(u);
                }

                groups.add(g);
            }
        } while (!(rs0 == null) && rs0.next());

        return groups;
    }

    /**
     * Creates a sql statement that will get all the groups associated with the group given as argument
     *
     * @param g Group object
     * @return ArrayList<PublicMessage> array list containing all the group messages for the given group
     * @throws SQLException an exception that provides information on a database access error or other errors.
     */
    public ArrayList<PublicMessage> getGroupMessages(Group g) throws SQLException {
        PreparedStatement myPreparedStatement;
        ResultSet rs = null;


        ArrayList<PublicMessage> groupMessages = new ArrayList<>();

        String query7 = "SELECT g.message, g.date, a.fname, a.lname, a.type, a.email FROM group_messages AS g " +
                "JOIN account AS a ON a.id = g.sender_id  JOIN projects AS p ON p.id = g.project_id " +
                "WHERE p.name = ? ORDER BY DATE";
        myPreparedStatement = c.prepareStatement(query7, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        myPreparedStatement.setString(1, g.getName());
        rs = myPreparedStatement.executeQuery();

        rs.beforeFirst();
        while (rs.next()) {
            User member = new User(rs.getString("email"), "a");
            member.setName(rs.getString("fname"));
            member.setSurname(rs.getString("lname"));
            member.setType(rs.getString("type"));
            GroupMessages gg = new GroupMessages(member, rs.getString("message"), g, rs.getTimestamp("date"));
            groupMessages.add(gg);
        }

        return groupMessages;
    }
}
