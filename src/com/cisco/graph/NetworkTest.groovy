package com.cisco.graph


def neodb='/tmp/neo4'

def networkService = new NetworkService()
def graphService = new GraphService()

graphService.cleanDB(neodb)


def db = graphService.init(neodb)


//graphSevice.createKary(db, 12,2)

networkService.createRing(db, 25)
networkService.getVertexAll(db)
networkService.exportML(db)

graphService.shutDown(db)



