import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIInterface extends Remote {
    String processFile(String filePath) throws RemoteException;
}
