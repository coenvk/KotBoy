@echo off
for /f "delims=" %%x in ('dir /on /b %~dp0\target\KotBoy-*-SNAPSHOT.jar') do set latestjar=%%x
javaw -jar %~dp0\target\%latestjar% $@