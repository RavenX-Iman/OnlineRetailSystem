@echo off
:: Navigate to the script directory (assumes src is current folder)
cd /d "%~dp0"

:: Set classpath to include current folder and the JDBC driver
set CLASSPATH=.;..\lib\mssql-jdbc-12.10.0.jre11.jar

echo Compiling Java files...
javac -cp "%CLASSPATH%" ^
onlineretailsystem\DBConnection.java ^
onlineretailsystem\ModelClasses.java ^
onlineretailsystem\CustomerDAO.java ^
onlineretailsystem\CategoryDAO.java ^
onlineretailsystem\TestCustomerDAO.java ^
onlineretailsystem\TestCategoryDAO.java

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo Compilation failed!
    pause
    exit /b
)

echo.
echo Compilation successful.
echo Running TestCustomerDAO...
echo.
java -cp "%CLASSPATH%;." onlineretailsystem.TestCustomerDAO

echo.
echo Running TestCategoryDAO...
echo.
java -cp "%CLASSPATH%;." onlineretailsystem.TestCategoryDAO

echo.
pause
