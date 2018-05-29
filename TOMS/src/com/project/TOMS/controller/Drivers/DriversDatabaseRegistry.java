/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.TOMS.controller.Drivers;

import com.project.TOMS.algorithms.dijkstra.controller.graphRegistryDatabase;
import com.project.TOMS.algorithms.dijkstra.model.Vertex;
import com.project.TOMS.controller.DispositionRecords.DispositionRegistryDatabase;
import com.project.TOMS.database.Database;
import com.project.TOMS.model.CustomerOrders.Disposition;
import com.project.TOMS.model.Drivers.Driver;
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
public class DriversDatabaseRegistry {
    Database database = Database.getInstance();
    static DriversDatabaseRegistry INSTANCE = null;
    ArrayList<Driver> drivers = new ArrayList<Driver>();
    graphRegistryDatabase graphDatabase = graphRegistryDatabase.getInstance();
    

    private DriversDatabaseRegistry()  {
       this.loadDrivers();
    }

    public static DriversDatabaseRegistry getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DriversDatabaseRegistry();
        }
        return INSTANCE;
    }
// TODO: create user defined exception.to handle integrity cases

    public void addDriver(Vertex address, String firstName, String lastName, 
            boolean availabilityStatus) {
        String sql = "INSERT INTO Driver(availabilityStatus, firstName, lastName, addressId"
                + ") VALUES ('"+(availabilityStatus?1:0)+"','"+firstName+"',"
                + "'"+lastName+"','"+address.getId()+"');";
        database.connect("central");
        try {
            database.update(sql);
            int driverId = database.getTableCount("Driver");
            Driver driver = new Driver(driverId, firstName, lastName, availabilityStatus, address);
            drivers.add(driver);
        } catch (SQLException ex) {
            ex.printStackTrace();
            if (ex.getErrorCode() == 0) {

            }
        }
        database.closeConnection();
    }

    public Driver getDriverOfDisposition(int id){
        Driver driver = null;
        if (drivers.isEmpty()) {
            this.getDrivers();
        }
        Iterator<Driver> it = drivers.iterator();
        while (it.hasNext()) {
            driver = it.next();
            if (driver.getId() == id) {
                break;
            }
        }

        return driver;
    }
    private void loadDrivers()  {
        if (drivers.isEmpty()) {
            String sql = "SELECT * FROM Driver;";
            try {
                ResultSet rs;
                database.connect("central");
                rs = database.query(sql);

                while (rs.next()) {
                    Driver driver = new Driver(rs.getInt("id"), rs.getString("firstName"), rs.getString("lastName"),
                    rs.getInt("availabilityStatus")==0 ? false : true, 
                    graphDatabase.getVertexWhereId(Integer.toString(rs.getInt("addressId"))));
                    drivers.add(driver);
                }

                database.closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
       
    }
    public ArrayList<Driver> getDrivers()  {
        
        this.cofigureDriverLocations();
        return drivers;
    }
    public void cofigureDriverLocations() {
        DispositionRegistryDatabase dispositionsDatabase = DispositionRegistryDatabase.getInstance();
        for(int i = 0; i < drivers.size(); i++){
            try {
                Driver driver = drivers.get(i);
                ArrayList<Disposition> driverDispositions = dispositionsDatabase.getDispositionsOfDriver(driver.getId());
                if(driverDispositions.isEmpty()){
                    //nothing
                    //System.out.println(driver.getFirstName()+" HAS NO DISPOSITIONS");
                }else if(isCurrentlyExecutingDisposition(driver, driverDispositions)){
                    Disposition disposition = getCurrentDisposition(driver, driverDispositions);
                    Vertex destination = disposition.getDestinationAddress();
                    driver.setAddress(destination);
                    //System.out.println(driver.getFirstName()+" IS CURRENTLY EXECUTING, ADDRESS: "+destination.getCity());
                }else if(hasPastDispositions(driver, driverDispositions)){
                    //System.out.println(driver.getFirstName()+" IS CURRENTLY IS NOT EXECUTING. HE HAS PAST DISPO ");
                    Disposition disposition = getLatestPastDisposition(driver, driverDispositions);
                    Vertex destination = disposition.getDestinationAddress();
                    driver.setAddress(destination);
                    //System.out.println(driver.getFirstName()+" IS CURRENTLY EXECUTING, ADDRESS: "+destination.getCity());
                }else{
                    //nothing
                }
            } catch (ParseException ex) {
                Logger.getLogger(DriversDatabaseRegistry.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
    }
    private boolean isCurrentlyExecutingDisposition(Driver driver, ArrayList<Disposition> driverDispositions) throws ParseException{
        boolean isExecuting = false;
        Date currentDate = this.getCurrentTime();
        for(int i = 0; i < driverDispositions.size(); i++){
            Disposition disposition = driverDispositions.get(i);
            Date dispoStartDate = this.convertDate(disposition.getStartDate());
            Date dispoFinishDate = this.convertDate(disposition.getFinishDate());
            if(currentDate.after(dispoStartDate) && currentDate.before(dispoFinishDate)){
                 isExecuting = true;
                 break;
            }
        }
        return isExecuting;
    }
    private boolean hasPastDispositions(Driver driver, ArrayList<Disposition> driverDispositions) throws ParseException{
        Date currentDate = this.getCurrentTime();
        boolean hasPastDispositions = false;
        for(int i = 0; i < driverDispositions.size(); i++){
            Disposition disposition = driverDispositions.get(i);
            Date dispoFinishDate = this.convertDate(disposition.getFinishDate());
            if(currentDate.after(dispoFinishDate)){
                hasPastDispositions = true;
                //System.out.println("HAS PAST DISPO: ADDRESS"+disposition.getDestinationAddress().getCity());
                break;
            }
        }
        return hasPastDispositions;
    }
    private Disposition getCurrentDisposition(Driver driver, ArrayList<Disposition> driverDispositions) throws ParseException{
        Date currentDate = this.getCurrentTime();
        Disposition currentDisposition = null;
        for(int i = 0; i < driverDispositions.size(); i++){
            Disposition disposition = driverDispositions.get(i);
            Date dispoStartDate = this.convertDate(disposition.getStartDate());
            Date dispoFinishDate = this.convertDate(disposition.getFinishDate());
            if(currentDate.after(dispoStartDate) && currentDate.before(dispoFinishDate)){
                currentDisposition = disposition;
                break;
            }
        }
        return currentDisposition;
    }
    private Disposition getLatestPastDisposition(Driver driver, ArrayList<Disposition> driverDispositions) throws ParseException{
        Date currentDate = this.getCurrentTime();
        Disposition latestPastDisposition = null;
        Date latestPastDate = veryPastDate();
        for(int i = 0; i < driverDispositions.size(); i++){
            Disposition disposition = driverDispositions.get(i);
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
    public ArrayList<Driver> getAvailableDrivers(Date newStart, Date newFinish, Disposition dispositionn) throws ParseException{
        DispositionRegistryDatabase dispositionsDatabase = DispositionRegistryDatabase.getInstance();
        this.getDrivers();
        ArrayList<Driver> availableDrivers = new ArrayList<Driver>();
        ArrayList<Driver> driversNearBy = this.getDriversNearBy(dispositionsDatabase, dispositionn.getOriginAddress(), newStart);
        for(int i = 0; i < driversNearBy.size(); i ++){
            Driver driver  = driversNearBy.get(i);
            ArrayList<Disposition> truckDispositions = dispositionsDatabase.getDispositionsOfDriver(driver.getId());
            boolean conflict = false;
            for(int j = 0; j < truckDispositions.size(); j ++ ){
                Disposition disposition = truckDispositions.get(j);
                if(disposition == dispositionn){
                    conflict = false;
                    System.out.println("DISPOSITION  :"+dispositionn.getId());
                }else{
                    Date dispoStartDate = this.convertDate(disposition.getStartDate());
                    Date dispoFinishDate = this.convertDate(disposition.getFinishDate());
                    if( ((dispoStartDate.before(newStart) || (dispoStartDate.equals(newStart))) && ((dispoFinishDate.after(newFinish)) || dispoFinishDate.equals(newFinish))) || 
                        ((dispoStartDate.before(newStart) || (dispoStartDate.equals(newStart))) && (dispoFinishDate.after(newStart) || dispoFinishDate.equals(newStart) )) ||
                        ((dispoStartDate.after(newStart) || dispoStartDate.equals(newStart)) && (dispoStartDate.before(newFinish)) || dispoStartDate.equals(newFinish) ) ||
                        (dispoStartDate.after(newStart) || dispoStartDate.equals(newStart)) && (dispoStartDate.before(newFinish) || dispoStartDate.equals(newFinish)) && 
                         (dispoFinishDate.before(newFinish) || dispoFinishDate.equals(newFinish) )){
                        conflict = true;
                        break;
                    }
                }
                
            }
            if(!conflict){
                availableDrivers.add(driver);
            }
        }
        return availableDrivers;
    }
    public ArrayList<Driver> getAvailableDrivers(Date newStart, Date newFinish, Vertex newDispositionLocation) throws ParseException{
        DispositionRegistryDatabase dispositionsDatabase = DispositionRegistryDatabase.getInstance();
        this.getDrivers();
        ArrayList<Driver> availableDrivers = new ArrayList<Driver>();
        ArrayList<Driver> driversNearBy = this.getDriversNearBy(dispositionsDatabase, newDispositionLocation, newStart);
        for(int i = 0; i < driversNearBy.size(); i ++){
            Driver driver  = driversNearBy.get(i);
            ArrayList<Disposition> truckDispositions = dispositionsDatabase.getDispositionsOfDriver(driver.getId());
            boolean conflict = false;
            for(int j = 0; j < truckDispositions.size(); j ++ ){
                Disposition disposition = truckDispositions.get(j);
                Date dispoStartDate = this.convertDate(disposition.getStartDate());
                Date dispoFinishDate = this.convertDate(disposition.getFinishDate());
                if( ((dispoStartDate.before(newStart) || (dispoStartDate.equals(newStart))) && ((dispoFinishDate.after(newFinish)) || dispoFinishDate.equals(newFinish))) || 
                    ((dispoStartDate.before(newStart) || (dispoStartDate.equals(newStart))) && (dispoFinishDate.after(newStart) || dispoFinishDate.equals(newStart) )) ||
                    ((dispoStartDate.after(newStart) || dispoStartDate.equals(newStart)) && (dispoStartDate.before(newFinish)) || dispoStartDate.equals(newFinish) ) ||
                    (dispoStartDate.after(newStart) || dispoStartDate.equals(newStart)) && (dispoStartDate.before(newFinish) || dispoStartDate.equals(newFinish)) && 
                     (dispoFinishDate.before(newFinish) || dispoFinishDate.equals(newFinish) )){
                    conflict = true;
                    break;
                }
                
            }
            if(!conflict){
                availableDrivers.add(driver);
            }
        }
        return availableDrivers;
    }
    
    private ArrayList<Driver> getDriversNearBy(DispositionRegistryDatabase instance, 
            Vertex newDispositionLocation, Date newStart) throws ParseException {
        ArrayList<Driver> driversNearBy = new ArrayList<Driver>();
        for(int i = 0; i < drivers.size(); i++){
            Driver driver = drivers.get(i);
            ArrayList<Disposition> driverDispositions = 
                    instance.getDispositionsOfTruck(driver.getId());
            Vertex driverLocation = null;
            if(driverDispositions.isEmpty() || getLatestDispositionAssigned(driverDispositions, newStart)== null)
                driverLocation = driver.getAddress();
            else{
                Disposition latestOrder = getLatestDispositionAssigned(driverDispositions, newStart);
                driverLocation = latestOrder.getDestinationAddress();
            }
            if(newDispositionLocation == driverLocation)
                driversNearBy.add(driver);
            
        }
        return driversNearBy;
    }

    private Disposition getLatestDispositionAssigned(ArrayList<Disposition> driverDispositions, Date startDate) throws ParseException {
        ArrayList<Disposition> latestDispositions = new ArrayList<Disposition>();
        for(int i = 0; i < driverDispositions.size(); i ++){
            Disposition disposition = driverDispositions.get(i);
            Date current = this.convertDate(disposition.getFinishDate());
            if(current.before(startDate)){
               latestDispositions.add(disposition);
            }
        }
        Disposition latestDisposition = null;
        Date latestDate = this.veryPastDate();
        for(int i = 0; i < latestDispositions.size(); i ++){
            Disposition disposition = latestDispositions.get(i);
            Date current = this.convertDate(disposition.getFinishDate());
            if(current.after(latestDate)){
                latestDate = current;
                latestDisposition = disposition;
            }
        }
        return latestDisposition;
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
    public void editDriver(Driver driver, Vertex address, boolean availabilityStatus, String firstName, 
            String lastName) {
        String sql = "UPDATE Driver SET  availabilityStatus='"+(availabilityStatus?1:0)+"', "
                + "firstName='"+firstName+"', lastName='"+lastName+"' , addressId="
                + "'"+address.getId()+"'  WHERE id='"+driver.getId()+"';";
        database.connect("central");
        try {
            database.update(sql);
            driver.setAvailabilityStatus(availabilityStatus);
            driver.setFirstName(firstName);
            driver.setLastName(lastName);
            driver.setAddress(address);
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        database.closeConnection();
    }

    public void delete(Driver driver) {
        drivers.remove(driver);
        String sql = "DELETE FROM Driver WHERE id='"+driver.getId()+"';";
        database.connect("central");
        try {
            database.update(sql);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        database.closeConnection();
    }
}
