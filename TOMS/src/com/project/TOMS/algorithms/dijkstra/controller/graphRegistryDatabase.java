/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.TOMS.algorithms.dijkstra.controller;

import com.project.TOMS.algorithms.dijkstra.model.Edge;
import com.project.TOMS.algorithms.dijkstra.model.Graph;
import com.project.TOMS.algorithms.dijkstra.model.Vertex;
import com.project.TOMS.database.Database;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.control.Alert;

/**
 *
 * @author EfthymiosChatziathanasiadis
 */
public class graphRegistryDatabase {
    
    static graphRegistryDatabase INSTANCE = null;
    ArrayList<Vertex> nodes = new ArrayList<Vertex>();
    ArrayList<Edge> edges = new ArrayList<Edge>();
    Database database = Database.getInstance();

    private graphRegistryDatabase() {
        this.getVertices();
        this.getEdges();
    }

    public static graphRegistryDatabase getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new graphRegistryDatabase();
        }
        return INSTANCE;
    }
    
    public Graph getGraph(){
        if (nodes.isEmpty())this.getVertices();
         
        if( edges.isEmpty())this.getEdges();
            
         return new Graph(nodes, edges);
    }

        
    
    private void getVertices(){
         String sql = "SELECT * FROM Vertex;";
         try {
                ResultSet rs;
                database.connect("central");
                rs = database.query(sql);

                while (rs.next()) {
                    Vertex vertex = new Vertex(Integer.toString(rs.getInt("id")), rs.getString("country"),
                            rs.getString("city"),Double.parseDouble(rs.getString("lat")), 
                            Double.parseDouble(rs.getString("lon") ));
                    nodes.add(vertex);
                }

                database.closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        
    }
    private void getEdges(){
         String sql = "SELECT * FROM Edge;";
         try {
                ResultSet rs;
                database.connect("central");
                rs = database.query(sql);

                while (rs.next()) {
                    Vertex source = this.getVertexWhereId(Integer.toString(rs.getInt("source")));
                    Vertex destination = this.getVertexWhereId(Integer.toString(rs.getInt("destination")));
                    Edge edge = new Edge(Integer.toString(rs.getInt("id")), 
                            source, destination, rs.getDouble("weight"));
                    edges.add(edge);
                }

                database.closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }
    public boolean addVertex(String country, String city, double lat, double lon){
        String sql = "INSERT INTO Vertex(country, city, lat, lon "
                + ") VALUES ('"+country+"','"+city+"','"+lat+"','"+lon+"');";
        if(!exists(country, city)){
            database.connect("central");
            try {
                database.update(sql);
                int id = database.getTableCount("Vertex");
                Vertex vertex = new Vertex(Integer.toString(id), country, city, 
                        lat,lon);
                nodes.add(vertex);
            } catch (SQLException ex) {
                ex.printStackTrace();
                if (ex.getErrorCode() == 0) {

                }
            }
            database.closeConnection();
            return true;
        }else{
            return false;
        }
        
    }
    private boolean exists(String newCountry, String newCity){
        boolean exists = false;
        for(int i = 0; i < nodes.size(); i++){
            Vertex node = nodes.get(i);
            String city = node.getCity();
            String country = node.getCountry();
            if(newCountry.equals(country) && newCity.equals(city)){
                exists = true;
                break;
            }
        }
        return exists;
    }
    /*public void editVertex(Vertex vertex, String country, String city, String lat, String lon){
        
    }*/
    public void addEdge(Vertex source, Vertex destination){
        double sourceLat = source.getLat();
        double destinationLat = destination.getLat();
        double sourceLon = source.getLong();
        double destinationLon = destination.getLong();
        double weight = this.computeWeight(sourceLat, destinationLat, sourceLon, destinationLon, 0.0, 0.0);
        String sql = "INSERT INTO Edge(source, destination, weight "
                + ") VALUES ('"+source.getId()+"','"+destination.getId()+"','"
                + ""+weight+"');";
        if(!edgeAlreadyExists(source, destination)){
                database.connect("central");
                try {
                    database.update(sql);
                    int id = database.getTableCount("Edge");
                    Edge edge = new Edge(Integer.toString(id), source, destination, weight);
                    edges.add(edge);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    if (ex.getErrorCode() == 0) {

                    }
                }
                database.closeConnection();
        }else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("EDGE EXISTS");
            alert.setHeaderText("EDGE with SRC: "+source.getCity()+", "+source.getCountry()+" "
                    + "DEST: "+destination.getCity()+", "+destination.getCountry()+" \nALREADY EXISTS ADD ANOTHER EDGE!");
            alert.setContentText("Add another edge!");
            alert.showAndWait();
            return;
        }
    }
    private boolean edgeAlreadyExists(Vertex newSource, Vertex newDestination){
        boolean exists = false;
        for(int i = 0 ; i < edges.size(); i ++){
            Edge edge = edges.get(i);
            Vertex source = edge.getSource();
            Vertex destination = edge.getDestination();
            if((source == newSource)&& (destination == newDestination)){
                exists = true;
                break;
            }
        }
        return exists;
    }
    public void editEdge(Edge edge, Vertex source, Vertex destination){
        double sourceLat = source.getLat();
        double destinationLat = destination.getLat();
        double sourceLon = source.getLong();
        double destinationLon = destination.getLong();
        double weight = this.computeWeight(sourceLat, destinationLat, sourceLon, destinationLon, 0.0, 0.0);
        edge.setSource(source);
        edge.getDestination();
        edge.setWeight(weight);
        String sql = "UPDATE Edge SET  source='"+Integer.parseInt(source.getId())+"', "
                + "destination='"+Integer.parseInt(destination.getId())+"', "
                + "weight='"+weight+"' WHERE id='"+Integer.parseInt(edge.getId())+"';";
        database.connect("central");
        try {
            database.update(sql);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        database.closeConnection();
        
    }
    public Vertex getVertexWhereId(String id){
        Vertex vertex = null;
        for(int i = 0; i < nodes.size(); i++){
            vertex = nodes.get(i);
            if(vertex.getId().equals(id)){
                break;
            }
        }
        return vertex;
    }
   
 /**https://stackoverflow.com/questions/3694380/calculating-distance-between-two-points-using-latitude-longitude-what-am-i-doi
 * Calculate distance between two points in latitude and longitude taking
 * into account height difference. If you are not interested in height
 * difference pass 0.0. Uses Haversine method as its base.
 * 
 * lat1, lon1 Start point lat2, lon2 End point el1 Start altitude in meters
 * el2 End altitude in meters
 * @returns Distance in Meters
 */
    public static double computeWeight(double lat1, double lat2, double lon1,
            double lon2, double el1, double el2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }
    
    
}