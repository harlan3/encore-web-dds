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

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import DDS.DataReader;
import DDS.SampleInfo;
import DDS.SampleInfoSeqHolder;

public class DDSReaderProxy {

   private String name;
   private String classname;
   private DDSSubscriberProxy subscriber;
   private DataReader datareader;
   private String eol = System.getProperty("line.separator");

   /** Suffix appended to topic type name for data reader. */
   private static final String READER_CLASS_SUFFIX = "DataReaderImpl";
   /** Suffix appended to topic type name for data sequence. */
   private static final String SEQUENCE_CLASS_SUFFIX = "SeqHolder";
   /** Field name of the SeqHolder class. */
   private static final String SEQ_VAL_FIELD = "value";
   /** Data reader method used to read data. */
   private static final String READ_METHOD = "read";
   /** Data reader method used to take data. */
   private static final String TAKE_METHOD = "take";
   /** Data reader method used to return data loan. */
   private static final String RETURN_METHOD = "return_loan";
   /** field variable of the SeqHolder class. */
   private Field valueField;
   /** Instance of SequenceHolder class. **/
   private Object sequenceObj;
   /** DDS data reader implementation class. */
   private Class<?> readerClass;
   /** Sequence of values class */
   private Class<?> sequenceClass;
   /** DDS data reader read method */
   private Method readMethod;
   /** DDS data reader take method */
   private Method takeMethod;
   /** DDS data reader return method */
   private Method returnMethod;
   /** DDS sample info sequence, used to read data. */
   private SampleInfoSeqHolder infoSequence = new SampleInfoSeqHolder();
   /** Lock object to prevent simultaneous reads */
   private Object lockObject = new Object();

   public DDSReaderProxy(DDSSubscriberProxy _subscriber, String _name,
         DataReader _datareader, String _classname) {

      subscriber = _subscriber;
      name = _name;
      datareader = _datareader;
      classname = _classname;

      String classBaseName = classname;
      // Cache topic data type class
      try {
         // dataClass = Class.forName(classBaseName);

         // Cache data reader implementation class
         readerClass = Class.forName(classBaseName + READER_CLASS_SUFFIX);
         // Create data sequence
         sequenceClass = Class.forName(classBaseName + SEQUENCE_CLASS_SUFFIX);
         sequenceObj = sequenceClass.newInstance();
         valueField = sequenceClass.getDeclaredField(SEQ_VAL_FIELD);

         // Cache reference to data reader read and take methods
         Class<?>[] arrReadTakeMethodParams = { sequenceClass,
               SampleInfoSeqHolder.class, int.class, int.class, int.class,
               int.class };
         readMethod = readerClass.getMethod(READ_METHOD,
               arrReadTakeMethodParams);
         takeMethod = readerClass.getMethod(TAKE_METHOD,
               arrReadTakeMethodParams);

         // Cache reference to data reader return method
         Class<?>[] arrReturnMethodParams = { sequenceClass,
               SampleInfoSeqHolder.class };
         returnMethod = readerClass.getMethod(RETURN_METHOD,
               arrReturnMethodParams);
      } catch (Exception e) {
         System.out.println("Problems creating datareader: " + e.getMessage());
      }
   }

   private String readOrTake(Method m, int maxLen, int sampleState,
         int viewState, int instanceState) {

      synchronized (lockObject) {
         String result = "{\"samples\": [" + eol + "  ";
         Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls()
               .serializeSpecialFloatingPointValues().create();
         Object msgInst = null;
         try {
            Object[] arrReadTakeMethodParams = { sequenceObj, infoSequence,
                  maxLen, sampleState, viewState, instanceState };
            /* Invoke read or take method, depending on the parameter */
            @SuppressWarnings("unused")
            int status = (Integer) m
                  .invoke(datareader, arrReadTakeMethodParams);
            // ErrorHandler.checkStatus(status, "DDS.Datareader.read");
            Object val = valueField.get(sequenceObj);
            SampleInfo info;
            int len = Array.getLength(val);

            // Process each sample that has valid data
            for (int i = 0; i < len; ++i) {
               msgInst = Array.get(val, i);
               info = (SampleInfo) infoSequence.value[i];
               if (i > 0) {
                  result = result + "," + eol + "  ";
               }
               result = result + "{\"data\": " + gson.toJson(msgInst) + ","
                     + " \"info\": " + gson.toJson(info) + "}" + eol;
            }
            result = result + "] }";
            if (len > 0) {
               Object[] arrReturnMethodParams = { sequenceObj, infoSequence };
               status = (Integer) returnMethod.invoke(datareader,
                     arrReturnMethodParams);
               // ErrorHandler.checkHandle(result,
               // "DDS.Datareader.return_loan");
            }
         } catch (Exception e) {
            if (m == readMethod)
               System.out.println("Reader read failed: " + e.getMessage());
            else if (m == takeMethod)
               System.out.println("Reader take failed: " + e.getMessage());
         }
         return result;
      }
   }

