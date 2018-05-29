/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.TOMS.view.CustomerOrders;
import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.DirectionsPane;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MapOptions;
import com.lynden.gmapsfx.javascript.object.MapTypeIdEnum;
import com.lynden.gmapsfx.service.directions.DirectionStatus;
import com.lynden.gmapsfx.service.directions.DirectionsRenderer;
import com.lynden.gmapsfx.service.directions.DirectionsRequest;
import com.lynden.gmapsfx.service.directions.DirectionsResult;
import com.lynden.gmapsfx.service.directions.DirectionsService;
import com.lynden.gmapsfx.service.directions.DirectionsServiceCallback;
import com.lynden.gmapsfx.service.directions.DirectionsWaypoint;
import com.lynden.gmapsfx.service.directions.TravelModes;
import com.lynden.gmapsfx.service.elevation.ElevationResult;
import com.lynden.gmapsfx.service.elevation.ElevationService;
import com.lynden.gmapsfx.service.elevation.ElevationServiceCallback;
import com.lynden.gmapsfx.service.elevation.ElevationStatus;
import com.lynden.gmapsfx.service.elevation.LocationElevationRequest;
import com.lynden.gmapsfx.service.geocoding.GeocodingService;
import com.project.TOMS.algorithms.dijkstra.model.Vertex;
import com.project.TOMS.controller.DispositionRecords.DispositionRegistryDatabase;
import com.project.TOMS.model.Address.Address;
import com.project.TOMS.model.CustomerOrders.CustomerOrder;
import com.project.TOMS.model.CustomerOrders.Disposition;

import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;


/**
 * FXML Controller class
 *
 * @author EfthymiosChatziathanasiadis
 */
public class MapController implements Initializable, MapComponentInitializedListener ,ElevationServiceCallback, DirectionsServiceCallback {

    /**
     * Initializes the controller class.
     */
        protected DirectionsPane directions;


    @FXML
    private GoogleMapView mapView;
    @FXML
    private TextField fied;
    protected DirectionsRenderer directionsRenderer = null;
    protected DirectionsService directionsService;
    DirectionsRenderer renderer;
    protected CustomerOrder order;
    protected DispositionRegistryDatabase dispositions;
    private Disposition disposition;
    private boolean isOrder;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    public void reinitCustomerOrder(){
        mapView.addMapInializedListener(this);
        dispositions = DispositionRegistryDatabase.getInstance();
        isOrder=true;

    }
    public void reinitDisposition() {
        mapView.addMapInializedListener(this);
        isOrder=false;
        
    }
        

    @Override
    public void mapInitialized()  {
         
        MapOptions options = new MapOptions();
        System.out.println("initialised!!!!!!!!!!!");
        options/*.center(new LatLong(52.520008, 13.404954))*/
                .zoomControl(true)
                .zoom(12)
                .overviewMapControl(false)
                .mapType(MapTypeIdEnum.ROADMAP);
        GoogleMap map = mapView.createMap(options);
        directions = mapView.getDirec();

        GeocodingService gs = new GeocodingService();
        
        DirectionsService ds = new DirectionsService();
        renderer = new DirectionsRenderer(true, map, directions);
        DirectionsRequest dr;
        if(isOrder){
            DirectionsWaypoint[] dw = null;
            try {
                dw = this.setDirectionsWaypoints();
            } catch (ParseException ex) {
                Logger.getLogger(MapController.class.getName()).log(Level.SEVERE, null, ex);
            }
            dr = this.setDirectionsRequest(dw);
        }else{
            dr = this.setDirectionsRequestOfDisposition();
            
        }
            
        ds.getRoute(dr, this, renderer);
        
        LatLong[] location = new LatLong[1];
        location[0] = new LatLong(-19.744056, -43.958699);
        LocationElevationRequest loc = new LocationElevationRequest(location);
        ElevationService es = new ElevationService();
        es.getElevationForLocations(loc, this);
       
    }

