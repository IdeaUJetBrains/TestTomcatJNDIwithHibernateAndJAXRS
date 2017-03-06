1. Create a project with RESRfull+Web+JPA frameworks
2. Add Hibernate libs to module libs
3. Create
- Entitybus class with  "eid" and "enumber" properties
- HelloWorld class using jax-rs:
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
}

4. Create in the Database view a local database and add there data from hsqldb_data.txt
5. Add Tomcat context descriptor via ProjectSettings->Modules->Web facet->"Add ApplicationServer specific descriptor" button
"META-INF/context.xml" appears in the "web" folder.
5. Add into context.xml file DB data:
<Context path="/">
    <Resource name="jdbc/DB" auth="Container"
          type="javax.sql.DataSource"
          username="sa" password="sa"
          driverClassName="org.hsqldb.jdbc.JDBCDriver"
          url="jdbc:hsqldb:file:C:/Users/Olga Pavlova/IdeaProjects/TestTomcatJNDIwithHibernateAndJAXRS/db/DB"/>
</Context>
6.
Add into web/WEB-INF/web.xml jax-rs data and add info about DB described in the context:
    <servlet>
        <servlet-name>javax.ws.rs.core.Application</servlet-name>
    </servlet>
    <servlet-mapping>
        <servlet-name>javax.ws.rs.core.Application</servlet-name>
        <url-pattern>/*</url-pattern>
    </servlet-mapping>

    <!--For database work -->
    <resource-ref>
        <res-ref-name>jdbc/DB</res-ref-name>
        <res-type>javax.sql.DataSource</res-type>
        <res-auth>Container</res-auth>
    </resource-ref>

7. Go to the ProjectSettings->Modules->Libs->Jersey
   Press "Change version" button-> add all the Jersey libs
   (Note: delete unnecessary javax.persistence.jar if it is already present)

8. Add all requered libs into artifact ProjectSettings->Modules->Artifacts

9.  Create RunConfiguration
- with a link for OpenBrowser: http://localhost:8080/helloworld
- with added artifact.

10.  Add hsqldb-2.3.2.jar
-into TOMCAT_HOME/lib folder

11. Run the created RunConfiguration.

RESULT:
1. opened http://localhost:8080/helloworld
2. On the page ist data:
ENTITYBUS #1: 1 1ENTITYBUS
ENTITYBUS #2: 2 2ENTITYBUS