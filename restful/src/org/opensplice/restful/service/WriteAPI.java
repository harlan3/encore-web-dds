/*
 *  RESTful DDS is a prototyping effort to demonstrate how DDS could be combined
 *  with HTTP clients, extending the realm of pub/sub distribution to true wide
 *  area networking.
 *
 *  Copyright (C) 2011 Harlan Murphy
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

import org.restlet.Context;
import org.restlet.data.Form;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Variant;

public class WriteAPI extends WriterResource {

   public WriteAPI(Context context, Request request, Response response) {
      super(context, request, response);
   }

   @Override
   public Representation represent(Variant variant) throws ResourceException {

      Representation result = null;

      return result;
   }

   public void acceptRepresentation(Representation entity)
         throws ResourceException {

      Form form = new Form(entity);
      String postData = form.getFirstValue("samples");

      @SuppressWarnings("unused")
      int status = getWriter().write(postData);

      // Set the response status
      getResponse().setStatus(Status.SUCCESS_CREATED);
   }

}
