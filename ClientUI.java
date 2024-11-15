import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientUI extends JFrame {
    private JTextField filePathField;
    private JButton browseButton;
    private JButton processButton;
    private JTextArea resultArea;
    
    public ClientUI() {
        setTitle("K-Means Processor Client");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        
        JLabel fileLabel = new JLabel("Archivo:");
        fileLabel.setBounds(20, 20, 100, 30);
        add(fileLabel);
        
        filePathField = new JTextField();
        filePathField.setBounds(80, 20, 200, 30);
        add(filePathField);
        
        browseButton = new JButton("Buscar");
        browseButton.setBounds(290, 20, 80, 30);
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int option = fileChooser.showOpenDialog(ClientUI.this);
                if (option == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    filePathField.setText(file.getAbsolutePath());
                }
            }
        });
        add(browseButton);
        
        processButton = new JButton("Procesar");
        processButton.setBounds(150, 70, 100, 30);
        processButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processFile(filePathField.getText());
            }
        });
        add(processButton);
        
        resultArea = new JTextArea();
        resultArea.setBounds(20, 120, 350, 120);
        resultArea.setEditable(false);
        add(resultArea);
    }
    
    private void processFile(String filePath) {
        try {

            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            RMIInterface rmi = (RMIInterface) registry.lookup("RMIInterface");
            

            String result = rmi.processFile(filePath);
            resultArea.setText(result);
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
