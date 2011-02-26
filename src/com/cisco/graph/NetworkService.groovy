package com.cisco.graph

import com.tinkerpop.gremlin.Gremlin

import com.tinkerpop.blueprints.pgm.impls.neo4j.Neo4jGraph
import com.tinkerpop.blueprints.pgm.Graph
import com.tinkerpop.blueprints.pgm.Vertex
import com.tinkerpop.blueprints.pgm.util.graphml.GraphMLWriter

//max nodes
//k**h

//kary(4, 2)

// bcube n=number of nodes in a BCube, k=number of levels, Level 0 = spine
//bcube(n,k)
//bcube(8, 1)
//kary(n,k)
//kary(8,2)


class NetworkService {

    //  param n = number of nodes
    public void createRing(graphService, numOfNodes) {

        println("EVENT | createRing | STARTED");
        println "VAR   | numofNodes= ${numOfNodes}"

        //create Nodes
        for (a in 0..(numOfNodes - 1)) {
            Vertex vertex = graphService.addVertex(null)
            vertex.type = "NODE"
        }

           for (Vertex x in graphService.V[[type: "NODE"]]) {
                x.each{
                    println it.id
                }
              if(x.id ==1)   {println "ID | 1"}


                   //graphService.addEdge(null, x[5], x[1], "external-link")
                }


    }

    def createStar(graphService, n, k) {
               println("EVENT | createStar | STARTED");

    }



    public void createKary(g, n, k) {
         println("EVENT | createKary | STARTED");


        for (a in 0..k - 1) {       //Create vertices
            for (b in 0..n - 1) {
                Vertex x = g.addVertex(null)
                def id = "${a},${b}"
                x.name = id.toString()
                x.level = a
                x.slot = b
                if (x.level == 0) {
                    x.type = "LEAF"
                }
                if (x.level == 1) {
                    x.type = "SPINE"
                }
            }
        }

        //Connect tree
        for (Vertex v: g.V[[type: "SPINE"]]) {
            for (Vertex w: g.V[[type: "LEAF"]]) {
                if (v != w) {
                    def z = g.addEdge(null, w, v, "link")
                    z.cost = "n"
                    z.inport = v.name
                    z.outport = w.name
                    z.type = "Backbone"

                }
            }
        }


        for (Vertex v: g.V[[level: k - 2]]) {    //get a leaf node
            //add Nodes
            for (a in 0..n - 1) {
                Vertex x = g.addVertex(null)
                x.name = "computeNode"
                x.type = "NODE"
                def addr = "1.1.1.${v.slot}"
                x.addr = addr.toString()
                def z = g.addEdge(null, v, x, 'link')
                z.type = "NodeLink"
            }
        }
    }



    public void createBcube(n, k) {

//Create switch vertices


        for (a in 0..k) {
            for (b in 0..n - 1) {
                Vertex x = g.addVertex(null)
                def id = "${a},${b}"

                x.name = id.toString()
                x.level = a
                x.slot = b
                if (x.level == 0) {
                    x.type = "LEAF"
                } else {
                    x.type = "SPINE"
                }
            }
        }



        for (Vertex v in g.V[[level: k - 1]]) {
            for (def a in 0..n ** k - 1) {
                //for (def b in 0..n ** k - 1) {
                Vertex x = g.addVertex(null)
                x.name = "${v.slot}${a}".toString()
                x.type = "NODE"
                x.slot = a
                g.addEdge(null, v, x, 'link')

                //}
            }

        }



        for (Vertex v in g.V[[type: "SPINE"]]) {
            for (Vertex x in g.V[[type: "NODE"]]) {
                if (v.slot == x.slot) {
                    g.addEdge(null, v, x, "external-link")
                }
            }
        }




        GraphMLWriter.outputGraph(g, new FileOutputStream("/tmp/graph-example-2.graphml"))
        g.shutdown();

    }



    def exportML(graph) {
        GraphMLWriter.outputGraph(graph, new FileOutputStream("/tmp/graph-example-2.graphml"))

    }











    interface createNetwork {
        def types = ["kary", "bcube", "hypcube"]
    }

    interface createForwardingDevice {

    }


    def getVertexAll(graphService) {
      println("EVENT | getVertexAll | STARTED");
         for (Vertex v: graphService.V) {
            println v.id
            println v.type



        }
    }


    def getNodes(def nodeindex, def type) {


        def x = g.V[nodeindex].outE[[type: type]].inV.addr
        println x
    }

}
