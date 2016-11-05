package carwars.chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import java.net.*;
import java.util.Scanner;

public class Client {
	private String name;

	public Client(String name) {
		this.name = name;
	}

	public void connect(String server, int port) {
		try {
			DataOutputStream out;
			DataInputStream in;
			
			Scanner sc = new Scanner(System.in);
			Socket client = new Socket(server, port);
			String message;

			System.out.println("Connected to " + client.getRemoteSocketAddress());

			out = new DataOutputStream(client.getOutputStream());
			out.writeUTF(this.name);

			new Thread() {
				@Override
				public void run() {
					try{
						DataInputStream in = new DataInputStream(client.getInputStream());
						String reply;
						while(!client.isClosed()){
							while((reply = in.readUTF()) == null){}
							System.out.println(reply);
						}
					} catch(Exception e) {
						//e.printStackTrace();
						return;
					}
				}
			}.start();

			do{
				message = sc.nextLine().trim();
				out.writeUTF(message);
			} while(!message.equalsIgnoreCase("exit"));

			client.close();
		} catch (ConnectException e) {
		    System.out.println(e);
		    return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

	public String getName(){
		return this.name;
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String serverName;
		String name;
		int port;

		if(args.length < 2) {
			help();
			return;
		}

		serverName = args[0];
		try{
			port = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
			help();
			return;
		}

		System.out.print("Enter your name: ");
		name = sc.nextLine();
		
		new Client(name).connect(serverName, port);	
	}

	private static void help() {
		System.out.println("Using the Client class:");
		System.out.println("java Client [ip address] [port number]");
	}
}