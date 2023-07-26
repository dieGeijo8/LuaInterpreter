# Lua Interpreter 
In this project a **Lua interpreter** is written in **Java**. **JNA** and **Lua C api** are used. The interpreter runs on **Linux**, to make it run on Windows you should convert the .so libraries to .ddl . <br>

To use the shared libraries run the following command from the terminal: export LD_LIBRARY_PATH=$LD_LIBRARY_PATH../Project/lib . <br> 

You need to install jna.jar and jna-platform.jar. Then, run the java file from cmd with the following commands:
  - javac -cp "../Project/jna/jna.jar:jna-platform.jar" filename.java
  - java -cp "../Project/jna/jna.jar:jna-platform.jar" filename

This project was done as part of the work for the internship at ESTECO, Trieste. 
