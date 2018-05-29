/**
 * Database class used to access the .sqlite database.
 * Implements the singleton design principle
 *
 * @author Efthymios Chatziathanasiadis
 *
 */
package com.project.TOMS.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database //Singleton Class
{

	private static Database INSTANCE = null;		//Singleton construct
	private Connection connection = null;
	private Statement stm = null;
        public static String databaseName = "central";

	private Database() {

	}

	public static Database getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new Database();
		}
		return INSTANCE;
	}

	public boolean connect(String db) {
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:database/"+databaseName+".db");	//use this when our program is packaged as a .jar
			
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
        
        public int getTableCount(String table){
            String sql = "SELECT seq as count from sqlite_sequence WHERE name='"+table+"';";
            int count=0;
            try {
                ResultSet rs = this.query(sql);
                if(rs.next())
                    count = rs.getInt("count");
            } catch (SQLException ex) {

            }
            return count;
        }
        
        public int getTableCount(String table, boolean bol){
            this.connect("central");
            String sql = "SELECT seq as count from sqlite_sequence WHERE name='"+table+"';";
            int count=0;
            try {
                ResultSet rs = this.query(sql);
                if(rs.next())
                    count = rs.getInt("count");
                    System.out.println(count);
            } catch (SQLException ex) {

            }
            this.closeConnection();
            return count;
        }
        
        public static void setDatabaseName(String name){
            databaseName = name;
        }

}
