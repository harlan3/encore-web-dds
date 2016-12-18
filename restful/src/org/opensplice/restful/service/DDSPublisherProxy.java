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

import java.util.HashMap;
import java.util.Map;

import DDS.ANY_STATUS;
import DDS.DataWriter;
import DDS.DataWriterQosHolder;
import DDS.Publisher;
import DDS.Topic;

public class DDSPublisherProxy {

   private Publisher publisher;
   private String name;
   private DDSParticipantProxy participant;
   private Map<String, DDSWriterProxy> writers;

   public DDSPublisherProxy(DDSParticipantProxy _participant, String _name,
         Publisher _publisher) {
      
      participant = _participant;
      name = _name;
      publisher = _publisher;
      writers = new HashMap<String, DDSWriterProxy>();
   }

   public DDSWriterProxy lookupWriter(String writerName, String topicName) {
      
      return writers.get(topicName + "," + writerName);
   }

   private void addWriter(DDSWriterProxy writer, String writerName,
         String topicName) {
      
      writers.put(topicName + "," + writerName, writer);
   }

   public DDSWriterProxy lookupOrCreateWriter(String writerName,
         String topicName, String typeName, Topic topic) {
      
      DDSWriterProxy result = lookupWriter(topicName, writerName);
      
      if (result == null) {

         DataWriterQosHolder dwQos = new DDS.DataWriterQosHolder();
         publisher.get_default_datawriter_qos(dwQos);

         TopicManager topicManager = TopicManager.getInstance();
         DDSTopicInfo topicInfo = topicManager.topicMap.get(topicName);

         if (topicInfo != null) {

            switch (topicInfo.topicQos) {

            case DDS_QOS_PROFILE_2:
               dwQos.value.durability.kind = DDS.DurabilityQosPolicyKind.TRANSIENT_DURABILITY_QOS;
               dwQos.value.history.kind = DDS.HistoryQosPolicyKind.KEEP_LAST_HISTORY_QOS;
               dwQos.value.history.depth = 1;
               break;

            case DDS_QOS_PROFILE_3:
               dwQos.value.reliability.kind = DDS.ReliabilityQosPolicyKind.RELIABLE_RELIABILITY_QOS;
               dwQos.value.history.kind = DDS.HistoryQosPolicyKind.KEEP_LAST_HISTORY_QOS;
               dwQos.value.history.depth = 30;
               break;

            case DDS_QOS_PROFILE_1:
            default:
               break;
            }
         }

         DataWriter writer = publisher.create_datawriter(topic, dwQos.value,
               null, ANY_STATUS.value);
         ErrorHandler.checkHandle(writer, "DDS.Publisher.create_datawriter");

         result = new DDSWriterProxy(this, writerName, writer, typeName);
         addWriter(result, topicName, writerName);
      }
      return result;
   }

   public String getName() {
      
      return name;
   }

   public DDSParticipantProxy getParticipant() {
      
      return participant;
   }
}
