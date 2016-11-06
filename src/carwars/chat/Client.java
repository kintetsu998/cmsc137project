package carwars.chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import carwars.init.PlayerLogin;

import java.net.*;

public class Client {
    
    private DataInputStream in;
    private DataOutputStream out;
    private int port;
    private String name;
    private String server;
    private Socket sock;
    private ChatRoom gui;

    public Client(String name, String server, int port, ChatRoom gui) {
        this.name = name;
        this.server = server;
        this.port = port;
        this.gui = gui;
    }

    public boolean connect() {
        // open socket connection
        try {
            sock = new Socket(server, port);
            
            // open input and output streams
            try{
                in  = new DataInputStream(sock.getInputStream());
                out = new DataOutputStream(sock.getOutputStream());
            }
            catch (IOException e1) {
                display("Exception creating new I/O streams: " + e1);
                return false;
            }

            out.writeUTF(this.name);

            // receive messages
            new Thread() {
                public void run() {
                    try{
                        String reply;
                        while(!sock.isClosed()){
                            while((reply = in.readUTF()) == null){}
                            display(reply);
                        }
                    } catch(Exception e1) {
                        display("Exception in thread: " + e1);
                    }
                }
            }.start();

        } catch (ConnectException e2) {
            display("Exception connecting to server: " + e2);
            System.out.println(e2);
            return false;
        } catch (IOException e3) {
            // e.printStackTrace();
            display("Exception in server/port I/O: " + e3);
            return false;
        }
        return true;
    }

    public void sendMessage(String message){
        try {
            out.writeUTF(message);
            display(name + ": " + message);
        }
        catch(IOException e) {
            display("Exception sending to server: " + e);
        }
    }

    public String getName(){
        return this.name;
    }

    // public static void main(String[] args) {
    //     Scanner sc = new Scanner(System.in);
    //     String serverName;
    //     String name;
    //     int port;

    //     if(args.length < 2) {
    //         help();
    //         return;
    //     }

    //     serverName = args[0];
    //     try{
    //         port = Integer.parseInt(args[1]);
    //     } catch (NumberFormatException e) {
    //         help();
    //         return;
    //     }

    //     System.out.print("Enter your name: ");
    //     name = sc.nextLine();
        
    //     new Client(name).connect(serverName, port); 
    // }

    public void display(String msg) {
        gui.append(msg + "\n");      // append to the PlayerLogin JTextArea (or whatever)
    }

    private static void help() {
        System.out.println("Using the Client class:");
        System.out.println("java Client [ip address] [port number]");
    }
}