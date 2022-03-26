package JDBC;

import com.messcode.transferobjects.ClassName;
import com.messcode.transferobjects.Container;

import java.sql.*;
import java.util.ArrayList;

public class ExportData
{
  private Character character;
  private Connection c;


  public ExportData()
  {
    /**
     * Constructor method for LoadCharacter. Gets the PostgreSQL connection.
     */
    try
    {
      Class.forName("org.postgresql.Driver");
      c = DriverManager
          .getConnection("jdbc:postgresql://localhost:5432/MessCode", "postgres",
              "almafast325"); //use your own password here

    }
    catch (SQLException | ClassNotFoundException e)
    {
      e.printStackTrace();
    }
  }

  /**
   * Creates an SQL statement, if the  email is found in the database
   * the method will return a boolean false. If they are not in the database the
   * credentials are unique and the method will return a boolean true
   *
   * @param email    String containing the email
   * @return boolean true if the account is unique and false if it is not
   * @throws SQLException an exception that provides information on a database
   *                      access error or other errors.
   */
  public boolean checkAccountUniqueness(String username, String email)
      throws SQLException
  {
    boolean unique = false;
    Statement st = c.createStatement();
    String query =
        "SELECT * FROM Accounts WHERE email ='" + email + "' ;";

    ResultSet rs = st.executeQuery(query);

    String ema = null;
    while (rs.next())
    {

      ema = rs.getString("email");

      System.out.println("email = " + ema);
      if ( ema != null)
      {
        System.out.println(unique);
        break;
      }
      System.out.println(unique);
    }
    if ( ema == null)
    {
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
   * @param email String containing the username
   * @param password String containing the password
   * @return a container that has the answer(boolean) from the database
   * @throws SQLException An exception that provides information on a database
   *                      access error or other errors.
   */
  public Container checkLogin(String email, String password)
      throws SQLException

  {
    boolean answer = false;

    Statement st = c.createStatement();
    String query =
        "SELECT * FROM Account WHERE  email = '" + email
            + "' AND password ='" + password + "' ;";

    ResultSet rs = st.executeQuery(query);

    String ema = null;
    String pass = null;

    while (rs.next())
    {

      pass = rs.getString("password");
      ema = rs.getString("email");

      System.out.println("pass= " + password);
      System.out.println("email = " + ema);

      if (email != null && password != null)
      {
        answer = true;
        System.out.println("ans1" + answer);
        break;
      }

    }
    System.out.println("ans3" + answer);
    ArrayList<Object> obj = new ArrayList<>();
    obj.add(answer);
    Container datapack = new Container(obj, ClassName.LOGIN_RESPONSE);
    return datapack;
  }

  /**
   * Creates an SQL statement that will get the account information and the groups, that
   * the user is part of , from the database.
   * @param email String containing the username
   * @param password String containing the password
   * @throws SQLException an exception that provides information on a database
   *                      access error or other errors.
   * @returns Container that contains a boolean true stating that the login was successfull, the account of the user and an ArrayList of groups.
   */
  public Container acceptLogin(String email, String password)
      throws SQLException
  {
    Statement st = c.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
    String query ="SELECT a.fname,a.lname, a.lname, a.email a.type  from public.account   WHERE public.username = '"+email +"' AND public.password= '"+password +"' AND  ";

    ResultSet rs = st.executeQuery(query);

    boolean doesAccountHaveGroups=true;
    String userame = null;
    String ema = null;
    String pass = null;



    if(!rs.next())
    {
      doesAccountHaveGroups = false;
      query =
          "SELECT * FROM \"Users\".\"Users\" WHERE  username  = '" + email + "' AND password ='" + password + "' ;";
      rs = st.executeQuery(query);



    }
    rs.beforeFirst();


    //ArrayList<> groupList = new ArrayList<>();

    ArrayList<String> plys = new ArrayList<>();
    ArrayList<Integer> charIDs = new ArrayList<>();

    while (rs.next())
    {
      userame = rs.getString("username");
      pass = rs.getString("password");
      ema = rs.getString("email");

      if(doesAccountHaveGroups==false) {
        break;}





      String k = rs.getString("usernamePlayers");
      String charid = rs.getString("characterIDs");
      // ng = null;
      //ng  = new Group(rs.getString("name"), rs.getInt("id"));
      //ng.addDM(new DM(rs.getString("usernameDM")));
      if(k!=null)
      {
        plys = sqlArrayToArrayListString(k);

        charIDs = sqlArrayToArrayListInteger(charid);

        for (int i = 0; i < plys.size(); i++)
        {
          if(plys.get(i)!=null)
          {
        //    Player a = new Player(plys.get(i));

            if(charIDs.get(i)!=0)
            { //a.addCharacterID(charIDs.get(i));
              //System.out.println("char id for "+ a.getName() + " is: " +charIDs.get(i));} else {a.addCharacterID(null);
              }
            //ng.addPlayer(a);
          }

        }
      }

      //System.out.println("group : "+ng.toString());
      //groupList.add(ng);


    }


    ArrayList<Object> objs = new ArrayList<>();
    boolean b = true; objs.add(b);
    //Account acc = new Account(userame, password, ema);
    //objs.add(acc);

    if(doesAccountHaveGroups)
    {
      //objs.add(groupList);

    }



    Container dataPack = new Container(objs, ClassName.LOGIN_RESPONSE);
    return dataPack;

  }

  /**
   * Just a parser to change sql Arrays to ArrayList of integers.
   * @param ar String
   * @return ArrayList<Integer>
   */
  public ArrayList<Integer> sqlArrayToArrayListInteger(String ar)
  {
    ArrayList<Integer> temp = new ArrayList<>();
    String[] ara = ar.split("\\{");
    String part2 = ara[1];
    String[] h = part2.split("}");
    String o = h[0];
    String[] l = o.split(",");

    for (int i = 0; i < l.length; i++)
    { if(l[i].equals("NULL")) {l[i]="0";}
      temp.add(Integer.parseInt(l[i]));

    }
    return temp;
  }
  /**
   * Just a parser to change sql Arrays to ArrayList of Strings.
   * @param ar String
   * @return ArrayList<String>
   */
  public static ArrayList<String> sqlArrayToArrayListString(String ar)
  {
    ArrayList<String> temp = new ArrayList<>();
    System.out.println("This is the array of usernames:"+ar);

    String[] ara = ar.split("\\{");
    String part2 = ara[1];
    String[] h = part2.split("}");
    String o = h[0];
    String[] l = o.split(",");

    for (int i = 0; i < l.length; i++)
    {
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
