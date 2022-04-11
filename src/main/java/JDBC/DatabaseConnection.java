/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JDBC;

/**
 *
 * @author Nao
 */
public class DatabaseConnection {
private final String conn = "jdbc:postgresql://localhost:5432/MessCode";
private final String name =  "postgres";
private final String pass =  "chickenattack777";

    public String getConn() {
        return conn;
    }

    public String getName() {
        return name;
    }

    public String getPass() {
        return pass;
    }

    public DatabaseConnection() {
    }


}
