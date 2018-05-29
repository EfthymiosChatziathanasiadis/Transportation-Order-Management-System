/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.TOMS.model.systemUserRecords;


/**
 *
 * @author EfthymiosChatziathanasiadis
 */
//TODO: redefine class not as singleton. 
public class Admin extends SystemUser {
    
    public Admin(int id, String password, String name, String surname){
        this.id=id;
        this.password=password;
        this.name=name;
        this.surname=surname;
    }
  
    
}
