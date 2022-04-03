package com.naderi.ghodrat.coffeecart;

import java.util.List;

public interface PrintService {
    void printMenu();

    double printInvoice(List<Product> products);

    void refreshScreen();

    double printInvoice(String order);
}
