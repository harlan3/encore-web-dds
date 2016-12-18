#!/usr/bin/python

# Initiate the clean script for the selected target.
# 
# If no app has been selected, display usage and show available apps.

import os
import subprocess
import signal
import sys

rootDirectory = os.getcwd()
applicationPath = rootDirectory + os.sep + "gwt-apps" + os.sep
targetPid = 0
cmdSep = "&"
posix = False

def intSignalHandler(intSignal, frame):
   os.kill(targetPid, signal.SIGKILL)
   sys.exit(0)

def clean(target):
   command = "cd " + applicationPath + target + cmdSep + "ant clean"
   p = subprocess.Popen(command, shell=True)
   if posix:
      global targetPid
      targetPid = p.pid
      os.waitpid(targetPid, 0)[1]
   else:
      p.wait()
         
def main():

    signal.signal(signal.SIGINT, intSignalHandler)

    # Check the type of command seperator to use
    if os.name == "posix":
        global cmdSep 
        cmdSep = ";"
        global posix 
        posix = True
    
    if len(sys.argv) == 2 and sys.argv[1] == "all":
        for direntry in os.listdir(applicationPath):
            if os.path.isdir(applicationPath + direntry) and \
                not direntry == ".svn":
                    print ("\n" + "Cleaning " + direntry)
                    clean(direntry)
        sys.exit()

    if len(sys.argv) != 2 or \
       not os.path.isdir(applicationPath + sys.argv[1]):
           print ("\nUsage: clean APP_NAME\n")
           print ("Available targets:")
           print ("all")
           for direntry in os.listdir(applicationPath):
               if os.path.isdir(applicationPath + direntry) and \
                   not direntry == ".svn":
                       print (direntry)
    else:
        clean(sys.argv[1])
    	
if __name__=='__main__':

    main()
