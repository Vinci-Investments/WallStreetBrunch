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
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
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
	private double[] histoPrixJus = new double  [20];
	private double[] histoPrixCafe = new double  [20];
	private double[] histoPrixChoco = new double  [20];
	private double[] histoPrixSand = new double  [20];
	private double[] histoPrixSalad = new double  [20];
	private double[] histoPrixCookie = new double  [20];
	private  XYChart.Series seriesJus = new XYChart.Series(); 
	private  XYChart.Series seriesCafe = new XYChart.Series();
	private  XYChart.Series seriesChoco = new XYChart.Series();
	private  XYChart.Series seriesSand = new XYChart.Series();
	private  XYChart.Series seriesSalad = new XYChart.Series();
	private  XYChart.Series seriesCookie = new XYChart.Series();
	

    @FXML private Label minuter;
    @FXML private Label varsLabel;
    @FXML private TableView<Product> TabView;
    @FXML private TableColumn<Product, String> prodCol;
    @FXML private TableColumn<Product, Double> priceCol;
	 @FXML
    private LineChart<?, ?> LineChartJus;

   

    @FXML
    private LineChart<?, ?> LineChartCafe;

    @FXML
    private LineChart<?, ?> LineChartGateau;

    @FXML
    private LineChart<?, ?> LineChartDonut;

    @FXML
    private LineChart<?, ?> LineChartSale;

    @FXML
    private LineChart<?, ?> LineChartChoco;

    @FXML
    private LineChart<?, ?> LineChartCookie;

    @FXML
    private LineChart<?, ?> LineChartMuffin;

   

    @FXML
    private LineChart<?, ?> LineChartCrepe;



    public Controller()
    {
        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://victorQrt:DansCetteDBYaDesCrepes@clustervi-shard-00-00-rsuki.mongodb.net:27017/test?ssl=true&replicaSet=ClusterVI-shard-0&authSource=admin"));
        db = mongoClient.getDatabase("products");
        tl = new Timeline(new KeyFrame(Duration.seconds(1), ae -> passASecond()));
        tl.setCycleCount(Animation.INDEFINITE);
        currentSecondSinceUpdate = 0;
		seriesJus.setName("JUS");
		seriesCafe.setName("Nesspresso");
		seriesChoco.setName("Chocolat chaud");
		seriesSand.setName("Sandwich");
		seriesSalad.setName("Salade");
		seriesCookie.setName("Cookie");
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
		addPriceHisto();
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
			
		
		
		seriesJus.getData().clear();
		
		seriesJus.getData().add(new XYChart.Data("0",histoPrixJus[0]));
		seriesJus.getData().add(new XYChart.Data("1:30",histoPrixJus[1]));
        seriesJus.getData().add(new XYChart.Data("3",histoPrixJus[2]));
        seriesJus.getData().add(new XYChart.Data("4:30",histoPrixJus[3]));
        seriesJus.getData().add(new XYChart.Data("6",histoPrixJus[4]));
        seriesJus.getData().add(new XYChart.Data("7:30",histoPrixJus[5]));
        seriesJus.getData().add(new XYChart.Data("9",histoPrixJus[6]));
        seriesJus.getData().add(new XYChart.Data("10:30",histoPrixJus[7]));
        seriesJus.getData().add(new XYChart.Data("12",histoPrixJus[8]));
		seriesJus.getData().add(new XYChart.Data("13:30",histoPrixJus[9]));
        seriesJus.getData().add(new XYChart.Data("15",histoPrixJus[10]));
        seriesJus.getData().add(new XYChart.Data("16:30",histoPrixJus[11]));
        seriesJus.getData().add(new XYChart.Data("18",histoPrixJus[12]));
        seriesJus.getData().add(new XYChart.Data("19:30",histoPrixJus[13]));
        seriesJus.getData().add(new XYChart.Data("21",histoPrixJus[14]));
        seriesJus.getData().add(new XYChart.Data("22:30",histoPrixJus[15]));
        seriesJus.getData().add(new XYChart.Data("24",histoPrixJus[16]));
		seriesJus.getData().add(new XYChart.Data("25:30",histoPrixJus[17]));
        seriesJus.getData().add(new XYChart.Data("27",histoPrixJus[18]));
        seriesJus.getData().add(new XYChart.Data("28:30",histoPrixJus[19]));
		
		seriesCafe.getData().clear();
		
		seriesCafe.getData().add(new XYChart.Data("0",histoPrixCafe[0]));
		seriesCafe.getData().add(new XYChart.Data("1:30",histoPrixCafe[1]));
        seriesCafe.getData().add(new XYChart.Data("3",histoPrixCafe[2]));
        seriesCafe.getData().add(new XYChart.Data("4:30",histoPrixCafe[3]));
        seriesCafe.getData().add(new XYChart.Data("6",histoPrixCafe[4]));
        seriesCafe.getData().add(new XYChart.Data("7:30",histoPrixCafe[5]));
        seriesCafe.getData().add(new XYChart.Data("9",histoPrixCafe[6]));
        seriesCafe.getData().add(new XYChart.Data("10:30",histoPrixCafe[7]));
        seriesCafe.getData().add(new XYChart.Data("12",histoPrixCafe[8]));
		seriesCafe.getData().add(new XYChart.Data("13:30",histoPrixCafe[9]));
        seriesCafe.getData().add(new XYChart.Data("15",histoPrixCafe[10]));
        seriesCafe.getData().add(new XYChart.Data("16:30",histoPrixCafe[11]));
        seriesCafe.getData().add(new XYChart.Data("18",histoPrixCafe[12]));
        seriesCafe.getData().add(new XYChart.Data("19:30",histoPrixCafe[13]));
        seriesCafe.getData().add(new XYChart.Data("21",histoPrixCafe[14]));
        seriesCafe.getData().add(new XYChart.Data("22:30",histoPrixCafe[15]));
        seriesCafe.getData().add(new XYChart.Data("24",histoPrixCafe[16]));
		seriesCafe.getData().add(new XYChart.Data("25:30",histoPrixCafe[17]));
        seriesCafe.getData().add(new XYChart.Data("27",histoPrixCafe[18]));
        seriesCafe.getData().add(new XYChart.Data("28:30",histoPrixCafe[19]));
		
		seriesChoco.getData().clear();
		
		seriesChoco.getData().add(new XYChart.Data("0",histoPrixChoco[0]));
		seriesChoco.getData().add(new XYChart.Data("1:30",histoPrixChoco[1]));
        seriesChoco.getData().add(new XYChart.Data("3",histoPrixChoco[2]));
        seriesChoco.getData().add(new XYChart.Data("4:30",histoPrixChoco[3]));
        seriesChoco.getData().add(new XYChart.Data("6",histoPrixChoco[4]));
        seriesChoco.getData().add(new XYChart.Data("7:30",histoPrixChoco[5]));
        seriesChoco.getData().add(new XYChart.Data("9",histoPrixChoco[6]));
        seriesChoco.getData().add(new XYChart.Data("10:30",histoPrixChoco[7]));
        seriesChoco.getData().add(new XYChart.Data("12",histoPrixChoco[8]));
		seriesChoco.getData().add(new XYChart.Data("13:30",histoPrixChoco[9]));
        seriesChoco.getData().add(new XYChart.Data("15",histoPrixChoco[10]));
        seriesChoco.getData().add(new XYChart.Data("16:30",histoPrixChoco[11]));
        seriesChoco.getData().add(new XYChart.Data("18",histoPrixChoco[12]));
        seriesChoco.getData().add(new XYChart.Data("19:30",histoPrixChoco[13]));
        seriesChoco.getData().add(new XYChart.Data("21",histoPrixChoco[14]));
        seriesChoco.getData().add(new XYChart.Data("22:30",histoPrixChoco[15]));
        seriesChoco.getData().add(new XYChart.Data("24",histoPrixChoco[16]));
		seriesChoco.getData().add(new XYChart.Data("25:30",histoPrixChoco[17]));
        seriesChoco.getData().add(new XYChart.Data("27",histoPrixChoco[18]));
        seriesChoco.getData().add(new XYChart.Data("28:30",histoPrixChoco[19]));
		
		seriesSand.getData().clear();
		
		seriesSand.getData().add(new XYChart.Data("0",histoPrixSand[0]));
		seriesSand.getData().add(new XYChart.Data("1:30",histoPrixSand[1]));
        seriesSand.getData().add(new XYChart.Data("3",histoPrixSand[2]));
        seriesSand.getData().add(new XYChart.Data("4:30",histoPrixSand[3]));
        seriesSand.getData().add(new XYChart.Data("6",histoPrixSand[4]));
        seriesSand.getData().add(new XYChart.Data("7:30",histoPrixSand[5]));
        seriesSand.getData().add(new XYChart.Data("9",histoPrixSand[6]));
        seriesSand.getData().add(new XYChart.Data("10:30",histoPrixSand[7]));
        seriesSand.getData().add(new XYChart.Data("12",histoPrixSand[8]));
		seriesSand.getData().add(new XYChart.Data("13:30",histoPrixSand[9]));
        seriesSand.getData().add(new XYChart.Data("15",histoPrixSand[10]));
        seriesSand.getData().add(new XYChart.Data("16:30",histoPrixSand[11]));
        seriesSand.getData().add(new XYChart.Data("18",histoPrixSand[12]));
        seriesSand.getData().add(new XYChart.Data("19:30",histoPrixSand[13]));
        seriesSand.getData().add(new XYChart.Data("21",histoPrixSand[14]));
        seriesSand.getData().add(new XYChart.Data("22:30",histoPrixSand[15]));
        seriesSand.getData().add(new XYChart.Data("24",histoPrixSand[16]));
		seriesSand.getData().add(new XYChart.Data("25:30",histoPrixSand[17]));
        seriesSand.getData().add(new XYChart.Data("27",histoPrixSand[18]));
        seriesSand.getData().add(new XYChart.Data("28:30",histoPrixSand[19]));
		
        seriesSalad.getData().clear();
		
		seriesSalad.getData().add(new XYChart.Data("0",histoPrixSalad[0]));
		seriesSalad.getData().add(new XYChart.Data("1:30",histoPrixSalad[1]));
        seriesSalad.getData().add(new XYChart.Data("3",histoPrixSalad[2]));
        seriesSalad.getData().add(new XYChart.Data("4:30",histoPrixSalad[3]));
        seriesSalad.getData().add(new XYChart.Data("6",histoPrixSalad[4]));
        seriesSalad.getData().add(new XYChart.Data("7:30",histoPrixSalad[5]));
        seriesSalad.getData().add(new XYChart.Data("9",histoPrixSalad[6]));
        seriesSalad.getData().add(new XYChart.Data("10:30",histoPrixSalad[7]));
        seriesSalad.getData().add(new XYChart.Data("12",histoPrixSalad[8]));
		seriesSalad.getData().add(new XYChart.Data("13:30",histoPrixSalad[9]));
        seriesSalad.getData().add(new XYChart.Data("15",histoPrixSalad[10]));
        seriesSalad.getData().add(new XYChart.Data("16:30",histoPrixSalad[11]));
        seriesSalad.getData().add(new XYChart.Data("18",histoPrixSalad[12]));
        seriesSalad.getData().add(new XYChart.Data("19:30",histoPrixSalad[13]));
        seriesSalad.getData().add(new XYChart.Data("21",histoPrixSalad[14]));
        seriesSalad.getData().add(new XYChart.Data("22:30",histoPrixSalad[15]));
        seriesSalad.getData().add(new XYChart.Data("24",histoPrixSalad[16]));
		seriesSalad.getData().add(new XYChart.Data("25:30",histoPrixSalad[17]));
        seriesSalad.getData().add(new XYChart.Data("27",histoPrixSalad[18]));
        seriesSalad.getData().add(new XYChart.Data("28:30",histoPrixSalad[19]));   
		
		seriesCookie.getData().clear();
		
		seriesCookie.getData().add(new XYChart.Data("0",histoPrixCookie[0]));
		seriesCookie.getData().add(new XYChart.Data("1:30",histoPrixCookie[1]));
        seriesCookie.getData().add(new XYChart.Data("3",histoPrixCookie[2]));
        seriesCookie.getData().add(new XYChart.Data("4:30",histoPrixCookie[3]));
        seriesCookie.getData().add(new XYChart.Data("6",histoPrixCookie[4]));
        seriesCookie.getData().add(new XYChart.Data("7:30",histoPrixCookie[5]));
        seriesCookie.getData().add(new XYChart.Data("9",histoPrixCookie[6]));
        seriesCookie.getData().add(new XYChart.Data("10:30",histoPrixCookie[7]));
        seriesCookie.getData().add(new XYChart.Data("12",histoPrixCookie[8]));
		seriesCookie.getData().add(new XYChart.Data("13:30",histoPrixCookie[9]));
        seriesCookie.getData().add(new XYChart.Data("15",histoPrixCookie[10]));
        seriesCookie.getData().add(new XYChart.Data("16:30",histoPrixCookie[11]));
        seriesCookie.getData().add(new XYChart.Data("18",histoPrixCookie[12]));
        seriesCookie.getData().add(new XYChart.Data("19:30",histoPrixCookie[13]));
        seriesCookie.getData().add(new XYChart.Data("21",histoPrixCookie[14]));
        seriesCookie.getData().add(new XYChart.Data("22:30",histoPrixCookie[15]));
        seriesCookie.getData().add(new XYChart.Data("24",histoPrixCookie[16]));
		seriesCookie.getData().add(new XYChart.Data("25:30",histoPrixCookie[17]));
        seriesCookie.getData().add(new XYChart.Data("27",histoPrixCookie[18]));
        seriesCookie.getData().add(new XYChart.Data("28:30",histoPrixCookie[19]));  
		
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
           	
		
		
		seriesJus.getData().clear();
	
		seriesJus.getData().add(new XYChart.Data("0",histoPrixJus[0]));
		seriesJus.getData().add(new XYChart.Data("1:30",histoPrixJus[1]));
        seriesJus.getData().add(new XYChart.Data("3",histoPrixJus[2]));
        seriesJus.getData().add(new XYChart.Data("4:30",histoPrixJus[3]));
        seriesJus.getData().add(new XYChart.Data("6",histoPrixJus[4]));
        seriesJus.getData().add(new XYChart.Data("7:30",histoPrixJus[5]));
        seriesJus.getData().add(new XYChart.Data("9",histoPrixJus[6]));
        seriesJus.getData().add(new XYChart.Data("10:30",histoPrixJus[7]));
        seriesJus.getData().add(new XYChart.Data("12",histoPrixJus[8]));
		seriesJus.getData().add(new XYChart.Data("13:30",histoPrixJus[9]));
        seriesJus.getData().add(new XYChart.Data("15",histoPrixJus[10]));
        seriesJus.getData().add(new XYChart.Data("16:30",histoPrixJus[11]));
        seriesJus.getData().add(new XYChart.Data("18",histoPrixJus[12]));
        seriesJus.getData().add(new XYChart.Data("19:30",histoPrixJus[13]));
        seriesJus.getData().add(new XYChart.Data("21",histoPrixJus[14]));
        seriesJus.getData().add(new XYChart.Data("22:30",histoPrixJus[15]));
        seriesJus.getData().add(new XYChart.Data("24",histoPrixJus[16]));
		seriesJus.getData().add(new XYChart.Data("25:30",histoPrixJus[17]));
        seriesJus.getData().add(new XYChart.Data("27",histoPrixJus[18]));
        seriesJus.getData().add(new XYChart.Data("28:30",histoPrixJus[19]));
		
		seriesCafe.getData().clear();
		
		seriesCafe.getData().add(new XYChart.Data("0",histoPrixCafe[0]));
		seriesCafe.getData().add(new XYChart.Data("1:30",histoPrixCafe[1]));
        seriesCafe.getData().add(new XYChart.Data("3",histoPrixCafe[2]));
        seriesCafe.getData().add(new XYChart.Data("4:30",histoPrixCafe[3]));
        seriesCafe.getData().add(new XYChart.Data("6",histoPrixCafe[4]));
        seriesCafe.getData().add(new XYChart.Data("7:30",histoPrixCafe[5]));
        seriesCafe.getData().add(new XYChart.Data("9",histoPrixCafe[6]));
        seriesCafe.getData().add(new XYChart.Data("10:30",histoPrixCafe[7]));
        seriesCafe.getData().add(new XYChart.Data("12",histoPrixCafe[8]));
		seriesCafe.getData().add(new XYChart.Data("13:30",histoPrixCafe[9]));
        seriesCafe.getData().add(new XYChart.Data("15",histoPrixCafe[10]));
        seriesCafe.getData().add(new XYChart.Data("16:30",histoPrixCafe[11]));
        seriesCafe.getData().add(new XYChart.Data("18",histoPrixCafe[12]));
        seriesCafe.getData().add(new XYChart.Data("19:30",histoPrixCafe[13]));
        seriesCafe.getData().add(new XYChart.Data("21",histoPrixCafe[14]));
        seriesCafe.getData().add(new XYChart.Data("22:30",histoPrixCafe[15]));
        seriesCafe.getData().add(new XYChart.Data("24",histoPrixCafe[16]));
		seriesCafe.getData().add(new XYChart.Data("25:30",histoPrixCafe[17]));
        seriesCafe.getData().add(new XYChart.Data("27",histoPrixCafe[18]));
        seriesCafe.getData().add(new XYChart.Data("28:30",histoPrixCafe[19]));
		
		seriesChoco.getData().clear();
		
		seriesChoco.getData().add(new XYChart.Data("0",histoPrixChoco[0]));
		seriesChoco.getData().add(new XYChart.Data("1:30",histoPrixChoco[1]));
        seriesChoco.getData().add(new XYChart.Data("3",histoPrixChoco[2]));
        seriesChoco.getData().add(new XYChart.Data("4:30",histoPrixChoco[3]));
        seriesChoco.getData().add(new XYChart.Data("6",histoPrixChoco[4]));
        seriesChoco.getData().add(new XYChart.Data("7:30",histoPrixChoco[5]));
        seriesChoco.getData().add(new XYChart.Data("9",histoPrixChoco[6]));
        seriesChoco.getData().add(new XYChart.Data("10:30",histoPrixChoco[7]));
        seriesChoco.getData().add(new XYChart.Data("12",histoPrixChoco[8]));
		seriesChoco.getData().add(new XYChart.Data("13:30",histoPrixChoco[9]));
        seriesChoco.getData().add(new XYChart.Data("15",histoPrixChoco[10]));
        seriesChoco.getData().add(new XYChart.Data("16:30",histoPrixChoco[11]));
        seriesChoco.getData().add(new XYChart.Data("18",histoPrixChoco[12]));
        seriesChoco.getData().add(new XYChart.Data("19:30",histoPrixChoco[13]));
        seriesChoco.getData().add(new XYChart.Data("21",histoPrixChoco[14]));
        seriesChoco.getData().add(new XYChart.Data("22:30",histoPrixChoco[15]));
        seriesChoco.getData().add(new XYChart.Data("24",histoPrixChoco[16]));
		seriesChoco.getData().add(new XYChart.Data("25:30",histoPrixChoco[17]));
        seriesChoco.getData().add(new XYChart.Data("27",histoPrixChoco[18]));
        seriesChoco.getData().add(new XYChart.Data("28:30",histoPrixChoco[19]));
		
		
		seriesSand.getData().clear();
		
		seriesSand.getData().add(new XYChart.Data("0",histoPrixSand[0]));
		seriesSand.getData().add(new XYChart.Data("1:30",histoPrixSand[1]));
        seriesSand.getData().add(new XYChart.Data("3",histoPrixSand[2]));
        seriesSand.getData().add(new XYChart.Data("4:30",histoPrixSand[3]));
        seriesSand.getData().add(new XYChart.Data("6",histoPrixSand[4]));
        seriesSand.getData().add(new XYChart.Data("7:30",histoPrixSand[5]));
        seriesSand.getData().add(new XYChart.Data("9",histoPrixSand[6]));
        seriesSand.getData().add(new XYChart.Data("10:30",histoPrixSand[7]));
        seriesSand.getData().add(new XYChart.Data("12",histoPrixSand[8]));
		seriesSand.getData().add(new XYChart.Data("13:30",histoPrixSand[9]));
        seriesSand.getData().add(new XYChart.Data("15",histoPrixSand[10]));
        seriesSand.getData().add(new XYChart.Data("16:30",histoPrixSand[11]));
        seriesSand.getData().add(new XYChart.Data("18",histoPrixSand[12]));
        seriesSand.getData().add(new XYChart.Data("19:30",histoPrixSand[13]));
        seriesSand.getData().add(new XYChart.Data("21",histoPrixSand[14]));
        seriesSand.getData().add(new XYChart.Data("22:30",histoPrixSand[15]));
        seriesSand.getData().add(new XYChart.Data("24",histoPrixSand[16]));
		seriesSand.getData().add(new XYChart.Data("25:30",histoPrixSand[17]));
        seriesSand.getData().add(new XYChart.Data("27",histoPrixSand[18]));
        seriesSand.getData().add(new XYChart.Data("28:30",histoPrixSand[19]));
		
		seriesSalad.getData().clear();
		
		seriesSalad.getData().add(new XYChart.Data("0",histoPrixSalad[0]));
		seriesSalad.getData().add(new XYChart.Data("1:30",histoPrixSalad[1]));
        seriesSalad.getData().add(new XYChart.Data("3",histoPrixSalad[2]));
        seriesSalad.getData().add(new XYChart.Data("4:30",histoPrixSalad[3]));
        seriesSalad.getData().add(new XYChart.Data("6",histoPrixSalad[4]));
        seriesSalad.getData().add(new XYChart.Data("7:30",histoPrixSalad[5]));
        seriesSalad.getData().add(new XYChart.Data("9",histoPrixSalad[6]));
        seriesSalad.getData().add(new XYChart.Data("10:30",histoPrixSalad[7]));
        seriesSalad.getData().add(new XYChart.Data("12",histoPrixSalad[8]));
		seriesSalad.getData().add(new XYChart.Data("13:30",histoPrixSalad[9]));
        seriesSalad.getData().add(new XYChart.Data("15",histoPrixSalad[10]));
        seriesSalad.getData().add(new XYChart.Data("16:30",histoPrixSalad[11]));
        seriesSalad.getData().add(new XYChart.Data("18",histoPrixSalad[12]));
        seriesSalad.getData().add(new XYChart.Data("19:30",histoPrixSalad[13]));
        seriesSalad.getData().add(new XYChart.Data("21",histoPrixSalad[14]));
        seriesSalad.getData().add(new XYChart.Data("22:30",histoPrixSalad[15]));
        seriesSalad.getData().add(new XYChart.Data("24",histoPrixSalad[16]));
		seriesSalad.getData().add(new XYChart.Data("25:30",histoPrixSalad[17]));
        seriesSalad.getData().add(new XYChart.Data("27",histoPrixSalad[18]));
        seriesSalad.getData().add(new XYChart.Data("28:30",histoPrixSalad[19]));		

	seriesCookie.getData().clear();
		
		seriesCookie.getData().add(new XYChart.Data("0",histoPrixCookie[0]));
		seriesCookie.getData().add(new XYChart.Data("1:30",histoPrixCookie[1]));
        seriesCookie.getData().add(new XYChart.Data("3",histoPrixCookie[2]));
        seriesCookie.getData().add(new XYChart.Data("4:30",histoPrixCookie[3]));
        seriesCookie.getData().add(new XYChart.Data("6",histoPrixCookie[4]));
        seriesCookie.getData().add(new XYChart.Data("7:30",histoPrixCookie[5]));
        seriesCookie.getData().add(new XYChart.Data("9",histoPrixCookie[6]));
        seriesCookie.getData().add(new XYChart.Data("10:30",histoPrixCookie[7]));
        seriesCookie.getData().add(new XYChart.Data("12",histoPrixCookie[8]));
		seriesCookie.getData().add(new XYChart.Data("13:30",histoPrixCookie[9]));
        seriesCookie.getData().add(new XYChart.Data("15",histoPrixCookie[10]));
        seriesCookie.getData().add(new XYChart.Data("16:30",histoPrixCookie[11]));
        seriesCookie.getData().add(new XYChart.Data("18",histoPrixCookie[12]));
        seriesCookie.getData().add(new XYChart.Data("19:30",histoPrixCookie[13]));
        seriesCookie.getData().add(new XYChart.Data("21",histoPrixCookie[14]));
        seriesCookie.getData().add(new XYChart.Data("22:30",histoPrixCookie[15]));
        seriesCookie.getData().add(new XYChart.Data("24",histoPrixCookie[16]));
		seriesCookie.getData().add(new XYChart.Data("25:30",histoPrixCookie[17]));
        seriesCookie.getData().add(new XYChart.Data("27",histoPrixCookie[18]));
        seriesCookie.getData().add(new XYChart.Data("28:30",histoPrixCookie[19]));  
		
		
		   }
	
        }
		
		

        if(currentSecondSinceUpdate == 75)
            minuter.setStyle("-fx-text-fill: red;");

        minuter.setText(((90-currentSecondSinceUpdate > 59) ? " 1 : " : " 0 : ") + ((((90-currentSecondSinceUpdate) % 60) > 9) ? "" : "0") + String.valueOf((90-currentSecondSinceUpdate) % 60));
   
		LineChartSale.getData().addAll(seriesSand);
		LineChartSale.getData().addAll(seriesSalad);
		LineChartChoco.getData().addAll(seriesChoco);
        LineChartCafe.getData().addAll(seriesCafe);
		LineChartJus.getData().addAll(seriesJus);
		LineChartCookie.getData().addAll(seriesCookie);
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
	
		private void addPriceHisto()
	{
						
		for(int j=1;j<20;j++)
		{
			histoPrixJus[j-1]=histoPrixJus[j];
			histoPrixCafe[j-1]=histoPrixCafe[j];
			histoPrixChoco[j-1]=histoPrixChoco[j];
			histoPrixSand[j-1]=histoPrixSand[j];
			histoPrixSalad[j-1]=histoPrixSalad[j];
			histoPrixCookie[j-1]=histoPrixCookie[j];
		} 
	
			histoPrixJus[19]=productList.get(7).priceProperty().getValue();   
			histoPrixCafe[19]=productList.get(8).priceProperty().getValue();
			histoPrixChoco[19]=productList.get(9).priceProperty().getValue();
			histoPrixSand[19]=productList.get(0).priceProperty().getValue();
			histoPrixSalad[19]=productList.get(1).priceProperty().getValue();
			histoPrixCookie[19]=productList.get(4).priceProperty().getValue();
			
		
	}
	
	

}
