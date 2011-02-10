package com.cisco.graph

import org.openspaces.core.GigaSpace
import com.gigaspaces.query.ISpaceQuery




class RouterService {

  def SpaceControllerService





  def addFIBEntry = {  sp, router, address, port ->
        def fib = router.FIB
        router.FIB = fib +[(address.toString()) : port]
        router.FIB.
        sp.write(router)
  }

  def delFIBEntry = { sp, router, address, port ->
        Router r = sp.getB

  }


  def createRouter = { name, ports ->
    Router router = new Router()
    router.numports = ports
    router.name = name


    for (i in 1..router.numports) {
      router.ports.add(i)

    }
    return router

  }

  def addNextHop = { address, port ->


  }

  public static void main(String[] arg) {

    def rs = new RouterService()
    def si = new SpaceControllerService()
    GigaSpace sp = si.initSpaceService("space")


    def router = rs.createRouter("router1", 48)

    sp.write(router)

    Router template = new Router()
    template.setName("router1")


    Router  b = sp.read(template)
    def c = sp.readById(Router.class, "router1")

    for (z in 1..200){
    rs.addFIBEntry(sp, c, "1.1.1.${z}","1")
    }


    println "Router Name = ${b}"
    println "Router ID = ${c.name}"

  }

}