package JDBC;

import JDBC.DBConn.DatabaseConnection;
import com.messcode.transferobjects.Group;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    
   
   }
   

