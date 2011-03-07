package com.cisco.graph


import com.tinkerpop.blueprints.pgm.Vertex
import java.security.MessageDigest



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

    def createStar(graphService, n) {
        println("EVENT | createStar | STARTED");

        for (a in 0..n - 1) {
            Vertex vertex = graphService.addVertex(null)
        }
        def anchorNode = graphService.v(1)
        anchorNode.type = "SPINE"

        for (a in 2..n) {
            def x = graphService.v(1)
            def y = graphService.v(a)
            y.type = "NODE"
            graphService.addEdge(null, x, y, "link")
        }

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

    public void sampleNetwork(graphService) {
        def ip = ["192.168.1", "192.168.2", "192.168.3", "192.168.4"]
        def r = new Random()

        //Create a random set of nodes in each subnet
        def seed = 20
        ip.each {
            def nodes = r.nextInt(seed)
            println "Creating nodes for subnet ${it}"
            for (a in 1..nodes) {
                Vertex vx = graphService.addVertex(null)
                def nodeAddr = r.nextInt(254)
                def nodeName = "${it}.${nodeAddr}"
                vx.nodeHash =  calcHash(nodeName.toString())
                vx.nodeName = nodeName.toString()
                vx.network = it
                vx.nodeAddr = nodeAddr
                vx.role = "Access"
            }
        }

//        //Connect all subnet nodes
//        ip.each {
//            println "Created subnet connections for ${it}"
//        for (Vertex v in graphService.V[[network: it]]) {
//            for (Vertex x in graphService.V[[network: it]]) {
//                if (v.name != x.name) {
//                    graphService.addEdge(null, v, x, "subnet-link")
//                }
//            }
//        }
//        }

        //Only want to show node connections to Vertex with connections across subnets?
        //pick a random vertex in each subnet
        def glist = []
        ip.each {
            println "Scanning network ${it}"
            for (Vertex v in graphService.V[[network: it]]) {
                println "Possible node candidate ${v.nodeAddr}"
                v.role = "Gateway"
                glist.add(v)
                break
            }
        }

        //Connect sub-net nodes to Gateway

        glist.each {
            println "Picked random nodes: ${it.nodeName} on network ${it.network}"

            for (Vertex v in graphService.V[[network: it.network]]) {
                if (v.nodeName != it.nodeName) {
                    graphService.addEdge(null, v, it, "Subnet-Link")

                }

            }
        }
        //connect all Gateway Nodes

        for (Vertex v in graphService.V[[role: "Gateway"]]) {
            for (Vertex w in graphService.V[[role: "Gateway"]]) {
                if (v.nodeName != w.nodeName) {
                    graphService.addEdge(null, v, w, "Gateway-Link")
                }
            }
        }

        //connect random Gateway nodes
//        glist = []
//        ip.each {
//            println "Scanning network ${it}"
//            println graphService.V[graphService:rand-nat()]
//
////            for (Vertex v in g[graphService:rand-nat()]) {
////                println "Possible Gateway candidate ${v.nodeAddr}"
////            }
//        }

        //Pick a random set of vertexes within each subnet
        //loop count
//        for (a in 0.1000) {
//            for (Vertex v in graphService.V[[network: ip[r.nextInt(ip.size())]]]) {
//                for (Vertex w in graphService.V[[network: ip[r.nextInt(ip.size())]]]) {
//                    if (v.name != w.name) {
//                        graphService.addEdge(null, v, w, "Link")
//                    }
//                }
//
//            }
//        }


    }






    interface createNetwork {
        def types = ["kary", "bcube", "hypcube"]
    }

    interface createForwardingDevice {

    }


    def getVertexAll(graphService) {
        println("EVENT | getVertexAll | STARTED");
        for (Vertex v: graphService.V) {
            println "ID: ${v.id} HASH:${v.nodeHash}"
        }
    }


    def getNodes(def nodeindex, def type) {


        def x = g.V[nodeindex].outE[[type: type]].inV.addr
        println x
    }

    public calcHash(String source) {

           def messageDigest = MessageDigest.getInstance("SHA1")
            messageDigest.update( source.getBytes() );

            def sha1Hex = new BigInteger(1, messageDigest.digest()).toString(16).padLeft( 40, '0' )

            return sha1Hex
                        /*
            * Why pad up to 40 characters? Because SHA-1 has an output
            * size of 160 bits. Each hexadecimal character is 4-bits.
            * 160 / 4 = 40
            */


    }


    }
