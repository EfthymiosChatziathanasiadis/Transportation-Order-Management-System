/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.TOMS.controller.Vehicles;

import com.project.TOMS.algorithms.dijkstra.controller.graphRegistryDatabase;
import com.project.TOMS.algorithms.dijkstra.model.Vertex;
import com.project.TOMS.controller.DispositionRecords.DispositionRegistryDatabase;
import com.project.TOMS.database.Database;
import com.project.TOMS.model.CustomerOrders.Disposition;
import com.project.TOMS.model.Drivers.Driver;
import com.project.TOMS.model.Vehicles.Truck;
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
public class TruckDatabaseRegistry {
    Database database = Database.getInstance();
    static TruckDatabaseRegistry INSTANCE = null;
    ArrayList<Truck> trucks = new ArrayList<Truck>();
    graphRegistryDatabase graphDatabase = graphRegistryDatabase.getInstance();
   

    private TruckDatabaseRegistry() {
        this.loadTrucks();
    }

    public static TruckDatabaseRegistry getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TruckDatabaseRegistry();
        }
        return INSTANCE;
    }
// TODO: create user defined exception.to handle integrity cases

    public void addTruck(Vertex address, boolean availabilityStatus, String plateNumber, 
            boolean conditionStatus) {
        String sql = "INSERT INTO Truck(availabilityStatus, plateNumber, conditionStatus, addressId"
                + ") VALUES ('"+(availabilityStatus?1:0)+"','"+plateNumber+"',"
                + "'"+(conditionStatus?1:0)+"','"+address.getId()+"');";
        database.connect("central");
        try {
            database.update(sql);
            int truckId = database.getTableCount("Truck");
            Truck truck = new Truck(truckId, availabilityStatus, plateNumber, 
                    conditionStatus, graphDatabase.getVertexWhereId(address.getId()));
            trucks.add(truck);
        } catch (SQLException ex) {
            ex.printStackTrace();
            if (ex.getErrorCode() == 0) {

            }
        }
        database.closeConnection();
    }

    public Truck getTruckOfDisposition(int id)  {
        Truck truck = null;
        if (trucks.isEmpty()) {
            this.getTrucks();
        }
        Iterator<Truck> it = trucks.iterator();
        while (it.hasNext()) {
            truck = it.next();
            if (truck.getId() == id) {
                break;
            }
        }

        return truck;
    }

    private void loadTrucks()  {
        if (trucks.isEmpty()) {
            String sql = "SELECT * FROM Truck;";
            try {
                ResultSet rs;
                database.connect("central");
                rs = database.query(sql);

                while (rs.next()) {
                    Truck truck = new Truck(rs.getInt("id"), rs.getInt("availabilityStatus")==0 ? false : true, 
                            rs.getString("plateNumber"), rs.getInt("conditionStatus")==0 ? false : true, 
                            graphDatabase.getVertexWhereId(Integer.toString(rs.getInt("addressId"))));
                    trucks.add(truck);
                }

                database.closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
    }
    public ArrayList<Truck> getTrucks()  {
        
        this.cofigureTruckLocations();
        return trucks;
    }
    public void cofigureTruckLocations() {
         DispositionRegistryDatabase dispositionsDatabase = DispositionRegistryDatabase.getInstance();
        for(int i = 0; i < trucks.size(); i++){
             try {
                 Truck truck = trucks.get(i);
                 ArrayList<Disposition> truckDispositions = dispositionsDatabase.getDispositionsOfTruck(truck.getId());
                 if(truckDispositions.isEmpty()){
                     //nothing
                      System.out.println(truck.getPlateNumber()+" HAS NO DISPOSITIONS");
                 }else if(isCurrentlyExecutingDisposition(truck, truckDispositions)){
                     Disposition disposition = getCurrentDisposition(truck, truckDispositions);
                     Vertex destination = disposition.getDestinationAddress();
                     truck.setAddress(destination);
                      System.out.println(truck.getPlateNumber()+" IS CURRENTLY EXECUTING, ADDRESS: "+destination.getCity()+" origin"+disposition.getOriginAddress().getCity());
                 }else if(hasPastDispositions(truck, truckDispositions)){
                     System.out.println(truck.getPlateNumber()+" IS CURRENTLY IS NOT EXECUTING. HE HAS PAST DISPO ");
                     Disposition disposition = getLatestPastDisposition(truck, truckDispositions);
                     Vertex destination = disposition.getDestinationAddress();
                     truck.setAddress(destination);
                     System.out.println(truck.getPlateNumber()+" IS CURRENTLY EXECUTING, ADDRESS: "+destination.getCity());
                 }else{
                     //nothing
                 }
             } catch (ParseException ex) {
                 Logger.getLogger(TruckDatabaseRegistry.class.getName()).log(Level.SEVERE, null, ex);
             }
            
        }
        
    }
    private boolean isCurrentlyExecutingDisposition(Truck truck, ArrayList<Disposition> truckDispositions) throws ParseException{
        boolean isExecuting = false;
        Date currentDate = this.getCurrentTime();
        for(int i = 0; i < truckDispositions.size(); i++){
            Disposition disposition = truckDispositions.get(i);
            Date dispoStartDate = this.convertDate(disposition.getStartDate());
            Date dispoFinishDate = this.convertDate(disposition.getFinishDate());
            if(currentDate.after(dispoStartDate) && currentDate.before(dispoFinishDate)){
                 isExecuting = true;
                 break;
            }
        }
        return isExecuting;
    }
    private boolean hasPastDispositions(Truck truck, ArrayList<Disposition> truckDispositions) throws ParseException{
        Date currentDate = this.getCurrentTime();
        boolean hasPastDispositions = false;
        for(int i = 0; i < truckDispositions.size(); i++){
            Disposition disposition = truckDispositions.get(i);
            Date dispoFinishDate = this.convertDate(disposition.getFinishDate());
            if(currentDate.after(dispoFinishDate)){
                hasPastDispositions = true;
                //System.out.println("HAS PAST DISPO: ADDRESS"+disposition.getDestinationAddress().getCity());
                break;
            }
        }
        return hasPastDispositions;
    }
    private Disposition getCurrentDisposition(Truck truck, ArrayList<Disposition> truckDispositions) throws ParseException{
        Date currentDate = this.getCurrentTime();
        Disposition currentDisposition = null;
        for(int i = 0; i < truckDispositions.size(); i++){
            Disposition disposition = truckDispositions.get(i);
            Date dispoStartDate = this.convertDate(disposition.getStartDate());
            Date dispoFinishDate = this.convertDate(disposition.getFinishDate());
            if(currentDate.after(dispoStartDate) && currentDate.before(dispoFinishDate)){
                currentDisposition = disposition;
                break;
            }
        }
        return currentDisposition;
    }
    private Disposition getLatestPastDisposition(Truck truck, ArrayList<Disposition> truckDispositions) throws ParseException{
        Date currentDate = this.getCurrentTime();
        Disposition latestPastDisposition = null;
        Date latestPastDate = veryPastDate();
        for(int i = 0; i < truckDispositions.size(); i++){
            Disposition disposition = truckDispositions.get(i);
            Date dispoFinishDate = this.convertDate(disposition.getFinishDate());
            if(currentDate.after(dispoFinishDate)){
                //System.out.println("IN THE LATEST PAST DISPO LOOP");
                if(dispoFinishDate.after(latestPastDate)){
                    latestPastDate = dispoFinishDate;
                    latestPastDisposition = disposition;
                    //System.out.println("IN THE LATEST PAST DISPO LOOP BUG FREE");
                }
            }
        }
        return latestPastDisposition;
    }
    private Date veryPastDate() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1000);
        return cal.getTime();
    }
    public ArrayList<Truck> getAvailableTrucks(Date newStart, Date newFinish, Vertex newDispositionLocation) throws ParseException{
         DispositionRegistryDatabase dispositionsDatabase = DispositionRegistryDatabase.getInstance();
        this.getTrucks();
        ArrayList<Truck> availableTrucks = new ArrayList<Truck>();
        ArrayList<Truck> trucksNearBy = this.getTrucksNearBy(dispositionsDatabase, newDispositionLocation, newStart);
        for(int i = 0; i < trucksNearBy.size(); i ++){
            Truck truck  = trucksNearBy.get(i);
            ArrayList<Disposition> truckDispositions = dispositionsDatabase.getDispositionsOfTruck(truck.getId());
            boolean conflict = false;
            for(int j = 0; j < truckDispositions.size(); j ++ ){
                Disposition disposition = truckDispositions.get(j);
                Date dispoStartDate = this.convertDate(disposition.getStartDate());
                Date dispoFinishDate = this.convertDate(disposition.getFinishDate());
               
                if(((dispoStartDate.before(newStart) || dispoStartDate.equals(newStart)) && (dispoFinishDate.after(newFinish) || dispoFinishDate.equals(newFinish)  )) || 
                    ((dispoStartDate.before(newStart) || dispoStartDate.equals(newStart)) && (dispoFinishDate.after(newStart) || dispoFinishDate.equals(newStart)))||
                    (dispoStartDate.after(newStart) && dispoStartDate.before(newFinish)) ||
                    ((dispoStartDate.after(newStart) || dispoStartDate.equals(newStart) ) && (dispoStartDate.before(newFinish) || dispoStartDate.equals(newFinish) ) && 
                     (dispoFinishDate.before(newFinish) || dispoFinishDate.equals(newFinish)))){
                    conflict = true;
                    break;
                }
            }
            if(!conflict){
               availableTrucks.add(truck);
            }
            
        }
        return availableTrucks;
    }
    public ArrayList<Truck> getAvailableTrucks(Date newStart, Date newFinish, Disposition dispositionn, Vertex newVertexLocation) throws ParseException{
         DispositionRegistryDatabase dispositionsDatabase = DispositionRegistryDatabase.getInstance();
        this.getTrucks();
        ArrayList<Truck> availableTrucks = new ArrayList<Truck>();
        ArrayList<Truck> trucksNearBy = this.getTrucksNearBy(dispositionsDatabase, newVertexLocation, newStart);
        for(int i = 0; i < trucksNearBy.size(); i ++){
            Truck truck  = trucksNearBy.get(i);
            ArrayList<Disposition> truckDispositions = dispositionsDatabase.getDispositionsOfTruck(truck.getId());
            boolean conflict = false;
            for(int j = 0; j < truckDispositions.size(); j ++ ){
                Disposition disposition = truckDispositions.get(j);
                if(disposition == dispositionn){
                    conflict = false;
                    System.out.println("DISPOSITION : "+dispositionn.getId());
                }else{
                    Date dispoStartDate = this.convertDate(disposition.getStartDate());
                    Date dispoFinishDate = this.convertDate(disposition.getFinishDate());
                    if(((dispoStartDate.before(newStart) || dispoStartDate.equals(newStart)) && (dispoFinishDate.after(newFinish) || dispoFinishDate.equals(newFinish)  )) || 
                        ((dispoStartDate.before(newStart) || dispoStartDate.equals(newStart)) && (dispoFinishDate.after(newStart) || dispoFinishDate.equals(newStart)))||
                        (dispoStartDate.after(newStart) && dispoStartDate.before(newFinish)) ||
                        ((dispoStartDate.after(newStart) || dispoStartDate.equals(newStart) ) && (dispoStartDate.before(newFinish) || dispoStartDate.equals(newFinish) ) && 
                         (dispoFinishDate.before(newFinish) || dispoFinishDate.equals(newFinish)))){
                        conflict = true;
                        break;
                    }
                }
            }
            if(!conflict){
               availableTrucks.add(truck);
            }
            
        }
        return availableTrucks;
    }
    private ArrayList<Truck> getTrucksNearBy(DispositionRegistryDatabase instance, 
            Vertex newDispositionLocation, Date newStart) throws ParseException {
        ArrayList<Truck> trucksNearBy = new ArrayList<Truck>();
        for(int i = 0; i < trucks.size(); i++){
            Truck truck = trucks.get(i);
            ArrayList<Disposition> truckDispositions = 
                    instance.getDispositionsOfTruck(truck.getId());
            Vertex truckLocation = null;
            if(truckDispositions.isEmpty() || getLatestDispositionAssigned(truckDispositions, newStart) == null)
                truckLocation = truck.getAddress();
            else{
                Disposition latestOrder = getLatestDispositionAssigned(truckDispositions, newStart);
                truckLocation = latestOrder.getDestinationAddress();
            }
            if(newDispositionLocation == truckLocation){
                System.out.println(newDispositionLocation.getCity()+" "+truckLocation.getCity());
                trucksNearBy.add(truck);
            }
                
            
        }
        return trucksNearBy;
    }
    

    private Disposition getLatestDispositionAssigned(ArrayList<Disposition> truckDispositions, Date startDate) throws ParseException {
        
        ArrayList<Disposition> latestDispositions = new ArrayList<Disposition>();
        for(int i = 0; i < truckDispositions.size(); i ++){
            Disposition disposition = truckDispositions.get(i);
            Date current = this.convertDate(disposition.getFinishDate());
            if(current.before(startDate)){
               latestDispositions.add(disposition);
            }
        }
        
        Disposition latestOrder = null;
        Date latestDate = this.veryPastDate();
        for(int i = 0; i < latestDispositions.size(); i ++){
            Disposition disposition = latestDispositions.get(i);
            Date current = this.convertDate(disposition.getFinishDate());
            if(current.after(latestDate)){
                latestDate = current;
                latestOrder = disposition;
            }
        }
        return latestOrder;
    }
    
    private Date getCurrentTime(){
        Date todayDate = Calendar.getInstance().getTime();
       return todayDate;
    }
    private Date convertDate(String dat) throws ParseException{
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date = format.parse(dat);
        return date;
    }

// TODO: create user defined exception.to handle integrity cases
    public void editTruck(Truck truck, Vertex address, boolean availabilityStatus, String plateNumber,
            boolean conditionStatus) {
        String sql = "UPDATE Truck SET  availabilityStatus='"+(availabilityStatus?1:0)+"', "
                + "plateNumber='"+plateNumber+"', conditionStatus='"+(conditionStatus?1:0)+"'  "
                + " , addressId='"+address.getId()+"' WHERE id='"+truck.getId()+"';";
        database.connect("central");
        try {
            database.update(sql);
            truck.setAvailability(availabilityStatus);
            truck.setPlateNumber(plateNumber);
            truck.setCondition(conditionStatus);
            truck.setAddress(address);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        database.closeConnection();
    }

    public void delete(Truck truck) {
        trucks.remove(truck);
        String sql = "DELETE FROM Truck WHERE id='"+truck.getId()+"';";
        database.connect("central");
        try {
            database.update(sql);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        database.closeConnection();
    }
}
