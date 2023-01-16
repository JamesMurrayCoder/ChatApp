import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

//Allow the user to login
public class Login {
    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    private String host;
    private int port;
    private String password;
    private final Scanner s = new Scanner(System.in);

    public String getUsername() {
        return username;
    }

    private String username;

    //Check if username exists. Then checks password. exits on 3 incorrect passwords.
    //Returns to menu of username doesn't exist.
    public void signIn() throws Exception {
        System.out.println("Please enter your username:");
        username = s.nextLine();
        if(checkAccount(username)) {
            int attempts = 0;
            while (attempts < 3) {
                System.out.println("Please confirm your password");
                String passwordAttempt = s.nextLine();
                if (!passwordAttempt.equals(password)) {
                    System.out.println("Sorry, that is not correct");

                    attempts++;
                } else return;
            }System.out.println("Incorrect password, shutting down");
            System.exit(0);
        }
    }
    //Check if the account exists and update the variables if it does.
    private boolean checkAccount(String username) throws Exception {
        try {
            Files.lines(Paths.get(username)).forEach(text -> {
                try {
                    String[] values = text.split(" ");
                    password = values[0];
                    host = values[1];
                    port = Integer.parseInt(values[2]);

                } catch (NumberFormatException e) {
                    System.err.println("Error: invalid integer value.");
                }
            });
        } catch (IOException e) {
            System.out.println("Error: file does not exist. Returning to the main menu");
            Controller controller = new Controller();
            controller.menu();
            return false;
        }
        return true;
    }
}
