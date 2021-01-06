package CollectFour;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class gameUser {
	public final static String DB_URL = "jdbc:mysql://localhost:3306/gameBase";
	public final static String USER = "root";
	public final static String PASSWORD = "1625";

	public static void insertData(String Name, String password) throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
			String sql = "INSERT INTO gameUser " + "(userName,password)" + "VALUES(?,?)";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, Name);
			stmt.setBytes(2, encryption.encryptKey(password));
			stmt.executeUpdate();

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		conn.close();
		stmt.close();
	}

	public static boolean checkData(String name, String password) throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
			String sql = "SELECT EXISTS(SELECT * from gameUser WHERE gameUser.userName=? and gameUser.password=?)";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, name);
			stmt.setBytes(2, encryption.encryptKey(password));
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return true;
			} else {
				return false;
			}
	}

	

}
