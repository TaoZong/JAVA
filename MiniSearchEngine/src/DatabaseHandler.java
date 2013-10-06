import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;



/* DatabaseHandler
 * Handles all database connections for FavoriteServer. Only one instance
 * should be active per server.
 */
public class DatabaseHandler 
{
	// necessary parameters for configuring database connection
	private String username;
	private String password;
	private String database;
	private String hostname;
	private String server;
	
	/* DatabaseHandler()
	 * Load database configuration and driver, and test database connection.
	 */
	DatabaseHandler()
	{
		// if either the configuration or driver loading fails,
		// exit immediately
		if(loadConfig() != Status.OK && loadDriver() != Status.OK)
		{
			System.out.println("Fatal error encountered while setting up database handler.");
			System.exit(-1);
		}
					
		// if unable to establish database connection, 
		// warn the user/administrator
		if(testConnection() != Status.OK)
		{
			System.out.println("Could not verify database connection.");
		}
		
		System.out.println("Database handler started.");
	}

	/* loadConfig()
	 * Loads database connection configuration from database.properties file.
	 */
	private Status loadConfig()
	{
		// create properties object
		Properties config = new Properties();
		
		// track error status
		Status status = Status.ERROR;
		
		try
		{
			System.out.println("Attempting to load database configuration...");
			
			// load configuration from file
			config.load(new FileReader("database.properties"));
			username = config.getProperty("username");
			password = config.getProperty("password");
			database = config.getProperty("database");
			hostname = config.getProperty("hostname");
			
			// if not empty or null
			if(checkString(username) && checkString(password) 
					&& checkString(database) && checkString(hostname))
			{
				// create server string
				server = "jdbc:mysql://" + hostname + "/" + database;
				
				System.out.println("Using user " + username + " and database " + database + " on " + hostname + ".");
				System.out.println("Loading of database configuration complete.");
				
				// set status code
				status = Status.OK;
			}
			else
			{
				// configuration failed, set status code
				System.out.println("Invalid database configuration file. Please make sure to specify username, password, database, and hostname in database.properties.");
				status = Status.INVALID_CONFIG;
			}
		}
		// catch various exceptions and set status code
		catch(FileNotFoundException ex)
		{
			System.out.println("Unable to locate database.properties configuration file.");
			status = Status.NO_CONFIG;
		}
		catch(IOException ex)
		{
			System.out.println("Unable to read database.properties configuration file.");	
			status = Status.NO_CONFIG;
		}
		catch(Exception ex)
		{
			System.out.println("Unknown exception occurred.");
			status = Status.ERROR;
		}
		
		// return status code
		return status;
	}

	/* loadDriver()
	 * Loads JDBC mySQL driver.
	 */
	private Status loadDriver()
	{
		// track error status
		Status status = Status.ERROR;
		System.out.println("Attempting to load mySQL JDBC driver...");
		
		try
		{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			// load succeeded, set status
			System.out.println("Loading of the mySQL JDBC driver complete.");
			status = Status.OK;
		}
		catch(Exception ex)
		{
			// load failed, set status
			System.out.println("Unable to load mySQL JDBC driver.");
			status = Status.NO_DRIVER;
		}
		
		// return status code
		return status;
	}

