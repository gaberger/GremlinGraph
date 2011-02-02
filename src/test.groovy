import com.tinkerpop.gremlin.Gremlin
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraphFactory
import com.tinkerpop.blueprints.pgm.impls.neo4j.Neo4jGraph
import com.tinkerpop.blueprints.pgm.Graph
import com.tinkerpop.blueprints.pgm.Vertex
import com.tinkerpop.blueprints.pgm.util.graphml.GraphMLWriter
/**
 * Created by IntelliJ IDEA.
 * User: gary
 * Date: 1/31/11
 * Time: 12:52 PM
 * To change this template use File | Settings | File Templates.
 */

Gremlin.load()
//    Graph graph = new Neo4jGraph("/tmp/my_graph");
//
//
//    def g = TinkerGraphFactory.createTinkerGraph()
//    def results = []
//    g.v(1).outE.inV >> results
//    println results;

def ports = 48
def switches = 1
def nodes = 4
def trees = 2



Graph g = new Neo4jGraph('/tmp/neo4j1')

for (a in 1..nodes) {
  Vertex x = g.addVertex(null)
  x.setProperty("name", "PNode${a}".toString())
 for (b in 1..nodes) {
    Vertex y = g.addVertex(null)
    y.setProperty("name", "LNode${b}".toString())
    g.addEdge(null, x, y, "link")
    for (c in 1..nodes) {
      Vertex z = g.addVertex(null)
      y.setProperty("name", "SNode${b}".toString())
      g.addEdge(null, y, z, "link")
    }
  }
}

////Spine Stage n
//for (def st in 1..trees) {
//  Vertex node = g.addVertex(null)
//  node.setProperty("name", "SNode${st}".toString())
//
////Processing Nodes
//  for (def n in 1..kary) {
//    Vertex node = g.addVertex(null)
//    node.setProperty("name", "PNode${n}".toString())
//    //Communication Switches
//
//  }

//}

//  for (def p in 1..ports){
//    Vertex pt = g.addVertex(null)
//      pt.setProperty("name","Port${p}".toString())
//      g.addEdge(null, sw, pt, "link" )
//}
//}

GraphMLWriter.outputGraph(g, new FileOutputStream("/tmp/graph-example-2.graphml"))
//g:save('/tmp/my-graph.graphml')

//m = [:]; c = 0;

//g.V.outE.inV.groupCount(m).loop(3){c++ < 1000}
//
//m.sort{a,b -> a.value <=> b.value}
g.shutdown();

