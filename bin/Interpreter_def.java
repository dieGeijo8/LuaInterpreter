//imports for GUI
import javax.swing.*;
import java.awt.*;  
import java.awt.event.*; 
//import for jna&others
import java.util.NoSuchElementException;
import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.Function;
import java.util.Scanner;

public class Interpreter_def implements ActionListener{

    //interface to interact with liblua.so
    public interface C_lua  extends Library {

        //to initialize
        Pointer luaL_newstate();
        void luaL_openlibs(Pointer p);
        //to perform luaL_dofile
        int luaL_loadfilex(Pointer P, String filename, String un);
        //for the interpreter
        int lua_pcallk(Pointer p, int args, int res, int msg, Pointer un1, Pointer un2);
        int luaL_loadbufferx(Pointer p, String buff, int size, String name, Pointer un);
        String lua_tolstring(Pointer p, int index, Pointer un);
        String lua_pushlstring(Pointer p, String line, int len);
        String lua_pushliteral(Pointer p, String s);
    }

    //interface to interact with libmylib.so
    public interface Mylib  extends Library {

        interface func_point extends Callback{
            //this is the function that I will set as callback function in mylib.so
            //and so as Lua print function
            void callback_print(String s);
        }
        //function of my lib.so that sets fn as callback function
        void set_callback(func_point fn);
    }

    //not a good pratcice but I need to call them in the interpreter that is called by the action listener
    //it is not simple to pass parameters to the action listener so I decide to make this variables private globals 
    private C_lua instance1;
    private Pointer L;
    private Mylib instance2;
    private Mylib.func_point fn;

    private JFrame f;
    private JTextArea jt1;
    private JTextArea jt2;
    private JButton b;

    //constructor
    Interpreter_def(){
        //init instances
        instance1 =(C_lua)Native.load("lua", C_lua.class);
        instance2 = (Mylib)Native.load("mylib", Mylib.class);

        //init fn with the function I will set as callback function in mylib.so and so as print function in Lua
        fn = new Mylib.func_point(){
            //function I want to set as callback 
            public void callback_print(String s){
                //print the string received in the output text area, consider multi prints
                jt2.setText(jt2.getText() + ' ' + s);
            }
        };
        //set fn as mylib.so callback function
        instance2.set_callback(fn);

        L = instance1.luaL_newstate();
        instance1.luaL_openlibs(L);
        //set fn as Lua print function
        instance1.luaL_loadfilex(L, "main_support.lua", null);
        instance1.lua_pcallk(L, 0, 1, 0, null, null);
        instance1.lua_pushlstring(L, "", "".length());

        //set the layout
        f = new JFrame("Lua interpreter");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(500,500);  

        //text area, editable, fixed dimensions
        jt1 = new JTextArea(10, 10);
        jt1.setBounds(50,50,250,150);

        //text area, not editable, to write the output, fixed dimensions
        jt2 = new JTextArea(5,5);
        jt2.setBounds(50,250,250,80);
        jt2.setForeground(Color.RED);
        jt2.setEditable(false);

        //button and associated action
        b = new JButton("Run");
        b.setBounds(50,350,150,20);
        b.addActionListener(this);

        //make all visible
        f.add(jt1);f.add(jt2);f.add(b);  
        f.setLayout(null);  
        f.setVisible(true);
    }

    //multiline interpreter
    //it is interactive but can also run correctly multiline commands
    public void multiline_interpreter (String user) {
        //for multi prints
        jt2.setText("");
        //take the last element on the stack, if in the prior iteration I called pcallk I will have only an empty string,
        //otherwise I will have the uncompleted command
        String stack = instance1.lua_tolstring(L, -1, null);

        //to avoid null in the absolute first iteration, it can cause problems
        if(stack == null){
            stack = "";
        }

        //push on the stack the concatenation of what I had in the stack and str, that is what I received from the user
        String input = stack + ' ' + user;
        
        //try to compile,if there are no errors status will be equal to 0, meaning the command is complete
        int status = instance1.luaL_loadbufferx(L, input, input.length(), "=stdin", null);
        
        //if status=0 I run the chunk with pcallk
        if (status == 0) {
            int res = instance1.lua_pcallk(L, 0, 1, 0, null, null);
            //pass en empty string to not get a null when taking from stack in the next iteration
            instance1.lua_pushlstring(L, "", 0);
        } else {
            //if status != 0 I push on the stack the uncomplete command I tried to compile to take it from the stack 
            //in the next iteration and concatenate to it the new user command
            instance1.lua_pushlstring(L, input, input.length()); 
        }
    }

    //action for the run button
    public void actionPerformed(ActionEvent e) {
        //get the text inserted by the user
        String s = jt1.getText();
        //empty the text area
        jt1.setText("");
        //compile and run the command
        multiline_interpreter(s);
        //jt2.setText("");
    }

    public static void main(String[] args){
        Interpreter_def res = new Interpreter_def();
    }
}


// class Something { 

//     int getParameter() {
//        return 42;
//     } 
    
//    @Override
//     void listener(Action event event) {
//        parameter = getParameter()
//       // do something with parameter      
//  } 
//   }
//  set_callback(new Something()::listener)
//  Riccardo Di Meo4:50 PM
//  IntSupplier() myMethod()  {
//  int foo = 42:
//  return lambda x: return foo;
//  }

//return L(Intepreter_def d)
//return d.L

// public class Support implements ActionListener {
//     private Intepreter_def interpreter;

//     public GenPLCListener(Inter myInterpreter) {
//         intepreter = myInterpreter;
//     }

//     public getJt1(){
//        return interpreter.getjt1();
//}
//     @Override
//     public void actionPerfomred(ActionEvent e) {//
//}
//         ;
//         
//     }
// }