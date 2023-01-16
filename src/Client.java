import java.io.*;
import java.net.Socket;
/* Main client class

 */
public class Client  extends Thread{
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private ConnectionListener connectionListener;

    //Initialise the connection listener
    public void setConnectionListener(ConnectionListener connectionListener) {
        this.connectionListener = connectionListener;
    }

    public Client(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            System.out.println("Error creating client.");
            e.printStackTrace();
            closeEverything(socket, bufferedWriter, bufferedReader);
        }
    }
    //Sends the message to the server
    public synchronized void sendMessageToServer(String messageToServer) {

        try
        {
            bufferedWriter.write(messageToServer);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }catch (IOException e){
            closeEverything(socket,bufferedWriter,bufferedReader);

        }
    }

    //New thread that is started to receive a message from the server.
    //Uses the ConnectionListener interface to try to reconnect with the server
    //if the connection is dropped.
    public synchronized void receiveMessageFromServer() {
        new Thread(() -> {
            while (true) {
                try {
                    String messageFromServer = bufferedReader.readLine();
                    printMessage(messageFromServer);
                } catch (IOException e) {
                    System.out.println("Connection lost, trying to reconnect...");
                    if (connectionListener != null) {
                        closeEverything(socket,bufferedWriter,bufferedReader);
                        try {
                            connectionListener.onConnectionLost();
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                    break;
                }
            }
        }).start();
    }
    private synchronized void printMessage(String messageFromClient) {
        System.out.println(messageFromClient);
    }
    //Shuts down the socket, BufferedReader, and BufferedWriter
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
            e.printStackTrace();
        }
    }
}