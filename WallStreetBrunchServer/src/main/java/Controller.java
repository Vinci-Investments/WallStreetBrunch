package sample;

import static com.mongodb.client.model.Filters.eq;
import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import org.bson.types.ObjectId;
import org.json.*;

public class Controller
{
    private MongoDatabase db;
    private ObservableList<Product> productList = FXCollections.observableArrayList();
    private Timeline tl;
    private int currentSecondSinceUpdate;
    private boolean nextCycleIsAKrach;

    @FXML private TableView<Product> TabView;
    @FXML private TableColumn<Product, String> prodCol;
    @FXML private TableColumn<Product, Double> priceCol;
    @FXML private TextArea logTxtArea;
    @FXML private Label timeCountLabel;

    @FXML public void initialize()
    {
        prodCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        TabView.setItems(productList);
        tl.play();
    }

    public Controller()
    {
        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://victorQrt:DansCetteDBYaDesCrepes@clustervi-shard-00-00-rsuki.mongodb.net:27017/test?ssl=true&replicaSet=ClusterVI-shard-0&authSource=admin"));
        db = mongoClient.getDatabase("products");
        tl = new Timeline(new KeyFrame(Duration.seconds(1), ae -> passASecond()));
        tl.setCycleCount(Animation.INDEFINITE);
        currentSecondSinceUpdate = 0;
        nextCycleIsAKrach = false;

        productList.addAll(new ArrayList<>(
                Arrays.asList(
                        new Product("Viennoiseries", 0),
                        new Product("Gateaux", 0),
                        new Product("Pancakes", 0),
                        new Product("Donuts", 0),
                        new Product("Cookies", 0),
                        new Product("Muffins", 0),
                        new Product("Crepes", 0),
                        new Product("Jus de fruits", 0),
                        new Product("Nespresso", 0),
                        new Product("Chocolat chaud", 0)
                )
        ));
    }

    public TextArea getLogTxtArea() {
        return logTxtArea;
    }

    public void getProductListFromCluster()
    {
        Block<Document> getColStrBlock = document -> {
            System.out.println("Downloaded : " + document.toJson());
            JSONObject jsonObj = new JSONObject(document.toJson());
            productList.add(new Product(jsonObj.getString("name"), jsonObj.getDouble("price")));
        };

        productList.clear();
        MongoCollection<Document> collection = db.getCollection("products");
        collection.find().forEach(getColStrBlock);
    }

    private void updateProductList()
    {
        if(nextCycleIsAKrach)
        {
            System.out.println("\nTHIS CYCLE IS A KRACH, PRICES HAVE DROPPED");

            for (Product p : productList)
            {
                switch (p.nameProperty().getValue())
                {
                    case "Viennoiseries":
                    case "Pancakes":
                    case "Donuts":
                    case "Cookies":
                    case "Muffins":
                    case "Crepes":
                        p.priceProperty().set(0.2);
                        break;
                    case "Gateaux":
                    case "Jus de fruits":
                    case "Nespresso":
                    case "Chocolat chaud":
                        p.priceProperty().set(0.3);
                        break;
                    default:
                        System.out.println("[ERROR] Unexpected product encountered while generating. skipping...");
                }
            }
        }

        else
        {
            System.out.println("\nCYCLE OVER, GENERATING NEW PRICES...");

            for (Product p : productList)
                p.randomizePrice();

            System.out.println("GENERATION SUCCESSFUL, SENDING PRICES TO ONLINE CLUSTER...");
        }

        nextCycleIsAKrach = false;
        MongoCollection<Document> collection = db.getCollection("products");

        for (Product p : productList)
            collection.updateOne(eq("name", p.nameProperty().getValue()), new Document("$set", new Document("price", String.valueOf(p.priceProperty().getValue()))));

        System.out.println("UPLOAD SUCCESSFUL, CLUSTER IS UP-TO-DATE");
    }

    private void passASecond()
    {
        MongoCollection<Document> collection = db.getCollection("synchronizer");
        collection.updateOne(eq("_id", new ObjectId("5a16ed1c4d7a9fe05cae7077")), new Document("$set", new Document("timeUntilUpdate", String.valueOf(90 - currentSecondSinceUpdate))));
        currentSecondSinceUpdate++;
        timeCountLabel.setText(((90-currentSecondSinceUpdate > 59) ? "1 minute " : "0 minutes ") + String.valueOf((90-currentSecondSinceUpdate) % 60) + " seconds");
        if(currentSecondSinceUpdate == 90)
        {
            currentSecondSinceUpdate = 0;
            new Thread(() -> Platform.runLater(() -> updateProductList())).start();
        }
    }

    @FXML private void krachTheBrunch()
    {
        nextCycleIsAKrach = true;
        System.out.println("NEXT CYCLE IS SET UP TO BE A KRACH, PRICES WILL DROP");
    }

    @FXML private void resetCycle()
    {
        currentSecondSinceUpdate = 0;
        System.out.println("\nREACHING CLUSTER...");
        getProductListFromCluster();
        System.out.println("CYCLE WAS HARD RESET");
    }

    @FXML private void skipCycle()
    {
        tl.stop();
        currentSecondSinceUpdate = 89;
        passASecond();
        tl.play();
    }
}