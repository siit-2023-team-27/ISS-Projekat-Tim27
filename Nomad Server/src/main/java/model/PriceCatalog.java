package model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PriceCatalog {
    private static PriceCatalog instance;

    // Map to store prices for accommodations on different dates
    private Map<Accommodation, Map<Date, Double>> prices;

    // Private constructor to prevent instantiation
    private PriceCatalog() {
        prices = new HashMap<>();
    }

    // Method to get the singleton instance
    public static synchronized PriceCatalog getInstance() {
        if (instance == null) {
            instance = new PriceCatalog();
        }
        return instance;
    }

    // Method to set the price for a specific accommodation on a given date
    public void setPrice(Accommodation accommodation, Date date, double price) {
        prices.computeIfAbsent(accommodation, k -> new HashMap<>()).put(date, price);
    }

    // Method to get the price for a specific accommodation on a given date
    public Double getPrice(Accommodation accommodation, Date date) {
        return prices.getOrDefault(accommodation, new HashMap<>()).get(date);
    }
}
