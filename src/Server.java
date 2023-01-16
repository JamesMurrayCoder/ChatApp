import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

//Server class with private inner class to handle clients
public class Server {
	private ServerSocket serverSocket;

	//Always running so it can accept new users. New thread for each one.
	public void start() throws IOException {
		serverSocket = new ServerSocket(1234);
		while (true)
			new EchoClientHandler(serverSocket.accept()).start();
	}

	//Private inner class to handle clients.
	private static class EchoClientHandler extends Thread {
		private Socket clientSocket;
		private PrintWriter out;
		private BufferedReader bufferedReader;
		private BufferedWriter bufferedWriter;
		private static List<PrintWriter> clientOutputStreams = new ArrayList<>();
		private String messageFromClient;
		private volatile boolean stopThread = false;

		//Constructor to initialize the relevant variables.  Contains ArrayList of clients 
		//that is added to as they join
		public EchoClientHandler(Socket socket) throws IOException {
			this.clientSocket = socket;
			this.bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
			this.out = new PrintWriter(clientSocket.getOutputStream(), true);
			clientOutputStreams.add(out);
			System.out.println("Connection made.");
		}
		//Allow the thread to stop gracefully
		private void stopThreadGracefully() {
		    stopThread = true;
		    System.out.println("User has disconnected");
		}
		//Receives message from client. 
		public void receiveMessageFromClient(String message) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					while (!stopThread) {
						try {
							String messageFromClient = bufferedReader.readLine();
							if (messageFromClient == null) {
								System.out.println("Lost connection to the client.");
								closeEverything(clientSocket, bufferedWriter, bufferedReader);
								break;
							} else {
								sendMessageToClients(messageFromClient);
							}
						} catch (IOException e) {							
							closeEverything(clientSocket, bufferedWriter, bufferedReader);
							stopThreadGracefully();
						}
					}
				}

			}).start();
		}


		//run method 
		public synchronized void run() {
		    receiveMessageFromClient(messageFromClient);
		}
		//Sends message to all clients in the chat at the time
		private void sendMessageToClients(String message) {
			for (PrintWriter writer : clientOutputStreams) {
				writer.println(message);
				writer.flush();

			}
		}
		//Closes the socket, BufferedWriter, BufferedReader
		private void closeEverything(Socket socket, BufferedWriter bufferedWriter, BufferedReader bufferedReader) {
			try {
				if (socket != null) {
					socket.close();
				}
				if (bufferedReader != null) {
					bufferedReader.close();
				}
				if (bufferedWriter != null) {
					bufferedWriter.close();
				}
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}

	}
}
