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

import DDS.RETCODE_NO_DATA;
import DDS.RETCODE_OK;

public class ErrorHandler {
   public static final int NR_ERROR_CODES = 13;

   /* Array to hold the names for all ReturnCodes. */
   public static String[] RetCodeName = new String[NR_ERROR_CODES];

   static {
      RetCodeName[0] = new String("DDS_RETCODE_OK");
      RetCodeName[1] = new String("DDS_RETCODE_ERROR");
      RetCodeName[2] = new String("DDS_RETCODE_UNSUPPORTED");
      RetCodeName[3] = new String("DDS_RETCODE_BAD_PARAMETER");
      RetCodeName[4] = new String("DDS_RETCODE_PRECONDITION_NOT_MET");
      RetCodeName[5] = new String("DDS_RETCODE_OUT_OF_RESOURCES");
      RetCodeName[6] = new String("DDS_RETCODE_NOT_ENABLED");
      RetCodeName[7] = new String("DDS_RETCODE_IMMUTABLE_POLICY");
      RetCodeName[8] = new String("DDS_RETCODE_INCONSISTENT_POLICY");
      RetCodeName[9] = new String("DDS_RETCODE_ALREADY_DELETED");
      RetCodeName[10] = new String("DDS_RETCODE_TIMEOUT");
      RetCodeName[11] = new String("DDS_RETCODE_NO_DATA");
      RetCodeName[12] = new String("DDS_RETCODE_ILLEGAL_OPERATION");
   }

   /**
    * Returns the name of an error code.
    **/
   public static String getErrorName(int status) {
      return RetCodeName[status];
   }

   /**
    * Check the return status for errors. If there is an error, then terminate.
    **/
   public static void checkStatus(int status, String info) {
      if (status != RETCODE_OK.value && status != RETCODE_NO_DATA.value) {
         System.out.println("Error in " + info + ": " + getErrorName(status));
         // System.exit(-1);
      } else {
         // System.out.println(
         // "Result in " + info + ": " + getErrorName(status));
      }
   }

   /**
    * Check whether a valid handle has been returned. If not, then terminate.
    **/
   public static void checkHandle(Object handle, String info) {
      if (handle == null) {
         System.out.println("Error in " + info
               + ": Creation failed: invalid handle");
         // System.exit(-1);
      } else {
         System.out.println("Success in " + info);
      }
   }
}