	/* testConnection()
	 * Tests database connection using loaded configuration.
	 */
	private Status testConnection()
	{
		Status status = Status.ERROR;
		Connection connection = null;
		Statement statement = null;
		
		try
		{
			System.out.println("Testing database connection...");	

			// attempt to establish connection to database
			connection = DriverManager.getConnection(server, username, password);
			
			// create an executable statement
			statement = connection.createStatement();
			
			// attempt to execute statement
			ResultSet results = statement.executeQuery("SHOW TABLES;");

			/* Note:
			 * Could put these in separate try/catch statements to better
			 * catch what went wrong in case of exception.
			 */
			
			/* Note:
			 * We could test to see if the tables are properly setup here,
			 * instead of just checking the number of tables.
			 */
			
			int num = 0;
			
			// while there are results from the database
			while(results.next())
			{
				// increment the number of results found
				num++;
			}
			
			// set status to okay
			System.out.println("Database connection test complete. " + num + " tables found.");
			status = Status.OK;
		}
		catch(SQLException ex)
		{
			System.out.println("Unable to establish database connection.");
			status = Status.CONNECTION_FAILED;
		}
		finally
		{
			// always attempt to close statement and connection
			// if anything goes wrong, log but keep going
			try
			{
				statement.close();
				connection.close();
				
				if(connection.isClosed())
				{
					System.out.println("Database connection closed successfully.");
				}
				else
				{
					System.out.println("Unable to close database connection.");
				}
			}
			catch(Exception ex)
			{
				System.out.println("Encounted exception while trying to close database connection.");
			}
		}
		
		return status;
	}
	
	/* registerUser(String, String)
	 * Attempts to register a new user account. Checks to make sure the
	 * username is not already in use.
	 */
	public Status registerUser(String newuser, String newpass)
	{
		Status status = Status.OK;
		String sql = null;
		
		Connection connection = null;
		PreparedStatement statement = null;
		
		System.out.println("Attempting to register user " + newuser + "...");
		
		// attempt to establish database connection
		try
		{			
			connection = DriverManager.getConnection(server, username, password);
		}
		catch(SQLException ex)
		{
			System.out.println("Unable to establish database connection.");
			status = Status.CONNECTION_FAILED;
		}
		
		// if connection established
		if(status == Status.OK && connection != null)
		{
			// check if username already exists
			try
			{
				/* Uses a PreparedStatement to help protect against 
				 * SQL injection attacks.
				 */				
				
				sql = "SELECT user FROM users WHERE user = ?";
				
				statement = connection.prepareStatement(sql);
				statement.setString(1, newuser);
				
				System.out.println("Executing statement " + sql);
				ResultSet results = statement.executeQuery();
				
				if(results.next())
				{
					System.out.println("Unable to register user, username already exists.");
					status = Status.DUPLICATE_USER;
				}
			}
			catch(SQLException ex)
			{
				System.out.println("Encountered SQL exception while accessing database. Please check database configuration.");
				status = Status.SQL_ERROR;
			}
		}
		
		// if connection established and username does not exist
		if(status == Status.OK && connection != null)
		{
			// attempt to add new user to database
			try
			{
				/* Uses a PreparedStatement to help protect against 
				 * SQL injection attacks.
				 */				
				
				sql = "INSERT INTO users (user, password) VALUES (?, ?);";
				statement = connection.prepareStatement(sql);
				statement.setString(1, newuser);
				statement.setString(2, newpass);
				
				System.out.println("Executing statement " + sql);
				statement.executeUpdate();
								
				System.out.println("User " + newuser + " successfully registered.");
				status = Status.OK;
			}
			catch(SQLException ex)
			{
				System.out.println("Encountered SQL exception while registering user.");
				status = Status.SQL_ERROR;
			}
		}
		
		// always try to close statement and connection
		try
		{
			statement.close();
			connection.close();
		}
		catch(Exception ex){}
		
		return status;
	}

	/* checkLogin(String, String)
	 * Checks whether the supplied user/pass match database. 
	 */
	public boolean checkLogin(String user, String pass)
	{
		boolean status = false;
		String sql = null;
		
		try
		{
			System.out.println("Attempting to verify user " + user + "...");
			
			Connection connection = DriverManager.getConnection(server, username, password);
			
			/* Uses a PreparedStatement to help protect against 
			 * SQL injection attacks.
			 */
			
			sql = "SELECT user FROM users WHERE user = ? AND password = ?";
			
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, user);
			statement.setString(2, pass);
			
			System.out.println("Executing statement " + sql);
			ResultSet results = statement.executeQuery();
			
			if(results.next())
			{
				System.out.println("User " + user + " entered correct password.");
				status = true;
			}
			else
			{
				status = false;
			}			
		}
		catch(Exception ex)
		{
			System.out.println("Unable to verify user " + user + ".");
			status = false;
		}
		
