package com.naderi.ghodrat.coffeecart;

import static com.naderi.ghodrat.coffeecart.ProductType.*;

public enum Product {
    SMALL_COFFEE(1, "Small Coffee", 2.50, "CHF", BEVERAGE),
    MEDIUM_COFFEE(2, "Medium Coffee", 3.00, "CHF", BEVERAGE),
    LARGE_COFFEE(3, "Large Coffee", 3.50, "CHF", BEVERAGE),
    BACON_ROLL(4, "Bacon Roll", 4.50, "CHF", SNACK),
    ORANGE_JUICE(5, "Orange Juice", 3.95, "CHF", BEVERAGE),
    EXTRA_MILK(6, "Extra milk", 0.30, "CHF", EXTRA),
    FOAMED_MILK(7, "Foamed milk", 0.50, "CHF", EXTRA),
    SPECIAL_ROAST(8, "Special roast", 0.90, "CHF", EXTRA),
    STAMP(9, "Stamp", 0.0, "CHF", DISCOUNT_STAMP);
    private Integer id;
    private String name;
    private Double cost;
    private String currency;
    private ProductType type;

    Product(Integer id, String name, Double cost, String currency, ProductType type) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.currency = currency;
        this.type = type;
    }

    public static Product of(Integer id) {
        Product pro = resolveById(id);
        if (pro == null) {
            throw new IllegalArgumentException("No matching Product with Id[" + id + "]");
        }
        return pro;
    }

    private static Product resolveById(Integer id) {
        for (Product pro : values()) {
            if (pro.getId().equals(id)) {
                return pro;
            }
        }
        return null;
    }

    public static Product of(String name) {
        Product pro = resolveByName(name);
        if (pro == null) {
            throw new IllegalArgumentException("No matching Product for value[" + name + "]");
        }
        return pro;
    }

    private static Product resolveByName(String name) {
        for (Product pro : values()) {
            if (pro.name.equalsIgnoreCase(name)) {
                return pro;
            }
        }
        return null;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getCost() {
        return cost;
    }

    public String getCurrency() {
        return currency;
    }

    public ProductType getType() {
        return type;
    }

    @Override
    public String toString() {
        return String.format("%-2s%-25s%-5.2f  %3s", id, name, cost, currency);
    }
}
