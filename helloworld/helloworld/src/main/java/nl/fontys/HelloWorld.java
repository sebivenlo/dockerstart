package nl.fontys;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.sql.DataSource;

@ManagedBean( name = "helloWorld", eager = true )
@SessionScoped
public class HelloWorld implements Serializable {

    private static final String DB_HOST = "app-db";
    private static final int DB_PORT = 5433;
    private static final String DB_NAME = "docker";
    private static final String DB_USER = "docker";
    private static final String DB_PASS = "docker";

    private static final long serialVersionUID = 1L;
    @Resource( lookup = "java:jboss/jdbc/dockerds" )
    DataSource ds;

    public List<String> getMessages() {
        //return Arrays.asList("Hello", "World");
        ResultSet rs;

        String query = "SELECT * FROM awesomeTable";
        List<String> records = new ArrayList<>();
        records.add( "Hi there too" );
        records.add( ds.toString() );
        Connection con = null;
        try {
            con = ds.getConnection();
            records.add( "con=" + con.toString() );
        } catch ( SQLException ex ) {
            Logger.getLogger( HelloWorld.class.getName() ).log( Level.SEVERE, null, ex );
            
            throw new RuntimeException( ex );
        }
        try (
                PreparedStatement ps = con.prepareStatement( query ); ) {
            ps.execute();
            rs = ps.getResultSet();

            while ( rs.next() ) {
                records.add( rs.getString( 2 ) );
            }
            records.add( "done" );
        } catch ( SQLException e ) {

            throw new RuntimeException(e);
        }
        return records;
    }

    public Connection getConnection() throws SQLException {
        Connection con = null;
        String url = "jdbc:postgresql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME;

        try {
            System.out.println( "Trying to connect to database: " + url );
            Class.forName( "org.postgresql.Driver" );
            con = DriverManager.getConnection( url, DB_USER, DB_PASS );
            System.out.println( "Connection completed." );
            return con;
        } catch ( ClassNotFoundException cnf ) {
            System.out.println( "Failed to load database library." );
        }
        return con;
    }
}