		return status;
	}
	
	/* boolean checkString(String)
	 * Checks whether the specified String is null or empty.
	 * Returns false if null or empty, true otherwise.
	 */	
	public static boolean checkString(String text)
	{
		if(text != null && !text.trim().isEmpty())
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public void saveHistory(String user, String query)
	{
		String sql = null;
		
		Connection connection = null;
		PreparedStatement statement = null;
		
		try
		{
			/* Uses a PreparedStatement to help protect against 
			 * SQL injection attacks.
			 */				
			
						
			connection = DriverManager.getConnection(server, username, password);
			
			sql = "SELECT user FROM history WHERE user ='" + user + "' AND query ='" + query + "';";
			
			statement = connection.prepareStatement(sql);
			//statement.setString(1, user);
			//statement.setString(1, query);
			
			System.out.println("Executing statement " + sql);
			ResultSet results = statement.executeQuery();
			
			if(results.next())
			{
				System.out.println("query already exists.");
				return;
			}
			else
			{
				sql = "INSERT INTO history (user, query) VALUES (?, ?);";

				statement = connection.prepareStatement(sql);
				statement.setString(1, user);
				statement.setString(2, query);
				
				System.out.println("Executing statement " + sql);
				statement.executeUpdate();
			}
		}
		catch(SQLException ex)
		{
			System.out.println("Encountered SQL exception while accessing database. Please check database configuration.");
		}
	}
	
	public ArrayList<String> getHistory(String user)
	{
		ArrayList<String> historys = new ArrayList<String>();
		
		String sql = null;
		
		Connection connection = null;
		PreparedStatement statement = null;
		
		try
		{
			/* Uses a PreparedStatement to help protect against 
			 * SQL injection attacks.
			 */				
			
						
			connection = DriverManager.getConnection(server, username, password);
			
			sql = "SELECT query FROM history WHERE user ='" + user + "';";
			
			statement = connection.prepareStatement(sql);
			//statement.setString(1, user);
			//statement.setString(1, query);
			
			System.out.println("Executing statement " + sql);
			ResultSet results = statement.executeQuery();
			
			while(results.next())
			{
				historys.add(results.getString("query"));
			}
		}
		catch(SQLException ex)
		{
			System.out.println("Encountered SQL exception while accessing database. Please check database configuration.");
		}
		
		return historys;
	}
	
	public void clearHistory(String user)
	{
		String sql = null;
		
		Connection connection = null;
		PreparedStatement statement = null;
		
		try
		{
			/* Uses a PreparedStatement to help protect against 
			 * SQL injection attacks.
			 */				
			
						
			connection = DriverManager.getConnection(server, username, password);
			
			sql = "DELETE FROM history WHERE user= \"" + user + "\";" ;
			
			statement = connection.prepareStatement(sql);
			//statement.setString(1, user);
			//statement.setString(1, query);
			
			System.out.println("Executing statement " + sql);
			statement.executeUpdate();
		}
		catch(SQLException ex)
		{
			System.out.println("Encountered SQL exception while accessing database. Please check database configuration.");
		}
		
	}
	
	
	public void saveVisitedPage(String user, String url)
	{
		String sql = null;
		
		Connection connection = null;
		PreparedStatement statement = null;
		
		try
		{
			/* Uses a PreparedStatement to help protect against 
			 * SQL injection attacks.
			 */				
			
						
			connection = DriverManager.getConnection(server, username, password);
			
			sql = "SELECT url FROM visited WHERE user ='" + user + "' AND url ='" + url + "';";
			
			statement = connection.prepareStatement(sql);
			//statement.setString(1, user);
			//statement.setString(1, query);
			
			System.out.println("Executing statement " + sql);
			ResultSet results = statement.executeQuery();
			
			if(results.next())
			{
				System.out.println("Record already exists.");
				return;
			}
			else
			{
				sql = "INSERT INTO visited (user, url) VALUES (?, ?);";

				statement = connection.prepareStatement(sql);
				statement.setString(1, user);
				statement.setString(2, url);
				
				System.out.println("Executing statement " + sql);
				statement.executeUpdate();
			}
		}
		catch(SQLException ex)
		{
			System.out.println("Encountered SQL exception while accessing database. Please check database configuration.");
		}
	}
	
	public void clearVisitedPage(String user)
	{
		String sql = null;
		
		Connection connection = null;
		PreparedStatement statement = null;
		
		try
		{
			/* Uses a PreparedStatement to help protect against 
			 * SQL injection attacks.
			 */				
			
						
			connection = DriverManager.getConnection(server, username, password);
			
			sql = "DELETE FROM visited WHERE user= \"" + user + "\";" ;
			
			statement = connection.prepareStatement(sql);
			//statement.setString(1, user);
			//statement.setString(1, query);
			
			System.out.println("Executing statement " + sql);
			statement.executeUpdate();
		}
		catch(SQLException ex)
		{
			System.out.println("Encountered SQL exception while accessing database. Please check database configuration.");
		}
		
	}
	
	public ArrayList<String> getVisitedPage(String user)
	{
		ArrayList<String> visitedPage = new ArrayList<String>();
		
		String sql = null;
		
		Connection connection = null;
		PreparedStatement statement = null;
		
		try
		{
			/* Uses a PreparedStatement to help protect against 
			 * SQL injection attacks.
			 */				
			
						
			connection = DriverManager.getConnection(server, username, password);
			
			sql = "SELECT url FROM visited WHERE user ='" + user + "';";
			
			statement = connection.prepareStatement(sql);
			//statement.setString(1, user);
			//statement.setString(1, url);
			
			System.out.println("Executing statement " + sql);
			ResultSet results = statement.executeQuery();
			
			while(results.next())
			{
				visitedPage.add(results.getString("url"));
			}
		}
		catch(SQLException ex)
		{
			System.out.println("Encountered SQL exception while accessing database. Please check database configuration.");
		}
		
		return visitedPage;
	}
	
	public void saveSnippet(String url, String context)
	{
		String sql = null;
		
		Connection connection = null;
		PreparedStatement statement = null;
		
		try
		{
			/* Uses a PreparedStatement to help protect against 
			 * SQL injection attacks.
			 */				
			
			
			connection = DriverManager.getConnection(server, username, password);

			sql = "SELECT id FROM snippet WHERE url ='" + url + "';";
			
			statement = connection.prepareStatement(sql);
			//statement.setString(1, user);
			//statement.setString(1, query);
			
			System.out.println("Executing statement " + sql);
			ResultSet results = statement.executeQuery();
			
			if(results.next())
			{
				System.out.println("page snippet already exists.");
				return;
			}
			else
			{
				sql = "INSERT INTO snippet (url, context) VALUES (?, ?);";

				statement = connection.prepareStatement(sql);
				statement.setString(1, url);
				statement.setString(2, context);
				
				System.out.println("Executing statement " + sql);
				statement.executeUpdate();
			}
		}
		catch(SQLException ex)
		{
			System.out.println("Encountered SQL exception while accessing database. Please check database configuration.");
		}
		
		// always try to close statement and connection
		try
		{
			statement.close();
			connection.close();
		}
		catch(Exception ex){}
	}
	
	public String getSnippet(String url)
	{
		String snippet = null;
		
		String sql = null;
		
		Connection connection = null;
		PreparedStatement statement = null;
		
		try
		{
			/* Uses a PreparedStatement to help protect against 
			 * SQL injection attacks.
			 */				
						
			connection = DriverManager.getConnection(server, username, password);
			
			sql = "SELECT context FROM snippet WHERE url ='" + url + "';";
			
			statement = connection.prepareStatement(sql);
			//statement.setString(1, user);
			//statement.setString(1, url);
			
			System.out.println("Executing statement " + sql);
			ResultSet results = statement.executeQuery();
			
			while(results.next())
			{
				snippet = results.getString("context");
			}
		}
		catch(SQLException ex)
		{
			System.out.println("Encountered SQL exception while accessing database. Please check database configuration.");
		}
		
		return snippet;
	}
	
	public void addSuggest(String query)
	{
		String sql = null;
		
		Connection connection = null;
		PreparedStatement statement = null;
		
		try
		{
			/* Uses a PreparedStatement to help protect against 
			 * SQL injection attacks.
			 */				
			
						
			connection = DriverManager.getConnection(server, username, password);
			
			sql = "SELECT counter FROM suggested WHERE query ='" + query + "';";
			
			statement = connection.prepareStatement(sql);
			//statement.setString(1, user);
			//statement.setString(1, query);
			
			System.out.println("Executing statement " + sql);
			ResultSet results = statement.executeQuery();
			
			if(results.next())
			{
				int counter = results.getInt("counter");
				counter ++;
				
				sql = "UPDATE suggested SET counter='" + counter + "' WHERE query='" + query + "';";

				
				statement = connection.prepareStatement(sql);
				//statement.setString(1, user);
				//statement.setString(1, query);
				
				System.out.println("Executing statement " + sql);
				statement.executeUpdate();
				
				return;
			}
			else
			{
				sql = "INSERT INTO suggested (query, counter) VALUES (?, 1);";

				statement = connection.prepareStatement(sql);
				//statement.setString(1, user);
				statement.setString(1, query);
				
				System.out.println("Executing statement " + sql);
				statement.executeUpdate();
			}
		}
		catch(SQLException ex)
		{
			System.out.println("Encountered SQL exception while accessing database. Please check database configuration.");
		}
	}
	
	public String getSuggest()
	{
		String suggest = "";
		
		String sql = null;
		
		Connection connection = null;
		PreparedStatement statement = null;
		
		try
		{
			/* Uses a PreparedStatement to help protect against 
			 * SQL injection attacks.
			 */				
						
			connection = DriverManager.getConnection(server, username, password);
			
			sql = "SELECT query FROM suggested ORDER BY counter DESC;";
			
			statement = connection.prepareStatement(sql);
			//statement.setString(1, user);
			//statement.setString(1, url);
			
			System.out.println("Executing statement " + sql);
			ResultSet results = statement.executeQuery();
			
			int numofSug = 5;
			
			while(results.next() && numofSug > 0)
			{
				suggest += results.getString("query") + " ";
				numofSug --;
			}
		}
		catch(SQLException ex)
		{
			System.out.println("Encountered SQL exception while accessing database. Please check database configuration.");
		}
		
		return suggest;
	}
	
	public void changePassword(String user, String npass)
	{
		String sql = null;
		
		Connection connection = null;
		PreparedStatement statement = null;
		
		try
		{
			/* Uses a PreparedStatement to help protect against 
			 * SQL injection attacks.
			 */				
			
						
			connection = DriverManager.getConnection(server, username, password);
			
			sql = "UPDATE users SET password = '" + npass + "' WHERE user = '" + user + "';";
			
			statement = connection.prepareStatement(sql);
			//statement.setString(1, npass);
			//statement.setString(2, user);
			
			System.out.println("Executing statement " + sql);
			//ResultSet results = statement.executeQuery();
			statement.executeUpdate();
		}
		catch(SQLException ex)
		{
			System.out.println("Encountered SQL exception while accessing database. Please check database configuration.");
		}
	}
}
