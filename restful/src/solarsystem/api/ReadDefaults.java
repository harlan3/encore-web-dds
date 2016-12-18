/*
 * This software is OSI Certified Open Source Software
 * 
 * The MIT License (MIT)
 * Copyright (C) 2012 by Harlan Murphy
 * Orbis Software - orbisoftware@gmail.com
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a 
 * copy of this software and associated documentation files (the "Software"), 
 * to deal in the Software without restriction, including without limitation 
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 * and/or sell copies of the Software, and to permit persons to whom the 
 * Software is furnished to do so, subject to the following conditions: 
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software. 
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO
 * EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES
 * OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 * 
 */

package solarsystem.api;

import org.restlet.Context;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;
import org.restlet.resource.ResourceException;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;
import java.io.File;
import java.io.FilenameFilter;

public class ReadDefaults extends Resource {

    public ReadDefaults(Context context, Request request, Response response) {
        super(context, request, response);

        // Allow modifications of this resource via POST requests.
        setModifiable(true);

        // Declare the kind of representations supported by this resource.
        getVariants().add(new Variant(MediaType.TEXT_PLAIN));
        
        /* Allow access across domains */
        Form responseHeaders = (Form) getResponse().getAttributes().get(
              "org.restlet.http.headers");
        if (responseHeaders == null) {
           responseHeaders = new Form();
           getResponse().getAttributes().put("org.restlet.http.headers",
                 responseHeaders);
        }
        responseHeaders.add("Access-Control-Allow-Origin", "*");
    }

    @Override
    public Representation represent(Variant variant) throws ResourceException {

        Representation result = null;
        String returnString = new String();

        String sysAllocDirStr = new File(".").getAbsolutePath()
                + "/www/SolarSystem/defaults";
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".xml");
            }
        };

        File dir = new File(sysAllocDirStr);
        String[] sysAllocFiles = dir.list(filter);

        if (sysAllocFiles != null) {
            for (int i = 0; i < sysAllocFiles.length; i++) {
                returnString += sysAllocFiles[i];
                if (i != sysAllocFiles.length - 1)
                    returnString += ",";
            }
        } else {
            System.out.println("Could not read defaults directory.");
            System.out.println(sysAllocDirStr);
        }

        if ((variant != null)
                && variant.getMediaType().equals(MediaType.TEXT_PLAIN)) {
            result = new StringRepresentation(returnString,
                    MediaType.TEXT_PLAIN);
        }
        return result;
    }
}
