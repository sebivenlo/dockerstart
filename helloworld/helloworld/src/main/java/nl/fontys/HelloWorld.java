package nl.fontys;

import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;

/**
 * Demo of using a EJB class to produce data.
 * Typically youe application has a thin (simple) controller class, that can be
 * accessed by a configurable name (helloworld in ths case). The work is done by
 * Enterprise Java Beans, Oracle n this case.
 *
 * @author EricSoldierer
 * @author Pieter van den Hombergh {@code <p.vandenhombergh@fontys.nl>}
 */
@SessionScoped
@Named( "helloWorld" )
public class HelloWorld implements Serializable {

    private static final long serialVersionUID = 1L;
    @EJB
    Oracle oracle;

    public List<String> getMessages() {
        List<String> result = oracle.getMessages();
        result.add( 0, "using oracle" ); // make sure we are using the oracle.
        return result;
    }
}
