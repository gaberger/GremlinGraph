package com.cisco.graph


def neodb='/tmp/neo4'

def networkService = new NetworkService()
def graphService = new GraphService()

graphService.cleanDB(neodb)


def db = graphService.initGraphService(neodb)


//graphSevice.createKary(db, 12,2)

networkService.createRing(db, 5)
networkService.getVertexAll(db)

graphService.shutDown(db)



