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
import java.util.Iterator;
import java.util.Map;

public class DDSRootProxy {

   private Map<String, DDSParticipantProxy> participants;

   public DDSRootProxy() {
      participants = new HashMap<String, DDSParticipantProxy>();
   }

   String getParticipants() {
      String result = "{ \"participants\": [";

      for (Iterator<String> it = participants.keySet().iterator(); it.hasNext();) {
         result = result + "\"" + it.next() + "\"";
         if (it.hasNext()) {
            result = result + ", ";
         }
      }
      result = result + "] }";
      return result;
   }

   public DDSParticipantProxy lookupParticipant(String name) {
      return participants.get(name);
   }

   public DDSParticipantProxy lookupOrCreateParticipant(String name) {
      DDSParticipantProxy result = lookupParticipant(name);
      if (result == null) {
         result = new DDSParticipantProxy(name);
         participants.put(name, result);
      }
      return result;
   }

   public void deleteParticipant(String name) {
      participants.remove(name);
   }

}
