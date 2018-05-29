/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.TOMS.controller.CustomerOrders;

import com.project.TOMS.algorithms.dijkstra.controller.graphRegistryDatabase;
import com.project.TOMS.algorithms.dijkstra.model.Vertex;
import com.project.TOMS.controller.CustomerRecords.CustomerRegistryDatabase;
import com.project.TOMS.controller.DispositionRecords.DispositionRegistryDatabase;
import com.project.TOMS.controller.Vehicles.TrailerDatabaseRegistry;
import com.project.TOMS.controller.systemUserRecords.SystemUserRegistryDatabase;
import com.project.TOMS.database.Database;
import com.project.TOMS.model.CarrierRecords.Carrier;
import com.project.TOMS.model.CarrierRecords.Internal;
import com.project.TOMS.model.CustomerOrders.CustomerOrder;
import com.project.TOMS.model.CustomerOrders.Disposition;
import com.project.TOMS.model.CustomerRecords.Customer;
import com.project.TOMS.model.Drivers.Driver;
import com.project.TOMS.model.Vehicles.Trailer;
import com.project.TOMS.model.Vehicles.Truck;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import com.project.TOMS.model.systemUserRecords.SystemUser;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author EfthymiosChatziathanasiadis
 */
public class CustomerOrderRegistryDatabase {

    Database database = Database.getInstance();
    static CustomerOrderRegistryDatabase INSTANCE = null;
    ArrayList<CustomerOrder> customerOrders;
    CustomerRegistryDatabase customerDatabase = CustomerRegistryDatabase.getInstance();
    SystemUserRegistryDatabase disponentDatabase = SystemUserRegistryDatabase.getInstance();
    
    graphRegistryDatabase graphDatabase = graphRegistryDatabase.getInstance();
    
    private CustomerOrderRegistryDatabase(){
        this.loadOrders();
    }

    public static CustomerOrderRegistryDatabase getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CustomerOrderRegistryDatabase();
        }
        return INSTANCE;
    }
    public void configureAvailability() throws ParseException{
        DispositionRegistryDatabase dispositionDatabase = DispositionRegistryDatabase.getInstance();
        for (int i = 0 ; i < customerOrders.size(); i ++){
            CustomerOrder order = customerOrders.get(i);
            Trailer trailer = order.getTrailer();
            Date startDate = this.convertDate(order.getOrderDate());
            Date finishDate = this.convertDate(order.getDeliveryDate());
            Date currentDate = this.getCurrentTime();
            if(currentDate.after(startDate) && currentDate.before(finishDate)){
                //in between
                trailer.setAvailability(false);
            }else{
                trailer.setAvailability(true);
            }
            ArrayList<Disposition> orderDispositions = dispositionDatabase.getDispositionsOfOrder(order.getId());
            for(int j = 0; j < orderDispositions.size(); j ++){
                Disposition disposition = orderDispositions.get(j);
                Carrier carrier = disposition.getCarrier();
                if(carrier.getRole() instanceof Internal){
                    Driver driver = disposition.getDriver();
                    Truck truck = (Truck) disposition.getTruck();
                    Date dispoStartDate = this.convertDate(disposition.getStartDate());
                    Date dispoFinishDate = this.convertDate(disposition.getFinishDate());
                    if(currentDate.after(dispoStartDate) && currentDate.before(dispoFinishDate)){
                        driver.setAvailabilityStatus(false);
                        truck.setAvailability(false);
                    }else{
                        driver.setAvailabilityStatus(true);
                        truck.setAvailability(true);
                    }
                }
                
            }
        }
    }
    
    private Date convertDate(String dat) throws ParseException{
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date = format.parse(dat);
        return date;
    }
    private Date getCurrentTime(){
        Date todayDate = Calendar.getInstance().getTime();
       return todayDate;
    }
