package com.cisco.graph

import com.j_spaces.core.IJSpace
import org.openspaces.core.space.UrlSpaceConfigurer
import org.openspaces.core.GigaSpaceConfigurer
import org.openspaces.core.GigaSpace


class SpaceControllerService {

      static transactional = false

      def initSpaceService(String s) {

        IJSpace space = new UrlSpaceConfigurer("jini://*/*/${s}").space()
        GigaSpace gigaSpace = new GigaSpaceConfigurer(space).gigaSpace()

        return gigaSpace

      }
  }
