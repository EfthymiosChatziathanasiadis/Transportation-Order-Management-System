/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.TOMS.controller.Vehicles;

import com.project.TOMS.algorithms.dijkstra.controller.graphRegistryDatabase;
import com.project.TOMS.algorithms.dijkstra.model.Vertex;
import com.project.TOMS.controller.CustomerOrders.CustomerOrderRegistryDatabase;
import com.project.TOMS.controller.DispositionRecords.DispositionRegistryDatabase;
import com.project.TOMS.database.Database;
import com.project.TOMS.model.CustomerOrders.CustomerOrder;
import com.project.TOMS.model.CustomerOrders.Disposition;
import com.project.TOMS.model.Drivers.Driver;
import com.project.TOMS.model.Vehicles.Trailer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author EfthymiosChatziathanasiadis
 */
public class TrailerDatabaseRegistry {
    Database database = Database.getInstance();
    static TrailerDatabaseRegistry INSTANCE = null;
    ArrayList<Trailer> trailers = new ArrayList<Trailer>();
    graphRegistryDatabase graphDatabase = graphRegistryDatabase.getInstance();
    
    private TrailerDatabaseRegistry() {
        this.loadTrailers();
    }

    public static TrailerDatabaseRegistry getInstance(){
        if (INSTANCE == null) {
            INSTANCE = new TrailerDatabaseRegistry();
        }
        return INSTANCE;
    }
// TODO: create user defined exception.to handle integrity cases

    public void addTrailer(Vertex address, boolean availabilityStatus, String plateNumber,
            boolean conditionStatus) {
        String sql = "INSERT INTO Trailer(availabilityStatus, plateNumber, conditionStatus, addressId"
                + ") VALUES ('"+(availabilityStatus?1:0)+"','"+plateNumber+"',"
                + "'"+(conditionStatus?1:0)+"','"+address.getId()+"');";
        database.connect("central");
        try {
            database.update(sql);
            int trailerId = database.getTableCount("Trailer");
            Trailer trailer = new Trailer(trailerId, availabilityStatus, plateNumber, conditionStatus, address);
            trailers.add(trailer);
        } catch (SQLException ex) {
            ex.printStackTrace();
            if (ex.getErrorCode() == 0) {

            }
        }
        database.closeConnection();
    }

