package nl.plusminos.bellettrie.wurm.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import nl.plusminos.bellettrie.wurm.exceptions.DatabaseWurmException;

public class H2 {
	private Connection conn;

	public H2() {

	}
	
	public void init() throws DatabaseWurmException {
		try {
			Class.forName("org.h2.Driver");
			conn = DriverManager.getConnection("jdbc:h2:./wurmendagboek", "sa", "");
			
			String query = "CREATE TABLE IF NOT EXISTS READTITLE("
					+ "TITLE VARCHAR(255),"
					+ "WRITER VARCHAR(255)"
					+ ")";
			
			Statement st = conn.createStatement();
			st.execute(query);
		} catch (ClassNotFoundException e) {
			System.out.println("[H2] Driver error");
			e.printStackTrace();
			conn = null;
			throw new DatabaseWurmException("[H2] Driver error");
		} catch (SQLException e) {
			System.out.println("[H2] Initializing database failed; database might be corrupted");
			e.printStackTrace();
			conn = null;
			throw new DatabaseWurmException("[H2] Initializing database failed; database might be corrupted");
		}
	}
	
	public void close() throws DatabaseWurmException {
		try {
			conn.close();
			conn = null;
		} catch (SQLException e) {
			System.out.println("[H2] Closing database connection failed");
			e.printStackTrace();
			throw new DatabaseWurmException("[H2] Closing database connection failed");
		}
	}
	
	public void executeInsert(String query) throws DatabaseWurmException {
		try {
			Statement st;
			st = conn.createStatement();
			st.execute(query);
		} catch (SQLException e) {
			System.out.println("[H2] Failed to execute insert");
			e.printStackTrace();
			throw new DatabaseWurmException("[H2] Failed to execute insert");
		}
	}
	
	public ResultSet executeQuery(String query) throws DatabaseWurmException {
		try {
			Statement st;
			st = conn.createStatement();
			
			st.execute(query);
			
			return st.getResultSet();
		} catch (SQLException e) {
			System.out.println("[H2] Failed to execute query");
			e.printStackTrace();
			throw new DatabaseWurmException("[H2] Failed to execute query");
		}
	}
	
	public Connection conn() {
		return conn;
	}
	
	public void storeTitle(String title, String writer) throws DatabaseWurmException {
		String query = "INSERT INTO READTITLE VALUES('" + title + "', '" + writer + "')";
		executeInsert(query);
	}
	
	public boolean hasTitle(String title) throws DatabaseWurmException {
		try {
			String query = "SELECT 1 FROM READTITLE WHERE TITLE = '" + title + "'";
			
			ResultSet rs = executeQuery(query);
			
			if (rs.next()) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			System.out.println("[H2] Failed to check the rows of the resultset");
			e.printStackTrace();
			throw new DatabaseWurmException("[H2] Failed to check the rows of the resultset");
		}
	}
	
	public int getAmountOfBooksRead() throws DatabaseWurmException {
		String query = "SELECT COUNT(*) FROM READTITLE";
		
		ResultSet rs = executeQuery(query);
		try {
			if (rs.next()) {
				return rs.getInt(1);
			} else {
				System.out.println("[H2] Count query did not return any rows");
				return -1;
			}
		} catch (SQLException e) {
			System.out.println("[H2] Failed to check the result of the amount of books query");
			e.printStackTrace();
			throw new DatabaseWurmException("[H2] Failed to check the result of the amount of books query");
		}
	}
	
	public static void main(String[] args) throws SQLException {
		H2 h2 = new H2();
		
		try {
			h2.init();
			h2.storeTitle("Ruben", "Schrijver1");
			h2.storeTitle("Michiel 2", "Schrijver2");
			
			System.out.println(h2.hasTitle("Ruben")+"");
			System.out.println(h2.hasTitle("Michiel 2")+"");
			
			h2.close();
		} catch (DatabaseWurmException e) {
			System.out.println("FAILURE");
			e.printStackTrace();
		}
	}
}
