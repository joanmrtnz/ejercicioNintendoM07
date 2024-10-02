package com.project;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ControllerMobil {

    @FXML
    private ListView<HBox> listView;

    @FXML
    private VBox detailsBox;

    @FXML
    private ImageView imageViewLarge;

    @FXML
    private Text titleLarge;

    @FXML
    private Text descriptionLarge;

    @FXML
    private Button buttonConsolas;

    @FXML
    private Button buttonPersonajes;

    @FXML
    private Button buttonJuegos;

    public class Item {
        private Map<String, Object> properties;

        public Item() {
            properties = new HashMap<>();
        }

        public void addProperty(String key, Object value) {
            properties.put(key, value);
        }

        public Object getProperty(String key) {
            return properties.get(key);
        }

        public String getNom() {
            return (String) properties.get("nom");
        }

        public String getImatge() {
            return (String) properties.get("imatge");
        }

        public Map<String, Object> getProperties() {
            return properties;
        }
    }

    @FXML
    public void initialize() {
        buttonConsolas.setOnAction(event -> handleCategoryButton("Consolas"));
        buttonPersonajes.setOnAction(event -> handleCategoryButton("Personajes"));
        buttonJuegos.setOnAction(event -> handleCategoryButton("Juegos"));

        listView.setVisible(false);
        listView.setManaged(false); 
        detailsBox.setVisible(false);
        detailsBox.setManaged(false); 
    }

    private void handleCategoryButton(String category) {
        loadJSONData(category);
        listView.setVisible(true);
        listView.setManaged(true); 
        detailsBox.setVisible(false); 
        detailsBox.setManaged(false); 

    }

    @FXML
    private void handleListItemClick() {
        HBox selectedItem = listView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            Item item = (Item) selectedItem.getUserData();

            if (item != null) {
                // Establecer los datos en detailsBox
                titleLarge.setText(item.getNom());

                // Cargar la imagen
                String imagePath = "assets/images/" + item.getImatge();
                try (InputStream imageStream = getClass().getClassLoader().getResourceAsStream(imagePath)) {
                    if (imageStream != null) {
                        Image image = new Image(imageStream);
                        imageViewLarge.setImage(image);
                    } else {
                        System.out.println("Error: No se pudo encontrar la imagen en la ruta: " + imagePath);
                    }
                } catch (Exception e) {
                    System.out.println("Error al cargar la imagen: " + imagePath);
                    e.printStackTrace();
                }

                // Mostrar propiedades adicionales en descriptionLarge
                StringBuilder description = new StringBuilder();
                for (Map.Entry<String, Object> entry : item.getProperties().entrySet()) {
                    if (!entry.getKey().equals("nom") && !entry.getKey().equals("imatge")) {
                        // Hago un capitalize de la key
                        description.append(entry.getKey().toUpperCase().charAt(0))
                                   .append(entry.getKey().substring(1))
                                   .append(": ")
                                   .append(entry.getValue())
                                   .append("\n");
                    }
                }
                descriptionLarge.setText(description.toString());

                listView.setVisible(false);
                listView.setManaged(false); 
                detailsBox.setVisible(true);
                detailsBox.setManaged(true);
            }
        }
    }

    private boolean isDesktopMode() {
        return listView.getScene().getWindow().getWidth() > 450;
    }

    private void loadJSONData(String category) {
        String jsonFilePath = getJSONFilePathForCategory(category);
        if (jsonFilePath == null) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(jsonFilePath))) {
            StringBuilder jsonContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line);
            }

            JSONArray jsonArray = new JSONArray(jsonContent.toString());
            ObservableList<HBox> items = FXCollections.observableArrayList();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                Item item = new Item();

                item.addProperty("nom", jsonObject.getString("nom"));
                item.addProperty("imatge", jsonObject.getString("imatge"));

                for (String key : jsonObject.keySet()) {
                    if (!key.equals("nom") && !key.equals("imatge")) {
                        item.addProperty(key, jsonObject.get(key));
                    }
                }

                String name = item.getNom();
                String imagePath = "assets/images/" + item.getImatge();

                HBox hBox = new HBox(10);
                ImageView imageView = new ImageView();

                try (InputStream imageStream = getClass().getClassLoader().getResourceAsStream(imagePath)) {
                    if (imageStream != null) {
                        Image image = new Image(imageStream);
                        imageView.setImage(image);
                    } else {
                        System.out.println("Error: No se pudo encontrar la imagen en la ruta: " + imagePath);
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Error al cargar la imagen: " + imagePath);
                    e.printStackTrace();
                }

                imageView.setFitHeight(50);
                imageView.setFitWidth(50);

                Label nameLabel = new Label(name);

                hBox.getChildren().addAll(imageView, nameLabel);
                hBox.setUserData(item); // Almacenar el objeto Item en el HBox

                items.add(hBox);
            }

            listView.setItems(items);

            listView.setOnMouseClicked(event -> handleListItemClick());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getJSONFilePathForCategory(String category) {
        switch (category) {
            case "Consolas":
                return "src/main/resources/assets/data/consoles.json";
            case "Juegos":
                return "src/main/resources/assets/data/jocs.json";
            case "Personajes":
                return "src/main/resources/assets/data/personatges.json";
            default:
                return null;
        }
    }

    public void setResponsiveListeners(Stage stage) {

        if (isDesktopMode()) {
            detailsBox.setVisible(true);
            detailsBox.setManaged(true);
        }
        stage.widthProperty().addListener((obs, oldVal, newVal) -> {
            double newWidth = newVal.doubleValue();
            listView.setPrefWidth(newWidth * 0.8);
            detailsBox.setPrefWidth(newWidth * 0.8);
        });

        stage.heightProperty().addListener((obs, oldVal, newVal) -> {
            double newHeight = newVal.doubleValue();
            listView.setPrefHeight(newHeight * 0.8);
            detailsBox.setPrefHeight(newHeight * 0.8);
        });
    }
}
