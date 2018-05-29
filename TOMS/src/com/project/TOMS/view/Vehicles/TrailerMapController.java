/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.TOMS.view.Vehicles;


import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.event.UIEventType;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.InfoWindow;
import com.lynden.gmapsfx.javascript.object.InfoWindowOptions;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MapOptions;
import com.lynden.gmapsfx.javascript.object.MapTypeIdEnum;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;
import com.lynden.gmapsfx.service.geocoding.GeocoderStatus;
import com.lynden.gmapsfx.service.geocoding.GeocodingResult;
import com.lynden.gmapsfx.service.geocoding.GeocodingService;
import com.project.TOMS.algorithms.dijkstra.model.Vertex;
import com.project.TOMS.controller.Vehicles.TrailerDatabaseRegistry;
import com.project.TOMS.controller.Vehicles.TruckDatabaseRegistry;
import com.project.TOMS.model.Vehicles.Truck;
import com.project.TOMS.model.Vehicles.Vehicle;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import netscape.javascript.JSObject;


/**
 * FXML Controller class
 *
 * @author EfthymiosChatziathanasiadis
 */
public class TrailerMapController implements Initializable, MapComponentInitializedListener {

    /**
     * Initializes the controller class.
     */


    @FXML
    private GoogleMapView mapView;
    private GoogleMap map;
    private GeocodingService geocodingService;
    private TrailerDatabaseRegistry trailerRegistry;
    private TruckDatabaseRegistry truckRegistry;
    private boolean isTruck;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    public void truckReinit(){
        mapView.addMapInializedListener(this);
        truckRegistry = TruckDatabaseRegistry.getInstance();
        isTruck = true;
        
    }
    public void trailerReinit(){
        mapView.addMapInializedListener(this);
        trailerRegistry = TrailerDatabaseRegistry.getInstance();
        isTruck = false;
    }
        

    @Override
    public void mapInitialized() {
        geocodingService = new GeocodingService();
        LatLong center = new LatLong(47.606189, -122.335842);
        mapView.addMapReadyListener(() -> {
            // This call will fail unless the map is completely ready.
        });
         
        MapOptions mapOptions = new MapOptions();

        mapOptions.center(center)
        .mapMarker(true)
        .mapType(MapTypeIdEnum.ROADMAP)                 
        .overviewMapControl(false)
        .panControl(false)
        .rotateControl(false)                                   
        .scaleControl(false)
        .streetViewControl(false)                               
        .zoomControl(true)                                  
        .zoom(2);                                      

        map = mapView.createMap(mapOptions);
        ArrayList<? extends Vehicle> vehicles = null;
        if(isTruck){
            vehicles = truckRegistry.getTrucks();
        }else{
            vehicles = trailerRegistry.getTrailers();
        }
                
        for(int i = 0; i < vehicles.size(); i ++ ){
                Vehicle vehicle = vehicles.get(i);
                Vertex address = vehicle.getAddress();
                String city = address.getCity();
                this.geocode(city, vehicle);


        }
            
        
        
        

        
       
    }
    private void geocode(String address, Vehicle vehicle){
          geocodingService.geocode(address, (GeocodingResult[] results, GeocoderStatus status) -> {
            
            LatLong latLong = null;
            
            if( status == GeocoderStatus.ZERO_RESULTS) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "No matching address found");
                alert.show();
                return;
            } else if( results.length > 1 ) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Multiple results found, showing the first one.");
                alert.show();
                latLong = new LatLong(results[0].getGeometry().getLocation().getLatitude(), results[0].getGeometry().getLocation().getLongitude());
            
            } else {
                latLong = new LatLong(results[0].getGeometry().getLocation().getLatitude(), results[0].getGeometry().getLocation().getLongitude());
            }
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLong)
                .visible(true)
                .title("Trailer ");

                Marker marker = new Marker( markerOptions );
                /*map.addUIEventHandler(marker, UIEventType.click, (JSObject obj) -> { 
                    InfoWindowOptions infoOptions = new InfoWindowOptions();
                    infoOptions.content("<h2>Trailer"+trailer.getLocation()+"</h2><h3></h3>");

                    InfoWindow window = new InfoWindow(infoOptions);
                    window.open(map, marker);
                
                });*/
                map.addMarker(marker);

              /*  InfoWindowOptions infoOptions = new InfoWindowOptions();
                infoOptions.content("<h2>Trailer "+trailer.getId()+"</h2><h3>"+trailer.getLocation()+"</h3>");

                System.out.println(trailer.getId());

                InfoWindow window = new InfoWindow(infoOptions);
                window.open(map, marker);*/
            
            
        });
    }

 

    

    
}
