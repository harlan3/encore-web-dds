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
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Resource;
import org.restlet.resource.Variant;

public abstract class InstanceReaderResource extends Resource {

   private DDSReaderProxy reader;
   protected String keyField;
   protected int instanceID;

   public InstanceReaderResource(Context context, Request request,
         Response response) {
      super(context, request, response);

      String participantName = (String) request.getAttributes().get(
            "participantname");
      String partitionName = (String) request.getAttributes().get(
            "partitionname");
      String topicName = (String) request.getAttributes().get("topicname");
      String typeName = (String) request.getAttributes().get("typename");
      String datareaderName = (String) request.getAttributes().get(
            "datareadername");

      keyField = (String) request.getAttributes().get("keyname");
      instanceID = Integer.parseInt((String) request.getAttributes().get(
            "instanceid"));

      DDSRootProxy rootProxy = ((RESTfulDDSAPI) getApplication())
            .getRootProxy();
      DDSParticipantProxy participantProxy = rootProxy
            .lookupOrCreateParticipant(participantName);
      DDSTopicProxy topicProxy = participantProxy.lookupTopic(topicName,
            typeName);
      if (topicProxy != null) {
         DDSSubscriberProxy subscriberProxy = participantProxy
               .lookupOrCreateSubscriber(partitionName);
         reader = subscriberProxy.lookupOrCreateReader(datareaderName,
               topicName, typeName, topicProxy.getTopic());
         if (reader != null) {
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

   protected DDSReaderProxy getReader() {
      return reader;
   }
}
