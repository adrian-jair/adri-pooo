package com.edrastx.pos.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ExcelReportUtil {
    public static Path exportDailySales(LocalDate date) {
        Path folder = Paths.get("data");
        try {
            if (!Files.exists(folder)) {
                Files.createDirectories(folder);
            }
            String fileName = "ventas_" + date + ".xlsx";
            Path file = folder.resolve(fileName);

            try (Workbook wb = new XSSFWorkbook();
                 Connection cn = DBUtil.getConnection()) {
                LocalDateTime from = date.atStartOfDay();
                LocalDateTime to = date.atTime(LocalTime.MAX);

                Sheet sheet = wb.createSheet("Ventas del día");
                Row header = sheet.createRow(0);
                header.createCell(0).setCellValue("ID");
                header.createCell(1).setCellValue("Fecha");
                header.createCell(2).setCellValue("Cliente");
                header.createCell(3).setCellValue("Usuario");
                header.createCell(4).setCellValue("Total");

                String sql = "select id,date_time,customer_name,user,total from sales where date_time between ? and ? order by date_time asc";
                try (PreparedStatement ps = cn.prepareStatement(sql)) {
                    ps.setString(1, from.toString());
                    ps.setString(2, to.toString());
                    ResultSet rs = ps.executeQuery();
                    int rowIndex = 1;
                    while (rs.next()) {
                        Row row = sheet.createRow(rowIndex++);
                        row.createCell(0).setCellValue(rs.getInt("id"));
                        row.createCell(1).setCellValue(rs.getString("date_time"));
                        row.createCell(2).setCellValue(rs.getString("customer_name"));
                        row.createCell(3).setCellValue(rs.getString("user"));
                        row.createCell(4).setCellValue(rs.getDouble("total"));
                    }
                }

                try (FileOutputStream out = new FileOutputStream(file.toFile())) {
                    wb.write(out);
                }
            }
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
