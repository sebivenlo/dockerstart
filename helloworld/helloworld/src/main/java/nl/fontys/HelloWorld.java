package nl.fontys;

import java.io.Serializable;
import java.sql.Connection;
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

    private static final long serialVersionUID = 1L;
    @Resource( lookup = "java:/jdbc/dockerds" )
    DataSource ds;

    public List<String> getMessages() {

        String query = "SELECT * FROM awesomeTable";
        List<String> records = new ArrayList<>();
        try ( Connection con = ds.getConnection();
                PreparedStatement ps = con.prepareStatement( query ); ) {
            ps.execute();
            try ( ResultSet rs = ps.getResultSet(); ) {
                while ( rs.next() ) {
                    records.add( rs.getString( 2 ) );
                }
            }
        } catch ( SQLException ex ) {
            Logger.getLogger( HelloWorld.class.getName() ).log( Level.SEVERE, null, ex );
        }
        return records;
    }
}
