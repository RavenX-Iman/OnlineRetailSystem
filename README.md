# CYANITY - Online Retail System

## Overview
CYANITY is a comprehensive desktop application for retail management, integrating Java OOP principles with a SQL Server database. Developed collaboratively, this system merges concepts from Java OOP and Database Systems into a single, functional solution. It showcases our ability to build a scalable, user-friendly retail management tool with real-time data handling.

## Features
- **Modern GUI**: Intuitive interface built with Java Swing, featuring a splash screen, dashboard, and multiple management panels.
- **Inventory Management**: Real-time tracking of products, stock levels, and transactions.
- **Order Processing**: Efficient handling of customer orders with payment integration.
- **Database Connectivity**: Seamless interaction with SQL Server for data persistence and retrieval.
- **Object-Oriented Design**: Implementation of classes, DAOs, and observer patterns for modular and maintainable code.
- **User Roles**: Separate dashboards for admin and customer functionalities.

## Technologies Used
- **Programming Language**: Java
- **Framework**: Java Swing
- **Database**: SQL Server
- **Development Tools**: IntelliJ IDEA/VS Code, Maven (suggested for dependency management)
- **Design**: Canva for GUI images

## Folder Structure
```plaintext
CYANITY/
├── .idea/              # IDE configuration files
├── lib/                # JDBC driver (mssql-jdbc-12.10.0.jre11.jar)
├── out/                # Compiled files (ignore for source control)
├── src/
│   ├── gui/            # GUI-related Java files (e.g., SplashScreenPanel.java)
│   ├── onlineretailsystem/ # Core logic, DAOs, models, and tests
│   └── Service/        # Service layer (e.g., OrderService.java)
├── Login Simple Retail Logo.png  # Splash screen image
├── Yellow and Black Simple Retail Logo.png  # Alternate logo
├── OnlineRetailSystem.iml  # IntelliJ project file
└── run_project.bat      # Batch file to run the project
```

## Setup Instructions
1. **Prerequisites**:
   - JDK 11 or higher.
   - SQL Server with connection: `jdbc:sqlserver://localhost:59598;databaseName=OnlineRetailDB;encrypt=true;trustServerCertificate=true`.
   - Credentials: Username `retail_userIman`, Password `imanhuma157`.
2. **Clone and Setup**:
   ```bash
   git clone https://github.com/RavenX-Iman/OnlineRetailSystem.git
    cd OnlineRetailSystem

   ```
   - Place the JDBC driver (`mssql-jdbc-12.10.0.jre11.jar`) in the `lib` folder.
   - Execute the SQL schema from `onlineretailsystem` to create tables.
3. **Run**:
   - Use `run_project.bat` in the `src` or `out/production/OnlineRetailSystem` directory, or compile with `javac src/onlineretailsystem/Main.java` and run with `java -cp lib/*;out onlineretailsystem.Main`.

## Screenshots
![Splash Screen](screenshots/splash.png)  
![Dashboard](screenshots/dashboard.png)  
![Order Panel](screenshots/order-panel.png)  


## Future Improvements
- **User Authentication**: Implement login security with password hashing.
- **Mobile Responsiveness**: Adapt the GUI for smaller screens.
- **Advanced Analytics**: Add graphical stats using JavaFX or JFreeChart.
- **Error Handling**: Enhance exception handling for better user feedback.
- **Testing**: Incorporate unit tests with JUnit.

## Credits
- **Developers**:
  **Iman Tahir | [Huma Ijaz](https://github.com/Huma-Ijaz)**
