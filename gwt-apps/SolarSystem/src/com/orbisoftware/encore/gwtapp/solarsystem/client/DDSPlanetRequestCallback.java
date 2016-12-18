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

import DDSSolarSystem.Planet;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.orbisoftware.encore.gwtapp.solarsystem.client.json.overlaytypes.JSON_Planet;

public class DDSPlanetRequestCallback implements RequestCallback {

    private static boolean DEBUG = false;

    public void onError(Request request, Throwable exception) {

    }

    public void onResponseReceived(Request request, Response response) {

        if (DEBUG) {
            if (response.getStatusCode() == 200) {
                SolarSystem.debugTextLabel.setText(response.getText());
            }
        }

        int code = response.getStatusCode();
        code += 0;
        String text = response.getText();
        text += "";

        if ((response.getStatusCode() == 200)
                && (response.getText().length() > 0)) {

            JSON_Planet responseObj = JSON_Planet.buildObject(response
                    .getText());

            for (int i = 0; i < responseObj.getSamplesLength(); i++) {

                synchronized (PlanetDisplayTable.dataRecords) {

                    Planet dataEntry = (Planet) PlanetDisplayTable.dataRecords
                            .get(responseObj.getPlanetId(i));

                    if (dataEntry == null) {
                        dataEntry = new Planet();
                        dataEntry.planetId = responseObj.getPlanetId(i);
                        PlanetDisplayTable.dataRecords.put(dataEntry.planetId,
                                dataEntry);
                    }

                    dataEntry.planetName = responseObj.getPlanetName(i);
                    dataEntry.planetColor = responseObj.getPlanetColor(i);
                    dataEntry.xPos = responseObj.getXPos(i);
                    dataEntry.yPos = responseObj.getYPos(i);
                    dataEntry.theta = responseObj.getTheta(i);
                    dataEntry.planetSize = responseObj.getPlanetSize(i);
                    dataEntry.orbitalRadius = responseObj.getOrbitalRadius(i);
                    dataEntry.orbitalVelocity = responseObj
                            .getOrbitalVelocity(i);
                }
            }
        }
    }
}
