/*
 *  RESTful DDS is a prototyping effort to demonstrate how DDS could be combined
 *  with HTTP clients, extending the realm of pub/sub distribution to true wide
 *  area networking.
 *
 *  Copyright (C) 2009 PrismTech Ltd.
 *  reinier.torenbeek@gmail.com
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License Version 3 dated 29 June 2007, as published by the
 *  Free Software Foundation.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with restful-dds; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package org.opensplice.restful.service;

import java.util.logging.Level;

import org.restlet.Component;
import org.restlet.data.Protocol;

public class RESTfulDDSMain {
   
   public static void main(String[] args) {
      
      try {

         String rootURI = null;
         
         if (args.length < 3) {
            System.out
                  .println("Usage: restlet <root URI> <URI Manifest> <port number>");
            System.exit(1);
         } else {
            rootURI = args[0];
            System.out.println("Using root URI " + rootURI);
         }

         // Load URI manifest for topics
         TopicManager topicManager = TopicManager.getInstance();
         topicManager.parseURIManifest(args[1]);
         
         // Create a new Component.
         Component component = new Component();
         component.getLogger().setLevel(Level.OFF);
         component.getLogService().setEnabled(false);

         // Add a new HTTP server listening on the application specific
         // service port number
         component.getServers().add(Protocol.HTTP, Integer.parseInt(args[2]));
         component.getClients().add(Protocol.FILE);

         // Attach the DDS API
         component.getDefaultHost().attach("/dds", new RESTfulDDSAPI());

         // Attach the file server URI
         component.getDefaultHost().attach("/static", new RESTfulDDSRootURI(rootURI));
         
         // Start the component.
         component.start();
      } catch (Exception e) {
         
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }
}
