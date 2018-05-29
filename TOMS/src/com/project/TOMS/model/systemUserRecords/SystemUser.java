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
public abstract class SystemUser {
    int id;
    String password;
    String name;
    String surname;
    
    
    
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id=id;
    }
    
    public String getIsAdmin(){
        if(this instanceof Admin){
            return "admin";
        }else return "planner";
    }
    
    public String getPassword(){
        return this.password;
    }
    public void setPassword(String password){
        this.password=password;
    }
    
    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name=name;
    }
    
    public String getSurname(){
        return this.surname;
    }
    public void setSurname(String surname){
        this.surname=surname;
    }
}
