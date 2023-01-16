import java.io.FileNotFoundException;
import java.io.PrintWriter;
//Save the data from signup in a txt file. Name of the file is the username.txt
public class Save {
    public void save(Signup signup) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(signup.getUsername());
        pw.write(signup.getPassword() + " "+ signup.getHost() + " " +signup.getPort());
        pw.close();
    }

}

