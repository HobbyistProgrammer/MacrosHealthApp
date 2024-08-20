package com.example.demomacros;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.time.LocalDate;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

public class AddFoodController {

    @FXML
    private Button backBtn;

    @FXML
    private TextField txtSearch;

    @FXML
    private ListView listSearched;

    private MainController helloController; // Reference to HelloController

    private LocalDate dateToAdd; // This will control which date to add too

    public void setHelloController(MainController helloController) {
        this.helloController = helloController;
    }

    public void initializeData(LocalDate date) {
        dateToAdd = date;
    }

    @FXML
    protected void onBackButtonClicked() {
        Stage stage = (Stage) backBtn.getScene().getWindow();
        stage.close();
    }


    @FXML
    protected void findProductByName() {

        String product = txtSearch.getText();

        String urlString = "https://api.spoonacular.com/food/products/search?";
        String apiKey = "apiKey=ENTER_API_HERE";
        String query = "&query=" + product;

        try{
            URL url = new URI(urlString + apiKey + query).toURL();

            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                // print(inputLine);
                response.append(inputLine);
            }
            in.close();

            con.disconnect();

            String jsonString = response.toString();
            JSONObject json = new JSONObject(jsonString);

            JSONArray productsArray = json.getJSONArray("products");
            Dictionary<String, Integer> foundItems = new Hashtable<>();

            // print(json.toString(2));

            for (int i = 0; i < productsArray.length(); i++) {
                JSONObject data = productsArray.getJSONObject(i);
                String name = data.getString("title");
                int id = data.getInt("id");
                // Combine the ID and title into a single string
                String combined = id + ": " + name;
                foundItems.put(combined, id);
            }

            // Create an ObservableList to hold the combined strings (ID: Title)
            ObservableList<String> productDetails = FXCollections.observableArrayList();

            // Add combined strings to the ObservableList
            Enumeration<String> keys = foundItems.keys();
            while (keys.hasMoreElements()) {
                String key = keys.nextElement();
                productDetails.add(key);
            }

            // Set the items of the ListView to the combined strings
            listSearched.setItems(productDetails);

            listSearched.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if(event.getClickCount() == 2) {
                        String selectedProduct = (String) listSearched.getSelectionModel().getSelectedItem();
                        if (selectedProduct != null) {
                            String[] values = selectedProduct.split(": ");
                            int id = Integer.parseInt(values[0]);
                            String title = values[1];

                            // print(id + ": " + title);

                            helloController.addSelectedProduct(id, title);

                            Stage stage = (Stage) listSearched.getScene().getWindow();
                            stage.close();
                        }
                    }
                }
            });

        } catch (Exception e) {
            print(e.toString());
        }
    }

    protected void print(String message){
        System.out.println(message);
    }
}
