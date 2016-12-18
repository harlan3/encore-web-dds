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

import org.restlet.Context;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;
import org.restlet.resource.ResourceException;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;

public class RootResourceAPI extends Resource {

   public RootResourceAPI(Context context, Request request, Response response) {
      super(context, request, response);
      setModifiable(true);
      getVariants().add(new Variant(MediaType.TEXT_PLAIN));
      
      /* Allow access across domains */
      Form responseHeaders = (Form) response.getAttributes().get(
            "org.restlet.http.headers");
      if (responseHeaders == null) {
         responseHeaders = new Form();
         response.getAttributes().put("org.restlet.http.headers",
               responseHeaders);
      }
      responseHeaders.add("Access-Control-Allow-Origin", "*");
   }

   @Override
   public Representation represent(Variant variant) throws ResourceException {

      Representation result = null;

      if ((variant != null)
            && variant.getMediaType().equals(MediaType.TEXT_PLAIN)) {
         DDSRootProxy rootProxy = ((RESTfulDDSAPI) getApplication())
               .getRootProxy();
         result = new StringRepresentation(rootProxy.getParticipants(),
               MediaType.TEXT_PLAIN);
      }

      if (result != null) {
         getResponse().setStatus(Status.SUCCESS_OK);
      } else {
         getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
      }

      return result;
   }

}
