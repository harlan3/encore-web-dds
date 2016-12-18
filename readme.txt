Encore Web DDS - Orbis Software
-------------------------------
Encore Web DDS is a framework to support the development of GWT web applications
for use with OpenSplice DDS.  This capability extends the data transport 
capabilities of DDS to include the World Wide Web.  GWT web applications act as 
DDS participants, subscribing to and publishing DDS JSON samples over the HTTP 
protocol.

Licensing
---------
Encore Web DDS is FOSS software, originating as a fork of the Restful DDS 
project.  The core Restful framework must therefore be used and distributed 
under the terms of the LGPL-3.0.

Software GWT web applications utilizing the framework are allowed to be 
developed under alternative licensing such as the Apache or MIT License (e.g. 
the GWT Solar System web application demo).  This software licensing model 
encourages both OSI and commercial development of GWT web applications 
utilizing the framework.  See the GWT terms-of-use for additional licensing
details.

Source code is available from the github repository at the project's
homepage: https://github.com/harlan3/encore-web-dds.git

Software Prerequisites
----------------------
   Java Development Kit
   OpenSplice DDS
   Google Web Toolkit
   Ant
   Python

Setup
-----
The JAVA_HOME, OSPL_HOME, GWT_HOME and BROWSER environment variables must be 
set prior to use in both Linux or Windows respectively.  In Linux, they can
be added to the users .bash_profile script.  In Windows, they can be added 
through the windows control panel.

   Linux Example
   -------------
   JAVA_HOME=/usr/local/jdk1.6.0_24
   OSPL_HOME=/usr/local/HDE/x86.linux2.6
   GWT_HOME=/usr/local/gwt-2.4.0
   BROWSER=/usr/bin/chromium-browser
   
   PATH=$JAVA_HOME/bin:$OSPL_HOME/bin:$PATH
   export PATH JAVA_HOME OSPL_HOME GWT_HOME BROWSER
   
   Windows Example
   ---------------
   JAVA_HOME    C:\Program Files\Java\jdk1.6.0_32
   OSPL_HOME    C:\HDE\x86.win32
   GWT_HOME     C:\gwt-2.4.0
   BROWSER      "C:\Program Files\Google\Chrome\Application\chrome.exe"
   ANT_HOME     C:\apache-ant-1.8.3
   PYTHON_HOME  C:\Python32
   
   PATH         %JAVA_HOME%\bin;%OSPL_HOME%\bin;%ANT_HOME%\bin;PYTHON_HOME%
  
Creating a new Encore GWT Web app
---------------------------------
   "MyGwtApp" creation steps
   -------------------------
   cd {GWT_HOME}
   webAppCreator -out MyGwtApp com.orbisoftware.encore.gwtapp.mygwtapp.MyGwtApp
   move MyGwtApp encore-web-dds/gwt-apps/
   
   Merge in aditional code from an existing app to support DDS connectivity.

Building and running an Encore GWT app
--------------------------------------
   See readme file located in each GWT app directory
   
Notes
-----
The OpenSplice DDS Spectrum Monitor tool is helpful to use as a DDS data 
publisher and subscriber during development.  The tool is available at:

    http://code.google.com/p/spectrum-dds-monitor/
