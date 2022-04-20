package JDBC;

import JDBC.DBConn.DatabaseConnection;
import com.messcode.transferobjects.Group;
import com.messcode.transferobjects.AccountManager;
import com.messcode.transferobjects.Group;
import com.messcode.transferobjects.User;
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





    public void saveMessage(PublicMessage pm)
            throws SQLException {
        boolean done = false;

        Statement st = c.createStatement();
        if (pm instanceof PrivateMessage) {
            int rid=0;
            int sid=0;
            PrivateMessage pm1= (PrivateMessage) pm;
            String query1 =
                    "select * from account where email='" + pm1.getSender().getEmail() + "' or email='" + pm1.getReceiver().getEmail() + "'";

            ResultSet rs = st.executeQuery(query1);
            while (rs.next()) {
              if(rs.getString("email").equals(pm.getSender().getEmail())){
                  sid=rs.getInt("id");
              }else {
                  rid= rs.getInt("id");
              }

            }

            String query =     "INSERT INTO private_messages VALUES( default," +sid + ","
                    +rid+ ",'"+pm1.getMsg()+"', '" + pm.getTime() + "') ";
            st.executeUpdate(query);


            }
              else if(pm instanceof GroupMessages)
        {       GroupMessages pm1= (GroupMessages) pm;
            int gid=0;
            int grid=0;
            String query0 =
                    "select * from account where email='" + pm1.getSender().getEmail() + "'";
            ResultSet rs = st.executeQuery(query0);
            while (rs.next()) {

                gid= rs.getInt("id");



            }

            int lid=0;

            String query1 =
                    "select * from projects where name='" + pm1.getGroup().getName() +  "'";

            rs = st.executeQuery(query1);
            while (rs.next()) {

                   grid= rs.getInt("id");
                   lid=rs.getInt("leader_id");


            }

            String query =     "INSERT INTO group_messages VALUES( default," +grid + ","
                    +gid+ ",'"+pm1.getMsg()+"', '" + pm.getTime() + "') ";
            st.executeUpdate(query);
        }
              else {

            int grid=0;
            String query0 =
                    "select * from account where email='" + pm.getSender().getEmail() + "'";
            ResultSet rs = st.executeQuery(query0);
            while (rs.next()) {

                grid= rs.getInt("id");



            }
            String query =     "INSERT INTO public_messages VALUES( default," +grid +  ",'"+pm.getMsg()+"', '" + pm.getTime() + "') ";
            st.executeUpdate(query);


        }


    }

   public void createGroup(Group g){
    int userId=0;

       String query1 =
        "select \n" +
        "a.id,\n" +
        "a.type\n" +
        "from account as a where a.email='" + g.getLeader().getEmail() + "'";



     Statement st;
        try {
            st = c.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = st.executeQuery(query1);
            rs.beforeFirst();
            rs.next();
            userId = rs.getInt("id");

            System.out.println("WTFFFFFFFFFFF"+ userId);

            if(rs.getString("type").equals("emplyee")){
            String query ="update public.account\n" +
            "set type = 'project_leader'\n" +
            "where id ="+userId ;

            st.executeQuery(query);
            }

            String query2 =
        "insert into public.projects (id,leader_id,name,description)\n" +
        "values (default,"+userId+",'"+g.getName()+"','"+g.getDescription() + "') ON CONFLICT(name) DO UPDATE\n" +
        "set description = '"+g.getDescription() +"',leader_id = "+userId +" returning id";

            rs = st.executeQuery(query2);
            rs.beforeFirst();
            rs.next();
            int projectId = rs.getInt("id");

            String query3 =
        "insert into public.project_members (id,project_id,account_id)\n" +
        "values (default,"+projectId+ ","+userId+")  returning id";


         st.executeQuery(query3);


        } catch (SQLException ex) {
            Logger.getLogger(ImportData.class.getName()).log(Level.SEVERE, null, ex);
        }


   }

   public void addGroupMembers(Group g) throws SQLException{
       Statement st = c.createStatement();
     ResultSet rs;
     for(int i=0;i<g.getMembers().size();i++){
     String query =
        "select\n" +
        "a.id,\n" +
        "p.id as project\n" +
        "from account as a\n" +
        "left join projects as p\n" +
        "on p.name = '"+g.getName() +"'\n" +
        "where a.email = '"+g.getMembers().get(i).getEmail() +"'";
     rs = st.executeQuery(query);
     rs.beforeFirst();
     while(rs.next()){

                String query2 =
                "insert into public.project_members (id,project_id,account_id)\n" +
                "values (default,"+rs.getInt("project_id") +","+rs.getInt("account_id") +") on conflict do nothing";
        }
        }
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
