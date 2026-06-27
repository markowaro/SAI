@echo off
setlocal

set JAVA_HOME=%CD%\j2sdk1.4.2_19
set JC_HOME=%CD%\java_card_kit-2_2_1
set PATH=%JAVA_HOME%\bin;%JC_HOME%\bin;%PATH%

echo [1/3] Kompilacja...
"%JAVA_HOME%\bin\javac" -g -source 1.3 -target 1.1 -classpath "%JC_HOME%\lib\api.jar" com\helloworld\HelloWorldApplet.java
if errorlevel 1 ( echo BLAD: Kompilacja nie powiodla sie. & exit /b 1 )
echo OK

echo [2/3] Konwersja do CAP...
call "%JC_HOME%\bin\converter.bat" -classdir . -exportpath "%JC_HOME%\api_export_files" -applet 0x01:0x02:0x03:0x04:0x05:0x00:0x01 com.helloworld.HelloWorldApplet -out CAP EXP JCA com.helloworld 0x01:0x02:0x03:0x04:0x05:0x00 1.0
if errorlevel 1 ( echo BLAD: Konwersja nie powiodla sie. & exit /b 1 )
echo OK

echo [3/3] Generowanie skryptu instalacyjnego...
call "%JC_HOME%\bin\scriptgen.bat" -o com\helloworld\javacard\helloworld_install.script com\helloworld\javacard\helloworld.cap
if errorlevel 1 ( echo BLAD: scriptgen nie powiodl sie. & exit /b 1 )
echo OK

echo.
echo SUKCES
echo   CAP    : com\helloworld\javacard\helloworld.cap
echo   EXP    : com\helloworld\javacard\helloworld.exp
echo   INSTALL: com\helloworld\javacard\helloworld_install.script
echo   TEST   : com\helloworld\javacard\helloworld.script

endlocal
pause