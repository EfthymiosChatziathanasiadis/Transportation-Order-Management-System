/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.TOMS.model.Vehicles;

import com.project.TOMS.algorithms.dijkstra.model.Vertex;


/**
 *
 * @author EfthymiosChatziathanasiadis
 */
public class Vehicle {
    int id;
    boolean availabilityStatus;
    boolean conditionStatus;
    String plateNumber;
    Vertex address;
    
    public Vehicle(int id, boolean availabilityStatus, String plateNumber, boolean conditionStatus, Vertex address){
        this.id=id;
        this.availabilityStatus=availabilityStatus;
        this.plateNumber=plateNumber;
        this.conditionStatus=conditionStatus;
        this.address=address;
    }
    
    public int getId(){
       return this.id;
    }
    public void setId(int id){
        this.id=id;
    }
    
    public String getLocation(){
        return this.address.getCity()+", "+this.address.getCountry();
    }
    public Vertex getAddress(){
        return this.address;
    }
   
    public void setAddress(Vertex address){
        this.address=address;
    }
    
    public boolean getAvailabilityStatus(){
        return this.availabilityStatus;
    }
    public String getAvailability(){
        if(availabilityStatus){
            return "Available";
        }else{
            return "Unavailable";
        }
    }
     
    public void setAvailability(boolean status){
        this.availabilityStatus=status;
    }
    
    public String getPlateNumber(){
        return this.plateNumber;
    }
    public void setPlateNumber(String plateNumber){
        this.plateNumber=plateNumber;
    }
    
    public boolean getConditionStatus(){
        return this.conditionStatus;
    }
    public String getCondition(){
        if(conditionStatus){
            return "Damaged";
        }else{
            return "Not Damaged";
        }
    }
    public void setCondition(boolean status){
        this.conditionStatus=status;
    }
    public String toString(){
        return this.plateNumber;
    }
}
