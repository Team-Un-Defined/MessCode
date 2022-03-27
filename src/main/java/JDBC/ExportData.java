package JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
          .getConnection("jdbc:postgresql://localhost:5432/apple", "postgres",
              "almafast325");

    }
    catch (SQLException | ClassNotFoundException e)
    {
      e.printStackTrace();
    }
  }

}