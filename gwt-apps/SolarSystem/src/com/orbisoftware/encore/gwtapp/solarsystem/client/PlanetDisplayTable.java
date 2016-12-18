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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import DDSSolarSystem.Planet;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.orbisoftware.encore.gwtapp.solarsystem.client.json.overlaytypes.JSON_Planet;

public class PlanetDisplayTable {

    // display widget containers
    public static ScrollPanel dataScrollPanel = new ScrollPanel();
    public static FlexTable dataTable = new FlexTable();

    // data container
    public static HashMap<Integer, Planet> dataRecords = new HashMap<Integer, Planet>();

    public PlanetDisplayTable() {

        dataTable.setText(0, 0, "planetId");
        dataTable.setText(0, 1, "planetName");
        dataTable.setText(0, 2, "planetColor");
        dataTable.setText(0, 3, "xPos");
        dataTable.setText(0, 4, "yPos");
        dataTable.setText(0, 5, "theta");
        dataTable.setText(0, 6, "planetSize");
        dataTable.setText(0, 7, "orbitalRadius");
        dataTable.setText(0, 8, "orbitalVelocity");

        dataTable.setCellPadding(6);
        dataTable.addStyleName("dataTable");
        dataTable.getRowFormatter().addStyleName(0, "tableHeader");
        dataTable.getColumnFormatter().addStyleName(0, "dataTableColumn");
        dataTable.getColumnFormatter().addStyleName(1, "dataTableColumn");
        dataTable.getColumnFormatter().addStyleName(2, "dataTableColumn");
        dataTable.getColumnFormatter().addStyleName(3, "dataTableColumn");
        dataTable.getColumnFormatter().addStyleName(4, "dataTableColumn");
        dataTable.getColumnFormatter().addStyleName(5, "dataTableColumn");
        dataTable.getColumnFormatter().addStyleName(6, "dataTableColumn");
        dataTable.getColumnFormatter().addStyleName(7, "dataTableColumn");
        dataTable.getColumnFormatter().addStyleName(8, "dataTableColumn");
        dataTable.getColumnFormatter().addStyleName(9, "dataTableColumn");

        dataScrollPanel.add(dataTable);
    }

    public void requestDataRecordCallback() {

        RequestBuilder builder = new RequestBuilder(RequestBuilder.GET,
                HttpSessionResource.getPlanetReadURI());

        try {
            builder.sendRequest(null, new DDSPlanetRequestCallback());
        } catch (RequestException e) {
        }
    }

    public void publishDataRecord(Planet ddsPlanet) {

        JSON_Planet obj = JSON_Planet.buildObject("{}");

        try {
            obj.setPlanetId(0, ddsPlanet.planetId);
            obj.setPlanetName(0, ddsPlanet.planetName);
            obj.setPlanetColor(0, ddsPlanet.planetColor);
            obj.setXPos(0, ddsPlanet.xPos);
            obj.setYPos(0, ddsPlanet.yPos);
            obj.setTheta(0, ddsPlanet.theta);
            obj.setPlanetSize(0, ddsPlanet.planetSize);
            obj.setOrbitalRadius(0, ddsPlanet.orbitalRadius);
            obj.setOrbitalVelocity(0, ddsPlanet.orbitalVelocity);

            RequestBuilder builder = new RequestBuilder(RequestBuilder.POST,
                    HttpSessionResource.getPlanetWriteURI());
            builder.setHeader("Content-Type",
                    "application/x-www-form-urlencoded");

            StringBuffer postData = new StringBuffer();
            postData.append(URL.encode("samples") + "="
                    + URL.encode(new JSONObject(obj).toString()));

            builder.sendRequest(postData.toString(), new DDSDefaultCallback());

        } catch (Exception e) {
            Window.alert(e.toString());
        }
    }

    public void updateDataRecordDisplay() {

        Iterator<Map.Entry<Integer, Planet>> it = dataRecords.entrySet()
                .iterator();
        int row = 1;
        while (it.hasNext()) {
            Map.Entry<Integer, Planet> pairs = it.next();
            row = updateDataRecordDisplay(dataRecords.get(pairs.getKey()), row);
        }
    }

    private int updateDataRecordDisplay(Planet planet, int row) {

        String planetIdText = Integer.toString(planet.planetId);
        String planetNameText = planet.planetName;
        String planetColorText = planet.planetColor;
        String xPosText = Double.toString(planet.xPos);
        String yPosText = Double.toString(planet.yPos);
        String thetaText = Double.toString(planet.theta);
        String planetSizeText = Double.toString(planet.planetSize);
        String orbitalRadiusText = Double.toString(planet.orbitalRadius);
        String orbitalVelocityText = Double.toString(planet.orbitalVelocity);

        dataTable.setText(row, 0, planetIdText);
        dataTable.setText(row, 1, planetNameText);
        dataTable.setText(row, 2, planetColorText);
        dataTable.setText(row, 3, xPosText);
        dataTable.setText(row, 4, yPosText);
        dataTable.setText(row, 5, thetaText);
        dataTable.setText(row, 6, planetSizeText);
        dataTable.setText(row, 7, orbitalRadiusText);
        dataTable.setText(row, 8, orbitalVelocityText);
        
        row++;
        return row;
    }
}