    @Override
    public void directionsReceived(DirectionsResult results, DirectionStatus status) {
        if(status.equals(DirectionStatus.OK)){
            mapView.getMap().showDirectionsPane();
            System.out.println("OK");
            
            DirectionsResult e = results;
            GeocodingService gs = new GeocodingService();
            
            System.out.println("SIZE ROUTES: " + e.getRoutes().size() + "\n" + "ORIGIN: " + e.getRoutes().get(0).getLegs().get(0).getStartLocation());
            //gs.reverseGeocode(e.getRoutes().get(0).getLegs().get(0).getStartLocation().getLatitude(), e.getRoutes().get(0).getLegs().get(0).getStartLocation().getLongitude(), this);
            System.out.println("LEGS SIZE: " + e.getRoutes().get(0).getLegs().size());
            System.out.println("WAYPOINTS " +e.getGeocodedWaypoints().size());
            /*double d = 0;
            for(DirectionsLeg g : e.getRoutes().get(0).getLegs()){
                d += g.getDistance().getValue();
                System.out.println("DISTANCE " + g.getDistance().getValue());
            }*/
            try{
                System.out.println("Distancia total = " + e.getRoutes().get(0).getLegs().get(0).getDistance().getText());
            } catch(Exception ex){
                System.out.println("ERRO: " + ex.getMessage());
            }
            System.out.println("LEG(0)");
            System.out.println(e.getRoutes().get(0).getLegs().get(0).getSteps().size());
            /*for(DirectionsSteps ds : e.getRoutes().get(0).getLegs().get(0).getSteps()){
                System.out.println(ds.getStartLocation().toString() + " x " + ds.getEndLocation().toString());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(ds.getStartLocation())
                        .title(ds.getInstructions())
                        .animation(Animation.DROP)
                        .visible(true);
                Marker myMarker = new Marker(markerOptions);
                map.addMarker(myMarker);
            }
                    */
            System.out.println(renderer.toString());
        }
    }

    @Override
    public void elevationsReceived(ElevationResult[] results, ElevationStatus status) {
            if(status.equals(ElevationStatus.OK)){
                for(ElevationResult e : results){
                    System.out.println(" Elevation on "+ e.getLocation().toString() + " is " + e.getElevation());
                }
            }    
    }
    private DirectionsWaypoint [] setDirectionsWaypoints() throws ParseException{
        DirectionsWaypoint [] waypoints ;
        ArrayList<Disposition> orderDispositions = dispositions.getDispositionsOfOrder(order.getId());
        if(orderDispositions.size() == 1){
            waypoints = null;
        }else{
            waypoints = new DirectionsWaypoint[orderDispositions.size()-1];
            for(int i =1; i<orderDispositions.size(); i++){
                Disposition disposition = orderDispositions.get(i);
                Vertex origin = disposition.getOriginAddress();
                waypoints[i-1] = new DirectionsWaypoint(origin.getCity()+", "+origin.getCountry());
                     
            }
        }
     
        return waypoints;
    }
    private DirectionsRequest setDirectionsRequest(DirectionsWaypoint [] waypoints){
        Vertex origin = order.getOriginAddress();
        Vertex destination = order.getDestinationAddress();
        if(waypoints == null){
             return new DirectionsRequest(origin.getCity()+", "+origin.getCountry(),destination.getCity()+", "+destination.getCountry(),
                                    TravelModes.DRIVING);
        }else{
             return new DirectionsRequest(origin.getCity()+", "+origin.getCountry(),destination.getCity()+", "+destination.getCountry(),
                                    TravelModes.DRIVING, waypoints);
        }
        
    }
    public void setCustomerOrder(CustomerOrder order){
        this.order=order;
    }

    public void setDisposition(Disposition disposition) {
        this.disposition = disposition;
    }

    private DirectionsRequest setDirectionsRequestOfDisposition() {
        Vertex origin = disposition.getOriginAddress();
        Vertex destination = disposition.getDestinationAddress();
        return new DirectionsRequest(origin.getCity()+", "+origin.getCountry(),destination.getCity()+", "+destination.getCountry(),
                                    TravelModes.DRIVING);
        
    }

    

    
}
