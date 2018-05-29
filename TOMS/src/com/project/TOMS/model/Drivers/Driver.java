/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.TOMS.model.Drivers;
import com.project.TOMS.algorithms.dijkstra.model.Vertex;
import com.project.TOMS.model.Address.Address;
/**
 *
 * @author EfthymiosChatziathanasiadis
 */
public class Driver {
    int id;
    String firstName;
    String lastName;
    boolean availabilityStatus;
    Vertex address;
    
    public Driver(int id, String firstName, String lastName, boolean availabilityStatus, Vertex address){
        this.id=id;
        this.firstName=firstName;
        this.lastName=lastName;
        this.availabilityStatus=availabilityStatus;
        this.address=address;
    }
    
    public int getId(){
        return this.id;
    }
    
    public Vertex getAddress(){
        return this.address;
    }
    public String getLocation(){
        return this.address.getCity()+", "+this.address.getCountry();
    }
    public boolean getAvailabilityStatus(){
        return this.availabilityStatus;
    }
    public void setAvailabilityStatus(boolean availability){
        this.availabilityStatus=availability;
    }
    public String getFirstName(){
        return this.firstName;
    }
    public void setFirstName(String name){
        this.firstName=name;
    }
    
    public String getLastName(){
        return this.lastName;
    }
    public void setLastName(String last){
        this.lastName=last;
    }
    @Override
    public String toString(){
        return this.lastName+", "+this.firstName;
    }

    public void setAddress(Vertex address) {
        this.address = address; 
    }
}
