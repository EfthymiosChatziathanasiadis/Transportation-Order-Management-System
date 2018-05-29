/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.TOMS.model.CustomerRecords;

import com.project.TOMS.algorithms.dijkstra.model.Vertex;
import java.util.ArrayList;

/**
 *
 * @author EfthymiosChatziathanasiadis
 */
public class Customer {
    
    int id;
    String company;
    String phone;
    Vertex address;
    
    
    public Customer(){
        
    }
    
    public Customer(int id, String company, String phone, Vertex address){
        this.id=id;
        this.company=company;
        this.phone=phone;
        this.address = address;
    }
    
    public int getId(){
        return this.id;
    }
    public void setId(int id){
        this.id=id;
    }
    
    public Vertex getAddress(){
        return this.address;
    }
    public String getCountry(){
        return this.address.getCountry();
    }
    public String getCity(){
        return this.address.getCity();
    }
    
    public void setAddress(Vertex address){
        this.address = address;
    }
    
   
    public String getPhone(){
        return this.phone;
    }
    public void setPhone(String phone){
        this.phone=phone;
    }
    
    public String getCompany(){
        return this.company;
    }
    public void setCompany(String company){
        this.company=company;
    }
    
    
    
    public String toString(){
        return this.company+", "+this.address.getCountry();
    }
}
