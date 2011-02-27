package com.cisco.graph

import com.tinkerpop.gremlin.Gremlin

import com.tinkerpop.blueprints.pgm.impls.neo4j.Neo4jGraph
import com.tinkerpop.blueprints.pgm.Vertex
import com.tinkerpop.blueprints.pgm.util.graphml.GraphMLWriter


class GraphService {
    static {
        println "EVENT | Bringing up Gremlin"
        Gremlin.load()

    }

    public init(db) {
        println "EVENT | Bringing up Neo4j | ${db}"
        def graphService = new Neo4jGraph(db)


        return graphService
    }

    public void shutDown(graphService) {
        println("EVENT | Shutting down Neo4J graph Db service...");
        // indexService.shutdown();
        graphService.shutdown();
    }

    public void cleanDB(directory) {
        println("EVENT | CLEANING DB | ${directory}");
        def dir = new File(directory)
        dir.deleteDir()

    }

    public void exportML(graph) {
        println "EVENT | exportML | ${graph}"
        GraphMLWriter.outputGraph(graph, new FileOutputStream("/tmp/graph-example-2.graphml"))

    }


}