// TODO: create user defined exception.to handle integrity cases
    
  
    public void addCustomerOrder(String type,
            Vertex origin, Vertex destination,
            String orderDate, String deliveryDate, double kg, Trailer trailer, 
            double tarrif, Customer customer, SystemUser disponent) {
       

        String sql = "INSERT INTO CustomerOrder(type, orderDate, deliveryDate, kg, trailerId, tarrif, "
                + "customerId, systemUserId, originAddressId, destinationAddressId) VALUES "
                + "('"+type+"','"+orderDate+"','"+deliveryDate+"','"+kg+"','"+trailer.getId()+"','"+tarrif+""
                + "','"+customer.getId()+"','"+disponent.getId()+"','"+origin.getId()+"','"
                + ""+destination.getId()+"');";
        database.connect("central");
        try {
            database.update(sql);
            int orderId = database.getTableCount("CustomerOrder");
            CustomerOrder order = new CustomerOrder(orderId, type, origin, 
                    destination, orderDate, 
                    deliveryDate, kg, trailer, tarrif, customer, disponent);
            customerOrders.add(order);
        } catch (SQLException ex) {
            ex.printStackTrace();
            if (ex.getErrorCode() == 0) {

            }
        }
        database.closeConnection();
    }
 

    public CustomerOrder getCustomerOrderOfDisposition(int id) {
        if (customerOrders.isEmpty()) {
            this.getCustomerOrders();
        }
        CustomerOrder order = null;
        Iterator it = customerOrders.iterator();
        while (it.hasNext()) {
            order = (CustomerOrder) it.next();
            if (order.getId() == id) {
                break;
            }
        }
        return order;
    }
    public ArrayList<CustomerOrder> getCustomerOrdersOfTrailer(int id) throws ParseException{
         if (customerOrders.isEmpty()) {
            this.getCustomerOrders();
        }
        ArrayList<CustomerOrder> trailerCustomerOrders = new ArrayList<CustomerOrder>();
        for(int i = 0 ; i < customerOrders.size(); i++ ){
            CustomerOrder order = customerOrders.get(i);
            Trailer trailer = order.getTrailer();
            if(trailer.getId() == id){
                trailerCustomerOrders.add(order);
            }
        }
        return trailerCustomerOrders;
    }

    //TODO: 
    // A) First Get order details that do not require join
    // B) get Customer by joining with customer table on id: Develop in CustomerRegistryClass..DONE
    // C) get dispositions by joining with disposition table: Develop in DispositionRegiatryClass
    public void loadOrders()  {
        TrailerDatabaseRegistry trailerDatabase = TrailerDatabaseRegistry.getInstance();
        customerOrders = new ArrayList<CustomerOrder>();
        String sql = "SELECT * FROM CustomerOrder;";
        try {
            ResultSet rs;
            database.connect("central");
            rs = database.query(sql);

            while (rs.next()) {
                CustomerOrder order = new CustomerOrder(rs.getInt("id"), rs.getString("type"),
                        graphDatabase.getVertexWhereId(Integer.toString(rs.getInt("originAddressId"))), 
                        graphDatabase.getVertexWhereId(Integer.toString(rs.getInt("destinationAddressId"))), 
                        rs.getString("orderDate"),rs.getString("deliveryDate"), rs.getDouble("kg"), 
                        trailerDatabase.getTrailerOfOrder(rs.getInt("trailerId")),rs.getDouble("tarrif"), 
                        customerDatabase.getCustomerOfOrder(rs.getInt("customerId")),
                        disponentDatabase.getDisponentOfOrder(rs.getInt("systemUserId")));
                customerOrders.add(order);
            }

            database.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        
        
    }
    public ArrayList<CustomerOrder> getCustomerOrders()  {
        
        return this.computeCustomerOrderStatus();
        
    }

   
// TODO: create user defined exception.to handle integrity cases

    public void editCustomerOrder(CustomerOrder order, 
            Vertex origin, Vertex destination, String type,
            String orderDate, String deliveryDate, double kg, Trailer trailer,
            double tarrif, Customer customer, SystemUser disponent) {
        
        String sql = "UPDATE CustomerOrder SET type='"+type+"', orderDate='"+orderDate+"', deliveryDate='"+deliveryDate+"', "
                + "kg='"+kg+"', trailerId='"+trailer.getId()+"', tarrif='"+tarrif+"',customerId='"
                + ""+customer.getId()+"', systemUserId='"+disponent.getId()+"', "
                + "originAddressId='"+origin.getId()+"', destinationAddressId='"+destination.getId()+"' "
                + "WHERE id='"+order.getId()+"';";
        database.connect("central");
        try {
            database.update(sql);
            order.setType(type);
            order.setOrderDate(orderDate);
            order.setDeliveryDate(deliveryDate);
            order.setQuantity(kg);
            order.setTrailer(trailer);
            order.setTarrif(tarrif);
            order.setCustomer(customer);
            order.setDisponent(disponent);
            order.setOriginAddress(origin);
            order.setDestinationAddress(destination);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        database.closeConnection();
    }
    

    public void delete(CustomerOrder order) {
        String sql = "DELETE FROM CustomerOrder WHERE id='"+order.getId()+"';";
        database.connect("central");
        try {
            database.update(sql);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        database.closeConnection();
        this.loadOrders();
        DispositionRegistryDatabase.getInstance().loadDispositions();
    }
    //set status of each order i.e. routing complete or routing in progress
    public ArrayList<CustomerOrder> computeCustomerOrderStatus() {
        DispositionRegistryDatabase dispositionDatabase = DispositionRegistryDatabase.getInstance();
        for(int i = 0; i < customerOrders.size(); i++){
            CustomerOrder order = customerOrders.get(i);
            ArrayList<Disposition> dispositions = dispositionDatabase.
                    getDispositionsOfOrder(order.getId());
            if(dispositions.isEmpty()){
                order.setStatus("IN PROGRESS");
            }else{
                Vertex orderOrigin = order.getOriginAddress();
                Vertex orderDestination = order.getDestinationAddress();
                if(dispositions.size()==1){
                    Disposition disposition = dispositions.get(0);
                    Vertex dispositionOrigin = disposition.getOriginAddress();
                    Vertex dispositionDestination = disposition.getDestinationAddress();
                    if(dispositionOrigin == orderOrigin &&
                            dispositionDestination == orderDestination){
                        order.setStatus("ROUTING COMPLETE");
                    }else{
                        order.setStatus("IN PROGRESS");
                    }
                    
                }else{
                    //check dispo.first = order.first
                    //check for each dispo.first = dispo.prev.last
                    //check dispo.last = order.last
                    Disposition first = dispositions.get(0);
                    Vertex dispositionOrigin = first.getOriginAddress();
                    
                    if(dispositionOrigin == orderOrigin){
                        boolean inProgress=false;
                        Vertex dispositionPrevDestination = first.getDestinationAddress();
                        for(int j = 1; j < dispositions.size(); j++){
                            Disposition current = dispositions.get(j);
                            if(!(current.getOriginAddress() == dispositionPrevDestination)){
                                order.setStatus("IN PROGRESS");
                                inProgress = true;
                                break;
                            }
                            dispositionPrevDestination = current.getDestinationAddress();
                        }
                        Disposition last = dispositions.get(dispositions.size()-1);
                        if(last.getDestinationAddress() == orderDestination && !inProgress){
                            order.setStatus("ROUTING COMPLETE");
                        }else{
                            order.setStatus("IN PROGRESS");
                        }
                    
                    }else{
                        order.setStatus("IN PROGRESS");
                    }
                    
                }
            }
        }
        return this.customerOrders;
    }
    public CustomerOrder getCustomerOrdersWhereId(int id) throws ParseException{
        CustomerOrder order = null;
        ArrayList<CustomerOrder> orders = this.getCustomerOrders();
        for(int i = 0; i < orders.size(); i ++){
            order = orders.get(i);
            if(order.getId() == id){
                break;
            }
        }
        return order;
    }
    public ArrayList<CustomerOrder> getCustomerOrdersWhereTrailerPlateNumber(String TrailerPlateNumber) throws ParseException{
        CustomerOrder order = null;
        ArrayList<CustomerOrder> orders = this.getCustomerOrders();
        ArrayList<CustomerOrder> ordersWithTrailer = new ArrayList<CustomerOrder>();
        for(int i = 0; i < orders.size(); i ++){
            order = orders.get(i);
            Trailer trailer = order.getTrailer();
            String plateNumber = trailer.getPlateNumber();
            if(plateNumber.equals(TrailerPlateNumber)){
                ordersWithTrailer.add(order);
            }
        }
        return ordersWithTrailer;
    }
    public ArrayList<CustomerOrder> getCustomerOrdersWhereCustomerCompany(String inputCompany) throws ParseException{
        CustomerOrder order = null;
        ArrayList<CustomerOrder> orders = this.getCustomerOrders();
        ArrayList<CustomerOrder> ordersWithCustomer = new ArrayList<CustomerOrder>();
        for(int i = 0; i < orders.size(); i ++){
            order = orders.get(i);
            Customer customer = order.getCustomer();
            String company = customer.getCompany();
            if(company.equals(inputCompany)){
                ordersWithCustomer.add(order);
            }
        }
        return ordersWithCustomer;
    }
    public ArrayList<CustomerOrder> getCustomerOrdersWhereDisponent(String inputDisponentLastName) throws ParseException{
        CustomerOrder order = null;
        ArrayList<CustomerOrder> orders = this.getCustomerOrders();
        ArrayList<CustomerOrder> ordersWithDisponent = new ArrayList<CustomerOrder>();
        for(int i = 0; i < orders.size(); i ++){
            order = orders.get(i);
            SystemUser disponent = order.getDisponent();
            String lastName = disponent.getSurname();
            if(lastName.equals(inputDisponentLastName)){
                ordersWithDisponent.add(order);
            }
        }
        return ordersWithDisponent;
    }
    public ArrayList<CustomerOrder> getRoutedCustomerOrders() throws ParseException{
        CustomerOrder order = null;
        ArrayList<CustomerOrder> orders = this.getCustomerOrders();
        ArrayList<CustomerOrder> routedOrders = new ArrayList<CustomerOrder>();
        for(int i = 0; i < orders.size(); i ++){
            order = orders.get(i);
            String status = order.getStatus();
            if(status.equals("ROUTING COMPLETE")){
                routedOrders.add(order);
            }
        }
        return routedOrders;
    }
    public ArrayList<CustomerOrder> getOrdersInProgressCustomerOrders() throws ParseException{
        CustomerOrder order = null;
        ArrayList<CustomerOrder> orders = this.getCustomerOrders();
        ArrayList<CustomerOrder> ordersInProgress = new ArrayList<CustomerOrder>();
        for(int i = 0; i < orders.size(); i ++){
            order = orders.get(i);
            String status = order.getStatus();
            if(status.equals("IN PROGRESS")){
                ordersInProgress.add(order);
            }
        }
        return ordersInProgress;
    }
    public ArrayList<CustomerOrder> getImportCustomerOrders() throws ParseException{
        CustomerOrder order = null;
        ArrayList<CustomerOrder> orders = this.getCustomerOrders();
        ArrayList<CustomerOrder> importOrders = new ArrayList<CustomerOrder>();
        for(int i = 0; i < orders.size(); i ++){
            order = orders.get(i);
            String type = order.getType();
            if(type.equals("Import")){
                importOrders.add(order);
            }
        }
        return importOrders;
    }
    public ArrayList<CustomerOrder> getExportCustomerOrders() throws ParseException{
        CustomerOrder order = null;
        ArrayList<CustomerOrder> orders = this.getCustomerOrders();
        ArrayList<CustomerOrder> exportOrders = new ArrayList<CustomerOrder>();
        for(int i = 0; i < orders.size(); i ++){
            order = orders.get(i);
            String type = order.getType();
            if(type.equals("Export")){
                exportOrders.add(order);
            }
        }
        return exportOrders;
    }
   

}
