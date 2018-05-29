/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.TOMS.controller.CarrierRecords;

import com.project.TOMS.model.CarrierRecords.Carrier;
import com.project.TOMS.model.CarrierRecords.Mode;
import com.project.TOMS.model.CarrierRecords.Rail;
import com.project.TOMS.model.CarrierRecords.Road;
import com.project.TOMS.model.CarrierRecords.Sea;
import com.project.TOMS.database.Database;
import com.project.TOMS.model.CarrierRecords.CarrierRole;
import com.project.TOMS.model.CarrierRecords.External;
import com.project.TOMS.model.CarrierRecords.Internal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author EfthymiosChatziathanasiadis
 */
public class CarrierRegistryDatabase {
    Database database = Database.getInstance();
    static CarrierRegistryDatabase INSTANCE = null;
    ArrayList<Carrier> carriers = new ArrayList<Carrier>();

    private CarrierRegistryDatabase() {
        this.loadCarriers();
    }

    public static CarrierRegistryDatabase getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CarrierRegistryDatabase();
        }
        return INSTANCE;
    }
// TODO: create user defined exception.to handle integrity cases

    public void addCarrier(String company, Mode mode, CarrierRole role) {
        String sql = "INSERT INTO Carrier (company, role, mode)"
                + " VALUES ('"+company+"','"+(role instanceof Internal?0:1)+"','"
                +this.getMode(mode)+"');";
        database.connect("central");
        try {
            database.update(sql);
            int carrierId = database.getTableCount("Carrier");
            Carrier customer = new Carrier(carrierId, company, mode, role);
            carriers.add(customer);
        } catch (SQLException ex) {
            ex.printStackTrace();
            if (ex.getErrorCode() == 0) {

            }
        }
        database.closeConnection();
    }

    public Carrier getCarrierOfDisposition(int id) {
        Carrier carrier = null;
        if (carriers.isEmpty()) {
            this.getCarriers();
        }
        Iterator<Carrier> it = carriers.iterator();
        while (it.hasNext()) {
            carrier = it.next();
            if (carrier.getId() == id) {
                break;
            }
        }

        return carrier;
    }

    private void loadCarriers() {
        if (carriers.isEmpty()) {
            String sql = "SELECT * FROM Carrier;";
            try {
                ResultSet rs;
                database.connect("central");
                rs = database.query(sql);

                while (rs.next()) {
                    Carrier carrier = new Carrier(rs.getInt("id"), rs.getString("company"), this.getMode(rs.getString("mode")), ((rs.getInt("role")==0) ? (new Internal()) : (new External())));
                    carriers.add(carrier);
                }

                database.closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public ArrayList<Carrier> getCarriers() {
        return carriers;
    }
    private Mode getMode(String mod){
        Mode mode = null;
        if(mod.equals("Rail"))mode = new Rail();
        else if(mod.equals("Road"))mode = new Road();
        else mode = new Sea();
        return mode;
    }
    private String getMode(Mode mod){
        String mode;
        if(mod instanceof Rail)mode = "Rail";
        else if(mod instanceof Road)mode = "Road";
        else mode = "Sea";
        return mode;
    }

// TODO: create user defined exception.to handle integrity cases
    public void editCarrier(Carrier carrier, String company, Mode mode, CarrierRole role) {
        carrier.setCompany(company);
        carrier.setMode(mode);
        carrier.setRole(role);
        String sql = "UPDATE Carrier SET  company='"+company+"', "
                + "role='"+(role instanceof Internal?0:1)+"', mode='"+this.getMode(mode)+"'  "
                + "WHERE id='"+carrier.getId()+"';";
        database.connect("central");
        try {
            database.update(sql);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        database.closeConnection();
    }

    public void delete(Carrier carrier) {
        carriers.remove(carrier);
        String sql = "DELETE FROM Carrier WHERE id='"+carrier.getId()+"';";
        database.connect("central");
        try {
            database.update(sql);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        database.closeConnection();
    }
    public Carrier getInternalCarrier(){
        for(int i = 0; i<carriers.size(); i++){
            Carrier carrier = carriers.get(i);
            if(carrier.getRole() instanceof Internal){
                return carrier;
            }
        }
        return null;
    }
    public ArrayList<Carrier> getInternalCarriers(){
        ArrayList<Carrier> carriers = this.getCarriers();
        ArrayList<Carrier> internalCarriers = new ArrayList<Carrier> ();
        for(int i = 0; i < carriers.size(); i++){
            Carrier carrier = carriers.get(i);
            if(carrier.getRole() instanceof Internal){
                internalCarriers.add(carrier);
            }
        }
        return internalCarriers;
    }
    public ArrayList<Carrier> getExternalCarriers(){
        ArrayList<Carrier> carriers = this.getCarriers();
        ArrayList<Carrier> externalCarriers = new ArrayList<Carrier> ();
        for(int i = 0; i < carriers.size(); i++){
            Carrier carrier = carriers.get(i);
            if(carrier.getRole() instanceof External){
                externalCarriers.add(carrier);
            }
        }
        return externalCarriers;
    }
}
