/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.TOMS.algorithms.disjkstra.algorithm;

import com.project.TOMS.algorithms.dijkstra.model.Edge;
import com.project.TOMS.algorithms.dijkstra.model.Graph;
import com.project.TOMS.algorithms.dijkstra.model.Vertex;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author EfthymiosChatziathanasiadis
 */
public class DijkstraAlgorithmTest {
    private  ArrayList<Vertex> vertices;
    private  ArrayList<Edge> edges;
    private Graph graph;
    
    
    public DijkstraAlgorithmTest() {
    }
    
    @Before
    public void setUp() {
        vertices = setUpVertices();
        edges = setUpEdges();
        graph = new Graph(vertices, edges);      
    }
    
    
    /**
     * Test of Dijkstra Algorithm 
     */
    @Test
    public void testDijkstraAlgorithm() {
        System.out.println("Test Dijkstra Algorithm");
        
        Vertex source = vertices.get(0);//A
        Vertex destination = vertices.get(4);//E
        System.out.println("Source: A Destination: E");
        DijkstraAlgorithm instance = new DijkstraAlgorithm(graph);
        instance.dijkstraExecute(source);
        ArrayList<Vertex> expectedShortestPath = new ArrayList<Vertex>();
        System.out.println("Expected shortest path --> A-I-J-K-E");
        //expected shortest path --> A-I-J-K-E
        expectedShortestPath.add(vertices.get(0));//A
        expectedShortestPath.add(vertices.get(8));//I
        expectedShortestPath.add(vertices.get(9));//J
        expectedShortestPath.add(vertices.get(10));//K
        expectedShortestPath.add(vertices.get(4));//E
        
        ArrayList<Vertex> result = instance.getShortestPath(destination);
        System.out.println("Actual shortest path --> "+result.get(0).getCity()+"-"+
                result.get(1).getCity()+"-"+result.get(2).getCity()+"-"
                +result.get(3).getCity()+"-"+result.get(4).getCity());
        
        assertTrue(equals(expectedShortestPath, result));
        
    }
    
    private  ArrayList<Vertex> setUpVertices(){
        vertices = new ArrayList<Vertex>();
        vertices.add(new Vertex("0", "A", "A", 0, 0));
        vertices.add(new Vertex("1", "B", "B",  0, 0 ));
        vertices.add(new Vertex("2", "C", "C",  0, 0 ));
        vertices.add(new Vertex("3", "D", "D",  0, 0 ));
        vertices.add(new Vertex("4", "E", "E",  0, 0 ));
        vertices.add(new Vertex("5", "F", "F",  0, 0 ));
        vertices.add(new Vertex("6", "G", "G", 0, 0 ));
        vertices.add(new Vertex("7", "H", "H",  0, 0));
        vertices.add(new Vertex("8", "I", "I", 0, 0 ));
        vertices.add(new Vertex("9", "J", "J", 0, 0 ));
        vertices.add(new Vertex("10", "K", "K", 0, 0 ));
        vertices.add(new Vertex("11", "L", "L", 0, 0 ));
        vertices.add(new Vertex("12", "M", "M", 0, 0 ));
        vertices.add(new Vertex("12", "N", "N", 0, 0 ));
        
        return vertices;
    }
    private  ArrayList<Edge> setUpEdges(){
        edges = new ArrayList<Edge>();
        //Possible paths from Athens, Greece to FrankFurt, Germany
        //A-B-C-D-E
        edges.add(new Edge("1", vertices.get(0), vertices.get(1), 100));
        edges.add(new Edge("2", vertices.get(1), vertices.get(2), 100));
        edges.add(new Edge("3", vertices.get(2), vertices.get(3), 100));
        edges.add(new Edge("4", vertices.get(3), vertices.get(4), 100));
        //A-F-G-H-E
        edges.add(new Edge("5", vertices.get(0), vertices.get(5), 50));
        edges.add(new Edge("6", vertices.get(5), vertices.get(6), 50));
        edges.add(new Edge("7", vertices.get(6), vertices.get(7), 50));
        edges.add(new Edge("8", vertices.get(7), vertices.get(4), 50));
        //A-I-J-K-E --> shortest path
        edges.add(new Edge("9", vertices.get(0), vertices.get(8), 10));
        edges.add(new Edge("10", vertices.get(8), vertices.get(9), 10));
        edges.add(new Edge("11", vertices.get(9), vertices.get(10), 10));
        edges.add(new Edge("12", vertices.get(10), vertices.get(4), 10));
        //A-L-M-N-E
        edges.add(new Edge("13", vertices.get(0), vertices.get(10), 90));
        edges.add(new Edge("14", vertices.get(10), vertices.get(11), 90));
        edges.add(new Edge("15", vertices.get(11), vertices.get(12), 90));
        edges.add(new Edge("16", vertices.get(12), vertices.get(4), 90));
        
        
        return edges;
    }

    private boolean equals(ArrayList<Vertex> expectedShortestPath, ArrayList<Vertex> result) {
        boolean equals = true;
        if(expectedShortestPath.size() != result.size())
            equals = false;
        
        for(int i = 0; i < result.size(); i++){
            if(expectedShortestPath.get(i) != result.get(i)){
                equals = false;
                break;
            }
        }
        return equals;
        
    }
    
}
