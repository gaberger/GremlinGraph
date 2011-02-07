import com.tinkerpop.gremlin.Gremlin
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraphFactory
import com.tinkerpop.blueprints.pgm.impls.neo4j.Neo4jGraph
import com.tinkerpop.blueprints.pgm.Graph
import com.tinkerpop.blueprints.pgm.Vertex
import com.tinkerpop.blueprints.pgm.util.graphml.GraphMLWriter
import com.tinkerpop.blueprints.pgm.Index

//max nodes
//k**h

//kary(4, 2)

// bcube n=number of nodes in a BCube, k=number of levels, Level 0 = spine
//bcube(n,k)
bcube(8, 1)

//def tree(k, h) {
//  Gremlin.load()
//  Graph g = new Neo4jGraph('/tmp/neo4')
//
////create spine first
//  for (a in 1..k) {
//    Vertex x = g.addVertex(null)
//    x.setProperty("name", "SNode${a}".toString())
//    x.setProperty("type", "spine")
//  }
//
////create leaf
//  for (a in 1..k) {
//    Vertex x = g.addVertex(null)
//    x.setProperty("name", "LNode")
//    x.setProperty("type", "leaf")
//  }
//
////Connect leaf to spine
//
//  for (Vertex v: g.V[[type: 'spine']]) {
//    for (Vertex w: g.V[[type: 'leaf']]) {
//      g.addEdge(null, v, w, "link-s")
//    }
//  }
//
////Create nodes and connect to leaf switches
//
//  for (Vertex v: g.V[[type: 'leaf']]) {    //get a leaf node
//    //add Nodes
//    for (a in 1..k) {
//      Vertex x = g.addVertex(null)
//      x.setProperty("name", "PNode")
//      x.setProperty("type", "computeNode")
//      g.addEdge(null, x, v, 'link')
//    }
//
//
//  }
//
//
//  GraphMLWriter.outputGraph(g, new FileOutputStream("/tmp/graph-example-2.graphml"))
//  g.shutdown();
//}




def kary(k, h) {
  Gremlin.load()
  Graph g = new Neo4jGraph('/tmp/neo4')

//Create vertices
  for (i in 1..h) { // Create a tree
    for (a in 1..k) {
      Vertex x = g.addVertex(null)
      x.setProperty("name", i + "," + a)
      x.setProperty("type", i)

      if (i == 1) {
        x.setProperty("devclass", "SPINE")
      }
      if (i == 2) {
        x.setProperty("devclass", "LEAF")
      }
      // x.setProperty("type", "spine")
    }
  } //h*k

  //Connect tree
  for (Vertex v: g.V[[devclass: "SPINE"]]) {
    for (Vertex w: g.V[[devclass: "LEAF"]]) {
      if (v != w) {
        z = g.addEdge(null, w, v, "link")
        z.setProperty("cost", "n")

      }
    }
  }

//

//Create nodes and connect to leaf switches

  for (Vertex v: g.V[[type: h]]) {    //get a leaf node
    //add Nodes
    for (a in 1..k) {
      Vertex x = g.addVertex(null)
      x.setProperty("name", "computeNode")
      x.setProperty("type", h + 1)
      x.setProperty("devclass", "NODE")
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
      x.setProperty("name", id.toString())
      x.setProperty("level", a)
      x.setProperty("slot", b)
      if (x.level == 0) {
        x.setProperty("type", "LEAF")
      } else {
        x.setProperty("type", "SPINE")
      }
    }
  }



  for (Vertex v in g.V[[level: k - 1]]) {
    for (def a in 0..n ** k - 1) {
      //for (def b in 0..n ** k - 1) {
      Vertex x = g.addVertex(null)
      x.setProperty("name", "${v.slot}${a}".toString())
      x.setProperty("type", "NODE")
      x.setProperty("slot", a)
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