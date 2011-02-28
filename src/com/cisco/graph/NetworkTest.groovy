package com.cisco.graph


def neodb = '/tmp/neo4'

def networkService = new NetworkService()
def graphService = new GraphService()

graphService.cleanDB(neodb)


def db = graphService.init(neodb)

//networkService.createKary(db, 12,2)
networkService.createRing(db, 25)
//networkService.createBcube(db, 8, 1)
// bcube n=number of nodes in a BCube, k=number of levels, Level 0 = spine




networkService.getVertexAll(db)
graphService.exportML(db)

graphService.shutDown(db)



