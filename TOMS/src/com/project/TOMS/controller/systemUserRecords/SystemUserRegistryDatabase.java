/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.TOMS.controller.systemUserRecords;

import com.project.TOMS.database.Database;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import com.project.TOMS.model.systemUserRecords.Admin;
import com.project.TOMS.model.systemUserRecords.SystemUser;
import com.project.TOMS.model.systemUserRecords.TransportPlanner;

/**
 *
 * @author EfthymiosChatziathanasiadis
 */
public class SystemUserRegistryDatabase {

    Database database = Database.getInstance();
    static SystemUserRegistryDatabase INSTANCE = null;
    SystemUser currentUser = null;
    ArrayList<SystemUser> users = new ArrayList<SystemUser>();

    private SystemUserRegistryDatabase() {
        this.loadUsers();
    }

    public static SystemUserRegistryDatabase getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SystemUserRegistryDatabase();
        }
        return INSTANCE;
    }

    public void setCurrentUser(SystemUser user) {
        this.currentUser = user;
    }
    public SystemUser getCusrrentUser(){
        return this.currentUser;
    }
    public SystemUser getDisponentOfOrder(int id){
        SystemUser disponent = null;
        if(users.isEmpty()){
            this.getUsers();
        }
        Iterator it = users.iterator();
        while(it.hasNext()){
            disponent = (SystemUser) it.next();
            if(disponent.getId() == id)break;
        }
        return disponent;
    }
// TODO: create user defined exception.to handle integrity cases

    public void addSystemUser(String name, String surname, String password, boolean type) {
        String sql = "INSERT INTO User(UserFirstName, UserLastName, UserPassword, UserIsAdmin"
                + ") VALUES ('"+name+"','"+surname+"',"
                + "'"+password+"','"+(type?1:0)+"');";
        database.connect("central");
        try {
            database.update(sql);
            int id = database.getTableCount("User");
            SystemUser user;
            if (type) {
                user = new Admin(id, password, name, surname);
            } else {
                user = new TransportPlanner(id, password, name, surname);
            }
            users.add(user);
        } catch (SQLException ex) {
            ex.printStackTrace();
            if (ex.getErrorCode() == 0) {

            }
        }
        database.closeConnection();
    }

    private void loadUsers() {
        if(users.isEmpty()){
            String sql = "SELECT * FROM User;";
            try {
                ResultSet rs;
                database.connect("central");
                rs = database.query(sql);

                while (rs.next()) {
                    SystemUser user;
                    if (rs.getInt("UserIsAdmin") == 1) {
                        user = new Admin(rs.getInt("UserID"), rs.getString("UserPassword"), rs.getString("UserFirstName"),
                                rs.getString("UserLastName"));
                    } else {
                        user = new TransportPlanner(rs.getInt("UserID"), rs.getString("UserPassword"), rs.getString("UserFirstName"),
                                rs.getString("UserLastName"));
                    }

                    users.add(user);
                }
                database.closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
    public ArrayList<SystemUser> getUsers() {
        return users;
    }

   
// TODO: create user defined exception.to handle integrity cases

    public void editUser(SystemUser user,  String password, String name, String surname) {
        user.setPassword(password);
        user.setName(name);
        user.setSurname(surname);
        String sql = "UPDATE User SET  UserFirstName='"+name+"', "
                + "UserLastName='"+surname+"', UserPassword='"+password+"'  WHERE id='"+user.getId()+"';";
        database.connect("central");
        try {
            database.update(sql);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        database.closeConnection();
    }

    public void deleteUser(SystemUser user) {
        users.remove(user);
        String sql = "DELETE FROM User WHERE id='"+user.getId()+"';";
        database.connect("central");
        try {
            database.update(sql);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        database.closeConnection();
    }

}
