package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Dao {
	// instance fields
	static Connection connect = null;
	static Statement statement = null;

	// constructor
	public static Connection getConnection() {
		// Setup the connection with the DB
		try {
			connect = DriverManager
					.getConnection("jdbc:mysql://sql9.freemysqlhosting.net/sql9266379?autoReconnect=true&useSSL=false"
							+ "&user=sql9266379&password=2uxhi47nE4");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connect;
	}

	public void createTables() {
		// variables for SQL Query table creations
		final String createTicketsTable = "CREATE TABLE mtokar_tickets(ticket_id INT AUTO_INCREMENT PRIMARY KEY, ticket_issuer VARCHAR(30),"
		+ " ticket_description VARCHAR(200))";
		final String createUsersTable = "CREATE TABLE mtokar_new_users(uid INT AUTO_INCREMENT PRIMARY KEY, uname VARCHAR(30), upass VARCHAR(30), admin TINYINT(1))";
		
		final String createTicketStatusTable = "CREATE TABLE mtokar_tickets_status1(uid INT AUTO_INCREMENT PRIMARY KEY, time_opened DATE, time_closed DATE, status ENUM('Opened', 'Closed'))";
		try {

			// create table

			statement = getConnection().createStatement();

			statement.executeUpdate(createTicketsTable);
			statement.executeUpdate(createUsersTable);
			statement.executeUpdate(createTicketStatusTable);
			System.out.println("Created tables in given database...");

			// end create table
			// close connection/statement object
			statement.close();
			connect.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		// add users to user table
		//addUsers();
	}

	public void addUsers() {
		// add list of users from userlist.csv file to users table

		// variables for SQL Query inserts
		String sql;
		Statement statement = null;
		BufferedReader br;
		List<List<String>> array = new ArrayList<>(); // array list to hold
														// spreadsheet rows &
														// columns

		// read data from file
		try {
			br = new BufferedReader(new FileReader(new File("./userlist.csv")));

			String line;
			while ((line = br.readLine()) != null) {
				array.add(Arrays.asList(line.split(",")));
			}
		} catch (Exception e) {
			System.out.println("There was a problem loading the file");
		}

		try {

			// Setup the connection with the DB

			statement = getConnection().createStatement();

			// create loop to grab each array index containing a list of values
			// and PASS (insert) that data into your User table
			for (List<String> rowData : array) {

				sql = "insert into mtokar_new_users(uname,upass,admin) " + "values('" + rowData.get(0) + "','" + rowData.get(1) +"','" + rowData.get(2)
						+ "');";
				statement.executeUpdate(sql);
			}
			System.out.println("Inserts completed in the given database...");

			// close connection/statement object
			statement.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
		
		//Delete function
		public void delete() throws Exception
		{
			try {

				// Setup the connection with the DB
				String sql;
				
				statement = getConnection().createStatement();

				sql = "DELETE FROM mtokar_tickets";
				statement.executeUpdate(sql);
				
				sql = "DELETE FROM mtokar_tickets_status1";
				statement.executeUpdate(sql);
							
				System.out.println("Delete completed in the given database...");

				// close connection/statement object
				statement.close();
				connect.close();
			}
			catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}			
	}