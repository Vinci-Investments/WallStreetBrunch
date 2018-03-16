package sample;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import org.bson.Document;
import org.json.JSONObject;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import static java.lang.StrictMath.abs;

public class Controller
{
    private MongoDatabase db;
    private ObservableList<Product> productList = FXCollections.observableArrayList();
    private ObservableList<Product> formerProductList = FXCollections.observableArrayList();
    private Timeline tl;
    private int currentSecondSinceUpdate;
    private int sync;

    @FXML private Label minuter;
    @FXML private Label varsLabel;
    @FXML private TableView<Product> TabView;
    @FXML private TableColumn<Product, String> prodCol;
    @FXML private TableColumn<Product, Double> priceCol;

    public Controller()
    {
        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://victorQrt:DansCetteDBYaDesCrepes@clustervi-shard-00-00-rsuki.mongodb.net:27017/test?ssl=true&replicaSet=ClusterVI-shard-0&authSource=admin"));
        db = mongoClient.getDatabase("products");
        tl = new Timeline(new KeyFrame(Duration.seconds(1), ae -> passASecond()));
        tl.setCycleCount(Animation.INDEFINITE);
        currentSecondSinceUpdate = 0;

        productList.addAll(new ArrayList<>(
                Arrays.asList(
                        new Product("Viennoiseries", 1),
                        new Product("Gateaux", 1),
                        new Product("Pancakes", 1),
                        new Product("Donuts", 1),
                        new Product("Cookies", 1),
                        new Product("Muffins", 1),
                        new Product("Crepes", 1),
                        new Product("Jus de fruits", 1),
                        new Product("Nespresso", 1),
                        new Product("Chocolat chaud", 1)
                )
        ));
    }

    @FXML public void initialize()
    {
        prodCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        TabView.setItems(productList);
        tl.play();

        getProductListFromCluster();
        computeVariations();
    }

    private void getProductListFromCluster()
    {
        Block<Document> getColStrBlock = document -> {
            JSONObject jsonObj = new JSONObject(document.toJson());
            productList.add(new Product(jsonObj.getString("name"), jsonObj.getDouble("price")));
        };

        formerProductList.clear();
        formerProductList.addAll(productList);
        productList.clear();
        MongoCollection<Document> collection = db.getCollection("products");
        collection.find().forEach(getColStrBlock);
    }

    private void passASecond()
    {
        currentSecondSinceUpdate++;

        if(currentSecondSinceUpdate == 90)
        {
            currentSecondSinceUpdate = 0;
            new Thread(() -> Platform.runLater(() -> getProductListFromCluster())).start();
            new Thread(() -> Platform.runLater(() -> computeVariations())).start();
            minuter.setStyle("-fx-text-fill: black;");
        }
        else if(currentSecondSinceUpdate % 5 == 0)
        {
            Block<Document> getSynchronizer = document -> {
                JSONObject jsonObj = new JSONObject(document.toJson());
                sync = Integer.parseInt(jsonObj.getString("timeUntilUpdate"));
            };

            MongoCollection<Document> collection = db.getCollection("synchronizer");
            collection.find().forEach(getSynchronizer);

            if(abs(sync - (90 - currentSecondSinceUpdate)) > 5)
            {
                currentSecondSinceUpdate = 90 - sync;
                new Thread(() -> Platform.runLater(() -> getProductListFromCluster())).start();
                new Thread(() -> Platform.runLater(() -> computeVariations())).start();
            }
        }

        if(currentSecondSinceUpdate == 75)
            minuter.setStyle("-fx-text-fill: red;");

        minuter.setText(((90-currentSecondSinceUpdate > 59) ? " 1 : " : " 0 : ") + ((((90-currentSecondSinceUpdate) % 60) > 9) ? "" : "0") + String.valueOf((90-currentSecondSinceUpdate) % 60));
    }

    private void computeVariations()
    {
        String variations = "";
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.DOWN);

        for(int i = 0; i<productList.size(); i++)
            variations += productList.get(i).nameProperty().getValue() + " : " + ((productList.get(i).priceProperty().getValue() / formerProductList.get(i).priceProperty().getValue() <= 1) ? "" : "+") + df.format(100 * (productList.get(i).priceProperty().getValue() - formerProductList.get(i).priceProperty().getValue()) / formerProductList.get(i).priceProperty().getValue()) + " %  ";

        varsLabel.setText(variations);
    }
}
