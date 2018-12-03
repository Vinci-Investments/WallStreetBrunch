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
                        new Product("Cookies", 0),
                        new Product("Crepes", 0),
                        new Product("Jus", 0),
                        new Product("Cafe", 0),
                        new Product("Choco", 0),
                        new Product("Club", 0),
                        new Product("Salad", 0)
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
    	System.out.println("\nCYCLE OVER, GENERATING NEW PRICES...");


        if (VarJus==0)
        {
        	productList.get(4).randomizePrice();
        }
        else
        {
        	productList.get(4).priceProperty().set(productList.get(4).priceProperty().get()+(VarJus*0.1));
        	System.out.println("\nJus Price forced to change by "+VarJus*0.1+" !");
        }
        if (VarChoco==0)
        {
        	productList.get(6).randomizePrice();
        }
        else
        {
        	productList.get(6).priceProperty().set(productList.get(6).priceProperty().get()+(VarChoco*0.1));
        	System.out.println("\nChoco Price forced to change by "+VarChoco*0.1+" !");
        }
        if (VarCafe==0)
        {
        	productList.get(5).randomizePrice();
        }
        else
        {
        	productList.get(5).priceProperty().set(productList.get(5).priceProperty().get()+(VarCafe*0.1));
        	System.out.println("\nCafe Price forced to change by "+VarCafe*0.1+" !");
        }
        if (VarCookies==0)
        {
        	productList.get(2).randomizePrice();
        }
        else
        {
        	productList.get(2).priceProperty().set(productList.get(2).priceProperty().get()+(VarCookies*0.1));
        	System.out.println("\nCookies Price forced to change by "+VarCookies*0.1+" !");
        }
        if (VarVienn==0)
        {
        	productList.get(0).randomizePrice();
        }
        else
        {
        	productList.get(0).priceProperty().set(productList.get(0).priceProperty().get()+(VarVienn*0.1));
        	System.out.println("\nViennoiseries Price forced to change by "+VarVienn*0.1+" !");
        }
        if (VarCrepes==0)
        {
        	productList.get(3).randomizePrice();
        }
        else
        {
        	productList.get(3).priceProperty().set(productList.get(3).priceProperty().get()+(VarCrepes*0.1));
        	System.out.println("\nCrepes Price forced to change by "+VarCrepes*0.1+" !");
        }
        if (VarGateaux==0)
        {
        	productList.get(1).randomizePrice();
        }
        else
        {
        	productList.get(1).priceProperty().set(productList.get(1).priceProperty().get()+(VarGateaux*0.1));
        	System.out.println("\nGateaux Price forced to change by "+VarGateaux*0.1+" !");
        }
        if (VarClub==0)
        {
        	productList.get(7).randomizePrice();
        }
        else
        {
        	productList.get(7).priceProperty().set(productList.get(7).priceProperty().get()+(VarClub*0.1));
        	System.out.println("\nClub Price forced to change by "+VarClub*0.1+" !");
        }
        if (VarSalad==0)
        {
        	productList.get(8).randomizePrice();
        }
        else
        {
        	productList.get(8).priceProperty().set(productList.get(8).priceProperty().get()+(VarSalad*0.1));
        	System.out.println("\nSalad Price forced to change by "+VarSalad*0.1+" !");
        }

        VarJus=0;
    	VarCafe=0;
    	VarChoco=0;
 	   	VarCookies=0;
	    VarVienn=0;
	    VarCrepes=0;
	    VarSalad=0;
	    VarClub=0;
	    VarGateaux=0; 

        if(nextCycleIsAKrach)
        {
            System.out.println("\nTHIS CYCLE IS A KRACH, PRICES HAVE DROPPED");

            for (Product p : productList)
            {
                switch (p.nameProperty().getValue())
                {
                    case "Viennoiseries":
                    case "Cookies":
                    case "Crepes":
                    case "Choco":  
                    case "Jus":
                    case "Gateaux":                                                          
                        p.priceProperty().set(0.2);
                        break;
                    case "Cafe":
                        p.priceProperty().set(0.3);
                        break;
                    case "Club":
                    case "Salad":
                    	p.priceProperty().set(0.5);
                    	break;
                    default:
                        System.out.println("[ERROR] Unexpected product encountered while generating. skipping...");
                }
            }
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
    private int VarJus=0;
    private int VarCafe=0;
    private int VarChoco=0;
    private int VarCookies=0;
    private int VarMuffins=0;
    private int VarVienn=0;
    private int VarCrepes=0;
    private int VarSalad=0;
    private int VarClub=0;
    private int VarGateaux=0;

    @FXML private void DownGateaux()
    {
    	VarGateaux--;
    }
    @FXML private void UpGateaux()
    {
    	VarGateaux++;
    }
    @FXML private void DownJus()
    {
    	VarJus--;
    }
    @FXML private void DownCafe()
    {
    	VarCafe--;
    }
    @FXML private void DownChoco()
    {
    	VarChoco--;
    }
    @FXML private void DownCookie()
    {
    	VarCookies--;
    }
    @FXML private void DownVienn()
    {
    	VarVienn--;
    }
    @FXML private void DownCrepe()
    {
    	VarCrepes--;
    }
    @FXML private void DownSalad()
    {
    	VarSalad--;
    }
    @FXML private void DownClub()
    {
    	VarClub--;
    }
    @FXML private void UpJus()
    {
    	VarJus++;
    }
    @FXML private void UpCafe()
    {
    	VarCafe++;
    }
    @FXML private void UpChoco()
    {
    	VarChoco++;
    }
    @FXML private void UpCookie()
    {
    	VarCookies++;
    }
    @FXML private void UpVienn()
    {
    	VarVienn++;
    }
    @FXML private void UpCrepe()
    {
    	VarCrepes++;
    }
    @FXML private void UpSalad()
    {
    	VarSalad++;
    }
    @FXML private void UpClub()
    {
    	VarClub++;
    }

}