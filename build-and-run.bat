@echo off
echo Building and running Spring Integration Test project...

echo.
echo Step 1: Building the project...
call mvnw.cmd clean compile
if %errorlevel% neq 0 (
    echo Build failed!
    pause
    exit /b 1
)

echo.
echo Step 2: Running tests...
call mvnw.cmd test
if %errorlevel% neq 0 (
    echo Tests failed!
    pause
    exit /b 1
)

echo.
echo Step 3: Starting the application...
echo Application will be available at http://localhost:8080
echo Press Ctrl+C to stop the application
call mvnw.cmd spring-boot:run