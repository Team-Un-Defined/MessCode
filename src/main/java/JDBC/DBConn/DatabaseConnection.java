/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JDBC.DBConn;

/**
 * @author Nao
 */
public class DatabaseConnection {
    private final String conn = "jdbc:postgresql://localhost:5432/MessCode";
    private final String name = "postgres";
    private final String pass = "chickenattack777";

    /**
     * Get method
     *
     * @return String object
     */
    public String getConn() {

        return conn;
    }

    /**
     * Get method
     *
     * @return String object
     */
    public String getName() {
        return name;
    }

    /**
     *  Get method
     *
     * @return String object
     */
    public String getPass() {
        return pass;
    }

    /**
     * Empty  constructor
     */
    public DatabaseConnection() {
    }
}
