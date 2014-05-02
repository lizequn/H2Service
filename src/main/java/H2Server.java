import org.h2.tools.Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author ZequnLi
 *         Date: 14-4-26
 */
public class H2Server {
    private Connection connection;
    private Server server;
    public H2Server() throws SQLException {
        try {
            Class.forName("org.h2.Driver");
            this.connection = DriverManager.getConnection("jdbc:h2:mem:log");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        final String[] args = new String[] {
                "-tcpPort", "8092",
                "-tcpAllowOthers","true" };

        this.server = Server.createTcpServer().start();
    }
    public void shutdown(){
        server.shutdown();
    }
    public static void main(String [] args) throws SQLException, InterruptedException {
        final H2Server server1 = new H2Server();
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                System.out.println("exit");
                server1.shutdown();
            }
        });
        MemoryMonitor memoryMonitor = new MemoryMonitor(10000);
        memoryMonitor.start();
        while (true){
            Thread.sleep(2000);
        }
    }

}
