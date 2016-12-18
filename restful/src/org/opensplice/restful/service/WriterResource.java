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
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Resource;
import org.restlet.resource.Variant;

public abstract class WriterResource extends Resource {

   private DDSWriterProxy writer;

   public boolean allowPost() {
      return true;
   }

   public WriterResource(Context context, Request request, Response response) {
      super(context, request, response);

      String participantName = (String) request.getAttributes().get(
            "participantname");
      String partitionName = (String) request.getAttributes().get(
            "partitionname");
      String topicName = (String) request.getAttributes().get("topicname");
      String typeName = (String) request.getAttributes().get("typename");
      String datawriterName = (String) request.getAttributes().get(
            "datawritername");

      DDSRootProxy rootProxy = ((RESTfulDDSAPI) getApplication())
            .getRootProxy();
      DDSParticipantProxy participantProxy = rootProxy
            .lookupOrCreateParticipant(participantName);
      DDSPublisherProxy publisherProxy = participantProxy
            .lookupOrCreatePublisher(partitionName);
      DDSTopicProxy topicProxy = participantProxy.lookupOrCreateTopic(
            topicName, typeName);

      if (topicProxy != null) {
         writer = publisherProxy.lookupOrCreateWriter(datawriterName,
               topicName, typeName, topicProxy.getTopic());
         if (writer != null) {
            getVariants().add(new Variant(MediaType.TEXT_PLAIN));
         }
      }
      
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

   protected DDSWriterProxy getWriter() {
      return writer;
   }
}
