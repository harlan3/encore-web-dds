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

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

public class PlanetXMLFileParser {

    public void loadXMLFile(String fileName) {

        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET,
                "defaults/" + fileName);
        try {
            requestBuilder.sendRequest(null, new RequestCallback() {
                public void onError(Request request, Throwable exception) {
                    requestFailed(exception);
                }

                public void onResponseReceived(Request request,
                        Response response) {
                    parseXML(response.getText());
                }
            });
        } catch (RequestException ex) {
            requestFailed(ex);
        }
    }

    public void parseXML(String xmlText) {

        SolarSystem.planetModelList.clear();

        com.google.gwt.xml.client.Document customerDom = XMLParser
                .parse(xmlText);
        XMLParser.removeWhitespace(customerDom);

        NodeList nodeList = customerDom.getElementsByTagName("Planets");

        NodeList planetNodes = nodeList.item(0).getChildNodes();

        for (int i = 0; i < planetNodes.getLength(); i++) {

            NodeList elementNodes = planetNodes.item(i).getChildNodes();

            int planetId = 0;
            String planetName = null;
            double orbitalRadius = 0.0;
            double planetSize = 0.0;
            String planetColor = null;

            for (int j = 0; j < elementNodes.getLength(); j++) {

                String nodeName = elementNodes.item(j).getNodeName();

                if (nodeName.equals("planetId")) {
                    planetId = Integer.parseInt(elementNodes.item(j)
                            .getFirstChild().getNodeValue());
                } else if (nodeName.equals("planetName")) {
                    planetName = new String(elementNodes.item(j)
                            .getFirstChild().getNodeValue());
                } else if (nodeName.equals("orbitalRadius")) {
                    orbitalRadius = Double.parseDouble(elementNodes.item(j)
                            .getFirstChild().getNodeValue());
                } else if (nodeName.equals("planetSize")) {
                    planetSize = Double.parseDouble(elementNodes.item(j)
                            .getFirstChild().getNodeValue());
                } else if (nodeName.equals("planetColor")) {
                    planetColor = new String(elementNodes.item(j)
                            .getFirstChild().getNodeValue());
                }
            }

            PlanetModel planetModel = new PlanetModel(planetId, planetName,
                    orbitalRadius, planetSize, planetColor);

            SolarSystem.planetModelList.add(planetModel);
        }
    }

    public void requestFailed(Throwable exception) {
        Window.alert("Failed to load XML data: " + exception.getMessage());
    }
}
