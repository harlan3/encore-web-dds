echo off

REM Prior to executing this script, the OSPL_HOME environment variable
REM must be set, and the OpenSplice enviornment sourced by executing
REM the file {OSPL_INSTALL_DIRECTORY}\HDE\{platform}\release.bat

REM Set the root URI folder
set RESTFUL_DIR=%~dp0%
set WWW_ROOT_URI=file:///%RESTFUL_DIR%www
set URI_MANIFEST=%RESTFUL_DIR%URI_Manifest.txt
set PORT_NUM=8080

REM Add the required jars to classpath
set CP=%OSPL_HOME%\jar\dcpssaj.jar;.\lib\org.restlet.jar;.\lib\com.noelios.restlet.jar;.\lib\gson-1.7.jar;.\lib\RestfulDDS.jar

REM Add DDS idl generated data classes/jars here
set CP=%CP%;.\lib\DDSSolarSystem.jar

REM Start the restlet server
java -cp %CP% org.opensplice.restful.service.RESTfulDDSMain %WWW_ROOT_URI% %URI_MANIFEST% %PORT_NUM%

