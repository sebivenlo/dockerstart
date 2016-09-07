package nl.fontys;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "helloWorld", eager = true)
@SessionScoped
public class HelloWorld implements Serializable {
    private static final String DB_HOST = "app-db_1";
    private static final int DB_PORT = 5432;
    private static final String DB_NAME = "docker";
    private static final String DB_USER = "docker";
    private static final String DB_PASS = "docker";
    
    private static final long serialVersionUID = 1L;

    public List<String> getMessages() {
        //return Arrays.asList("Hello", "World");
        ResultSet rs;
        PreparedStatement ps;
        Connection con = getConnection();
        String stmt = "SELECT * FROM awesomeTable";
        List<String> records = new ArrayList<>();
        try {
            ps = con.prepareStatement(stmt);
            ps.execute();
            rs = ps.getResultSet();

            while (rs.next()) {
                records.add(rs.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }

    public Connection getConnection() {
        Connection con = null;
        String url = "jdbc:postgresql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME;
        
        try {
            System.out.println("Trying to connect to database: " + url);
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(url, DB_USER, DB_PASS);
            System.out.println("Connection completed.");
        } catch (SQLException ex) {
            System.out.println("Connection failed.");
            System.out.println(ex.getMessage());
        } catch (ClassNotFoundException cnf){
            System.out.println("Failed to load database library.");
        }
        return con;
    }
}
