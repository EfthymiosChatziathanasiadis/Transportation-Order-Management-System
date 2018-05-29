/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.TOMS.model.world;

/**
 *
 * @author EfthymiosChatziathanasiadis
 */
public class Country {
    String country;
    public Country(String country){
        this.country = country;
    }
    
    public String getCountry(){
        return this.country;
    }
    @Override
    public String toString(){
        return this.country;
    }
   
}
