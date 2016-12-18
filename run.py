#!/usr/bin/python

# Initiate the run script for the selected target.
# 
# If no app has been selected, display usage and show available apps.

import sys
import os
import subprocess

rootDirectory = os.getcwd()
applicationPath = rootDirectory + os.sep + "gwt-apps" + os.sep
serverURL = "http://localhost:8080/static/"
browser = os.environ['BROWSER']

def run(target):
   command_list = browser + " " + serverURL + target + "/index.html"
   os.system(command_list)

def main():

    if len(sys.argv) != 2 or \
       not os.path.isdir(applicationPath + sys.argv[1]):
           print ("\nUsage: run APP_NAME\n")
           print ("Available targets:")
           for direntry in os.listdir(applicationPath):
               if os.path.isdir(applicationPath + direntry) and \
                   not direntry == ".svn":
                       print (direntry)
    else:
        run(sys.argv[1])
    	
if __name__=='__main__':

    main()
