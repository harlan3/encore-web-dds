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

import java.util.ArrayList;
import java.util.List;
import org.restlet.Context;
import org.restlet.Directory;
import org.restlet.data.Form;
import org.restlet.data.Request;
import org.restlet.data.Response;

public class NonCachedDirectory extends Directory {

   private List<String> nonCachedDirList;

   public NonCachedDirectory(Context context, String rootURI) {
      super(context, rootURI);

      nonCachedDirList = new ArrayList<String>();

      /* Disable cache for these applications */
      nonCachedDirList.add("/static/DeltaImageStream");
      nonCachedDirList.add("/static/FastImageStream");
      nonCachedDirList.add("/static/HttpLiveStream");
   }

   public void handle(Request request, Response response) {

      boolean isNonCached = false;

      /* Get Response Headers */
      Form responseHeaders = (Form) response.getAttributes().get(
            "org.restlet.http.headers");

      if (responseHeaders == null) {
         responseHeaders = new Form();
         response.getAttributes().put("org.restlet.http.headers",
               responseHeaders);
      }

      /* Allow access across domains */
      responseHeaders.add("Access-Control-Allow-Origin", "*");

      for (String nonCachedDir : nonCachedDirList) {

         if (request.getResourceRef().getPath().contains(nonCachedDir)) {
            isNonCached = true;
            break;
         }
      }

      if (isNonCached) {

         /* Cache Control */
         responseHeaders.add("Cache-Control",
               "no-cache, no-store, must-revalidate");
      }

      super.handle(request, response);
   }
}
