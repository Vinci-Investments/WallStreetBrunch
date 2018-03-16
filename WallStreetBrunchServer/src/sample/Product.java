package sample;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.Random;

public class Product
{
    private static Random randomizer = new Random();
    private final static double[] ViennPriceMap = {0.40, 0.50, 1, 1.20, 1.40, 1.50, 1.80,0.40, 0.50, 1, 1.20, 1.40, 1.50, 1.80, 2, 3, 4, 5, 6};
    private final static double[] GateauPriceMap = {0.20, 0.50, 0.70, 1, 1.50, 1.80, 2, 2.20, 2.40, 2.50, 0.20, 0.50, 0.70, 1, 1.50, 1.80, 2, 2.20, 2.40, 2.50, 3.50, 4.50, 5.50};
    private final static double[] PancakePriceMap = {0.50, 0.70, 1, 1.20, 1.40, 1.50, 0.50, 0.70, 1, 1.20, 1.40, 1.50, 2, 2.50, 3, 3.50, 4};
    private final static double[] DonutPriceMap = {0.50, 0.70, 1, 1.20, 1.40, 1.50, 1.80, 2, 2.20, 2.40, 2.50, 0.50, 0.70, 1, 1.20, 1.40, 1.50, 1.80, 2, 2.20, 2.40, 2.50, 3, 3.50, 4, 4.50, 5, 5.50};
    private final static double[] GaufrePriceMap = {0.30, 0.40, 0.50, 0.70, 1, 1.20, 1.50, 1.80, 2, 2.20, 2.40, 2.50, 0.30, 0.40, 0.50, 0.70, 1, 1.20, 1.50, 1.80, 2, 2.20, 2.40, 2.50, 3, 4, 5, 6, 7, 8};
    private final static double[] MuffinPriceMap = {0.30, 0.40, 0.50, 0.70, 1, 1.20, 1.40, 1.50, 1.80, 2, 2.20, 2.40, 2.50, 0.30, 0.40, 0.50, 0.70, 1, 1.20, 1.40, 1.50, 1.80, 2, 2.20, 2.40, 2.50, 2.80, 3, 4, 5, 6, 7};
    private final static double[] CrepePriceMap = {0.50, 0.70, 1.20, 1.40, 1.50, 1.80, 0.50, 0.70, 1.20, 1.40, 1.50, 1.80, 2, 3, 4, 5, 6};
    private final static double[] BoissonPriceMap = {0.30, 0.50, 0.70, 1, 1.20, 1.50, 0.20, 0.50, 0.70, 1, 1.20, 1.50, 2, 2.50, 3};

    private SimpleDoubleProperty price;
    private SimpleStringProperty name;

    public Product(String name, double price)
    {
        this.name = new SimpleStringProperty(name);
        this.price = new SimpleDoubleProperty(price);
    }

    public SimpleDoubleProperty priceProperty() {
        return price;
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    private double pickAValue(double[] valueMap) {
        return valueMap[randomizer.nextInt(valueMap.length)];
    }

    public void randomizePrice()
    {
        switch (name.getValue())
        {
            case "Viennoiseries":
                price.set(pickAValue(ViennPriceMap));
                break;
            case "Gateaux":
                price.set(pickAValue(GateauPriceMap));
                break;
            case "Pancakes":
                price.set(pickAValue(PancakePriceMap));
                break;
            case "Donuts":
                price.set(pickAValue(DonutPriceMap));
                break;
            case "Cookies":
                price.set(pickAValue(GaufrePriceMap));
                break;
            case "Muffins":
                price.set(pickAValue(MuffinPriceMap));
                break;
            case "Crepes":
                price.set(pickAValue(CrepePriceMap));
                break;
            case "Jus de fruits":
            case "Nespresso":
            case "Chocolat chaud":
                price.set(pickAValue(BoissonPriceMap));
                break;
            default:
                System.out.println("[ERROR] Unexpected product encountered while generating. skipping...");
        }
    }
}
