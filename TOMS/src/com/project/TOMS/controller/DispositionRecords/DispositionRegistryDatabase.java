/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.TOMS.controller.DispositionRecords;

import com.lynden.gmapsfx.service.directions.DirectionStatus;
import com.lynden.gmapsfx.service.directions.DirectionsRenderer;
import com.lynden.gmapsfx.service.directions.DirectionsRequest;
import com.lynden.gmapsfx.service.directions.DirectionsResult;
import com.lynden.gmapsfx.service.directions.DirectionsService;
import com.lynden.gmapsfx.service.directions.DirectionsServiceCallback;
import com.lynden.gmapsfx.service.directions.TravelModes;
import com.project.TOMS.algorithms.dijkstra.controller.graphRegistryDatabase;
import com.project.TOMS.algorithms.dijkstra.model.Vertex;
import com.project.TOMS.controller.CarrierRecords.CarrierRegistryDatabase;
import com.project.TOMS.controller.CustomerOrders.CustomerOrderRegistryDatabase;
import com.project.TOMS.controller.Drivers.DriversDatabaseRegistry;
import com.project.TOMS.controller.Vehicles.TruckDatabaseRegistry;
import com.project.TOMS.database.Database;
import com.project.TOMS.model.CarrierRecords.Carrier;
import com.project.TOMS.model.CarrierRecords.External;
import com.project.TOMS.model.CarrierRecords.Internal;
import com.project.TOMS.model.CustomerOrders.CustomerOrder;
import com.project.TOMS.model.CustomerOrders.Disposition;
import com.project.TOMS.model.Drivers.Driver;
import com.project.TOMS.model.Vehicles.Truck;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author EfthymiosChatziathanasiadis
 */
public class DispositionRegistryDatabase implements DirectionsServiceCallback {

    Database database = Database.getInstance();
    static DispositionRegistryDatabase INSTANCE = null;
    static ArrayList<Disposition> dispositions;
    static CarrierRegistryDatabase carrierRegistry = CarrierRegistryDatabase.getInstance();
    graphRegistryDatabase graphDatabase = graphRegistryDatabase.getInstance();

    
    private DispositionRegistryDatabase() {
        this.loadDispositions();
    }

    public static DispositionRegistryDatabase getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DispositionRegistryDatabase();
        }
        return INSTANCE;
    }
