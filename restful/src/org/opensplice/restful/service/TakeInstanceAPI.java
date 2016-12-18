/*
 *  RESTful DDS is a prototyping effort to demonstrate how DDS could be combined
 *  with HTTP clients, extending the realm of pub/sub distribution to true wide
 *  area networking.
 *
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

import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;

import DDS.ANY_INSTANCE_STATE;
import DDS.ANY_SAMPLE_STATE;
import DDS.ANY_VIEW_STATE;

public class TakeInstanceAPI extends InstanceReaderResource {

   public TakeInstanceAPI(Context context, Request request, Response response) {
      super(context, request, response);
   }

   @Override
   public Representation represent(Variant variant) throws ResourceException {

      Representation result = null;

      if ((variant != null)
            && variant.getMediaType().equals(MediaType.TEXT_PLAIN)) {
         String samples = getReader().takeInstance(keyField, instanceID,
               DDS.LENGTH_UNLIMITED.value, ANY_SAMPLE_STATE.value,
               ANY_VIEW_STATE.value, ANY_INSTANCE_STATE.value);
         result = new StringRepresentation(samples, MediaType.TEXT_PLAIN);
      }
      return result;
   }
}