    public Trailer getTrailerOfOrder(int id) {
        Trailer trailer = null;
        if (trailers.isEmpty()) {
            this.getTrailers();
        }
        Iterator<Trailer> it = trailers.iterator();
        while (it.hasNext()) {
            trailer = it.next();
            if (trailer.getId() == id) {
                break;
            }
        }

        return trailer;
    }
    private void loadTrailers(){
        if (trailers.isEmpty()) {
            trailers = new ArrayList<Trailer>();
            String sql = "SELECT id, availabilityStatus, plateNumber, conditionStatus, addressId FROM Trailer;";
            try {
                ResultSet rs;
                database.connect("central");
                rs = database.query(sql);

                while (rs.next()) {
                    Trailer trailer = new Trailer(rs.getInt("id"), rs.getInt("availabilityStatus")==0 ? false : true, rs.getString("plateNumber"),
                            rs.getInt("conditionStatus")==0 ? false : true, 
                            graphDatabase.getVertexWhereId(Integer.toString(rs.getInt("addressId"))));
                    trailers.add(trailer);

                }
                database.closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
    }
    public ArrayList<Trailer> getTrailers(){
        this.cofigureTrailerLocations();
        return trailers;
    }
    public void cofigureTrailerLocations() {
        CustomerOrderRegistryDatabase orderDatabase = CustomerOrderRegistryDatabase.getInstance();
        for(int i = 0; i < trailers.size(); i++){
            try {
                Trailer trailer = trailers.get(i);
                ArrayList<CustomerOrder> trailerOrders = orderDatabase.getCustomerOrdersOfTrailer(trailer.getId());
                if(trailerOrders.isEmpty()){
                    //nothing
                }else if(isCurrentlyExecutingCustomerOrder(trailer, trailerOrders)){
                    CustomerOrder order = getCurrentCustomerOrder(trailer, trailerOrders);
                    Vertex destination = order.getDestinationAddress();
                    trailer.setAddress(destination);
                }else if(hasPastCustomerOrder(trailer, trailerOrders)){
                    CustomerOrder disposition = getLatestPastCustomerOrder(trailer, trailerOrders);
                    Vertex destination = disposition.getDestinationAddress();
                    trailer.setAddress(destination);
                }else{
                    //nothing
                }
            } catch (ParseException ex) {
                Logger.getLogger(TrailerDatabaseRegistry.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
    }
    private boolean isCurrentlyExecutingCustomerOrder(Trailer driver, ArrayList<CustomerOrder> trailerCustomerOrders) throws ParseException{
        boolean isExecuting = false;
        Date currentDate = this.getCurrentTime();
        for(int i = 0; i < trailerCustomerOrders.size(); i++){
            CustomerOrder disposition = trailerCustomerOrders.get(i);
            Date dispoStartDate = this.convertDate(disposition.getOrderDate());
            Date dispoFinishDate = this.convertDate(disposition.getDeliveryDate());
            if(currentDate.after(dispoStartDate) && currentDate.before(dispoFinishDate)){
                 isExecuting = true;
                 break;
            }
        }
        return isExecuting;
    }
    private boolean hasPastCustomerOrder(Trailer trailer, ArrayList<CustomerOrder> trailerCustomerOrders) throws ParseException{
        Date currentDate = this.getCurrentTime();
        boolean hasPastDispositions = false;
        for(int i = 0; i < trailerCustomerOrders.size(); i++){
            CustomerOrder order = trailerCustomerOrders.get(i);
            Date dispoFinishDate = this.convertDate(order.getDeliveryDate());
            if(currentDate.after(dispoFinishDate)){
                hasPastDispositions = true;
                break;
            }
        }
        return hasPastDispositions;
    }
    private CustomerOrder getCurrentCustomerOrder(Trailer trailer, ArrayList<CustomerOrder> trailerCustomerOrders) throws ParseException{
        Date currentDate = this.getCurrentTime();
        CustomerOrder currentOrder = null;
        for(int i = 0; i < trailerCustomerOrders.size(); i++){
            CustomerOrder order = trailerCustomerOrders.get(i);
            Date dispoStartDate = this.convertDate(order.getOrderDate());
            Date dispoFinishDate = this.convertDate(order.getDeliveryDate());
            if(currentDate.after(dispoStartDate) && currentDate.before(dispoFinishDate)){
                currentOrder = order;
                break;
            }
        }
        return currentOrder;
    }
    private CustomerOrder getLatestPastCustomerOrder(Trailer driver, ArrayList<CustomerOrder> trailerCustomerOrders) throws ParseException{
        Date currentDate = this.getCurrentTime();
        CustomerOrder latestPastOrder = null;
        Date latestPastDate = veryPastDate();
        for(int i = 0; i < trailerCustomerOrders.size(); i++){
            CustomerOrder order = trailerCustomerOrders.get(i);
            Date dispoFinishDate = this.convertDate(order.getDeliveryDate());
            if(currentDate.after(dispoFinishDate)){
                if(dispoFinishDate.after(latestPastDate)){
                    latestPastDate = dispoFinishDate;
                    latestPastOrder = order;
                }
            }
        }
        return latestPastOrder;
    }
    private Date veryPastDate() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1000);
        return cal.getTime();
    }
    private Date getCurrentTime(){
        Date todayDate = Calendar.getInstance().getTime();
       return todayDate;
    }
    
    public ArrayList<Trailer> getAvailableTrailers(Date newStart, Date newFinish, 
            Vertex newOrderLocation) throws ParseException{
        
        CustomerOrderRegistryDatabase orderDatabase = 
                CustomerOrderRegistryDatabase.getInstance();
        this.getTrailers();//load trailers
        ArrayList<Trailer> availableTrailers = new ArrayList<Trailer>();
        ArrayList<Trailer> trailersNearBy = this.getTrailersNearBy(orderDatabase, newOrderLocation, newStart);
        for(int i = 0; i < trailersNearBy.size(); i++){
            Trailer trailer = trailersNearBy.get(i);
            ArrayList<CustomerOrder> trailerOrders = orderDatabase.getCustomerOrdersOfTrailer(trailer.getId());
            boolean conflict = false;
            for(int j = 0 ; j < trailerOrders.size(); j ++){
                CustomerOrder order = trailerOrders.get(j);
                Date orderStartDate = this.convertDate(order.getOrderDate());
                Date orderFinishDate = this.convertDate(order.getDeliveryDate());
                if( ((orderStartDate.before(newStart) || (orderStartDate.equals(newStart))) && ((orderFinishDate.after(newFinish)) || orderFinishDate.equals(newFinish))) || 
                    ((orderStartDate.before(newStart) || (orderStartDate.equals(newStart))) && (orderFinishDate.after(newStart) || orderFinishDate.equals(newStart) )) ||
                    ((orderStartDate.after(newStart) || orderStartDate.equals(newStart)) && (orderStartDate.before(newFinish)) || orderStartDate.equals(newFinish) ) ||
                    (orderStartDate.after(newStart) || orderStartDate.equals(newStart)) && (orderStartDate.before(newFinish) || orderStartDate.equals(newFinish)) && 
                     (orderFinishDate.before(newFinish) || orderFinishDate.equals(newFinish) )){
                    conflict = true;
                    break;
                }
            }
            if(!conflict){
               availableTrailers.add(trailer);
            }
        }
        return availableTrailers;
    }
    private Date convertDate(String dat) throws ParseException{
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date = format.parse(dat);
        return date;
    }
    

// TODO: create user defined exception.to handle integrity cases
    public void editTrailer(Trailer trailer, Vertex address, 
            boolean availabilityStatus, String plateNumber,
            boolean conditionStatus) {
        String sql = "UPDATE Trailer SET  availabilityStatus='"+(availabilityStatus?1:0)+"', "
                + "plateNumber='"+plateNumber+"', conditionStatus='"+(conditionStatus?1:0)+"', addressId='"+address.getId()+"'  WHERE id='"+trailer.getId()+"';";
        database.connect("central");
        try {
            database.update(sql);
            trailer.setAvailability(availabilityStatus);
            trailer.setPlateNumber(plateNumber);
            trailer.setCondition(conditionStatus);
            trailer.setAddress(address);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        database.closeConnection();
    }

    public void delete(Trailer trailer) {
        trailers.remove(trailer);
        String sql = "DELETE FROM Trailer WHERE id='"+trailer.getId()+"';";
        database.connect("central");
        try {
            database.update(sql);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        database.closeConnection();
    }

    public ArrayList<Trailer> getAvailableTrailers(Date newStart, Date newFinish, CustomerOrder orderr, Vertex sourceVertex) throws ParseException {
        CustomerOrderRegistryDatabase orderDatabase = CustomerOrderRegistryDatabase.getInstance();
        this.getTrailers();//load trailers
        ArrayList<Trailer> availableTrailers = new ArrayList<Trailer>();
        ArrayList<Trailer> trailersNearBy = getTrailersNearBy(orderDatabase, sourceVertex, newStart);
        for(int i = 0; i < trailersNearBy.size(); i++){
            Trailer trailer = trailersNearBy.get(i);
            ArrayList<CustomerOrder> trailerOrders = orderDatabase.getCustomerOrdersOfTrailer(trailer.getId());
            boolean conflict = false;
            
            for(int j = 0 ; j < trailerOrders.size(); j ++){
                CustomerOrder order = trailerOrders.get(j);
                
                if(order == orderr){
                    conflict = false;
                }else{
                    Date orderStartDate = this.convertDate(order.getOrderDate());
                    Date orderFinishDate = this.convertDate(order.getDeliveryDate());
                    if( ((orderStartDate.before(newStart) || (orderStartDate.equals(newStart))) && ((orderFinishDate.after(newFinish)) || orderFinishDate.equals(newFinish))) || 
                        ((orderStartDate.before(newStart) || (orderStartDate.equals(newStart))) && (orderFinishDate.after(newStart) || orderFinishDate.equals(newStart) )) ||
                        ((orderStartDate.after(newStart) || orderStartDate.equals(newStart)) && (orderStartDate.before(newFinish)) || orderStartDate.equals(newFinish) ) ||
                        (orderStartDate.after(newStart) || orderStartDate.equals(newStart)) && (orderStartDate.before(newFinish) || orderStartDate.equals(newFinish)) && 
                         (orderFinishDate.before(newFinish) || orderFinishDate.equals(newFinish) )){
                        conflict = true;
                        break;
                    }
                }
            }
            if(!conflict){
               availableTrailers.add(trailer);
            }
        }
        return availableTrailers;
    }
    
    private ArrayList<Trailer> getTrailersNearBy(CustomerOrderRegistryDatabase instance, 
            Vertex newOrderLocation, Date startDate) throws ParseException {
        ArrayList<Trailer> trailersNearBy = new ArrayList<Trailer>();
        for(int i = 0; i < trailers.size(); i++){
            Trailer trailer = trailers.get(i);
            ArrayList<CustomerOrder> trailerOrders = 
                    instance.getCustomerOrdersOfTrailer(trailer.getId());
            Vertex trailerLocation = null;
            if(trailerOrders.isEmpty() || getLatestOrderAssigned(trailerOrders, startDate) == null)
                trailerLocation = trailer.getAddress();
            else{
                CustomerOrder latestOrder = getLatestOrderAssigned(trailerOrders, startDate);
                trailerLocation = latestOrder.getDestinationAddress();
            }
            if(newOrderLocation == trailerLocation)
                trailersNearBy.add(trailer);
            
        }
        return trailersNearBy;
    }

    private CustomerOrder getLatestOrderAssigned(ArrayList<CustomerOrder> trailerOrders, Date startDate) throws ParseException {
        
        ArrayList<CustomerOrder> latestOrders = new ArrayList<CustomerOrder>();
        for(int i = 0; i < trailerOrders.size(); i ++){
            CustomerOrder order = trailerOrders.get(i);
            Date current = this.convertDate(order.getDeliveryDate());
            if(current.before(startDate)){
               latestOrders.add(order);
            }
        }
        CustomerOrder latestOrder = null;
        Date latestDate = this.veryPastDate();
        for(int i = 0; i < latestOrders.size(); i ++){
            CustomerOrder order = latestOrders.get(i);
            Date current = this.convertDate(order.getDeliveryDate());
            if(current.after(latestDate)){
                latestDate = current;
                latestOrder = order;
            }
        }
        return latestOrder;
    }
}
