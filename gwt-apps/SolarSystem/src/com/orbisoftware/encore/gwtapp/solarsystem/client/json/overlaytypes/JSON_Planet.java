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

package com.orbisoftware.encore.gwtapp.solarsystem.client.json.overlaytypes;

import com.google.gwt.core.client.JavaScriptObject;

// JSON javascript overlay type
public class JSON_Planet extends JavaScriptObject {

    protected JSON_Planet() { }

    static public final native JSON_Planet buildObject(String json)
    /*-{ return eval('(' + json + ')'); }-*/;
    
    public final native int getSamplesLength() /*-{ 
        return this.samples.length;
    }-*/;
    
    public final native int getInstanceState(int sampleIndex) /*-{ 
        return this.samples[sampleIndex].info.instance_state;
    }-*/;
    
    public final native int getPlanetId(int sampleIndex) /*-{ 
        return this.samples[sampleIndex].data.planetId;
    }-*/;
    
    public final native void setPlanetId(int sampleIndex, int value) /*-{
    
        if (this.value === undefined)
            this.value = new Array();
        
        if (this.value[sampleIndex] === undefined)
            this.value[sampleIndex] = new Object();
    
        this.value[sampleIndex].planetId = value;
    }-*/;

    public final native String getPlanetName(int sampleIndex) /*-{ 
        return this.samples[sampleIndex].data.planetName;
    }-*/;
    
    public final native void setPlanetName(int sampleIndex, String value) /*-{
    
        if (this.value === undefined)
            this.value = new Array();
        
        if (this.value[sampleIndex] === undefined)
            this.value[sampleIndex] = new Object();
    
        this.value[sampleIndex].planetName = value;
    }-*/;
    
    public final native String getPlanetColor(int sampleIndex) /*-{ 
        return this.samples[sampleIndex].data.planetColor;
    }-*/;
    
    public final native void setPlanetColor(int sampleIndex, String value) /*-{
    
        if (this.value === undefined)
            this.value = new Array();
        
        if (this.value[sampleIndex] === undefined)
            this.value[sampleIndex] = new Object();
    
        this.value[sampleIndex].planetColor = value;
    }-*/;

    public final native double getXPos(int sampleIndex) /*-{ 
        return this.samples[sampleIndex].data.xPos;
    }-*/;
    
    public final native void setXPos(int sampleIndex, double value) /*-{
    
        if (this.value === undefined)
            this.value = new Array();
        
        if (this.value[sampleIndex] === undefined)
            this.value[sampleIndex] = new Object();
    
        this.value[sampleIndex].xPos = value;
    }-*/;
    
    public final native double getYPos(int sampleIndex) /*-{ 
        return this.samples[sampleIndex].data.yPos;
    }-*/;
    
    public final native void setYPos(int sampleIndex, double value) /*-{
    
        if (this.value === undefined)
            this.value = new Array();
        
        if (this.value[sampleIndex] === undefined)
            this.value[sampleIndex] = new Object();
    
        this.value[sampleIndex].yPos = value;
    }-*/;
    
    public final native double getTheta(int sampleIndex) /*-{ 
        return this.samples[sampleIndex].data.theta;
    }-*/;
    
    public final native void setTheta(int sampleIndex, double value) /*-{
    
        if (this.value === undefined)
            this.value = new Array();
        
        if (this.value[sampleIndex] === undefined)
            this.value[sampleIndex] = new Object();
    
        this.value[sampleIndex].theta = value;
    }-*/;
    
    public final native double getPlanetSize(int sampleIndex) /*-{ 
        return this.samples[sampleIndex].data.planetSize;
    }-*/;
    
    public final native void setPlanetSize(int sampleIndex, double value) /*-{
    
        if (this.value === undefined)
            this.value = new Array();
        
        if (this.value[sampleIndex] === undefined)
            this.value[sampleIndex] = new Object();
    
        this.value[sampleIndex].planetSize = value;
    }-*/;
    
    public final native double getOrbitalRadius(int sampleIndex) /*-{ 
        return this.samples[sampleIndex].data.orbitalRadius;
    }-*/;
    
    public final native void setOrbitalRadius(int sampleIndex, double value) /*-{
    
        if (this.value === undefined)
            this.value = new Array();
        
        if (this.value[sampleIndex] === undefined)
            this.value[sampleIndex] = new Object();
    
        this.value[sampleIndex].orbitalRadius = value;
    }-*/;
    
    public final native double getOrbitalVelocity(int sampleIndex) /*-{ 
        return this.samples[sampleIndex].data.orbitalVelocity;
    }-*/;
    
    public final native void setOrbitalVelocity(int sampleIndex, double value) /*-{
    
        if (this.value === undefined)
            this.value = new Array();
        
        if (this.value[sampleIndex] === undefined)
            this.value[sampleIndex] = new Object();
    
        this.value[sampleIndex].orbitalVelocity = value;
    }-*/;
}