// TODO: create user defined exception.to handle integrity cases

    public void addExternalDisposition(Vertex origin, Vertex destination,
            Carrier carrier, String startDate, String finishDate, double tarrif, 
            CustomerOrder order) {
        
        String sql = "INSERT INTO Disposition(carrierId, startDate, finishDate, tarrif, customerOrderId, "
                + "originAddressId, destinationAddressId) VALUES "
                + "('"+carrier.getId()+"','"+startDate+"','"+finishDate+"','"+tarrif+"','"+order.getId()+""
                + "','"+origin.getId()+"','"+destination.getId()+"');";
        database.connect("central");
        try {
            database.update(sql);
            int dispositionId = database.getTableCount("Disposition");
            Disposition disposition = new Disposition(dispositionId, carrier, origin, 
                    destination, startDate, 
                    finishDate, tarrif, order);
            dispositions.add(disposition);
        } catch (SQLException ex) {
            ex.printStackTrace();
            if (ex.getErrorCode() == 0) {

            }
        }
        database.closeConnection();
    }
    public void addInternalDisposition( Vertex origin, Vertex destination,
            Carrier carrier, String startDate, String finishDate, double tarrif, 
            CustomerOrder order, Truck truck, Driver driver) {
        String sql = "INSERT INTO Disposition(carrierId, startDate, finishDate, tarrif, truckId, driverId, customerOrderId, "
                + "originAddressId, destinationAddressId) VALUES "
                + "('"+carrier.getId()+"','"+startDate+"','"+finishDate+"','"+tarrif+"',"
                + "'"+truck.getId()+"','"+driver.getId()+"','"+order.getId()+""
                + "','"+origin.getId()+"','"+destination.getId()+"');";
        database.connect("central");
        try {
            database.update(sql);
            int dispositionId = database.getTableCount("Disposition");
            Disposition disposition = new Disposition(dispositionId, carrier, 
                    origin, destination, startDate, finishDate, tarrif, 
                    order, driver, truck);
            dispositions.add(disposition);
        } catch (SQLException ex) {
            ex.printStackTrace();
            if (ex.getErrorCode() == 0) {

            }
        }
        database.closeConnection();
    }

    public ArrayList<Disposition> getDispositionsOfOrder(int id)  {
        ArrayList<Disposition> orderDispositions = new ArrayList<Disposition>();

        if (dispositions.isEmpty()) {
            this.getDispositions();
        }
        Iterator<Disposition> it = dispositions.iterator();
        while (it.hasNext()) {
            Disposition disposition = it.next();
            CustomerOrder order = disposition.getCustomerOrder();
            if (order.getId() == id) {
                orderDispositions.add(disposition);
            }
        }

        return orderDispositions;
    }
    
    public ArrayList<Disposition> getDispositionsOfDriver(int id) throws ParseException{
        ArrayList<Disposition> driverDispositions = new ArrayList<Disposition>();
        if(dispositions.isEmpty()){
            this.getDispositions();
        }
        for(int i = 0; i < dispositions.size(); i ++ ){
            Disposition disposition = dispositions.get(i);
            if(disposition.getCarrier().getRole() instanceof Internal){
                Driver driver = disposition.getDriver();
                if(driver.getId() == id){
                    driverDispositions.add(disposition);
                }
            }
        }
        return driverDispositions;
    }
    public ArrayList<Disposition> getDispositionsOfTruck(int id) throws ParseException{
        ArrayList<Disposition> truckDispositions = new ArrayList<Disposition>();
        if(dispositions.isEmpty()){
            this.getDispositions();
        }
        for(int i =0 ; i < dispositions.size(); i++){
            Disposition disposition = dispositions.get(i);
            if(disposition.getCarrier().getRole() instanceof Internal){
                Truck truck = (Truck) disposition.getTruck();
                if(truck.getId() == id)truckDispositions.add(disposition);
            }
        }
        return truckDispositions;
    }
 

    public void loadDispositions() {
        CustomerOrderRegistryDatabase customerOrderRegistry;
        TruckDatabaseRegistry truckDatabase = TruckDatabaseRegistry.getInstance();
        DriversDatabaseRegistry driverDatabase = DriversDatabaseRegistry.getInstance();
        dispositions = new ArrayList<Disposition>();
        customerOrderRegistry = CustomerOrderRegistryDatabase.getInstance();
        String sql = "SELECT * FROM Disposition;";
        try {
            ResultSet rs;
            database.connect("central");
            rs = database.query(sql);

            while (rs.next()) {
                Disposition disposition;
                Carrier carrier = carrierRegistry.getCarrierOfDisposition(rs.getInt("carrierId"));
                if(carrier.getRole() instanceof External){
                        disposition = new Disposition(rs.getInt("id"), carrierRegistry.getCarrierOfDisposition(rs.getInt("carrierId")),
                        graphDatabase.getVertexWhereId(Integer.toString(rs.getInt("originAddressId"))), 
                        graphDatabase.getVertexWhereId(Integer.toString(rs.getInt("destinationAddressId"))),
                        rs.getString("startDate"), rs.getString("finishDate"), rs.getDouble("tarrif"),
                        customerOrderRegistry.getCustomerOrderOfDisposition(rs.getInt("customerOrderId")));

                }else{
                        disposition = new Disposition(rs.getInt("id"), carrierRegistry.getCarrierOfDisposition(rs.getInt("carrierId")),
                        graphDatabase.getVertexWhereId(Integer.toString(rs.getInt("originAddressId"))), 
                        graphDatabase.getVertexWhereId(Integer.toString(rs.getInt("destinationAddressId"))),
                        rs.getString("startDate"), rs.getString("finishDate"), rs.getDouble("tarrif"),
                        customerOrderRegistry.getCustomerOrderOfDisposition(rs.getInt("customerOrderId")), 
                        driverDatabase.getDriverOfDisposition(rs.getInt("driverId")), 
                        truckDatabase.getTruckOfDisposition(rs.getInt("truckId")));
                }

               dispositions.add(disposition);
            }

            database.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
       
    }
    public ArrayList<Disposition> getDispositions() {
        
        return dispositions;
    }
    private void printList(ArrayList<Disposition> dispositions){
        System.out.println("LIST SIZE : "+ dispositions.size());
        for(int i = 0; i < dispositions.size(); i++){
            System.out.println(dispositions.get(i).getId());
        }

    }
// TODO: create user defined exception.to handle integrity cases
    public void editExternalDisposition(Disposition disposition, Vertex origin, Vertex destination,
            Carrier carrier, String startDate, String finishDate, double tarrif, 
            CustomerOrder order) {
        
        String sql = "UPDATE Disposition SET  carrierId='"+carrier.getId()+"', startDate='"+startDate+"', "
                + "finishDate='"+finishDate+"', tarrif='"+tarrif+"', "
                + "customerOrderId='"+order.getId()+"', originAddressId='"+origin.getId()+"', "
                + "destinationAddressId='"+destination.getId()+"', truckId=NULL, "
                + "driverId=NULL WHERE id='"+disposition.getId()+"';";
        database.connect("central");
        try {
            database.update(sql);
            disposition.setCarrier(carrier);
            disposition.setStartDate(startDate);
            disposition.setFinishDate(finishDate);
            disposition.setTarrif(tarrif);
            disposition.setOrigin(origin);
            disposition.setDestination(destination);
            disposition.setTruck(null);
            disposition.setDriver(null);
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        database.closeConnection();
    }
    
    public void editInternalDisposition(Disposition disposition, Vertex origin, Vertex destination,
            Carrier carrier, String startDate, String finishDate, double tarrif, 
            CustomerOrder order,  Truck truck, Driver driver) {
        
        String sql = "UPDATE Disposition SET  carrierId='"+carrier.getId()+"', startDate='"+startDate+"', "
                + "finishDate='"+finishDate+"', tarrif='"+tarrif+"', customerOrderId='"
                + ""+order.getId()+"', truckId='"+truck.getId()+"', "
                + "driverId='"+driver.getId()+"', originAddressId='"+origin.getId()+"', "
                + "destinationAddressId='"+destination.getId()+"' WHERE id='"+disposition.getId()+"';";
        database.connect("central");
        try {
            database.update(sql);
            disposition.setCarrier(carrier);
            disposition.setStartDate(startDate);
            disposition.setFinishDate(finishDate);
            disposition.setTarrif(tarrif);
            disposition.setTruck(truck);
            disposition.setDriver(driver);
            disposition.setOrigin(origin);
            disposition.setDestination(destination);
            
          } catch (SQLException ex) {
            ex.printStackTrace();
        }
        database.closeConnection();
    }

    public void delete(Disposition disposition) {
        
        String sql = "DELETE FROM Disposition WHERE id='"+disposition.getId()+"';";
        database.connect("central");
        try {
            database.update(sql);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        database.closeConnection();
        this.loadDispositions();
    }
    public void calculateTarriff(){
        DirectionsService directionsService = new DirectionsService();
        DirectionsRequest request = new DirectionsRequest("Athens", "Italy", TravelModes.DRIVING);
        DirectionsRenderer directionsRenderer = new DirectionsRenderer();
        directionsService.getRoute(request, this, directionsRenderer);
    }

    @Override
    public void directionsReceived(DirectionsResult dr, DirectionStatus ds) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
