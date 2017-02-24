import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Olga Pavlova on 1/20/2016.
 */
// The Java class will be hosted at the URI path "/helloworld"
@Path("/helloworld")
public class HelloWorld {

    @GET
    public String testJNDI() throws NamingException {
        String result = "";
        try {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:comp/env");
            DataSource ds = (DataSource) envContext.lookup("jdbc/DB");
            Connection conn = null;
            //Class.forName("org.hsqldb.jdbc.JDBCDriver").newInstance(); ;
            conn =   ds.getConnection();


            Statement statement = conn.createStatement();
            String sql = "select EID,ENUMBER from ENTITYBUS";
            ResultSet rs = statement.executeQuery(sql);

            int count = 1;
            while (rs.next()) {
                result = result + String.format("ENTITYBUS #%d: %-15s %s",
                        count++,
                        rs.getString("eid"),
                        rs.getString("enumber")) + "  \n  ";

            }
            rs.close();
            rs = null;
            statement.close();
            statement = null;
            conn.close();
            conn = null;
        } catch (NamingException ex) {
            result = result + ex;
        } catch (SQLException ex) {
            result = result + ex;
        }
        return result;
    }

   /* public static void main(String[] args) throws IOException {
        HttpServer server = HttpServerFactory.create("http://localhost:9998/");
        server.start();

        System.out.println("Server running");
        System.out.println("Visit: http://localhost:9998/helloworld");
        System.out.println("Hit return to stop...");
        System.in.read();
        System.out.println("Stopping server");
        server.stop(0);
        System.out.println("Server stopped");
    }*/
}
