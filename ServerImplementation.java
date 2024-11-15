import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

public class ServerImplementation extends UnicastRemoteObject implements RMIInterface {

    protected ServerImplementation() throws RemoteException {
        super();
    }

    @Override
    public String processFile(String filePath) throws RemoteException {
        try {
            Path path = Paths.get(filePath);

            return "Archivo procesado exitosamente: " + new String(Files.readAllBytes(path));
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al procesar el archivo: " + e.getMessage();
        }
    }
}
