import com.tinkerpop.gremlin.Gremlin
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraphFactory
import com.tinkerpop.blueprints.pgm.impls.neo4j.Neo4jGraph
import com.tinkerpop.blueprints.pgm.Graph
import com.tinkerpop.blueprints.pgm.Vertex
import com.tinkerpop.blueprints.pgm.util.graphml.GraphMLWriter
import com.tinkerpop.blueprints.pgm.Index

//max nodes
//k**h


kary(12, 3)


def tree(k, h) {
  Gremlin.load()
  Graph g = new Neo4jGraph('/tmp/neo4')

//create spine first
  for (a in 1..k) {
    Vertex x = g.addVertex(null)
    x.setProperty("name", "SNode${a}".toString())
    x.setProperty("type", "spine")
  }

//create leaf
  for (a in 1..k) {
    Vertex x = g.addVertex(null)
    x.setProperty("name", "LNode")
    x.setProperty("type", "leaf")
  }

//Connect leaf to spine

  for (Vertex v: g.V[[type: 'spine']]) {
    for (Vertex w: g.V[[type: 'leaf']]) {
      g.addEdge(null, v, w, "link-s")
    }
  }

//Create nodes and connect to leaf switches

  for (Vertex v: g.V[[type: 'leaf']]) {    //get a leaf node
    //add Nodes
    for (a in 1..k) {
      Vertex x = g.addVertex(null)
      x.setProperty("name", "PNode")
      x.setProperty("type", "computeNode")
      g.addEdge(null, x, v, 'link')
    }


  }


  GraphMLWriter.outputGraph(g, new FileOutputStream("/tmp/graph-example-2.graphml"))
  g.shutdown();
}




def kary(k, h) {
  Gremlin.load()
  Graph g = new Neo4jGraph('/tmp/neo4')

//Create vertices
  for (i in 1..h) {
    for (a in 1..k) {
      Vertex x = g.addVertex(null)
      x.setProperty("name", i + "," + a)
      x.setProperty("type", i)
      // x.setProperty("type", "spine")
    }
  }

  //Connect tree
  for (j in 1..h - 1) {   // Each tree level
    for (Vertex v: g.V[[type: j]]) {
      for (l in 1..k) {
        for (Vertex w: g.V[[type: j + 1]]) {
          if (v != w) {
            g.addEdge(null, w, v, "link")
          }
        }
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
      x.setProperty("type", h+1)
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