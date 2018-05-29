/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.TOMS.controller.Authentication;

import com.project.TOMS.controller.systemUserRecords.SystemUserRegistryDatabase;
import com.project.TOMS.database.Database;
import com.project.TOMS.model.systemUserRecords.Admin;
import com.project.TOMS.model.systemUserRecords.SystemUser;
import com.project.TOMS.model.systemUserRecords.TransportPlanner;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author EfthymiosChatziathanasiadis
 */
public class Authentication {

    private final SystemUserRegistryDatabase usersRegistry;
    private final Database database = Database.getInstance();

    public Authentication(SystemUserRegistryDatabase userRegistry) {
         this.usersRegistry=userRegistry;
    }

    public boolean authenticate(String id, String password) {
        boolean authentication = false;
        try {

            database.connect("central");
            ResultSet resultset = database.query("SELECT * FROM User WHERE UserID=\'" + id + "\' AND UserPassword=\'" + password + "\'");
            //isBeforeFirst returns false if no rows in resultset
            if (!resultset.isBeforeFirst()) {
                authentication = false;
            } else {
                boolean isAdmin = (resultset.getInt("UserIsAdmin") == 1) ? true : false;
                SystemUser user = null;
                if (isAdmin) {
                    user = new Admin(resultset.getInt("UserID"), resultset.getString("UserPassword"), resultset.getString("UserFirstName"), resultset.getString("UserLastName"));
                } else {
                    user = new TransportPlanner(resultset.getInt("UserID"), resultset.getString("UserPassword"), resultset.getString("UserFirstName"), resultset.getString("UserLastName"));
                }
                authentication = true;
                usersRegistry.setCurrentUser(user);
                database.closeConnection();
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return authentication;
    }
}
