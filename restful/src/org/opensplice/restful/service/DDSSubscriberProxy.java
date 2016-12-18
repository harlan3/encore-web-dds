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

import java.util.HashMap;
import java.util.Map;

import DDS.ANY_STATUS;
import DDS.DataReader;
import DDS.DataReaderQosHolder;
import DDS.Subscriber;
import DDS.Topic;

public class DDSSubscriberProxy {

   private Subscriber subscriber;
   private String name;
   private DDSParticipantProxy participant;
   private Map<String, DDSReaderProxy> readers;

   public DDSSubscriberProxy(DDSParticipantProxy _participant, String _name,
         Subscriber _subscriber) {
      participant = _participant;
      name = _name;
      subscriber = _subscriber;
      readers = new HashMap<String, DDSReaderProxy>();
   }

   public DDSReaderProxy lookupReader(String readerName, String topicName) {
      return readers.get(topicName + "," + readerName);
   }

   private void addReader(DDSReaderProxy reader, String readerName,
         String topicName) {
      readers.put(topicName + "," + readerName, reader);
   }

   public DDSReaderProxy lookupOrCreateReader(String readerName,
         String topicName, String typeName, Topic topic) {

      DDSReaderProxy result = lookupReader(topicName, readerName);

      if (result == null) {

         DataReaderQosHolder drQos = new DDS.DataReaderQosHolder();
         subscriber.get_default_datareader_qos(drQos);

         TopicManager topicManager = TopicManager.getInstance();
         DDSTopicInfo topicInfo = topicManager.topicMap.get(topicName);

         if (topicInfo != null) {

            switch (topicInfo.topicQos) {

            case DDS_QOS_PROFILE_2:
               drQos.value.durability.kind = DDS.DurabilityQosPolicyKind.TRANSIENT_DURABILITY_QOS;
               drQos.value.history.kind = DDS.HistoryQosPolicyKind.KEEP_LAST_HISTORY_QOS;
               drQos.value.history.depth = 1;
               break;

            case DDS_QOS_PROFILE_3:
               drQos.value.reliability.kind = DDS.ReliabilityQosPolicyKind.RELIABLE_RELIABILITY_QOS;
               drQos.value.history.kind = DDS.HistoryQosPolicyKind.KEEP_LAST_HISTORY_QOS;
               drQos.value.history.depth = 30;
               break;

            case DDS_QOS_PROFILE_1:
            default:
               break;
            }
         }

         DataReader reader = subscriber.create_datareader(topic, drQos.value,
               null, ANY_STATUS.value);
         ErrorHandler.checkHandle(reader, "DDS.Subscriber.create_datareader");

         result = new DDSReaderProxy(this, readerName, reader, typeName);
         addReader(result, topicName, readerName);
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
