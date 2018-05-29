 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.TOMS.model.CustomerOrders;

import com.project.TOMS.algorithms.dijkstra.model.Vertex;
import com.project.TOMS.model.CustomerRecords.Customer;
import java.util.ArrayList;
import com.project.TOMS.model.systemUserRecords.SystemUser;
import com.project.TOMS.model.Address.Address;
import com.project.TOMS.model.Vehicles.Trailer;

/**
 *
 * @author EfthymiosChatziathanasiadis
 */
public class CustomerOrder {
    int id;
    String type;
    Vertex originAddress;
    Vertex destinationAddress;
    String orderDate;
    String deliveryDate;
    double kg;
    Trailer trailer;
    double tarrif;
    Customer customer;
    SystemUser disponent;
    String status;
    
    
 
    
    /*public CustomerOrder(int id, String type, String orderDate, String deliveryDate, double quantity, String plateNumber, double tarrif, Customer customer, SystemUser disponent  ){
        this.id=id;
        this.type=type;
        this.orderDate=orderDate;
        this.deliveryDate=deliveryDate;
        this.kg=quantity;
        this.trailer=trailer;
        this.tarrif=tarrif;
        this.customer=customer;
        this.disponent=disponent;
        
    }*/
    public CustomerOrder(int id, String type, Vertex originAddress, Vertex destinationAddess,
            String orderDate, String deliveryDate, double quantity, Trailer trailer, 
            double tarrif, Customer customer, SystemUser disponent ){
        this.id=id;
        this.type=type;
        this.originAddress=originAddress;
        this.destinationAddress=destinationAddess;
        this.orderDate=orderDate;
        this.deliveryDate=deliveryDate;
        this.kg=quantity;
        this.trailer=trailer;
        this.tarrif=tarrif;
        this.customer=customer;
        this.disponent=disponent;
        
    }
    public String getStatus(){
        return this.status;
    }
    public void setStatus(String status){
        this.status = status;
    }
    public Trailer getTrailer(){
        return this.trailer;
    }
    public String getTrailerPlate(){
        return this.trailer.getPlateNumber();
    }
    public void setTrailer(Trailer trailer){
        this.trailer = trailer;
    }
    
    public String getType(){
        return this.type;
    }
    public void setType(String type){
        this.type=type;
    }
  
    public void setOriginAddress(Vertex address){
        this.originAddress=address;
    }
    public Vertex getOriginAddress(){
        return this.originAddress;
    }
    public String getOrigin(){
        return this.originAddress.getCity()+", "+this.originAddress.getCountry();
    }
    
    public void setDestinationAddress(Vertex address){
        this.destinationAddress=address;
    }
    public Vertex getDestinationAddress(){
        return this.destinationAddress;
    }
    public String getDestination(){
        return this.destinationAddress.getCity()+", "+this.destinationAddress.getCountry();
    }
    
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id=id;
    }
    
    public String getOrderDate(){
        return this.orderDate;
    }
    public void setOrderDate(String date){
        this.orderDate=date;
    }
    
    public String getDeliveryDate(){
        return this.deliveryDate;
    }
    public void setDeliveryDate(String date){
        this.deliveryDate=date;
    }
    
    public double getQuantity(){
        return this.kg;
    }
    public void setQuantity(double quantity){
        this.kg=quantity;
    }
   
    
    public double getTarrif(){
        return this.tarrif;
    }
    public void setTarrif(double tarrif){
        this.tarrif=tarrif;
    }
    
    public Customer getCustomer(){
        return this.customer;
    }
    public String getCustomerCompany(){
        return this.customer.getCompany();
    }
    public void setCustomer(Customer customer){
        this.customer=customer;
    }
    
    public SystemUser getDisponent(){
        return disponent;
    }
    public String getDisponentSurname(){
        return this.disponent.getSurname();
    }
    public void setDisponent(SystemUser user){
        this.disponent=user;
    }
}
