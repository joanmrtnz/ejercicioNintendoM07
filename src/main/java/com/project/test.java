import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class test {

    // Función para leer los archivos JSON y convertir su contenido en arrays de arrays
    public static String[][] readJsonFilesToArray(String folderPath) {
        File folder = new File(folderPath);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".json")); // Filtrar archivos JSON

        if (files != null) {
            // Creamos un array principal donde guardaremos los arrays de cada archivo JSON
            String[][] arrayOfArrays = new String[files.length][];

            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                try {
                    // Leer el contenido del archivo JSON como una cadena de texto
                    String content = new String(Files.readAllBytes(Paths.get(file.getPath())));

                    // Eliminar corchetes y dividir los elementos del array
                    content = content.trim().replaceAll("\\[|\\]", ""); // Remover corchetes
                    String[] elements = content.split(","); // Dividir los elementos por coma

                    // Eliminar espacios en blanco alrededor de cada elemento
                    for (int j = 0; j < elements.length; j++) {
                        elements[j] = elements[j].trim();
                    }

                    // Asignar este array (elements) al array de arrays
                    arrayOfArrays[i] = elements;

                } catch (IOException e) {
                    System.err.println("Error al leer el archivo: " + file.getName());
                }
            }

            return arrayOfArrays;

        } else {
            System.out.println("No se encontraron archivos en la carpeta: " + folderPath);
            return new String[0][0]; // Devolver un array vacío si no se encuentran archivos
        }
    }

    public static void main(String[] args) {
        // Ruta de la carpeta que contiene los archivos JSON
        String folderPath = "C:\\Users\\joanc\\Documents\\GitHub\\DAM-DesenvInterficies\\JavaFX\\02 Subvistes\\ejercicioNintendo\\src\\main\\resources\\assets\\data";

        // Leer los archivos y convertirlos en un array de arrays
        String[][] arrayOfArrays = readJsonFilesToArray(folderPath);

        // Mostrar el contenido del array de arrays
        for (int i = 0; i < arrayOfArrays.length; i++) {
            System.out.println("Contenido del archivo " + (i + 1) + ":");
            for (int j = 0; j < arrayOfArrays[i].length; j++) {
                System.out.print(arrayOfArrays[i][j] + " ");
            }
            System.out.println();
        }
    }
}
