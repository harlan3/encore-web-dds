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

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class SolarSystem implements EntryPoint {

    private static int REFRESH_INTERVAL = 100; // ms

    private static int imgWidth = 425;
    private static int imgHeight = 425;

    private static enum ColorType {
        blue, cyan, gray, green, lightGray, magenta, orange, pink, red, yellow
    }

    private static final String upgradeMessage = "Your browser does not support the HTML5 Canvas. Please upgrade your browser to view this demo.";

    public static Label debugTextLabel = new Label();
    public static Label lastUpdatedLabel = new Label();

    public static MenuBar defaultsMenu = new MenuBar(true);

    public static ArrayList<PlanetModel> planetModelList = new ArrayList<PlanetModel>();
    private static PlanetDisplayTable planetTable;

    private static Canvas canvas;
    private static Context2d context;

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {

    	String refreshInterval = Window.Location.getParameter("refreshInterval");
        VerticalPanel mainPanel = new VerticalPanel();
        VerticalPanel canvasPanel = new VerticalPanel();
        HorizontalPanel controlPanel = new HorizontalPanel();

        Label planetIDLabel = new Label();
        final TextBox planetIDTextBox = new TextBox();

        Label planetNameLabel = new Label();
        final TextBox planetNameTextBox = new TextBox();

        Label planetColorLabel = new Label();
        final ListBox planetColorListBox = new ListBox();

        Label planetSizeLabel = new Label();
        final TextBox planetSizeTextBox = new TextBox();

        Label orbitalRadiusLabel = new Label();
        final TextBox orbitalRadiusTextBox = new TextBox();

        Button publishButton = new Button();

        // Initialize the Http Session
        HttpSessionResource.initializeSession(Window.Location.getHostName(),
                8080, UUID.uuid(15));

        // create the menu bar
        MenuBar menu = new MenuBar();
        menu.setAutoOpen(true);
        menu.setWidth(imgWidth + "px");
        menu.setAnimationEnabled(true);

        // create the file menu
        MenuBar fileMenu = new MenuBar(true);
        fileMenu.setAnimationEnabled(true);
        menu.addItem(new MenuItem("File", fileMenu));

        fileMenu.addSeparator();
        fileMenu.addItem("Load Defaults", defaultsMenu);
        fileMenu.addSeparator();

        mainPanel.add(menu);

        // create the canvas
        canvas = Canvas.createIfSupported();

        if (canvas == null) {
            RootPanel.get("displayApp").add(new Label(upgradeMessage));
            return;
        }

        canvas.setWidth(imgWidth + "px");
        canvas.setHeight(imgHeight + "px");
        canvas.setCoordinateSpaceWidth(imgWidth);
        canvas.setCoordinateSpaceHeight(imgHeight);
        context = canvas.getContext2d();

        canvasPanel.add(canvas);
        mainPanel.add(canvasPanel);

        // create the control Panel
        planetIDLabel.setText("Planet ID:");
        planetIDLabel.addStyleName("controlPanelLabel");
        planetIDTextBox.addStyleName("narrowControlTextBox");
        planetIDTextBox.setText("10");

        planetNameLabel.setText("Planet Name:");
        planetNameLabel.addStyleName("controlPanelLabel");
        planetNameTextBox.addStyleName("wideControlTextBox");
        planetNameTextBox.setText("Planet X");

        planetColorLabel.setText("Planet Color:");
        planetColorLabel.addStyleName("controlPanelLabel");
        for (ColorType colorType : ColorType.values())
            planetColorListBox.addItem(colorType.name());

        planetSizeLabel.setText("Planet Size:");
        planetSizeLabel.addStyleName("controlPanelLabel");
        planetSizeTextBox.addStyleName("narrowControlTextBox");
        planetSizeTextBox.setText("20");

        orbitalRadiusLabel.setText("Orbital Radius:");
        orbitalRadiusLabel.addStyleName("controlPanelLabel");
        orbitalRadiusTextBox.addStyleName("narrowControlTextBox");
        orbitalRadiusTextBox.addStyleName("padRight");
        orbitalRadiusTextBox.setText("100");

        publishButton.setText("Publish");

        publishButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {

                int planetId = Integer.parseInt(planetIDTextBox.getText());
                String planetName = planetNameTextBox.getText();
                double orbitalRadius = Double.parseDouble(orbitalRadiusTextBox
                        .getText());
                double planetSize = Double.parseDouble(planetSizeTextBox
                        .getText());
                String planetColor = planetColorListBox
                        .getItemText(planetColorListBox.getSelectedIndex());

                PlanetModel planetModel = new PlanetModel(planetId, planetName,
                        orbitalRadius, planetSize, planetColor);
                planetModelList.add(planetModel);

            }
        });

        controlPanel.add(planetIDLabel);
        controlPanel.add(planetIDTextBox);
        controlPanel.add(planetNameLabel);
        controlPanel.add(planetNameTextBox);
        controlPanel.add(planetColorLabel);
        controlPanel.add(planetColorListBox);
        controlPanel.add(planetSizeLabel);
        controlPanel.add(planetSizeTextBox);
        controlPanel.add(orbitalRadiusLabel);
        controlPanel.add(orbitalRadiusTextBox);
        controlPanel.add(publishButton);
        controlPanel.addStyleName("controlPanel");

        DecoratorPanel controlDecoratorPanel = new DecoratorPanel();
        controlDecoratorPanel.add(controlPanel);
        mainPanel.add(controlDecoratorPanel);

        lastUpdatedLabel.addStyleName("lastUpdatedLabel");
        mainPanel.add(lastUpdatedLabel);

        DecoratorPanel planetTablePanel = new DecoratorPanel();
        planetTable = new PlanetDisplayTable();
        planetTablePanel.add(PlanetDisplayTable.dataScrollPanel);
        mainPanel.add(planetTablePanel);

        mainPanel.addStyleName("mainPanel");
        mainPanel.add(debugTextLabel);

        RootPanel.get("displayApp").add(mainPanel);

        // set the screen dimensions in the planet model
        PlanetModel.setScreenDimensions(imgWidth, imgHeight);

        // request the server to populate the submenu containing
        // the xml defaults files
        RequestBuilder builder = new RequestBuilder(RequestBuilder.GET,
                HttpSessionResource.getReadXMLDefaults());
        try {
            builder.sendRequest(null, new PopulateFileMenuCallback());
        } catch (RequestException e) {
            Window.alert("caught exception");
        }

        // Setup timer to refresh display.
        Timer updateTimer = new Timer() {
            @Override
            public void run() {
                refreshDisplay();
            }
        };

        if (refreshInterval == null)
            updateTimer.scheduleRepeating(REFRESH_INTERVAL);
        else
            updateTimer.scheduleRepeating(Integer.parseInt(refreshInterval));
    }

    private void refreshDisplay() {

        // Update data record display tables
        planetTable.updateDataRecordDisplay();

        context.clearRect(0, 0, imgWidth, imgHeight);
        updatePlanetModels();

        // Display timestamp showing last refresh.
        lastUpdatedLabel.setText("Last update : "
                + DateTimeFormat.getFormat(
                        DateTimeFormat.PredefinedFormat.HOUR_MINUTE_SECOND)
                        .format(new Date()));

        // Request data for availability on next display cycle
        planetTable.requestDataRecordCallback();
    }

    private void updatePlanetModels() {

        for (PlanetModel planetModel : planetModelList) {
            planetModel.draw(context);
            planetTable.publishDataRecord(planetModel.getDDSPlanet());
        }
    }
}
