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

package com.orbisoftware.encore.gwtapp.solarsystem.client;

public class HttpSessionResource {

    public static void initializeSession(String restfulDDSHost,
            int restfulDDSPort, String sessionID) {

        baseURI = "http://" + restfulDDSHost + ":" + restfulDDSPort + "/dds/"
                + sessionID;
    }

    public static String getPlanetReadURI() {
        return baseURI
                + "/*/Planet/DDSSolarSystem.Planet/subscribe/webReader/read_on_change";
    }

    public static String getPlanetWriteURI() {
        return baseURI
                + "/*/Planet/DDSSolarSystem.Planet/publish/webWriter/write";
    }

    public static String getReadXMLDefaults() {
        return baseURI + "/solarsystem/api/ReadDefaults";
    }

    private static String baseURI = null;
}
