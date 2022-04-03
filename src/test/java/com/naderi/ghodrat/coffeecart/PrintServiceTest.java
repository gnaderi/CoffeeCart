package com.naderi.ghodrat.coffeecart;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class PrintServiceTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    private PrintService printService;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
        this.printService = new PrintServiceImpl();
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    void printInvoice() {
        double totalToPay = printService.printInvoice("large coffee with extra milk, small coffee with special roast, bacon roll, orange juice");
        assertEquals(15.35, totalToPay);
        assertTrue(outContent.toString().contains("Total: 15.35 CHF"));
        assertTrue(outContent.toString().contains("*** 1 Promo on 'orders a beverage and a snack, one of the extra's is free' applied!"));
        assertTrue(outContent.toString().contains("You Saved Today: 0.30"));
    }

    @Test
    void testPrintInvoice() {
        assertEquals(11.95, printService.printInvoice(
                Stream.of(Product.BACON_ROLL, Product.LARGE_COFFEE, Product.SMALL_COFFEE,
                        Product.STAMP, Product.SPECIAL_ROAST, Product.ORANGE_JUICE).collect(Collectors.toList())));
        assertTrue(outContent.toString().contains("Total: 11.95 CHF"));
        assertTrue(outContent.toString().contains("*** 1 Stamp card(s) applied!"));
        assertTrue(outContent.toString().contains("*** 1 Promo on 'orders a beverage and a snack, one of the extra's is free' applied!"));
        assertTrue(outContent.toString().contains("You Saved Today: 3.40"));
    }
    @Test
    void testPrintInvoiceOrderFullMenu() {
        assertEquals(16.35, printService.printInvoice(Arrays.asList(Product.values())));
        assertTrue(outContent.toString().contains("Total: 16.35 CHF"));
        assertTrue(outContent.toString().contains("*** 1 Stamp card(s) applied!"));
        assertTrue(outContent.toString().contains("*** 1 Promo on 'orders a beverage and a snack, one of the extra's is free' applied!"));
        assertTrue(outContent.toString().contains("You Saved Today: 2.80"));
    }
    @Test
    void printMenu() {
        printService.printMenu();
        assertEquals("============================================\n" +
                "\t\tCoffee Cart Menu\t\t\t\t\t\t\n" +
                "--------------------------------------------\n" +
                "1 Small Coffee             2.50   CHF\n" +
                "2 Medium Coffee            3.00   CHF\n" +
                "3 Large Coffee             3.50   CHF\n" +
                "4 Bacon Roll               4.50   CHF\n" +
                "5 Orange Juice             3.95   CHF\n" +
                "6 Extra milk               0.30   CHF\n" +
                "7 Foamed milk              0.50   CHF\n" +
                "8 Special roast            0.90   CHF\n" +
                "9 Stamp                    0.00   CHF\n" +
                "Select your snack and beverage: ",outContent.toString());
    }

    @Test
    void refreshScreen() {
        printService.refreshScreen();
        assertEquals("\033[H\033[2J",outContent.toString());
    }
}