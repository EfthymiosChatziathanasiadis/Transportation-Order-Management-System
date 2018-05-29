/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.TOMS.model.Vehicles;

import com.project.TOMS.algorithms.dijkstra.model.Vertex;


/**
 *
 * @author EfthymiosChatziathanasiadis
 */
public class Trailer extends Vehicle{
  
    
    public Trailer(int id, boolean availabilityStatus, String plateNumber, boolean conditionStatus, Vertex address){
        super(id, availabilityStatus, plateNumber, conditionStatus, address);
    }
    
   
    
}
