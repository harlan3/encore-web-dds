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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;

public class TopicManager {
   
   public HashMap<String, DDSTopicInfo> topicMap = new HashMap<String, DDSTopicInfo>();

   private static TopicManager instance = null;

   protected TopicManager() {

      topicMap.clear();
   }

   public static TopicManager getInstance() {

      if (instance == null) {
         instance = new TopicManager();
      }
      return instance;
   }
   
   public void parseURIManifest(String fileName) {

      topicMap.clear();

      try {

         BufferedReader buffReader = new BufferedReader(
               new FileReader(fileName));
         String uriLine;

         while ((uriLine = buffReader.readLine()) != null) {

            DDSTopicInfo ddsTopic = new DDSTopicInfo();
            String topicKey = new String();

            boolean uriValid = false;

            if (uriLine.trim().charAt(0) != '#') {

               StringTokenizer uriLineComponents = new StringTokenizer(
                     uriLine, ",");

               String uriSection = uriLineComponents.nextToken().trim();
               String qosSetting = uriLineComponents.nextToken().trim();

               StringTokenizer uriParts = new StringTokenizer(uriSection, "/");

               if (uriParts.countTokens() == 3) {

                  ddsTopic.partition = uriParts.nextToken();
                  ddsTopic.name = uriParts.nextToken();
                  ddsTopic.dataType = uriParts.nextToken();
                  topicKey = ddsTopic.name;

                  uriValid = true;
               }

               if (qosSetting == null) {

                  qosSetting = new String("DDS_QOS_PROFILE_1");
               }

               if (uriValid) {

                  try {
                     ddsTopic.topicQos = DDSTopicInfo.QoSProfile.valueOf(qosSetting);
                  } catch (Exception e) {
                     System.out.println("Unsupport QoS profile referenced in URI manifest.");
                     System.exit(1);
                  }
                  topicMap.put(topicKey, ddsTopic);
               }
            }
         }
         buffReader.close();
      } catch (IOException e) {
         System.err.println("Error: Could not read " + fileName);
      }
   }
}
