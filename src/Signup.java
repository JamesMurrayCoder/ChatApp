import java.io.FileNotFoundException;
import java.util.Scanner;
//Allow user to create an account. Takes data from user and uses Save class to save it
public class Signup {
    private String host;
    private int port;
    private String username;
    private String password;

    private final Scanner s = new Scanner(System.in);
    public void createAccount() throws FileNotFoundException {
        System.out.println("Enter your user name:");
        username = s.nextLine();
        System.out.println("Enter your password:");
        password = s.nextLine();
        System.out.println("Enter your host IP address:");
        host = s.nextLine();
        System.out.println("Enter the port number:");
        port = Integer.parseInt(s.nextLine());
        Signup signup = new Signup(host,port,username,password);
        save(signup);
    }
    private void save(Signup signup) throws FileNotFoundException {
        Save save = new Save();
        save.save(signup);
    }

    public Signup(String host, int port, String username, String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    public Signup() {
    }

    public String getHost() {
        return host;
    }


    public int getPort() {
        return port;
    }


    public String getUsername() {
        return username;
    }


    public String getPassword() {
        return password;
    }

}
