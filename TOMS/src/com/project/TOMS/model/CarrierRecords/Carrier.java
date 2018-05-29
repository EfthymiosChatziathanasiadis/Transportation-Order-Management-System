/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.TOMS.model.CarrierRecords;

import java.util.ArrayList;

/**
 *
 * @author EfthymiosChatziathanasiadis
 */
public class Carrier {
    int id;
    String company;
    Mode mode;
    CarrierRole role;
    
   public Carrier(int id, String company, Mode mode, CarrierRole role){
       this.id=id;
       this.company=company;
       this.mode=mode;
       this.role=role;
   }
   public CarrierRole getRole(){
       return this.role;
   }
   public String getCarrierRole(){
       if(role instanceof Internal)return "Internal";
       else return "External";
   }
   public void setRole(CarrierRole role){
       this.role=role;
   }
   
   public int getId(){
       return this.id;
   }
   public void setId(int id){
       this.id=id;
   }
   
   public String getCompany(){
       return this.company;
   }
   public void setCompany(String company){
       this.company=company;
   }
   
   public Mode getMode(){
       return mode;
   }
   public String getCarrierMode(){
       if(this.mode instanceof Road){
           return "Road";
       }else if(this.mode instanceof Sea){
           return "Sea";
       }else{
           return "Rail";
       }
   }
   public void setMode(Mode mode){
       this.mode=mode;
   }
   @Override
   public String toString(){
       return this.company+", "+this.getCarrierMode();
   }
   
  
}
