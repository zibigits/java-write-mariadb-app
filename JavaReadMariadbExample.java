import java.util.*;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.sql.*;

public class JavaReadMariadbExample {

	public static void main(String[] args) {

	try {
		System.out.println("--------------- Application settings ---------------");

		// check the environment variables needed for the application
		String cf_mariadb_server = System.getenv("CF_MARIADB_SERVER");
                if (cf_mariadb_server == null) {
			System.err.println("CF_MARIADB_SERVER environment variable is not defined ! Exiting.");
                        System.exit(0);
                } else {
                        System.out.println("CF_MARIADB_SERVER:" + cf_mariadb_server);
                }
                // check the environment variables needed for the application
                String cf_mariadb_database = System.getenv("CF_MARIADB_DATABASE");
                if (cf_mariadb_database == null) {
                        System.err.println("CF_MARIADB_DATABASE environment variable is not defined ! Exiting.");
                        System.exit(0);
                } else {
                        System.out.println("CF_MARIADB_DATABASE:" + cf_mariadb_database);
                }
                // check the environment variables needed for the application
                String cf_mariadb_user = System.getenv("CF_MARIADB_USER");
                if (cf_mariadb_user == null) {
                        System.err.println("CF_MARIADB_USER environment variable is not defined ! Exiting.");
                        System.exit(0);
                } else {
                        System.out.println("CF_MARIADB_USER:" + cf_mariadb_user);
                }
                // check the environment variables needed for the application
                String cf_mariadb_pass = System.getenv("CF_MARIADB_PASS");
                if (cf_mariadb_pass == null) {
                        System.err.println("CF_MARIADB_PASS environment variable is not defined ! Exiting.");
                        System.exit(0);
                } else {
                        System.out.println("CF_MARIADB_PASS:" + cf_mariadb_pass);
                }
                // check the environment variables needed for the application
                String cf_check_interval = System.getenv("CF_CHECK_INTERVAL");
                if (cf_check_interval == null) {
			cf_check_interval = "30";
                        System.err.println("CF_CHECK_INTERVAL environment variable is not defined ! set check_interval for " + cf_check_interval + " seconds");
                } else {
                        System.out.println("CF_CHECK_INTERVAL:" + cf_check_interval);
                }

            	// Register JDBC driver
		Class.forName("org.mariadb.jdbc.Driver");

                // Database address and credentials
                String DB_URL = "jdbc:mariadb://" + cf_mariadb_server + ":3306/" + cf_mariadb_database;
                String USER = cf_mariadb_user;
                String PASS = cf_mariadb_pass;

		// Create SQL query
                String sql = "SELECT * FROM clipboard WHERE dba_date >= (NOW() - INTERVAL " + cf_check_interval + " SECOND);";

            	// Get network settings
            	String SystemName = InetAddress.getLocalHost().getHostName();
		System.out.println("Hostname: " + SystemName);
                InetAddress iAddress = InetAddress.getLocalHost();
                String currentIp = iAddress.getHostAddress();
                System.out.println("Current IP address : " + currentIp);
		int counter = 1;

		do {
			System.out.println("-------------------- " + counter + " -------------------------");

			Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);

                        LocalDateTime now = LocalDateTime.now();
                        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        String formatDateTime = now.format(format);
                        System.out.println("Current datetime:" + formatDateTime);

	                Statement stmt = conn.createStatement();
            	        ResultSet rs = stmt.executeQuery(sql);

			if (rs.next()) {
        	        // Process the result set and display the rows
                	do {
                    	// Extract column values for each row
                    		int dba_id = rs.getInt("id");
                    		String dba_date = rs.getString("dba_date");
                    		String dba_hostname = rs.getString("hostname");
                    		String dba_description = rs.getString("description");
                    		int dba_rand_number = rs.getInt("rand_number");
                    		int dba_operation_status = rs.getInt("operation_status");
                    		// Display the row data
                    		System.out.println("id:" + dba_id + ", dba_date: " + dba_date + ", hostname: " + dba_hostname + ", description: " + dba_description + ", rand_number: " + dba_rand_number + ", operation_status: " + dba_operation_status);

                	} while (rs.next());
            		} else {
                		System.out.println("No rows returned.");
            		}

			// set a delay in reading from the database
			int check_interval = Integer.parseInt(cf_check_interval);
			Thread.sleep(check_interval*1000);

		        // Clean up resources
			rs.close();
            		stmt.close();
            		conn.close();

                        counter = counter + 1;
		} while (true);

        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
