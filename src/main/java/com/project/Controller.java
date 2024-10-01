package com.project;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.ListView;

public class Controller implements Initializable {

    @FXML
    private Button button0, button1, button2, buttonWeekdays, buttonMonths;
    @FXML
    private Label choiceLabel;
    @FXML
    private FlowPane container;
    @FXML
    private ChoiceBox<String> choiceBox;
    @FXML
    private VBox yPane;

    @FXML
    private ListView<String> listView;

    // Ruta de la carpeta que contiene los archivos JSON
    String folderPath = "C:\\Users\\joanc\\Documents\\GitHub\\DAM-DesenvInterficies\\JavaFX\\02 Subvistes\\ejercicioNintendo\\src\\main\\resources\\assets\\data";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Leer los archivos y convertirlos en un array de arrays
        String[][] data = readJsonFilesToArray(folderPath);

        // Crear la lista observable
        ObservableList<List<String>> observableList = FXCollections.observableArrayList();

        // Convertir cada fila del array bidimensional en una lista y agregarlo a la lista observable
        for (String[] row : data) {
            List<String> rowList = new ArrayList<>(List.of(row));  // Usar List.of() en lugar de Arrays.asList()
            observableList.add(rowList);
        }

        // Inicialización del choiceBox
        choiceBox.getItems().addAll("Consoles", "Jocs", "Personatges");
        choiceBox.setValue("Consoles");

        choiceBox.setOnAction((event) -> {
            String valor = choiceBox.getSelectionModel().getSelectedItem();
            if ("Consoles".equals(valor)) {
                listView.setItems(FXCollections.observableArrayList(observableList.get(0)));
            }
            if ("Jocs".equals(valor)) {
                listView.setItems(FXCollections.observableArrayList(observableList.get(1)));
            }
            if ("Personatges".equals(valor)) {
                listView.setItems(FXCollections.observableArrayList(observableList.get(2)));
            }
        });
    }

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
}


