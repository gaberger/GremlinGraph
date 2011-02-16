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


class NetworkService{

def kary(n, k) {
  Gremlin.load()
  Graph g = new Neo4jGraph('/tmp/neo4')

//Create vertices

  for (a in 0..k-1) {
    for (b in 0..n - 1) {
      Vertex x = g.addVertex(null)
      def id  = "${a},${b}"
      x.name  =  id.toString()
      x.level =  a
      x.slot  =  b
      if (x.level == 0) {
        x.type =  "LEAF"
      }
      if (x.level == 1){
        x.type = "SPINE"
      }
    }
  }


  //Connect tree
  for (Vertex v: g.V[[type: "SPINE"]]) {
    for (Vertex w: g.V[[type: "LEAF"]]) {
      if (v != w) {
        def z = g.addEdge(null, w, v, "link")
        z.cost    =  "n"
        z.inport  =  v.name
        z.outport =  w.name

      }
    }
  }


  for (Vertex v: g.V[[level: k-2]]) {    //get a leaf node
    //add Nodes
    for (a in 0..n-1) {
      Vertex x = g.addVertex(null)
      x.name = "computeNode"
      x.type = "NODE"
      x.addr = "1.1.1.1"
      g.addEdge(null, v, x, 'link')
    }
  }







  GraphMLWriter.outputGraph(g, new FileOutputStream("/tmp/graph-example-2.graphml"))
  g.shutdown();

}

//  //Full mesh
//  for (j in 1..h - 1) {   // Each tree level
//    for (Vertex v: g.V[[type: j]]) {
//      for (l in 1..k) {
//        for (Vertex w: g.V[[type: j + 1]]) {
//          if (v != w) {
//            g.addEdge(null, v, w, "link")
//          }
//        }
//      }
//    }
//  }


def bcube(n, k) {
  Gremlin.load()
  Graph g = new Neo4jGraph('/tmp/neo4')

//Create switch vertices


  for (a in 0..k) {
    for (b in 0..n - 1) {
      Vertex x = g.addVertex(null)
      def id = "${a},${b}"

      x.name  = id.toString()
      x.level = a
      x.slot  = b
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
      x.slot =  a
      g.addEdge(null, v, x, 'link')

      //}
    }

  }



  for (Vertex v in g.V[[type: "SPINE"]]) {
    for (Vertex x in g.V[[type: "NODE"]]){
      if (v.slot == x.slot) {
        g.addEdge(null, v, x, "external-link")
      }
    }
  }




  GraphMLWriter.outputGraph(g, new FileOutputStream("/tmp/graph-example-2.graphml"))
  g.shutdown();

}
}