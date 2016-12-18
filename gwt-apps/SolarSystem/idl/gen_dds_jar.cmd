echo off

REM Generate OpenSplice DDS java files
echo Generating OpenSplice DDS java files
idlpp -S -l java -d .\ DDSSolarSystem.idl

REM Prepare class directory
mkdir class
del /q class\*

REM Compile OpenSplice DDS java files
javac -d .\class DDSSolarSystem\*.java

REM Generate jar file
cd class
echo Manifest-Version: 1.0>>tmp
jar cmf tmp ..\DDSSolarSystem.jar DDSSolarSystem
del tmp
cd ..

REM Cleanup
rmdir /s /q class
rmdir /s /q DDSSolarSystem

echo.
echo DDSSolarSystem.jar created
