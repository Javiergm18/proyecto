import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class AreaBajoCurva {
    public static void main(String[] args) {
        double a = 1.0;
        double b = 10000000.0;
        double c = b * 100000; // Calcular c
        int n = (int)c; // Usar c como el número de intervalos

        long startTime = System.currentTimeMillis(); // Tiempo inicial

        double h = (b - a) / n; // Ancho de cada intervalo
        double area = 0.5 * (f(a) + f(b)); // Inicializar área

        for (int i = 1; i < n; i++) {
            area += f(a + i * h); // Sumar áreas de los rectángulos
        }
        area *= h; // Multiplicar por el ancho para obtener el área total

        long endTime = System.currentTimeMillis(); // Tiempo final
        long duration = endTime - startTime; // Duración en ms

        System.out.println("El área bajo la curva es: " + area);
        System.out.println("Tiempo de ejecución: " + duration + " ms");

        // Guardar resultados en un archivo
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("resultados.txt", true))) {
            writer.write("Procesadores: " + Runtime.getRuntime().availableProcessors());
            writer.write(", Área: " + area);
            writer.write(", Tiempo: " + duration + " ms");
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error al escribir el archivo: " + e.getMessage());
        }
    }

    public static double f(double x) {
        return 1.0 / x; // Función f(x) = 1/x
    }
}
