import com.tinkerpop.gremlin.Gremlin
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraphFactory
import com.tinkerpop.blueprints.pgm.impls.neo4j.Neo4jGraph
import com.tinkerpop.blueprints.pgm.Graph
import com.tinkerpop.blueprints.pgm.Vertex
import com.tinkerpop.blueprints.pgm.util.graphml.GraphMLWriter
import com.tinkerpop.blueprints.pgm.Index


//max nodes
//k**h


tree(16,2)


def tree(k,h) {
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



