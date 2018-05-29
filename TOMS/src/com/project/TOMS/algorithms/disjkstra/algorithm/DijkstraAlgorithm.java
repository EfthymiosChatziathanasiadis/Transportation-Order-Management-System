/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.TOMS.algorithms.disjkstra.algorithm;

import com.project.TOMS.algorithms.dijkstra.model.Vertex;
import com.project.TOMS.algorithms.dijkstra.model.Edge;
import com.project.TOMS.algorithms.dijkstra.model.Graph;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author EfthymiosChatziathanasiadis
 */
public class DijkstraAlgorithm {

    private final ArrayList<Vertex> nodes;
    private final ArrayList<Edge> edges;
    private Set<Vertex> settledNodes;
    private Set<Vertex> unSettledNodes;
    private Map<Vertex, Vertex> predecessors;
    private Map<Vertex, Double> distance;

    public DijkstraAlgorithm(Graph graph) {
        // load graph
        this.nodes = new ArrayList<Vertex>(graph.getVertexes());
        this.edges = new ArrayList<Edge>(graph.getEdges());
    }
    //Dijkstra algorithm
    public void dijkstraExecute(Vertex source) {
        settledNodes = new HashSet<Vertex>();
        unSettledNodes = new HashSet<Vertex>();
        distance = new HashMap<Vertex, Double>();
        predecessors = new HashMap<Vertex, Vertex>();
        distance.put(source, 0.0);
        unSettledNodes.add(source);
        while (unSettledNodes.size() > 0) {
            Vertex node = getMinimum(unSettledNodes);
            settledNodes.add(node);
            unSettledNodes.remove(node);
            relaxation(node);
        }
    }
    /*This procedure updates the distances of all vertices u that are neighbours
    *of v if there is any improvement in the estimated shortest path distance to u 
    *by including (u,v) in the path to v.
    */
    private void relaxation(Vertex node) {
        List<Vertex> adjacentNodes = getAdjacentNodes(node);
        for (Vertex target : adjacentNodes) {
            if (getShortestDistance(target) > getShortestDistance(node)
                    + getWeight(node, target)) {
                distance.put(target, getShortestDistance(node)
                        + getWeight(node, target));
                predecessors.put(target, node);
                unSettledNodes.add(target);
            }
        }

    }

    private double getWeight(Vertex node, Vertex target) {
        for (Edge edge : edges) {
            if (edge.getSource().equals(node)
                    && edge.getDestination().equals(target)) {
                return edge.getWeight();
            }
        }
        throw new RuntimeException("Should not happen");
    }

    private List<Vertex> getAdjacentNodes(Vertex node) {
        List<Vertex> neighbors = new ArrayList<Vertex>();
        for (Edge edge : edges) {
            if (edge.getSource().equals(node)
                    && !settled(edge.getDestination())) {
                neighbors.add(edge.getDestination());
            }
        }
        return neighbors;
    }

    private Vertex getMinimum(Set<Vertex> vertexes) {
        Vertex minimum = null;
        for (Vertex vertex : vertexes) {
            if (minimum == null) {
                minimum = vertex;
            } else {
                if (getShortestDistance(vertex) < getShortestDistance(minimum)) {
                    minimum = vertex;
                }
            }
        }
        return minimum;
    }

    private boolean settled(Vertex vertex) {
        return settledNodes.contains(vertex);
    }

    private double getShortestDistance(Vertex destination) {
        Double d = distance.get(destination);
        if (d == null) {
            return Integer.MAX_VALUE;
        } else {
            return d;
        }
    }

    /*
     * Returns the path from source to destination
     */
    public ArrayList<Vertex> getShortestPath(Vertex target) {
        ArrayList<Vertex> path = new ArrayList<Vertex>();
        Vertex step = target;
        // check if a path exists
        if (predecessors.get(step) == null) {
            return null;
        }
        path.add(step);
        while (predecessors.get(step) != null) {
            step = predecessors.get(step);
            path.add(step);
        }
        Collections.reverse(path);
        return path;
    }

}