@echo off
echo Setting Java path...
set JAVA_PATH="C:\glassfish3\jdk7\bin\java.exe"
set JPADAPP_PATH="app\bin\jpadapp.jar"
echo Starting %JPADAPP_PATH%...
%JAVA_PATH% -jar %JPADAPP_PATH% --visualiser

