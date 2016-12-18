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

import org.restlet.Application;
import org.restlet.Restlet;

public class RESTfulDDSRootURI extends Application {

   /**
    * Creates a root Restlet that will receive all incoming calls.
    */

   private String rootURI;

   public RESTfulDDSRootURI(String _rootURI) {
      super();
      rootURI = _rootURI;
   }

   @Override
   public synchronized Restlet createRoot() {
      return new NonCachedDirectory(getContext(), rootURI);
   }

}
