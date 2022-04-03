package com.naderi.ghodrat.coffeecart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public class PrintServiceImpl implements PrintService {

    @Override
    public void printMenu() {
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t".replace("\t", "===="));
        System.out.println("\t\tCoffee Cart Menu\t\t\t\t\t\t");
        System.out.println("\t\t\t\t\t----\t\t\t\t\t".replace("\t", "----"));
        Arrays.stream(Product.values()).sequential().forEach(System.out::println);
        System.out.print("Select your snack and beverage: ");
    }

    @Override
    public double printInvoice(List<Product> products) {

        Map<ProductType, List<Product>> orderByProductTypeMap = products.stream().collect(groupingBy(Product::getType, Collectors.toList()));
        List<Product> beverages = orderByProductTypeMap.getOrDefault(ProductType.BEVERAGE, new ArrayList<Product>()).stream().sorted().toList();
        List<Product> snacks = orderByProductTypeMap.getOrDefault(ProductType.SNACK, new ArrayList<Product>());
        List<Product> extras = orderByProductTypeMap.getOrDefault(ProductType.EXTRA, new ArrayList<Product>()).stream().sorted().toList();
        int discountStamp = orderByProductTypeMap.getOrDefault(ProductType.DISCOUNT_STAMP, new ArrayList<Product>()).size();

        //total bill before applying stamps and promotions
        double totalCostBeforePromo = products.stream().mapToDouble(Product::getCost).sum();

        double totalCost = totalCostBeforePromo;
        //apply promo based on Stamp
        if (beverages.size() > 0 && discountStamp > 0) {
            totalCost -= beverages.subList(0, Math.min(beverages.size(), discountStamp)).stream().mapToDouble(Product::getCost).sum();
        }
        //apply free extra promo if a beverage and a snack ordered by a customer
        if (extras.size() > 0) {
            totalCost -= extras.subList(0, Math.min(beverages.size(), snacks.size())).stream().mapToDouble(Product::getCost).sum();
        }


        System.out.println("\t\t\t\t\t----\t\t\t\t\t".replace("\t", "----"));
        System.out.println("\t\tCoffee Cart Invoice\t\t\t\t\t\t");
        System.out.println("\t\t\t\t\t----\t\t\t\t\t".replace("\t", "----"));
        int counter = 1;
        for (Product p : products) {
            System.out.printf("%-3s%-25s%-5.2f  %3s%n", counter++, p.getName(), p.getType() == ProductType.DISCOUNT_STAMP ? 0.0 : p.getCost(),
                    p.getType() == ProductType.DISCOUNT_STAMP ? "*" : p.getCurrency());
        }
        System.out.printf("Total: %-5.2f %s\n", totalCost, Product.SMALL_COFFEE.getCurrency());
        if (discountStamp > 0) {
            System.out.println("*** " + discountStamp + " Stamp card(s) applied!");
        }
        int extraPromoApplied = Math.min(Math.min(beverages.size(), snacks.size()), extras.size());
        if (extraPromoApplied > 0) {
            System.out.println("*** " + extraPromoApplied + " Promo on 'orders a beverage and a snack, one of the extra's is free' applied!");
        }
        double totalSaving = totalCostBeforePromo - totalCost;
        if (totalSaving > 0) {
            System.out.printf("You Saved Today:%5.2f\n", totalSaving);
        }
        return Math.round(totalCost*100)/100.00;
    }

    @Override
    public double printInvoice(String order) {
        if (order != null) {
            String[] selectOptions = order.strip().split(",");
            List<Product> passCmdOrderItems = new ArrayList<>();
            Arrays.stream(selectOptions).forEach(item -> {
                item = item.strip();
                if (item.contains("with")) {
                    String[] coupledItem = item.split("with");
                    List<Product> decoupledItem = Arrays.stream(coupledItem).map(p -> Product.of(p.strip())).toList();
                    passCmdOrderItems.addAll(decoupledItem);
                } else {
                    passCmdOrderItems.add(Product.of(item));
                }
            });
            return this.printInvoice(passCmdOrderItems);
        }
        return 0.0;
    }

    @Override
    public void refreshScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
