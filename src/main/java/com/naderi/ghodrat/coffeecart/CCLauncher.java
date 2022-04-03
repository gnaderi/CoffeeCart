package com.naderi.ghodrat.coffeecart;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CCLauncher {
    private static final Logger logger = Logger.getLogger(CCLauncher.class.getName());
    private PrintService printService;

    public CCLauncher() {
        this.printService = new PrintServiceImpl();
    }

    public static void main(String[] args) {
        new CCLauncher().launch(args.length > 0 ? args[0] : null);
    }

    public void launch(String order) {
        if (order != null && order.strip().length() > 0) {
            try {
                printService.printInvoice(order);
            } catch (Exception ex) {
                System.err.println("Unable to parse the order!\nError Details:" + ex.getMessage());
                System.err.println("==============================================================");

            }
        }
        BufferedReader csl = new BufferedReader(new InputStreamReader(System.in));
        printService.printMenu();
        String input;
        try {
            while ((input = csl.readLine()) != null && (!input.strip().toLowerCase().contains("x") && !input.strip().toLowerCase().contains("q"))) {
                try {
                    if (input.toLowerCase().contains("c")) {
                        printService.refreshScreen();
                        printService.printMenu();
                        continue;
                    }
                    List<Product> products = Arrays.stream(input.split(",")).map(Integer::valueOf).map(Product::of).toList();
                    printService.printInvoice(products);
                    System.out.println("\t\t\t\t\t____\t\t\t\t\t".replace("\t", "____"));
                    System.out.println("Enter (x) or (exit) to close app.");
                    System.out.println("Enter (c) or (clear) to clear screen.\n");
                } catch (Exception ex) {
                    System.err.println("Unable to parse the order!");
                    System.err.println("Please enter the choices[1-9] in comma seperated(1,3,4,4,5,9).");
                } finally {
                    Thread.sleep(1000);
                    printService.printMenu();
                }
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Something bad happened in CoffeeCart that I  can't handle it!");
        }
    }

}
