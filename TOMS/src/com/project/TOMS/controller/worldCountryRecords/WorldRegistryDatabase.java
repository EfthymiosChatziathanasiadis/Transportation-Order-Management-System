/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.TOMS.controller.worldCountryRecords;

import com.project.TOMS.database.WorldDatabase;
import com.project.TOMS.model.world.City;
import com.project.TOMS.model.world.Country;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author EfthymiosChatziathanasiadis
 */
public class WorldRegistryDatabase {
    WorldDatabase database = WorldDatabase.getInstance();
    static WorldRegistryDatabase INSTANCE = null;
    ArrayList<Country> countries = new ArrayList<Country>();
    
    private WorldRegistryDatabase() {
       
    }

    public static WorldRegistryDatabase getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new WorldRegistryDatabase();
        }
        return INSTANCE;
    }
    public ArrayList<Country> getCountries(){
        if(countries.isEmpty()){
            String sql = "select DISTINCT country from world;";
            try {
                ResultSet rs;
                database.connect();
                rs = database.query(sql);
                while (rs.next()) {
                    Country country = new Country(rs.getString("country"));
                    countries.add(country);
                }

                database.closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return countries;
    }
   
    public ArrayList<City> getCitiesWhereCountry(Country country){
        String sql = "select city, lat, long  from world where country='"+country.getCountry()+"';";
        ArrayList<City> cities = new ArrayList<City>();
        try {
            ResultSet rs;
            database.connect();
            rs = database.query(sql);
            while (rs.next()) {
                try{
                    City city = new City(rs.getString("city"), 
                            Double.parseDouble(rs.getString("lat")), 
                            Double.parseDouble(rs.getString("long")), country);
                    cities.add(city);
                }catch(NumberFormatException e){
                    System.out.println("error");
                }
            }

            database.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return cities;
    }
    

   
    
}
