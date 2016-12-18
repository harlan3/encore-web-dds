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

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import DDS.ANY_STATUS;
import DDS.DomainParticipant;
import DDS.DomainParticipantFactory;
import DDS.DomainParticipantQosHolder;
import DDS.Duration_t;
import DDS.Subscriber;
import DDS.Publisher;
import DDS.SubscriberQosHolder;
import DDS.PublisherQosHolder;
import DDS.Topic;

public class DDSParticipantProxy {

   private DomainParticipant participant;
   private String name;
   private Map<String, DDSSubscriberProxy> subscribers;
   private Map<String, DDSPublisherProxy> publishers;
   private Map<String, DDSTopicProxy> topics;

   public DDSParticipantProxy(String _name) {

      name = _name;
      subscribers = new HashMap<String, DDSSubscriberProxy>();
      publishers = new HashMap<String, DDSPublisherProxy>();
      topics = new HashMap<String, DDSTopicProxy>();
      /* Create participant */
      DomainParticipantFactory dpf = DDS.DomainParticipantFactory
            .get_instance();
      ErrorHandler
            .checkHandle(dpf, "DDS.DomainParticipantFactory.get_instance");
      DomainParticipantQosHolder dpQos = new DomainParticipantQosHolder();
      int status = dpf.get_default_participant_qos(dpQos);
      ErrorHandler.checkStatus(status,
            "DDS.DominParticipantFactory.get_default_participant_qos");
      participant = dpf.create_participant(null, dpQos.value, null,
            DDS.ANY_STATUS.value);
      ErrorHandler.checkHandle(participant,
            "DDS.DomainParticipantFactory.create_participant");
      dpQos = null;
   }

   public DDSSubscriberProxy lookupSubscriber(String partition) {

      return subscribers.get(partition);
   }

   public DDSSubscriberProxy lookupOrCreateSubscriber(String partition) {

      DDSSubscriberProxy result = lookupSubscriber(partition);

      if (result == null) {

         SubscriberQosHolder subQos = new SubscriberQosHolder();
         int status = participant.get_default_subscriber_qos(subQos);
         ErrorHandler.checkStatus(status,
               "DDS.DomainParticipant.get_default_subscriber_qos");
         subQos.value.partition.name = new String[1];
         subQos.value.partition.name[0] = partition;
         Subscriber subscriber = participant.create_subscriber(subQos.value,
               null, ANY_STATUS.value);
         ErrorHandler.checkHandle(subscriber,
               "DDS.DomainParticipant.create_subscriber");
         subQos = null;
         result = new DDSSubscriberProxy(this, partition, subscriber);
         subscribers.put(partition, result);
      }
      return result;
   }

   public DDSPublisherProxy lookupPublisher(String partition) {

      return publishers.get(partition);
   }

   public DDSPublisherProxy lookupOrCreatePublisher(String partition) {

      DDSPublisherProxy result = lookupPublisher(partition);
      if (result == null) {
         PublisherQosHolder pubQos = new PublisherQosHolder();
         int status = participant.get_default_publisher_qos(pubQos);
         ErrorHandler.checkStatus(status,
               "DDS.DomainParticipant.get_default_publisher_qos");
         pubQos.value.partition.name = new String[1];
         pubQos.value.partition.name[0] = partition;
         Publisher publisher = participant.create_publisher(pubQos.value, null,
               ANY_STATUS.value);
         ErrorHandler.checkHandle(publisher,
               "DDS.DomainParticipant.create_publisher");
         pubQos = null;
         result = new DDSPublisherProxy(this, partition, publisher);
         publishers.put(partition, result);
      }
      return result;
   }

   public void delete() {
      int status = participant.delete_contained_entities();
      ErrorHandler.checkStatus(status,
            "DDS.DomainParticipant.delete_contained_entities");
      DomainParticipantFactory dpf = DDS.DomainParticipantFactory
            .get_instance();
      ErrorHandler
            .checkHandle(dpf, "DDS.DomainParticipantFactory.get_instance");
      status = dpf.delete_participant(participant);
      ErrorHandler.checkStatus(status,
            "DDS.DomainParticipant.delete_participant");
   }

   public DDSTopicProxy lookupTopic(String topicName) {
      return topics.get(topicName);
   }

   private static final String TYPE_SUPPORT_CLASS_SUFFIX = "TypeSupport";
   private static final String GET_TYPE_NAME_METHOD = "get_type_name";
   private static final String REGISTER_TYPE_METHOD = "register_type";

