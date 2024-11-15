
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientUI extends JFrame {

    private JTextField filePathField;
    private JTextArea resultArea;

    public ClientUI() {
        setTitle("MPI Jobs - Cliente RMI");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel filePathLabel = new JLabel("Ruta del archivo:");
        filePathLabel.setBounds(10, 10, 100, 25);
        add(filePathLabel);

        filePathField = new JTextField();
        filePathField.setBounds(120, 10, 200, 25);
        add(filePathField);

        JButton processButton = new JButton("Procesar");
        processButton.setBounds(120, 50, 100, 25);
        add(processButton);

        resultArea = new JTextArea();
        resultArea.setBounds(10, 90, 360, 150);
        resultArea.setEditable(false);
        add(resultArea);

        processButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processFile();
            }
        });
    }

    private void processFile() {
        try {
            String filePath = filePathField.getText();
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            RMIInterface stub = (RMIInterface) registry.lookup("RMIInterface");

            String response = stub.processFile(filePath);
            resultArea.setText(response);
        } catch (Exception ex) {
            resultArea.setText("Error al procesar el archivo: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ClientUI clientUI = new ClientUI();
            clientUI.setVisible(true);
        });
    }
}
