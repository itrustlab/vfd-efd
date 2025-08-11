@echo off
echo Building iTrust VFD Microservice for SIT...

REM Clean and build the project
call mvn clean install -DskipTests

REM Check if build was successful
if %ERRORLEVEL% NEQ 0 (
    echo Build failed!
    pause
    exit /b 1
)

echo Build completed successfully!
echo.
echo To run the application:
echo mvn spring-boot:run -Dspring.profiles.active=sit
echo.
echo Or to run with Docker:
echo docker build -t itrust-vfd .
echo docker run -p 8085:8085 itrust-vfd
echo.
pause 