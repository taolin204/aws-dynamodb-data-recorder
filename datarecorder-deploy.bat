REM Deploy CADClient to TWE-Client folder
REM package-TWE.bat for parameter
cd target
move aws-dynamodb-printerweb-1.0-SNAPSHOT.war printerweb.war
xcopy /S /Y /I printerweb.war	C:\Dev\Libraries\Apache\TomEE\7.0.4\webapps

pause