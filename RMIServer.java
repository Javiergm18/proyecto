import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer {
    public static void main(String[] args) {
        try {
            // Crear registro RMI en el puerto 1099
            Registry registry = LocateRegistry.createRegistry(1099);
            // Crear una instancia del servicio y enlazarlo al registro
            registry.rebind("RMIInterface", new ServerImplementation());
            System.out.println("Servidor RMI listo.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
