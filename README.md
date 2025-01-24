# Macros Calendar Tracking App

A feature-rich desktop application designed for users who are looking to track and manage daily macro intake. This app was built with Java, JavaFX, and SceneBuilder, as well as utilizing Spoonacular Product API to enable users to search for and log food macros efficiently.

![alt text](https://github.com/HobbyistProgrammer/MacrosHealthApp/blob/main/assets/macrosapp.png)

## Features  
- **Macro Management**: Add, edit, or delete daily macro data such as protein, carbohydrate (carbs), fats, and calories.
- **Food Search**: Retrieve local grocery product nuritional information using the Spoonacular Product API.
- **Data Persistence**: Save and manage daily macro data using JDBC for database connectivity. 
- **User Interface**: Intuitive and visually appealing UI build with JavaFX and SceneBuilder. 
- **Custom Calendar Integration**: Custom Calendar integration allows users to go forward and back through the calendar to track macro entries for a specific day, plan macro entries ahead of time, or review past data.

## Technologies Used  
- **Programming Language**: Java.
- **Development Tools**: Intellij IDEA and SceneBuilder for UI Development.
- **Database**: JDBC for local secure data storage.
- **API Integration**: Spoonacular Product API for fetching searhced product macro details.

## Architecture  
The application is designed with a modular achritecture:  
1. **Frontend** Built using JavaFX for an interactive user interface.  
2. **Backend Services** Handles API integration, data validation, and database communication.
3. **Database** Utilizes JDBC to store and manage user data locally

## Getting Started  

### Prerequisites  
1. Download JDK (https://www.oracle.com/java/technologies/downloads/?er=221886).  
2. Setp up IntelliJ (https://www.jetbrains.com/idea/)
3. Obtain API key from Spoonacular API and update config file (https://spoonacular.com/food-api)

### Installation  
1. Clone the repository:  
   ```bash
   git clone https://github.com/HobbyistProgrammer/MacrosHealthApp
