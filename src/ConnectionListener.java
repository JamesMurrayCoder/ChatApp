//Interface that allows the Client to contact the Controller without
//keeping an instance of it.
public interface ConnectionListener {
    void onConnectionLost() throws Exception;
}
