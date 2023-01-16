import java.io.IOException;
import java.net.Socket;
import java.rmi.ConnectException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

//Main hub of the server side.
public class Controller implements ConnectionListener {
    private Client client;
    private String host;
    private int port;
    private String messageFromClient;
    private String username;
    private boolean connected =false;
    private int countConnectAttempt = 0;
    private volatile boolean stopThread = false;


    public Controller(){
    }

    //Show the menu
    public void menu() throws Exception {
        Menu m = new Menu();
        m.show();
    }
    //Allow the user to signup then automatically starts the chat when they have.
    public void signup() throws Exception {
        Signup signup = new Signup();
        signup.createAccount();
        host = signup.getHost();
        port = signup.getPort();
        username = signup.getUsername();
        initialize();
    }

    //Allow the user to login
    public void login() throws Exception {
        Login login = new Login();
        login.signIn();
        host = login.getHost();
        port = login.getPort();
        username = login.getUsername();
        initialize();
    }

    //Starts the connection with the server.
    private void initialize() throws Exception {
        while(!stopThread) {
            try {
                client = new Client(new Socket(this.host, this.port));
                System.out.println("Connected to server. Please type your message:");
                connected = true;
                client.setConnectionListener(this);
                break;
            } catch (ConnectException e) {
                System.out.println("Could not connect to server, trying again in 5 seconds");

            } catch (IOException e) {
                while (countConnectAttempt<5) {
                    countConnectAttempt++;
                    System.out.println("Could not connect to server, trying again in 5 seconds");
                    TimeUnit.SECONDS.sleep(5);
                    initialize();
                }System.out.println("Could not connect to server, shutting down");
                System.exit(0);
                break;
            }
        }
        client.receiveMessageFromServer();
        getMessage();
    }
    //implementation of the ConnectionListener
    @Override
    public void onConnectionLost() throws Exception {
            try {
                countConnectAttempt = 0;
                TimeUnit.SECONDS.sleep(2);
                initialize();
            } catch (Exception e) {
                e.printStackTrace();
            }

    }
    //Gets the message from the client. Shuts down if 'q' is entered
    private synchronized void getMessage() throws Exception {
        Scanner s = new Scanner(System.in);
        while (connected) {
            messageFromClient = s.nextLine();
            messageFromClient = username+ ": " +messageFromClient;
            if (messageFromClient.equals(username+ ": q")) {
                client.sendMessageToServer(username+ " has disconnected");
                stopThreadGracefully();
                System.out.println("Shutting down, goodbye...");
                System.exit(0);
                break;
            }
            client.sendMessageToServer(messageFromClient);
        }

    }
    //Allow the thread to be stopped gracefully
    private void stopThreadGracefully() {
        stopThread = true;
    }

}
