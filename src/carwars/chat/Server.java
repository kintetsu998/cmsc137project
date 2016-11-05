package carwars.chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import java.util.HashMap;

public class Server extends Thread {
	private ServerSocket serverSocket;
	private HashMap<String, Socket> sockets;

	public Server(int port) throws IOException {
		serverSocket = new ServerSocket(port);
		sockets = new HashMap<>();
	}

	public void run(){
		System.out.println("Server waiting... ");

		try{
			while(true){
				final Socket server = serverSocket.accept();

				new Thread(){
					@Override
					public void run(){
						String name = null;
						try {
							DataInputStream in;
							DataOutputStream out;
							String message;

							in = new DataInputStream(server.getInputStream());
							name = Server.this.checkDuplicate(in.readUTF());

							sockets.put(name, server);

							out = new DataOutputStream(server.getOutputStream());	
							out.writeUTF("You are now connected as " + name);

							System.out.println(name + " connected!");
							Server.this.sendToAll(name + " connected!", server);
							while(!(message = in.readUTF()).equalsIgnoreCase("exit")) {
								System.out.println(name + ": " + message);
								Server.this.sendToAll(name + ": " + message, server);
							}

							System.out.println(name + " disconnected.");
							Server.this.sendToAll(name + " disconnected.", server);
						} catch(IOException e) {
							e.printStackTrace();
						} finally {
							try{
								server.close();
							} catch(IOException e) {
								e.printStackTrace();
							}
							sockets.remove(name);
						}
					}
				}.start();
			}
		} catch(IOException e) {
			e.printStackTrace();
		}

		System.out.println("Server closing...");
	}

	private String checkDuplicate(String name) {
		for(String s : sockets.keySet()) {
			if(s.equals(name)) {
				return checkDuplicate2(name, name, 0);
			}
		}

		return name;
	}

	private String checkDuplicate2(String name, String base, int i) {
		for(String s : sockets.keySet()) {
			if(s.equals(name)) {
				return checkDuplicate2(base + (i+1), base, i+1);
			}
		}

		return name;
	}

	private void sendToAll(String message, Socket socket) {
		for(String name : sockets.keySet()) {
			Socket s = sockets.get(name);

			if(socket != s && !s.isClosed()) {
				try{
					DataOutputStream out = new DataOutputStream(s.getOutputStream());
					out.writeUTF(message);
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static void help() {
		System.out.println("Using the Server class:");
		System.out.println("java Server [port number]");
	}

	public static void main(String[] args) {
		int port;

		try{
			port = Integer.parseInt(args[0]);
		} catch (Exception e) {
			help();
			return;
		}

		try{
			Thread t = new Server(port);
			t.start();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}
}