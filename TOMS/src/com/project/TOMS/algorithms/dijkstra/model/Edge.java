/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.TOMS.algorithms.dijkstra.model;

/**
 *
 * @author EfthymiosChatziathanasiadis
 */
public class Edge  {
    private final String id;
    private  Vertex source;
    private  Vertex destination;
    private  double weight;

    public Edge(String id, Vertex source, Vertex destination, double weight) {
        this.id = id;
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }
    public String getSourceCountry(){
        return this.source.getCountry();
        
    }
    public String getSourceCity(){
        return this.source.getCity();
    }
    
    public String getDestCountry(){
        return this.destination.getCountry();
    }
    public String getDestCity(){
        return this.destination.getCity();
    }
    public String getId() {
        return id;
    }
    public Vertex getDestination() {
        return destination;
    }
    public String getSrc(){
        return this.getSourceCity()+", "+this.getSourceCountry();
    }
    public String getDest(){
        return this.getDestCity()+", "+this.getDestCountry();
    }
    public void setDestination(Vertex vertex){
        this.destination = vertex;
    }

    public Vertex getSource() {
        return source;
    }
    public void setSource(Vertex vertex){
        this.source = vertex;
    }
    public double getWeight() {
        return weight;
    }
    public void setWeight(double weight){
        this.weight = weight;
    }

    @Override
    public String toString() {
        return source + " " + destination;
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
