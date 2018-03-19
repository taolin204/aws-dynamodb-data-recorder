REM Deploy CADClient to TWE-Client folder
REM package-TWE.bat for parameter
cd target
move aws-dynamodb-datarecorder-1.0-SNAPSHOT.war datarecorder.war
xcopy /S /Y /I datarecorder.war	C:\Dev\Libraries\Apache\TomEE\7.0.4\webapps

pause