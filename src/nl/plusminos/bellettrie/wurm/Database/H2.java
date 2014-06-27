package nl.plusminos.bellettrie.wurm.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class H2 {
	private Connection conn;

	public H2() {
		try {
			Class.forName("org.h2.Driver");
			conn = DriverManager.getConnection("jdbc:h2:./wurmendagboek", "sa", "");
			
			String query = "CREATE TABLE IF NOT EXISTS READTITLE(TITLE VARCHAR(255))";
			Statement st = conn.createStatement();
			st.execute(query);
		} catch (ClassNotFoundException e) {
			System.out.println("[H2] Driver error");
			e.printStackTrace();
			conn = null;
		} catch (SQLException e) {
			System.out.println("[H2] Initializing database failed; database might be corrupted");
			e.printStackTrace();
			conn = null;
		}
	}
	
	public void close() {
		try {
			conn.close();
			conn = null;
		} catch (SQLException e) {
			System.out.println("[H2] Closing database connection failed");
			e.printStackTrace();
		}
	}
	
	public void executeInsert(String query) throws SQLException {
		Statement st = conn.createStatement();
		
		st.execute(query);
	}
	
	public ResultSet executeQuery(String query) throws SQLException {
		Statement st = conn.createStatement();
		
		st.execute(query);
		
		return st.getResultSet();
	}
	
	public Connection conn() {
		return conn;
	}
	
	public void storeTitle(String title) throws SQLException {
		String query = "INSERT INTO READTITLE VALUES('" + title + "')";
		executeInsert(query);
	}
	
	public boolean hasTitle(String title) throws SQLException {
		String query = "SELECT 1 FROM READTITLE WHERE TITLE = '" + title + "'";
		
		ResultSet rs;
		
		rs = executeQuery(query);
		
		if (rs.next()) {
			return true;
		} else {
			return false;
		}
	}
	
	public int getAmountOfBooksRead() {
		String query = "SELECT COUNT(*) FROM READTITLE";
		try {
			ResultSet rs = executeQuery(query);
			if (rs.next()) {
				return rs.getInt(1);
			} else {
				System.out.println("[H2] Count query did not return any rows");
				return -1;
			}
		} catch (SQLException e) {
			System.out.println("[H2] Failed to retrieve the amount of titles in the database");
			e.printStackTrace();
			return -1;
		}	
	}
	
	public static void main(String[] args) throws SQLException {
		H2 h2 = new H2();
		
		h2.storeTitle("Ruben");
		h2.storeTitle("Michiel 2");
		
		System.out.println(h2.hasTitle("Ruben")+"");
		System.out.println(h2.hasTitle("Michiel 2")+"");
		
		h2.close();
	}
}
