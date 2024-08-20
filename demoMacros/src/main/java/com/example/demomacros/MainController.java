package com.example.demomacros;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

public class MainController {
    @FXML
    private DatePicker datePicker;

    @FXML
    private Label dateLabel, lblFats, lblCalories, lblCarbs, lblProteins;

    @FXML
    private Button prevDate, nextDate;

    @FXML
    private ListView listTrackedFood;

    private String foundTitle;
    private int foundId;

    @FXML
    protected void initialize() {

        datePicker.setValue(LocalDate.now());
        dateLabel.setText(datePicker.getValue().toString());

        // Generate main screen data on initialize
        searchDailyConsumption();

        prevDate.setOnAction(event -> {

            LocalDate prevDate = datePicker.getValue().minusDays(1);

            datePicker.setValue(prevDate);
            dateLabel.setText(datePicker.getValue().toString());

            searchDailyConsumption();
        });

        nextDate.setOnAction(event -> {

            LocalDate nextDate = datePicker.getValue().plusDays(1);

            datePicker.setValue(nextDate);
            dateLabel.setText(datePicker.getValue().toString());

            searchDailyConsumption();
        });

        datePicker.setOnAction(event -> {

            dateLabel.setText(datePicker.getValue().toString());
            datePicker.setVisible(false);

            searchDailyConsumption();
        });
    }

    @FXML
    private void showDatePicker() {
        datePicker.setVisible(true);
        datePicker.show();
    }

    protected void searchDailyConsumption() {
        try{
            String url = "jdbc:mysql://127.0.0.1:3306/DATABASE_NAME?useSSL=false";
            String user = "root";
            String password = "ENTER_PASSWORD_HERE";

            Connection connection = DriverManager.getConnection(
                    url,
                    user,
                    password
            );
            print("connected to db");

            LocalDate currDate = datePicker.getValue();

            print(currDate.toString());

            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery("SELECT * FROM food WHERE dateadded LIKE " + '"' + currDate + '"' + ";");

            float totalCalories = 0.0f, totalCarbs = 0.0f, totalFats = 0.0f, totalProteins = 0.0f;

            Dictionary<String, Integer> foundItems = new Hashtable<>();

            while(results.next()) {

                int id = Integer.parseInt(results.getString(1));
                String title = results.getString(2);
                float calories = results.getFloat(3);
                float carbs = results.getFloat(4);
                float fats = results.getFloat(5);
                float proteins = results.getFloat(6);

                foundItems.put(title, id);

                totalCalories += calories;
                totalCarbs += carbs;
                totalFats += fats;
                totalProteins += proteins;
            }

            lblCalories.setText(String.valueOf(totalCalories));
            lblCarbs.setText(String.valueOf(totalCarbs));
            lblFats.setText(String.valueOf(totalFats));
            lblProteins.setText(String.valueOf(totalProteins));

            ObservableList<String> productDetails = FXCollections.observableArrayList();
            Enumeration<String> keys = foundItems.keys();
            while (keys.hasMoreElements()) {
                String key = keys.nextElement();
                productDetails.add(key);
            }

            listTrackedFood.setItems(productDetails);

            connection.close();

        } catch (SQLException e) {
            print(e.toString());
        }
    }

    @FXML
    protected void onAddClicked() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("addfood-view.fxml"));
            Parent root = fxmlLoader.load();

            AddFoodController addFoodController = fxmlLoader.getController();
            addFoodController.setHelloController(this);

            Stage stage = new Stage();
            addFoodController.initializeData(datePicker.getValue());

            stage.setTitle("Add Food Item");
            stage.setScene(new Scene(root));

            // Prevent user from going back to first window.
            stage.initModality(Modality.APPLICATION_MODAL);

            stage.showAndWait();
        } catch (IOException e){
            print(e.toString());
        }
    }

    public void addSelectedProduct(int id, String title) {
        foundId = id;
        foundTitle = title;
        print(id + ": " + title);

        JSONObject data = findProductInfoById(id);
        if (data == null) {
            print("Cant find id");
        } else {

            //print(data.toString(2));

            JSONObject nutrition = data.getJSONObject("nutrition");

            print(nutrition.toString());

            // JSONObject caloricBreakdown = nutrition.getJSONObject("caloricBreakdown");

            // print(caloricBreakdown.toString());

            float carbs = nutrition.has("carbs") && !nutrition.isNull("carbs")
                    ? Float.parseFloat(nutrition.getString("carbs").replace("g", "").trim()) : 0.0f;

            float protein = nutrition.has("protein") && !nutrition.isNull("protein")
                    ? Float.parseFloat(nutrition.getString("protein").replace("g", "").trim()) : 0.0f;

            float fat = nutrition.has("fat") && !nutrition.isNull("fat")
                    ? Float.parseFloat(nutrition.getString("fat").replace("g", "").trim()) : 0.0f;

            float calories = nutrition.has("calories") && !nutrition.isNull("calories")
                    ? nutrition.getInt("calories") : 0.0f;

            LocalDate date = datePicker.getValue();

            ProductInfo info = new ProductInfo(foundId, foundTitle, calories, carbs, protein, fat, date);

            print(info.getId() + ", " +  info.getTitle() + ", " + info.getDate());

            try{
                String url = "jdbc:mysql://127.0.0.1:3306/DATABASE_NAME?useSSL=false";
                String user = "root";
                String password = "ENTER_PASSWORD_HERE";

                Connection connection = DriverManager.getConnection(
                        url,
                        user,
                        password
                );
                print("connected to db");

                String insertStatement = "INSERT INTO food (id, foodname, calories, carbs, fats, proteins, dateadded) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";

                try (PreparedStatement statement = connection.prepareStatement(insertStatement)) {
                    statement.setInt(1, info.getId());
                    statement.setString(2, info.getTitle());
                    statement.setFloat(3, info.getCalories());
                    statement.setFloat(4, info.getCarbs());
                    statement.setFloat(5, info.getFats());
                    statement.setFloat(6, info.getProteins());

                    java.util.Date adddate = Date.from(info.getDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
                    java.sql.Date sqlDate = new java.sql.Date(adddate.getTime());

                    statement.setDate(7, sqlDate);


                    int results = statement.executeUpdate();
                    print(results + " row(s) inserted.");

                    searchDailyConsumption();
                } catch (SQLException e) {
                    print(e.toString());
                }
            } catch (SQLException e) {
                print(e.toString());
            }
        }
    }

    protected JSONObject findProductInfoById(int id) {

        try {

            String hostUrl = "https://api.spoonacular.com/food/products/";
            String apikey = "?apiKey=ENTER_API_KEY";

            URL url = new URL(hostUrl + id + apikey);

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

            return json;
        } catch (Exception e) {
            print(e.toString());
        }
        return null;
    }


    protected void print(String message){
        System.out.println(message);
    }
}