This is a Lua interpreter written in Java.

To use the shared libraries run the following command from the terminal:
    export LD_LIBRARY_PATH=$LD_LIBRARY_PATH../Project/lib

You need to install jna,jar and jna-platform.jar.
Run the java file with the following commands:
    javac -cp "../Project/jna/jna.jar:jna-platform.jar" filename.java
    java -cp "../Project/jna/jna.jar:jna-platform.jar" filename.java