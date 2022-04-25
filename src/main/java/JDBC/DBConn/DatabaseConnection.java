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
    private final String pass = "ih de TRfvj85Jl 55 igh 94";

    /**
     * @return
     */
    public String getConn() {
        return conn;
    }

    /**
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * @return
     */
    public String getPass() {
        return pass;
    }

    /**
     *
     */
    public DatabaseConnection() {
    }
}
