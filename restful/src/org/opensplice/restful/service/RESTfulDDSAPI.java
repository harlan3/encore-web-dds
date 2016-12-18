/*
 *  RESTful DDS is a prototyping effort to demonstrate how DDS could be combined
 *  with HTTP clients, extending the realm of pub/sub distribution to true wide
 *  area networking.
 *
 *  Original API Definition
 *  Copyright (C) 2009 PrismTech Ltd.
 *  reinier.torenbeek@gmail.com
 *
 *  Extended API for Encore Web DDS
 *  Copyright (C) 2012 Harlan Murphy
 *  orbisoftware@gmail.com
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

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.Router;

public class RESTfulDDSAPI extends Application {

   private DDSRootProxy rootProxy;

   public RESTfulDDSAPI() {
      super();
      rootProxy = new DDSRootProxy();
   }

   /**
    * Creates a root Restlet that will receive all incoming calls.
    */
   @Override
   public synchronized Restlet createRoot() {
      // Create a router Restlet that defines routes.
      Router router = new Router(getContext());

      URI_Router.attachApplicationURIs(router);

      // Defines a route for querying all available participants
      router.attach("/", RootResourceAPI.class);

      // Defines a route for the resource "read"
      router.attach(
            "/{participantname}/{partitionname}/{topicname}/{typename}/subscribe/{datareadername}/read",
            ReadAPI.class);

      // Defines a route for the resource "read_on_change"
      router.attach(
            "/{participantname}/{partitionname}/{topicname}/{typename}/subscribe/{datareadername}/read_on_change",
            ReadOnChangeAPI.class);

      // Defines a route for the resource "read_instance"
      router.attach(
            "/{participantname}/{partitionname}/{topicname}/{typename}/subscribe/{datareadername}/read_instance/{keyname}/{instanceid}",
            ReadInstanceAPI.class);

      // Defines a route for the resource "read_instance_on_change"
      router.attach(
            "/{participantname}/{partitionname}/{topicname}/{typename}/subscribe/{datareadername}/read_instance_on_change/{keyname}/{instanceid}",
            ReadInstanceOnChangeAPI.class);

      // Defines a route for the resource "take"
      router.attach(
            "/{participantname}/{partitionname}/{topicname}/{typename}/subscribe/{datareadername}/take",
            TakeAPI.class);

      // Defines a route for the resource "take_instance"
      router.attach(
            "/{participantname}/{partitionname}/{topicname}/{typename}/subscribe/{datareadername}/take_instance/{keyname}/{instanceid}",
            TakeInstanceAPI.class);

      // Defines a route for the resource "write"
      router.attach(
            "/{participantname}/{partitionname}/{topicname}/{typename}/publish/{datawritername}/write",
            WriteAPI.class);

      return router;
   }

   public DDSRootProxy getRootProxy() {
      return rootProxy;
   }

}
