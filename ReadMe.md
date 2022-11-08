# Lua Interpreter 
This is a **Lua interpreter** written in **Java**. It uses **JNA** and **Lua C api**. It runs on **Linux**, to make it run on Windows you should convert the .so libraries in .ddl . <br>

To use the shared libraries run the following command from the terminal: export LD_LIBRARY_PATH=$LD_LIBRARY_PATH../Project/lib . <br> 

You need to install jna.jar and jna-platform.jar. Then, run the java file from cmd with the following commands:
  - javac -cp "../Project/jna/jna.jar:jna-platform.jar" filename.java
  - java -cp "../Project/jna/jna.jar:jna-platform.jar" filename
