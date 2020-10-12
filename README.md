# Allegro - REST API Tests
	
## Setup
* Make sure you have Java 8 SDK installed.  
* Create your new allegro application - https://apps.developer.allegro.pl/new  
* Set your Client ID and Client Secret in the ``Constants`` file.

Two ways to run tests:
##### Use IntelliJ IDEA IDE
1. Open project using IntelliJ IDEA IDE. [How to open project.](https://www.jetbrains.com/help/webstorm/opening-reopening-and-closing-projects.html)
2. Click the gutter icon next to the ``CategoryAndParametersTests`` test class. [More information how to run tests](https://www.jetbrains.com/help/idea/performing-tests.html)

##### Use terminal
1. Open a terminal in the main project folder. `~/allegro-tests`
2. (Windows) Run the ``gradlew :test --tests "com.example.allegroRESTAPItests.CategoryAndParametersTests"`` command.  
(Linux / MacOS) Run the ``./gradlew :test --tests "com.example.allegroRESTAPItests.CategoryAndParametersTests"`` command.