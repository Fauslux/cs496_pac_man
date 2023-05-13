:: This file is used to compile and run a Pac-Man game server
::@echo off
set CurrDir=%cd%
cd ".."
set ParentDir=%cd%
cd %CurrDir%
::javac -cp "%ParentDir%\bin;%ParentDir%\json-simple-1.1.jar;" common/*.java server/*.java 
::javac common/*.java server/*.java 
"C:\Program Files\Zulu\zulu-17\bin\java.exe" -p "C:\Users\Kevin\Documents\GitHub\cs496_pac_man\bin;C:\Users\Kevin\Documents\GitHub\cs496_pac_man\json-simple-1.1.1.jar" -m cs496_pac_man/server.PacmanServerDriver %1 %2
set /p workspace="End now"