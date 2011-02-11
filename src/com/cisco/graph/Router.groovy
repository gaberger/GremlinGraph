package com.cisco.graph

import com.gigaspaces.annotation.pojo.SpaceClass
import com.gigaspaces.annotation.pojo.SpaceId


@SpaceClass
class Router {
  String name
  String type
  Integer numports
  def ports = []
  def FIB  = [:]


  @SpaceId(autoGenerate = true)
  public String getName()  {
    return name
  }

}
