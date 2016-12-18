General
-------
See encore-web-dds/readme.txt for general non-app specific info.

Restful/Restlet compilation
---------------------------
   cd encore-web-dds/restful
   ant

Generate DDS jar file
---------------------
   cd encore-web-dds/gwt-apps/SolarSystem/idl
   ./gen_dds_jar

SolarSystem compilation and deployment
--------------------------------------
   cd encore-web-dds
   ./build.py SolarSystem 
   ./deploy.py SolarSystem 

Start SolarSystem app and view in browser 
-----------------------------------------
   1) Start opensplice
      open new terminal
      ospl start
   2) Start restlet:
      open new terminal
      cd encore-web-dds/restful
      ./restlet
   3) Execute SolarSystem GWT App:
      open new terminal
      cd encore-web-dds
      ./run.py SolarSystem 

Application URL with optional settings
--------------------------------------
   http://localhost:8080/static/SolarSystem/index.html?refreshInterval=100

