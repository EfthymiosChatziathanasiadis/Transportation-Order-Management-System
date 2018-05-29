/**
 * Database class used to access the .sqlite database.
 * Implements the singleton design principle
 *
 * @author Efthymios Chatziathanasiadis
 *
 */
package com.project.TOMS.database;

import com.project.TOMS.model.CustomerRecords.Customer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WorldDatabase //Singleton Class
{

	private static WorldDatabase INSTANCE = null;		//Singleton construct
	private Connection connection = null;
	private Statement stm = null;

	private WorldDatabase() {

	}

	public static WorldDatabase getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new WorldDatabase();
		}
		return INSTANCE;
	}

	public boolean connect() {
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:database/world");	//use this when our program is packaged as a .jar
			
			return true;
		} catch (SQLException e) {
			return false;
		}
	}

	public boolean closeConnection() {
		try {
			stm.close();
			connection.close();
			return true;
		} catch (SQLException ex) {
			return false;
		}
	}

	public ResultSet query(String sql) throws SQLException {
		ResultSet resultset = null;
		stm = connection.createStatement();
		resultset = stm.executeQuery(sql);
		return resultset;
	}

	public void update(String sql) throws SQLException {
		stm = connection.createStatement();
		stm.executeUpdate(sql);
	}
        
        
        
        

}
