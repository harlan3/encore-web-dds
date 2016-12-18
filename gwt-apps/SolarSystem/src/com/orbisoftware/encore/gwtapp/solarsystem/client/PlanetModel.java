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

import com.google.gwt.canvas.dom.client.Context2d;

public class PlanetModel {

    static int width;
    static int height;

    private DDSSolarSystem.Planet ddsPlanet;

    private final double UPDATE_RATE_SCALING_FACTOR = 3.333333333;

    public PlanetModel(int index, String planetName, double orbitalRadius,
            double planetSize, String planetColor) {

        ddsPlanet = new DDSSolarSystem.Planet();

        ddsPlanet.planetId = index;
        ddsPlanet.planetName = planetName;
        ddsPlanet.orbitalRadius = orbitalRadius;
        ddsPlanet.planetSize = planetSize;
        ddsPlanet.planetColor = planetColor;

        if (ddsPlanet.orbitalRadius == 0.0)
            ddsPlanet.orbitalVelocity = 0.0;
        else
            ddsPlanet.orbitalVelocity = Math.sqrt(5000.0 / Math.pow(
                    orbitalRadius, 2));
    }

    public static void setScreenDimensions(int newWidth, int newHeight) {
        width = newWidth;
        height = newHeight;
    }

    public DDSSolarSystem.Planet getDDSPlanet() {
        return ddsPlanet;
    }

    public void draw(Context2d g2) {

        // Update orbit
        updateOrbit();

        // Draw orbit
        drawOrbit(g2);

        // Draw planet
        g2.setStrokeStyle(ddsPlanet.planetColor);
        g2.setFillStyle(ddsPlanet.planetColor);
        drawPlanet(g2);
    }

    private void drawPlanet(Context2d g2) {

        g2.beginPath();
        g2.arc(ddsPlanet.xPos, ddsPlanet.yPos, ddsPlanet.planetSize / 2.0, 0,
                Math.PI * 2);
        g2.closePath();
        g2.fill();
    }

    private void drawOrbit(Context2d g2) {

        g2.setStrokeStyle("black");
        g2.beginPath();
        g2.arc(width / 2.0, height / 2.0, ddsPlanet.orbitalRadius, 0,
                Math.PI * 2);
        g2.closePath();
        g2.stroke();
    }

    private void updateOrbit() {

        if (ddsPlanet.orbitalRadius == 0) {
            ddsPlanet.theta = 0;
            ddsPlanet.xPos = width / 2;
            ddsPlanet.yPos = height / 2;
        } else {
            ddsPlanet.theta = ddsPlanet.theta
                    + (ddsPlanet.orbitalVelocity * UPDATE_RATE_SCALING_FACTOR)
                    / ddsPlanet.orbitalRadius;
            ddsPlanet.xPos = width / 2 - ddsPlanet.orbitalRadius
                    * Math.sin(ddsPlanet.theta);
            ddsPlanet.yPos = height / 2 - ddsPlanet.orbitalRadius
                    * Math.cos(ddsPlanet.theta);
        }
    }
}
