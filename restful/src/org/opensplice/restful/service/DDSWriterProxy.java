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

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import DDS.DataWriter;

public class DDSWriterProxy {

   private String name;
   private String classname;
   private DDSPublisherProxy publisher;
   private DataWriter datawriter;

   /** Suffix appended to topic type name for data writer. */
   private static final String WRITER_CLASS_SUFFIX = "DataWriterImpl";
   /** Suffix appended to topic type name for data sequence. */
   private static final String SEQUENCE_CLASS_SUFFIX = "SeqHolder";
   /** Field name of the SeqHolder class. */
   private static final String SEQ_VAL_FIELD = "value";
   /** Data writer method used to write data. */
   private static final String WRITE_METHOD = "write";
   /** field variable of the SeqHolder class. */
   private Field valueField;
   /** Instance of SequenceHolder class. **/
   private Object dataClassObj;
   /** Instance of class BaseName. **/
   private Object sequenceObj;
   /** Instance of SequenceHolder class. **/
   private Class<?> dataClass;
   /** DDS data writer implementation class. */
   private Class<?> writerClass;
   /** Sequence of values class */
   private Class<?> sequenceClass;
   /** DDS data writer write method */
   private Method writeMethod;
   /** Lock object to prevent simultaneous writes */
   private Object lockObject = new Object();

   public DDSWriterProxy(DDSPublisherProxy _publisher, String _name,
         DataWriter _datawriter, String _classname) {

      publisher = _publisher;
      name = _name;
      datawriter = _datawriter;
      classname = _classname;

      String classBaseName = classname;
      // Cache topic data type class
      try {
         dataClass = Class.forName(classBaseName);

         // Cache data writer implementation class
         writerClass = Class.forName(classBaseName + WRITER_CLASS_SUFFIX);
         // Create data sequence
         sequenceClass = Class.forName(classBaseName + SEQUENCE_CLASS_SUFFIX);
         dataClassObj = dataClass.newInstance();
         sequenceObj = sequenceClass.newInstance();
         valueField = sequenceClass.getDeclaredField(SEQ_VAL_FIELD);

         // Cache reference to data writer write
         Class<?>[] arrWriteMethodParams = { dataClass, long.class };
         writeMethod = writerClass
               .getMethod(WRITE_METHOD, arrWriteMethodParams);

      } catch (Exception e) {
         System.out.println("Problems creating Datawriter: " + e.getMessage());
      }
   }

   private int write(Method m, String samplesJSON) {

      synchronized (lockObject) {
         int status = 0;
         Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls()
               .serializeSpecialFloatingPointValues().create();
         
         try {

            sequenceObj = gson.fromJson(samplesJSON, sequenceClass);

            Object val = valueField.get(sequenceObj);
            int len = Array.getLength(val);

            // Process each sample
            for (int i = 0; i < len; ++i) {

               dataClassObj = Array.get(val, i);

               Object[] arrWriteMethodParams = { dataClassObj,
                     DDS.HANDLE_NIL.value };
               status = (Integer) m.invoke(datawriter, arrWriteMethodParams);
            }

         } catch (Exception e) {
            System.out.println("Datawriter write failed: " + e.getMessage());
         }
         return status;
      }
   }

   public int write(String samplesJSON) {
      return write(writeMethod, samplesJSON);
   }

   public String getName() {
      return name;
   }

   public String getClassname() {
      return classname;
   }

   public DDSPublisherProxy getPublisher() {
      return publisher;
   }

   public DataWriter getDataWriter() {
      return this.datawriter;
   }
}
