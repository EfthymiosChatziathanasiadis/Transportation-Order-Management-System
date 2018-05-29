/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.TOMS.model.CustomerOrders;

import com.project.TOMS.algorithms.dijkstra.model.Vertex;
import com.project.TOMS.model.CarrierRecords.Carrier;
import com.project.TOMS.model.Drivers.Driver;
import com.project.TOMS.model.Vehicles.Vehicle;

/**
 *
 * @author EfthymiosChatziathanasiadis
 */
public class Disposition {
    int id;
    Carrier carrier;
    CustomerOrder order;
    Vertex originAddress;
    Vertex destinationAddress;
    String startDate;
    String finishDate;
    double tarrif;
    Vehicle truck;
    Driver driver;
    
   
    public Disposition(int id, Carrier carrier, Vertex originAddress, Vertex destinationAddress, 
            String startDate, String finishDate, double tarrif, CustomerOrder order){
        this.id=id;
        this.carrier=carrier;
        this.originAddress=originAddress;
        this.destinationAddress=destinationAddress;
        this.order=order;    
        this.startDate=startDate;
        this.finishDate=finishDate;
        this.tarrif=tarrif;
             
    }
    public Disposition(int id, Carrier carrier, Vertex originAddress, Vertex destinationAddress, String startDate, String finishDate, 
            double tarrif, CustomerOrder order, Driver driver, Vehicle truck){
        this.id=id;
        this.carrier=carrier;
        this.originAddress=originAddress;
        this.destinationAddress=destinationAddress;
        this.order=order;    
        this.startDate=startDate;
        this.finishDate=finishDate;
        this.tarrif=tarrif;
        this.driver=driver;
        this.truck=truck;
        
             
    }
    
    public Vehicle getTruck(){
        return this.truck;
    }
  
    public String getTruckPlateNumber(){
        if(truck == null){
            return "N/A";
        }else{
            return this.truck.getPlateNumber();
        }
    }
   
    public void setTruck(Vehicle truck){
        this.truck=truck;
    }
    
    public Driver getDriver(){
        return this.driver;
    }
    public String getDriverId(){
        if(driver == null){
            return "N/A";
        }else{
            return this.driver.getId()+"";
        }
        
    }
    public String getDriverSurname(){
        if(driver == null){
            return "N/A";
        }else{
            return this.driver.getLastName();
        }
        
    }
    public void setDriver(Driver driver){
        this.driver=driver;
    }
    
    
    public Vertex getOriginAddress(){
        return this.originAddress;
    }
    
    
    public Vertex getDestinationAddress(){
        return this.destinationAddress;
    }
    
    
    public int getId(){
        return this.id;
    }
    public void setId(int id){
        this.id=id;
    }
    public Carrier getCarrier(){
        return this.carrier;
    }
    public void setCarrier(Carrier carrier){
        this.carrier=carrier;
    }
    public String getCarrierRole(){
        return this.carrier.getCarrierRole();
    }
    
    public CustomerOrder getCustomerOrder(){
        return this.order;
    }
    public void setCustomerOrder(CustomerOrder order){
        this.order=order;
    }
    
    public String getCarrierCompany(){
        return this.carrier.getCompany();
    }
    public String getCarrierMode(){
        return this.carrier.getCarrierMode();
    }
  
    public String getOrigin(){
        return this.originAddress.getCity()+", "+this.originAddress.getCountry();
    }
    public void setOrigin(Vertex origin){
        this.originAddress=origin;
    }
    
    public String getDestination(){
        return this.destinationAddress.getCity()+", "+this.destinationAddress.getCountry();
    }
    public void setDestination(Vertex destination){
        this.destinationAddress=destination;
    }
    
    public String getStartDate(){
        return this.startDate;
    }
    public void setStartDate(String date){
        this.startDate=date;
    }
    
    public String getFinishDate(){
        return this.finishDate;
    }
    public void setFinishDate(String date){
        this.finishDate=date;
    }
    
    public double getTarrif(){
        return this.tarrif;
    }
    public void setTarrif(double tarrif){
        this.tarrif=tarrif;
    }
    
    
   
   
}
