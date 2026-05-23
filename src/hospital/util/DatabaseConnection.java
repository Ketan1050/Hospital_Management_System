package hospital.util;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DatabaseConnection {

    private static final Logger LOG = Logger.getLogger(DatabaseConnection.class.getName());

    // ── MySQL Configuration ───────────────────────────────────────────────────
    private static final String HOST     = "localhost";
    private static final String PORT     = "3306";
    private static final String DB_NAME  = "hospital_db";
    private static final String USER     = "root";        
    private static final String PASSWORD = "root@12345";          
    
    private static final String URL =
        "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB_NAME
        + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    // ─────────────────────────────────────────────────────────────────────────

    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            connection.setAutoCommit(true);
            LOG.info("MySQL connection established → " + DB_NAME);
        } catch (ClassNotFoundException e) {
            LOG.log(Level.SEVERE, "MySQL JDBC Driver not found. Add mysql-connector-j.jar to build path.", e);
            throw new RuntimeException("MySQL Driver not found.", e);
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Failed to connect to MySQL. Check host/port/credentials.", e);
            throw new RuntimeException("MySQL connection failed: " + e.getMessage(), e);
        }
    }

    /** Thread-safe Singleton using double-checked locking. */
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null || isClosed()) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    private static boolean isClosed() {
        try {
            return instance.connection == null || instance.connection.isClosed();
        } catch (SQLException e) {
            return true;
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                LOG.info("MySQL connection closed.");
            }
        } catch (SQLException e) {
            LOG.log(Level.WARNING, "Error closing connection.", e);
        }
    }
}
