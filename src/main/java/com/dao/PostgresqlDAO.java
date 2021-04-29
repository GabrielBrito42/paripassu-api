package com.dao;

import java.sql.Connection;


import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.model.Password;

public class PostgresqlDAO {
	private static Connection con = null;
	private static String url =  "jdbc:postgresql://localhost:5432/passwords_control";
	
	static {
		try {
			Class.forName("org.postgresql.Driver");
			String urlTocreateDb = "jdbc:postgresql://localhost:5432/";
			con = DriverManager.getConnection(urlTocreateDb, "postgres", "1234");
			PreparedStatement pstd = con.prepareStatement("CREATE DATABASE passwords_control");
			try {
				pstd.execute();
			} catch(Exception ex) {
				System.out.println("Database already created");
			}
			con = DriverManager.getConnection(url, "postgres", "1234");
			PreparedStatement pst = con.prepareStatement("CREATE TABLE IF NOT EXISTS passwords("
					+ "type CHAR(1) NOT NULL,"
					+ "password INT NOT NULL,"
					+ "enabled BOOLEAN NOT NULL)");
			pst.execute();
        } catch (Exception ex) {
            Logger lgr = Logger.getLogger(PostgresqlDAO.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
		System.out.println("Connection into database successfully");
	}
	
	public PostgresqlDAO() {
		
	}
	
	public ArrayList<Password> getData() throws SQLException, ClassNotFoundException {
		Class.forName("org.postgresql.Driver");
		con = DriverManager.getConnection(url, "postgres", "1234");
		
		PreparedStatement pst = con.prepareStatement("SELECT * FROM public.passwords");
		ResultSet rs = pst.executeQuery();
		
		ArrayList<Password> data = new ArrayList<Password>();
        while ( rs.next() ) {
            String password = rs.getString("password");
            String type = rs.getString("type");
            Boolean enabled = rs.getBoolean("enabled");
        	Password passwordToAdd = new Password(password, type, enabled);
            data.add(passwordToAdd);
        }
        rs.close();
        pst.close();
        con.close();
        return data;
	}
	
	public int getPasswordsFromTypeSize(String type) throws SQLException, ClassNotFoundException {
		Class.forName("org.postgresql.Driver");
		con = DriverManager.getConnection(url, "postgres", "1234");
		PreparedStatement pst = con.prepareStatement("SELECT COUNT(*) FROM public.passwords WHERE type=(?)");
		pst.setString(1, type);
		ResultSet rs = pst.executeQuery();
		int result = 0;
        while ( rs.next() ) {
    		result = rs.getInt(1);
        }
        pst.close();
        con.close();
		return result;
	}
	
	public int getPasswordsFromTypeSizeTrue(String type) throws SQLException, ClassNotFoundException {
		Class.forName("org.postgresql.Driver");
		con = DriverManager.getConnection(url, "postgres", "1234");
		PreparedStatement pst = con.prepareStatement("SELECT COUNT(*) FROM public.passwords WHERE type=(?) AND enabled IS TRUE");
		pst.setString(1, type);
		ResultSet rs = pst.executeQuery();
		int result = 0;
        while ( rs.next() ) {
    		result = rs.getInt(1);
        }
        pst.close();
        con.close();
		return result;
	}
	
	public ArrayList<Password> getPasswordsFromType(String passwordtype) throws ClassNotFoundException, SQLException{
		Class.forName("org.postgresql.Driver");
		con = DriverManager.getConnection(url, "postgres", "1234");
		PreparedStatement pst = con.prepareStatement("SELECT * FROM public.passwords WHERE type=(?) AND enabled IS TRUE ORDER BY password");
		pst.setString(1, passwordtype);
		ResultSet rs = pst.executeQuery();
		ArrayList<Password> data = new ArrayList<Password>();
        while ( rs.next() ) {
            String password = rs.getString("password");
            String type = rs.getString("type");
            Boolean enabled = rs.getBoolean("enabled");
        	Password passwordToAdd = new Password(password, type, enabled);
            data.add(passwordToAdd);
        }
        return data;
	}
	
	public void savePassword(int passwordValue, String passwordType) throws SQLException {
		String query = "INSERT INTO public.passwords(type, password, enabled) VALUES(?, ?, ?)";
		con = DriverManager.getConnection(url, "postgres", "1234");
		PreparedStatement pst = con.prepareStatement(query);
		pst.setString(1, passwordType);
		pst.setInt(2, passwordValue);
		pst.setBoolean(3, true);
		pst.execute();
        pst.close();
        con.close();
	}
	
	public void callNextPassword(String password, String type) throws SQLException {
		con = DriverManager.getConnection(url, "postgres", "1234");
		String query = "UPDATE public.passwords SET enabled=false WHERE password=(?) AND type=(?)";
		PreparedStatement pst = con.prepareStatement(query);
		pst.setInt(1, Integer.parseInt(password));
		pst.setString(2, type);
		pst.execute();
        pst.close();
        con.close();
	}
	
	public void resetPasswords() throws SQLException {
		String query = "DELETE from public.passwords";
		con = DriverManager.getConnection(url, "postgres", "1234");
		PreparedStatement pst = con.prepareStatement(query);
		pst.execute();
        pst.close();
        con.close();
	}
}
