package com.gzhuoj.usr.utils;

import com.gzhuoj.usr.model.entity.UserDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Slf4j
public class ExcelWriter {

    public static byte[] writeUsersToExcel(List<UserDO> users) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Users");
        // 创建标题行
        String[] headers = {"用户名", "账号", "密码"};
        Row headerRow = sheet.createRow(0);
        // 表头
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        for (int i = 0, j = 1; i < users.size(); i++) {
            Row row = sheet.createRow(j++);
            row.createCell(0).setCellValue(users.get(i).getUserAccount());
            row.createCell(1).setCellValue(users.get(i).getUsername());
            row.createCell(2).setCellValue(users.get(i).getPassword());
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        return outputStream.toByteArray();
    }
}
