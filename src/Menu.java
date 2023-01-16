import java.util.Scanner;
//Simple menu to display the options
public class Menu {

    private final Scanner s;
    private boolean keepRunning = true;
    public Menu(){
        s = new Scanner(System.in);
    }
    private final Controller controller = new Controller();

    private void showOptions() {
        System.out.println();
        System.out.println("************************************************************");
        System.out.println("*       ATU - Dept. Computer Science & Applied Physics     *");
        System.out.println("*                                                          *");
        System.out.println("*                     Chat App                             *");
        System.out.println("*                                                          *");
        System.out.println("************************************************************");
        System.out.println("(1) Sign Up");
        System.out.println("(2) Login");
        System.out.println("(3) Quit");

        //Output a menu of options and solicit text from the user
        System.out.print("Select Option [1-3]>");
        System.out.println();
    }

    public void show() throws Exception {
        while(keepRunning){
            showOptions();
            try{
                int choice = Integer.parseInt(s.next());

            switch (choice){
                case 1 -> signup();
                case 2 -> login();
                case 3 -> {
                    System.out.println("[INFO} Shutting down... please wait...");
                    keepRunning = false;
                }
                default -> System.out.println("[ERROR] Invalid input");
            }
        }catch (NumberFormatException e){
                System.out.println("Invalid input, please enter a number between 1 and 3.");
            }
        }
    }

    private void signup() throws Exception {
        controller.signup();
    }
    private void login() throws Exception {
        controller.login();
    }


}


