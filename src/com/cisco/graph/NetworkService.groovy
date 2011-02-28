package com.cisco.graph


import com.tinkerpop.blueprints.pgm.Vertex

//max nodes
//k**h

//kary(4, 2)



class NetworkService {

    //  param n = number of nodes
    public void createRing(graphService, numOfNodes) {

        println("EVENT | createRing | STARTED");
        println "VAR   | numofNodes | ${numOfNodes}"

        //create Nodes
        for (a in 1..(numOfNodes)) {
            Vertex vertex = graphService.addVertex(null)
            vertex.type = "NODE"
        }

        def firstNode = graphService.v(1)
        def lastNode = graphService.v(numOfNodes)
        def edge = graphService.addEdge(null, lastNode, firstNode, "link")

        for (a in 1..numOfNodes - 1) {
            def x = graphService.v(a)
            def y = graphService.v(a + 1)
            graphService.addEdge(null, x, y, "link")
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



    public void createBcube(graphService, n, k) {
        println("EVENT | createBcube | STARTED");

//Create switch vertices


        for (a in 0..k) {
            for (b in 0..n - 1) {
                Vertex x = graphService.addVertex(null)
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



        for (Vertex v in graphService.V[[level: k - 1]]) {
            for (def a in 0..n ** k - 1) {
                //for (def b in 0..n ** k - 1) {
                Vertex x = graphService.addVertex(null)
                x.name = "${v.slot}${a}".toString()
                x.type = "NODE"
                x.slot = a
                graphService.addEdge(null, v, x, 'link')

                //}
            }

        }



        for (Vertex v in graphService.V[[type: "SPINE"]]) {
            for (Vertex x in graphService.V[[type: "NODE"]]) {
                if (v.slot == x.slot) {
                    graphService.addEdge(null, v, x, "external-link")
                }
            }
        }


    }






    interface createNetwork {
        def types = ["kary", "bcube", "hypcube"]
    }

    interface createForwardingDevice {

    }


    def getVertexAll(graphService) {
        println("EVENT | getVertexAll | STARTED");
        for (Vertex v: graphService.V) {
            println "ID: ${v.id} TYPE: ${v.type}"
        }
    }


    def getNodes(def nodeindex, def type) {


        def x = g.V[nodeindex].outE[[type: type]].inV.addr
        println x
    }

}
