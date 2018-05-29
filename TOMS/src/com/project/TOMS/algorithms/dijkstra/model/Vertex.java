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
public class Vertex {
    final private String id;
    final private String country;
    final private String city;
    final private double lat;
    final private double lon;


    public Vertex(String id, String country, String city, double lat, double lon) {
        this.id = id;
        this.country = country;
        this.city = city;
        this.lat = lat;
        this.lon = lon;
    }

    public Vertex() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    public String getId() {
        return id;
    }

    public String getCountry() {
        return this.country;
    }
    public String getCity(){
        return this.city;
    }
    public double getLat(){
        return this.lat;
    }
    public String getLatitude(){
        return this.lat+"";
    }
    public double getLong(){
        return this.lon;
    }
    public String getLongt(){
        return this.lon+"";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Vertex other = (Vertex) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return this.city+", "+this.country;
    }

}
