package sample;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.Random;

public class Product
{
    private static Random randomizer = new Random();
    private final static double[] ViennPriceMap = {0.40, 0.50, 0.60, 0.70, 0.80, 0.90, 1.00, 1.10, 1.20, 1.30, 1.40, 1.50};
    private final static double[] GateauPriceMap = {0.50, 0.60, 0.70, 0.80, 0.90, 1.00, 1.10, 1.20, 1.30, 1.40, 1.50};
    private final static double[] CookiePriceMap = {0.30, 0.40, 0.50, 0.60, 0.70, 0.80, 0.90, 1.00};
    private final static double[] BoissonPriceMap = {0.40, 0.50, 0.60, 0.70, 0.80, 0.90, 1.00, 1.10, 1.20, 1.30, 1.40, 1.50};
    private final static double[] CafePriceMap = {0.50, 0.60, 0.70, 0.80, 0.90, 1.00, 1.10, 1.20, 1.30, 1.40, 1.50};
    private final static double[] ClubPriceMap = {2.50, 2.60, 2.70, 2.80, 2.90, 3.00, 3.10, 3.20, 3.30, 3.40, 3.50, 3.60, 3.70, 3.80, 3.90, 4.00, 4.10, 4.20, 4.30, 4.40, 4.50};


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
            case "Cookies":
                price.set(pickAValue(CookiePriceMap));
                break;
            case "Viennoiseries":
                price.set(pickAValue(ViennPriceMap));
                break;
            case "Crepes":
                price.set(pickAValue(GateauPriceMap));
                break;
            case "Gateaux":
                price.set(pickAValue(GateauPriceMap));
                break;

            case "Jus":
            case "Choco":
                price.set(pickAValue(BoissonPriceMap));
                break;
            case "Cafe":
                price.set(pickAValue(CafePriceMap));
                break;

            case "Club":
                price.set(pickAValue(ClubPriceMap));
                break;
            case "Salad":
                price.set(pickAValue(ClubPriceMap));
                break;
            default:
                System.out.println("[ERROR] Unexpected product encountered while generating. skipping...");
        }
    }
}
