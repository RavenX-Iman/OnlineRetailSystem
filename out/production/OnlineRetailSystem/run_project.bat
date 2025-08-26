@echo off
:: Navigate to the script directory (assumes src is current folder)
cd /d "%~dp0"

:: Set classpath to include current folder and the JDBC driver
set CLASSPATH=.;..\lib\mssql-jdbc-12.10.0.jre11.jar

:: Compile all Java source files
javac -cp "%CLASSPATH%" ^
onlineretailsystem\DBConnection.java ^
onlineretailsystem\ModelClasses.java ^
onlineretailsystem\CustomerDAO.java ^
onlineretailsystem\CategoryDAO.java ^
onlineretailsystem\ProductDAO.java ^
onlineretailsystem\OrderDAO.java ^
onlineretailsystem\OrderItemDAO.java ^
onlineretailsystem\PaymentDAO.java ^
onlineretailsystem\InventoryTransactionDAO.java ^
onlineretailsystem\TestCustomerDAO.java ^
onlineretailsystem\TestCategoryDAO.java ^
onlineretailsystem\TestProductDAO.java ^
onlineretailsystem\TestOrderDAO.java ^
onlineretailsystem\TestOrderItemDAO.java ^
onlineretailsystem\TestPaymentDAO.java ^
onlineretailsystem\TestInventoryTransactionDAO.java

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo Compilation failed!
    pause
    exit /b
)

echo.
echo Compilation successful.

:: Run all test classes
for %%F in (
    TestCustomerDAO
    TestCategoryDAO
    TestProductDAO
    TestOrderDAO
    TestOrderItemDAO
    TestPaymentDAO
    TestInventoryTransactionDAO
) do (
    echo Running %%F...
    java -cp "%CLASSPATH%;." onlineretailsystem.%%F
    echo.
)

pause
echo All tests completed.
exit /b 0