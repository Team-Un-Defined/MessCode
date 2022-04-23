package JDBC;

import JDBC.DBConn.DatabaseConnection;
import com.messcode.transferobjects.*;
import com.messcode.transferobjects.Group;
import com.messcode.transferobjects.messages.GroupMessages;
import com.messcode.transferobjects.messages.PrivateMessage;
import com.messcode.transferobjects.messages.PublicMessage;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
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
     * @param  us User object containing all the account data.
     * @throws SQLException an exception that provides information on a database
     *                      access error or other errors.
     */
    public Container createAccount(User us) throws SQLException {
        boolean done = true;

        String query = "INSERT INTO account VALUES(default, ?, ?, ?, ?, ?, ?) ON CONFLICT(email) DO NOTHING RETURNING id";
        PreparedStatement myPreparedStatement = c.prepareStatement(query);
        myPreparedStatement.setString(1, us.getName());
        myPreparedStatement.setString(2, us.getSurname());
        myPreparedStatement.setString(3, Arrays.toString(us.getHashedPassword()));
        myPreparedStatement.setString(4, us.getSalt());
        myPreparedStatement.setString(5, us.getType());
        myPreparedStatement.setString(6, us.getEmail());
        ResultSet rs = myPreparedStatement.executeQuery();

        if(!rs.next()) {
            done = false;
        }

        return new Container(done,ClassName.CREATE_ACCOUNT);
    }

    public void saveMessage(PublicMessage pm) throws SQLException {
        boolean done = false;

        PreparedStatement myPreparedStatement;
        ResultSet rs;
        if (pm instanceof PrivateMessage) {
            int rid = 0;
            int sid = 0;
            PrivateMessage pm1 = (PrivateMessage) pm;

            String query1 = "SELECT * FROM account WHERE email = ? OR email = ?";
            myPreparedStatement = c.prepareStatement(query1);
            myPreparedStatement.setString(1, pm1.getSender().getEmail());
            myPreparedStatement.setString(2, pm1.getReceiver().getEmail());
            rs = myPreparedStatement.executeQuery();

            while (rs.next()) {
                if (rs.getString("email").equals(pm.getSender().getEmail())){
                    sid = rs.getInt("id");
                } else {
                    rid = rs.getInt("id");
                }
            }

            String query = "INSERT INTO private_messages VALUES(default, ?, ?, ?, ?)";
            myPreparedStatement = c.prepareStatement(query);
            myPreparedStatement.setInt(1, sid);
            myPreparedStatement.setInt(2, rid);
            myPreparedStatement.setString(3, pm1.getMsg());
            myPreparedStatement.setTimestamp(4, pm.getTime());
            myPreparedStatement.executeUpdate();

        } else if (pm instanceof GroupMessages) {
            GroupMessages pm1 = (GroupMessages) pm;
            int gid = 0;
            int grid = 0;

            String query0 = "SELECT * FROM account WHERE email = ?";
            myPreparedStatement = c.prepareStatement(query0);
            myPreparedStatement.setString(1, pm1.getSender().getEmail());
            rs = myPreparedStatement.executeQuery();

            while (rs.next()) {
                gid = rs.getInt("id");
            }

            int lid = 0;

            String query1 = "SELECT * FROM projects WHERE name = ?";
            myPreparedStatement = c.prepareStatement(query1);
            myPreparedStatement.setString(1, pm1.getGroup().getName());
            rs = myPreparedStatement.executeQuery();

            while (rs.next()) {
                   grid = rs.getInt("id");
                   lid = rs.getInt("leader_id");
            }

            String query = "INSERT INTO group_messages VALUES (default, ?, ?, ?, ?)";
            myPreparedStatement = c.prepareStatement(query);
            myPreparedStatement.setInt(1, grid);
            myPreparedStatement.setInt(2, gid);
            myPreparedStatement.setString(3, pm1.getMsg());
            myPreparedStatement.setTimestamp(4, pm.getTime());
            myPreparedStatement.executeUpdate();
        } else {
            int grid = 0;

            String query0 = "SELECT * FROM account WHERE email = ?";
            myPreparedStatement = c.prepareStatement(query0);
            myPreparedStatement.setString(1, pm.getSender().getEmail());
            rs = myPreparedStatement.executeQuery();

            while (rs.next()) {
                grid= rs.getInt("id");
            }

            String query = "INSERT INTO public_messages VALUES (default, ?, ?, ?)";
            myPreparedStatement = c.prepareStatement(query);
            myPreparedStatement.setInt(1, grid);
            myPreparedStatement.setString(2, pm.getMsg());
            myPreparedStatement.setTimestamp(3, pm.getTime());
            myPreparedStatement.executeUpdate();
        }
    }

   public void createGroup(Group g) {
       PreparedStatement myPreparedStatement;
       ResultSet rs;

       try {
           int userId=0;

           String query1 = "SELECT a.id, a.type FROM account AS a WHERE a.email = ?";
           myPreparedStatement = c.prepareStatement(query1, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
           myPreparedStatement.setString(1, g.getLeader().getEmail());
           rs = myPreparedStatement.executeQuery();

           rs.beforeFirst();

           rs.next();
           userId = rs.getInt("id");
           System.out.println("WTFFFFFFFFFFF"+ userId);

           if (rs.getString("type").equals("employee")) {
               String query = "UPDATE public.account SET type = 'project_leader' WHERE id = ?";
               myPreparedStatement = c.prepareStatement(query);
               myPreparedStatement.setInt(1, userId);
               myPreparedStatement.executeUpdate();
           }

           String query2 = "INSERT INTO public.projects (id, leader_id, name, description) VALUES (default, ?, ?, ?) " +
                   "ON CONFLICT (name) DO UPDATE SET description = ?, leader_id = ? returning id";
           myPreparedStatement = c.prepareStatement(query2);
           myPreparedStatement.setInt(1, userId);
           myPreparedStatement.setString(2, g.getName());
           myPreparedStatement.setString(3, g.getDescription());
           myPreparedStatement.setString(4, g.getDescription());
           myPreparedStatement.setInt(5, userId);
           rs = myPreparedStatement.executeQuery();

           rs.next();
           int projectId = rs.getInt("id");

           String query3 = "INSERT INTO public.project_members (id, project_id, account_id) VALUES (default, ?, ?) returning id";
           myPreparedStatement = c.prepareStatement(query3);
           myPreparedStatement.setInt(1, projectId);
           myPreparedStatement.setInt(2, userId);
           myPreparedStatement.executeQuery();
       } catch (SQLException ex) {
           Logger.getLogger(ImportData.class.getName()).log(Level.SEVERE, null, ex);
       }
   }

   public void addGroupMembers(Group g) throws SQLException{
        PreparedStatement myPreparedStatement;
        ResultSet rs;

        for (int i = 0; i < g.getMembers().size(); i++) {
            String query = "SELECT a.id, p.id AS project FROM account AS a LEFT JOIN projects AS p ON p.name = ? " +
                    "WHERE a.email = ?";
            myPreparedStatement = c.prepareStatement(query);
            myPreparedStatement.setString(1, g.getName());
            myPreparedStatement.setString(2, g.getMembers().get(i).getEmail());
            rs = myPreparedStatement.executeQuery();

            rs.beforeFirst();
            while (rs.next()) {

                String query2 = "INSERT INTO public.projects_members (id, project_id, account_id) " +
                        "VALUES (default, ?, ?) ON CONFLICT DO NOTHING";
                myPreparedStatement = c.prepareStatement(query);
                myPreparedStatement.setInt(1, rs.getInt("project_id"));
                myPreparedStatement.setInt(2, rs.getInt("account_id"));
                myPreparedStatement.executeUpdate();
            }
        }
   }

   public void saveDataOnExit(User us) throws SQLException {
        PreparedStatement myPreparedStatement;
        ResultSet rs;
        int userid = 0;

       String query = "SELECT * FROM account WHERE email = ?";
       myPreparedStatement = c.prepareStatement(query);
       myPreparedStatement.setString(1, us.getEmail());
       rs = myPreparedStatement.executeQuery();

       while (rs.next()) {
            userid = rs.getInt("id");
       }

       for(int i = 0; i < us.getUnreadMessages().size(); i++) {
           if(us.getUnreadMessages().get(i) instanceof PrivateMessage) {
               int pmid = 0;
               int rid = 0;
               int lid = 0;

               String query1 = "SELECT p.receiver_id, p.sender_id, p.date, p.id FROM private_messages AS p " +
                       "JOIN account AS a ON a.id = p.sender_id OR a.id = p.receiver_id " +
                       "WHERE ((p.receiver_id = ? AND a.email = ?) OR (p.sender_id = ? AND a.email = ?)) AND date = ?";
               myPreparedStatement = c.prepareStatement(query1);
               myPreparedStatement.setInt(1, userid);
               myPreparedStatement.setString(2, ((PrivateMessage)(us.getUnreadMessages().get(i))).getReceiver().getEmail());
               myPreparedStatement.setInt(3, userid);
               myPreparedStatement.setString(4, ((PrivateMessage)(us.getUnreadMessages().get(i))).getReceiver().getEmail());
               myPreparedStatement.setString(5, us.getUnreadMessages().get(1).getTime().toString());
               rs = myPreparedStatement.executeQuery();

               while (rs.next()) {
                   pmid = rs.getInt("id");
                   rid = rs.getInt("receiver_id");

                   if (rid == userid) {
                       rid = rs.getInt("sender_id");
                   }
               }

               String query2 = "SELECT ls.private_message_id, pm.sender_id, pm.receiver_id, ls.id FROM last_seen AS ls " +
                       "JOIN private_messages AS pm ON pm.id = ls.private_message_id " +
                       "WHERE ls.user_id = ? AND (pm.sender_id = ? OR pm.receiver_id = ?)";
               myPreparedStatement = c.prepareStatement(query2);
               myPreparedStatement.setInt(1, userid);
               myPreparedStatement.setInt(2, rid);
               myPreparedStatement.setInt(3, rid);
               rs = myPreparedStatement.executeQuery();

               while (rs.next()) {
                   lid= rs.getInt("id");
               }

               String query3 = "UPDATE public.last_seen SET private_message_id = ? WHERE id = ?";
               myPreparedStatement = c.prepareStatement(query3);
               myPreparedStatement.setInt(1, pmid);
               myPreparedStatement.setInt(2, lid);
               myPreparedStatement.executeUpdate();

               System.out.println("EXECUTED QUERY");

           } else if (us.getUnreadMessages().get(i) instanceof GroupMessages) {
               int gmid = 0;
               int lid = 0;

               String query1 = "SELECT gm.id AS gmid, gm.project_id, gm.sender_id, gm.message, gm, date, p.id, p.leader_id, p.name, p.description " +
                       "FROM group_messages gm JOIN projects AS p ON p.id = gm.project_id WHERE p.name = ? AND date = ?";
               myPreparedStatement = c.prepareStatement(query1);
               myPreparedStatement.setString(1, ((GroupMessages) us.getUnreadMessages().get(i)).getGroup().getName());
               myPreparedStatement.setTimestamp(2, us.getUnreadMessages().get(i).getTime());
               rs = myPreparedStatement.executeQuery();

               while (rs.next()) {
                   gmid = rs.getInt("gmid");
               }

               String query2 = "SELECT ls.group_message_id, gm.sender_id, ls.id FROM last_seen AS ls " +
                       "JOIN group_messages AS gm ON gm.id = ls.group_message_id WHERE ls.user_id = ?";
               myPreparedStatement = c.prepareStatement(query2);
               myPreparedStatement.setInt(1, userid);
               rs = myPreparedStatement.executeQuery();

               while (rs.next()) {
                   lid = rs.getInt("id");
               }

               String query3 = "UPDATE public.last_seen SET group_nessage_id = ? WHERE id = ?";
               myPreparedStatement = c.prepareStatement(query3);
               myPreparedStatement.setInt(1, gmid);
               myPreparedStatement.setInt(2, lid);
               myPreparedStatement.executeUpdate();

               System.out.println("EXECUTED QUERY");
           }  else {
               int pmid = 0;
               int lid = 0;

               String query1 = "SELECT pm.id = pmid, pm.sender_id, pm.nessage, pm.date FROM public_nessages pm " +
                       "WHERE date = ?";
               myPreparedStatement = c.prepareStatement(query1);
               myPreparedStatement.setTimestamp(1, us.getUnreadMessages().get(i).getTime());
               myPreparedStatement.executeQuery();

               while (rs.next()) {
                   pmid = rs.getInt("gmid");
               }

               String query2 = "SELECT ls.public_message_id, gm.sender_id, ls.id FROM last_seen AS ls " +
                       "JOIN public_messages AS gm ON gm.id = ls.public_message_id WHERE ls.user_id = ?";
               myPreparedStatement = c.prepareStatement(query2);
               myPreparedStatement.setInt(1, userid);
               rs = myPreparedStatement.executeQuery();

               while (rs.next()) {
                   lid = rs.getInt("id");
               }

               String query3 = "UPDATE public.last_seen SET public_message_id = ? WHERE id = ?";
               myPreparedStatement = c.prepareStatement(query3);
               myPreparedStatement.setInt(1, pmid);
               myPreparedStatement.setInt(2, lid);
               myPreparedStatement.executeUpdate();

               System.out.println("EXECUTED QUERY");
           }
       }
   }

   public boolean test(String password, String email) throws SQLException {
       boolean unique = false;

       String query = "SELECT * FROM account WHERE email = ?";
       PreparedStatement myPreparedStatement = c.prepareStatement(query);
       myPreparedStatement.setString(1, email);
       ResultSet rs = myPreparedStatement.executeQuery();

        String ema = null;
        String salt = null;
        String hashedPassword = null;
        byte[] pass = null;

        if (rs.next()) {
            hashedPassword = rs.getString("pwd_hash");
            ema = rs.getString("email");
            salt = rs.getString("pwd_salt");

            System.out.println("salt = " + salt);
            System.out.println("password =  " +hashedPassword);
            AccountManager am = new AccountManager();
            pass = am.hashPassword(password, salt);
            System.out.println("cli:"+ Arrays.toString(pass));
        }

       String query2 = "UPDATE public.account SET pwd_hash = ? WHERE email = ?";
       myPreparedStatement = c.prepareStatement(query2);
       myPreparedStatement.setString(1, Arrays.toString(pass));
       myPreparedStatement.setString(2, email);
       myPreparedStatement.executeUpdate();

       System.out.println("EXECUTED QUERY");

       return unique;
   }

   public boolean changePassword(User us) throws SQLException {
       PreparedStatement myPreparedStatement;
       ResultSet rs;

       String query0 = "SELECT * FROM account WHERE email = ?";
       myPreparedStatement = c.prepareStatement(query0);
       myPreparedStatement.setString(1, us.getEmail());
       rs = myPreparedStatement.executeQuery();

       String salt="";
       String pwd="";

       while (rs.next()) {
           salt = rs.getString("pwd_salt");
           pwd = rs.getString("pwd_hash");
       }

       AccountManager m = new AccountManager();
       byte[] current = m.hashPassword(us.getStrPassword(),salt);

       if (!pwd.equals(Arrays.toString(current))) {
           System.out.println("PASSWORDS DIDNT MATCH");
           return false;
       }

       String query1 = "UPDATE account SET pwd_salt = ? WHERE pwd_hash = ?";
       myPreparedStatement = c.prepareStatement(query1);
       myPreparedStatement.setString(1, us.getSalt());
       myPreparedStatement.setString(2, Arrays.toString(current));
       myPreparedStatement.executeUpdate();

       String query = "UPDATE account SET pwd_hash = ? WHERE pwd_hash = ?";
       myPreparedStatement = c.prepareStatement(query);
       myPreparedStatement.setString(1, Arrays.toString(us.getHashedPassword()));
       myPreparedStatement.setString(2, Arrays.toString(current));
       myPreparedStatement.executeUpdate();

        System.out.println("PASSWORDS MATCHED");
        return true;
    }

    public boolean deleteUser(User u) throws SQLException {

        Statement st = c.createStatement();

        String query0=    "UPDATE account set  pwd_hash = 'deleted' where email='"+u.getEmail()+"' ;";
        st.executeUpdate(query0);

        String query1= "select * from account where email='"+u.getEmail()+"' ;";

        ResultSet rs =    st.executeQuery(query1);
        while(rs.next())
        {
            if(rs.getString("pwd_hash").equals("deleted"))
            {
                return true;
            }
        }
return false;
    }
}
