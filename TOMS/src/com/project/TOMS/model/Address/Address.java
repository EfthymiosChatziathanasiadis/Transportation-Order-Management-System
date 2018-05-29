/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.TOMS.model.Address;

/**
 *
 * @author EfthymiosChatziathanasiadis
 */
public class Address {
    int id;
    String street;
    String postCode;
    String city;
    String country;

    
    public Address( int id,  String street, String postCode, String city, String country){
        this.id=id;
        this.street=street;
        this.postCode=postCode;
        this.city=city;
        this.country=country;
    }
    
    public void setId(int id){
        this.id=id;
    }
    public int getId(){
        return this.id;
    }
    
    public void setStreet(String street){
        this.street=street;
    }
    public String getStreet(){
        return this.street;
    }
    
    public void setPostCode(String code){
        this.postCode=code;
    }
    public String getPostCode(){
        return this.postCode;
    }
    
    public void setCity(String city){
        this.city=city;
    }
    public String getCity(){
        return this.city;
    }
    
    public void setCountry(String country){
        this.country=country;
    }
    public String getCountry(){
        return this.country;
    }
}
