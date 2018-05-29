/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.TOMS.algorithms.dijkstra.model;

import java.util.ArrayList;

/**
 *
 * @author EfthymiosChatziathanasiadis
 */
public class Graph {
    private final ArrayList<Vertex> vertexes;
    private final ArrayList<Edge> edges;

    public Graph(ArrayList<Vertex> vertexes, ArrayList<Edge> edges) {
        this.vertexes = vertexes;
        this.edges = edges;
    }

    public ArrayList<Vertex> getVertexes() {
        return vertexes;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }



}
