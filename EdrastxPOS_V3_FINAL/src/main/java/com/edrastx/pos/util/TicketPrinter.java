package com.edrastx.pos.util;

import com.edrastx.pos.model.SaleItem;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TicketPrinter {
    public static void printTicket(String saleId, String user, LocalDateTime dateTime, String customerName, List<SaleItem> items, double total) {
        Printer printer = Printer.getDefaultPrinter();
        if (printer == null) {
            return;
        }
        VBox root = new VBox();
        root.setSpacing(4);

        InputStream logoStream = TicketPrinter.class.getResourceAsStream("/images/logo.png");
        if (logoStream != null) {
            ImageView logo = new ImageView(new Image(logoStream));
            logo.setFitWidth(160);
            logo.setPreserveRatio(true);
            root.getChildren().add(logo);
        }

        StringBuilder sb = new StringBuilder();
        sb.append("     EDRASTX POS CAFÉ\n");
        sb.append("------------------------------\n");
        if (customerName != null && !customerName.isBlank()) {
            sb.append(String.format("Cliente : %s\n", customerName));
        } else {
            sb.append("Cliente : -\n");
        }
        sb.append(String.format("Venta   : #%s\n", saleId));
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        sb.append(String.format("Fecha   : %s\n", dateTime.format(fmt)));
        sb.append(String.format("Usuario : %s\n", user));
        sb.append("------------------------------\n");
        sb.append("Cant  Producto           Total\n");
        sb.append("------------------------------\n");
        for (SaleItem item : items) {
            String name = item.getProductName() != null ? item.getProductName() : "";
            if (name.length() > 14) {
                name = name.substring(0, 14);
            }
            String line = String.format("%-4d %-14s %7.2f", item.getQuantity(), name.toUpperCase(), item.getSubtotal());
            sb.append(line).append("\n");
        }
        sb.append("------------------------------\n");
        sb.append(String.format("TOTAL A PAGAR: %10.2f\n", total));
        sb.append("Gracias por su compra \u2615");

        Text text = new Text(sb.toString());
        text.setFont(Font.font("Consolas", 10));
        root.getChildren().add(text);

        PrinterJob job = PrinterJob.createPrinterJob(printer);
        if (job == null) {
            return;
        }
        PageLayout layout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.DEFAULT);
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        job.printPage(layout, root);
        job.endJob();
        stage.close();
    }
}
