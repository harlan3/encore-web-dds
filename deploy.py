#!/usr/bin/python

# Initiate the deploy script for the selected target.
# 
# If no app has been selected, display usage and show available apps.

import os
import shutil
import sys

rootDirectory = os.getcwd()
applicationPath = rootDirectory + os.sep + "gwt-apps" + os.sep
restfulWWWPath = rootDirectory + os.sep + "restful" + os.sep + "www"
posix = False

def deploy(target):

   if posix:
       if os.path.exists(restfulWWWPath + os.sep + target):
           os.unlink(restfulWWWPath + os.sep + target)
       os.symlink(applicationPath + target + os.sep + "war", restfulWWWPath + os.sep + target)
   else:
       shutil.rmtree(restfulWWWPath + os.sep + target, ignore_errors=True)
       shutil.copytree(applicationPath + target + os.sep + "war", restfulWWWPath + os.sep + target, symlinks=True)

def main():
    
    # Determine os type
    if os.name == "posix":
        global posix 
        posix = True
        
    if len(sys.argv) == 2 and sys.argv[1] == "all":
        for direntry in os.listdir(applicationPath):
            if os.path.isdir(applicationPath + direntry) and \
                not direntry == ".svn":
                    print ("\n" + "Deploying " + direntry)
                    deploy(direntry)
        sys.exit()

    if len(sys.argv) != 2 or \
       not os.path.isdir(applicationPath + sys.argv[1]):
           print ("\nUsage: deploy APP_NAME\n")
           print ("Available targets:")
           print ("all")
           for direntry in os.listdir(applicationPath):
               if os.path.isdir(applicationPath + direntry) and \
                   not direntry == ".svn":
                       print (direntry)
    else:
        deploy(sys.argv[1])
        
if __name__=='__main__':

    main()
