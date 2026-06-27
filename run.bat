@echo off
setlocal

set JAVA_HOME=%CD%\j2sdk1.4.2_19
set JC_HOME=%CD%\java_card_kit-2_2_1
set PATH=%JAVA_HOME%\bin;%JC_HOME%\bin;%PATH%

echo Uruchamianie emulatora cref...
start "cref" "%JC_HOME%\bin\cref.exe" -o eeprom.dat

echo Czekam 2 sekundy na start emulatora...
timeout /t 2 /nobreak > nul

echo Instalacja i test apletu...
call "%JC_HOME%\bin\apdutool.bat" com\helloworld\javacard\combined2.script

echo.
echo Gotowe. Sprawdz powyzsze wyjscie:
echo   INS:b8  SW=9000  = CREATE (install) OK
echo   INS:a4  SW=9000  = SELECT OK
echo   INS:40  Le:0b 48 65 6c 6c 6f 20 57 6f 72 6c 64 SW=9000 = Hello World OK
echo.
echo EEPROM zapisany w: eeprom.dat

endlocal
pause