   private String readOrTakeInstance(Method m, String keyField, int instanceID,
         int maxLen, int sampleState, int viewState, int instanceState) {

      synchronized (lockObject) {

         String result = "{\"samples\": [" + eol + "  ";
         Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls()
               .serializeSpecialFloatingPointValues().create();
         Object msgInst = null;
         try {
            Object[] arrReadTakeMethodParams = { sequenceObj, infoSequence,
                  maxLen, sampleState, viewState, instanceState };
            /* Invoke read or take method, depending on the parameter */
            @SuppressWarnings("unused")
            int status = (Integer) m
                  .invoke(datareader, arrReadTakeMethodParams);
            // ErrorHandler.checkStatus(status, "DDS.Datareader.read");
            Object val = valueField.get(sequenceObj);
            SampleInfo info;
            int len = Array.getLength(val);
            int count = 0;

            // Process each sample that has valid data
            for (int i = 0; i < len; ++i) {

               msgInst = Array.get(val, i);

               Class<?> _class = Class.forName(classname);

               Field field = null;
               Field[] objFields = _class.getDeclaredFields();
               int value;

               boolean found = false;

               // Determine if the sample matches the instanceID
               for (int n = 0; n < objFields.length; n++) {

                  if (keyField.equals(objFields[n].getName())) {

                     objFields[n].setAccessible(true);
                     field = objFields[n];
                     value = (Integer) field.get(msgInst);

                     if (instanceID == value) {
                        found = true;
                        break;
                     }
                  }
               }

               if (found) {

                  info = (SampleInfo) infoSequence.value[i];
                  if (count > 0) {
                     result = result + "," + eol + "  ";
                  }
                  count++;

                  result = result + "{\"data\": " + gson.toJson(msgInst) + ","
                        + " \"info\": " + gson.toJson(info) + "}" + eol;
                  result = result + "] }";
               }
            }

            // Nothing to return
            if (count == 0)
               result = result + "] }";

            if (len > 0) {
               Object[] arrReturnMethodParams = { sequenceObj, infoSequence };
               status = (Integer) returnMethod.invoke(datareader,
                     arrReturnMethodParams);
               // ErrorHandler.checkHandle(result,
               // "DDS.Datareader.return_loan");
            }
         } catch (Exception e) {
            if (m == readMethod)
               System.out.println("Reader read failed: " + e.getMessage());
            else if (m == takeMethod)
               System.out.println("Reader take failed: " + e.getMessage());
         }
         return result;
      }
   }

   public String read(int maxLen, int sampleState, int viewState,
         int instanceState) {

      return readOrTake(readMethod, maxLen, sampleState, viewState,
            instanceState);
   }

   public String readInstance(String keyField, int instanceID, int maxLen,
         int sampleState, int viewState, int instanceState) {

      return readOrTakeInstance(readMethod, keyField, instanceID, maxLen,
            sampleState, viewState, instanceState);
   }

   public String take(int maxLen, int sampleState, int viewState,
         int instanceState) {

      return readOrTake(takeMethod, maxLen, sampleState, viewState,
            instanceState);
   }

   public String takeInstance(String keyField, int instanceID, int maxLen,
         int sampleState, int viewState, int instanceState) {

      return readOrTakeInstance(takeMethod, keyField, instanceID, maxLen,
            sampleState, viewState, instanceState);
   }

   public String getName() {
      return name;
   }

   public String getClassname() {
      return classname;
   }

   public DDSSubscriberProxy getSubscriber() {
      return subscriber;
   }

   public DataReader getDataReader() {
      return this.datareader;
   }

}