   private boolean registerType(String classBaseName) {
      boolean result = true;

      String typeSupportName = classBaseName + TYPE_SUPPORT_CLASS_SUFFIX;
      try {

         Class<?> typeSupportClass = Class.forName(typeSupportName);
         Object typeSupportInstance = typeSupportClass.newInstance();
         Class<?>[] arrGetTypeNameTypeParams = {};
         Method getTypeName = typeSupportClass.getMethod(GET_TYPE_NAME_METHOD,
               arrGetTypeNameTypeParams);
         Object[] arrGetTypeNameParams = {};
         String usedTypeName = (String) getTypeName.invoke(typeSupportInstance,
               arrGetTypeNameParams);
         Class<?>[] arrRegisterTypeTypeParams = { DomainParticipant.class,
               String.class };
         Method registerType = typeSupportClass.getMethod(REGISTER_TYPE_METHOD,
               arrRegisterTypeTypeParams);
         Object[] arrRegisterTypeParams = { participant, usedTypeName };
         int status = (Integer) registerType.invoke(typeSupportInstance,
               arrRegisterTypeParams);
         ErrorHandler.checkStatus(status, classBaseName + ".register_type");
      } catch (Exception e) {
         System.out.println("Could not register type " + classBaseName + ": "
               + e.getMessage());
         result = false;
      }

      return result;
   }

   public DDSTopicProxy lookupTopic(String topicName, String typeName) {
      DDSTopicProxy result = lookupTopic(topicName);

      if (result == null) {
         registerType(typeName);
         Duration_t duration = new DDS.Duration_t();
         duration.nanosec = 0;
         duration.sec = 0;
         Topic topic = participant.find_topic(topicName, duration);
         if (topic != null) {
            ErrorHandler.checkHandle(topic, "DDS.Participant.find_topic");
            result = new DDSTopicProxy(this, topicName, typeName, topic);
            topics.put(topicName, result);
         }
      }
      return result;
   }

   public DDSTopicProxy lookupOrCreateTopic(String topicName, String typeName) {

      DDSTopicProxy result = lookupTopic(topicName);

      if (result == null) {
         registerType(typeName);

         String typeSupportName = typeName + TYPE_SUPPORT_CLASS_SUFFIX;
         String defaultTypeName = null;
         try {

            Class<?> typeSupportClass = Class.forName(typeSupportName);
            Object typeSupportInstance = typeSupportClass.newInstance();
            Class<?>[] arrGetTypeNameTypeParams = {};
            Method getTypeName = typeSupportClass.getMethod(
                  GET_TYPE_NAME_METHOD, arrGetTypeNameTypeParams);
            Object[] arrGetTypeNameParams = {};
            defaultTypeName = (String) getTypeName.invoke(typeSupportInstance,
                  arrGetTypeNameParams);

         } catch (Exception e) {
            System.out
                  .println("Could not obtain default type name for data-type: "
                        + typeName + ": " + e.getMessage());
         }

         DDS.TopicQosHolder tQos = new DDS.TopicQosHolder();
         participant.get_default_topic_qos(tQos);

         TopicManager topicManager = TopicManager.getInstance();
         DDSTopicInfo topicInfo = topicManager.topicMap.get(topicName);

         if (topicInfo != null) {

            switch (topicInfo.topicQos) {

            case DDS_QOS_PROFILE_2:
               tQos.value.durability.kind = DDS.DurabilityQosPolicyKind.TRANSIENT_DURABILITY_QOS;
               tQos.value.history.kind = DDS.HistoryQosPolicyKind.KEEP_LAST_HISTORY_QOS;
               tQos.value.history.depth = 1;
               break;

            case DDS_QOS_PROFILE_3:
               tQos.value.reliability.kind = DDS.ReliabilityQosPolicyKind.RELIABLE_RELIABILITY_QOS;
               tQos.value.history.kind = DDS.HistoryQosPolicyKind.KEEP_LAST_HISTORY_QOS;
               tQos.value.history.depth = 30;
               break;

            case DDS_QOS_PROFILE_1:
            default:
               break;
            }
         }

         Topic topic = participant.create_topic(topicName, defaultTypeName,
               tQos.value, null, ANY_STATUS.value);

         if (topic != null) {
            ErrorHandler.checkHandle(topic, "DDS.Participant.create_topic");
            result = new DDSTopicProxy(this, topicName, typeName, topic);
            topics.put(topicName, result);
         }
      }
      return result;
   }

   public DomainParticipant getParticipant() {
      return participant;
   }

   public String getName() {
      return name;
   }
}
