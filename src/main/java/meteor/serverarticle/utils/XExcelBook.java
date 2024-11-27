package meteor.serverarticle.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;



public class XExcelBook {


    private static final List<String> hat = List.of("№ чертежа", "Наименование", "Описание", "Кол-во", "Формат", "Дата", "Разработал", "Примечание");
    private static final Path p = Paths.get("Nonstandard.xlsx");
    private static final String fileName = p.toString();
    private static final File file = new File(fileName);

    private static synchronized void createFile(File file) throws IOException {
        if (!Files.exists(p)) {
            Files.createFile(p);
            try (BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(file))) {
                Workbook workbook = new XSSFWorkbook();

                workbook.write(fos);
                workbook.close();
            }
        }
    }

    public static synchronized void writeToXLS(List<String> newRow, Long group) throws IOException {

        if (!Files.exists(p)) {
            createFile(file);
        }


        String sheetName = group.toString();

        Workbook workbook = getBook();
        //получаем лист
        Sheet sheet = getSheetGroup(sheetName, workbook);
        Row rowHat = sheet.createRow(0);
        for (int i = 0; i < hat.size(); i++) {
            Cell cell = rowHat.createCell(i);
            //записываем в ячейку данные
            cell.setCellValue(hat.get(i));
        }

        int rowCount = sheet.getPhysicalNumberOfRows();
        System.out.println(rowCount + "   - Index");

        //создаем строку
        Row row = sheet.createRow(rowCount);

        //записываем строку
        for (int i = 0; i < newRow.size(); i++) {
            Cell cell = row.createCell(i);
            //записываем в ячейку данные
            cell.setCellValue(newRow.get(i));
        }

        //  Sheet sheet = workbook.getSheet("350");


        closeBook(workbook);

    }

    //
    public static synchronized void deleteToXLS(Long group, String article) throws IOException {


        //имя страницы
        String sheetName = group.toString();

        if (!Files.exists(p)) {
           throw new IOException("Didn't foget  excel file");
        }

//индекс начальный

        //получаем страницу по имени

        Workbook workbook = getBook();
        Sheet sheet = getSheetGroup(sheetName, workbook);


        //ищем строку с артикулом
        int ind = getIndexRow(sheet, article);
        if (ind == -1) {
            throw new RuntimeException("Error delete article");
        }

        //удаление строки если не последняя и лежит от 0 до последней
        if (ind >= 0 && ind < sheet.getLastRowNum()) {
            // строка которую надо удалить
            Row removingRow = sheet.getRow(ind);

            //если не пустая
            if (removingRow != null) {
                //удаляем
                sheet.removeRow(removingRow);
            }

            //сдвигаем строчки  sheet.shiftRows(int startRow, int endRow, int n);
            //                startRow = В которую нам нужно вставить строку.
            //                        endRows = Всего строк
            //                        n = Сколько строк мы собираемся вставить
            sheet.shiftRows(ind + 1, sheet.getLastRowNum(), -1);
            ind--;
        }

        if (ind == sheet.getLastRowNum()) {
            Row removingRow = sheet.getRow(ind);
            if (removingRow != null) {
                sheet.removeRow(removingRow);
            }
        }

        //записываем изменения в файл и закрываем поток
        closeBook(workbook);


    }

    //редактирование файла
    public static synchronized void editeToXLS(Long group, String article, List<String> newRow) throws IOException {

        if (!Files.exists(p)) {
            throw new IOException("Didn't foget  excel file");
        }

        String sheetName = group.toString();

        //получаем книгу
        Workbook workbook = getBook();

        //получаем страницу по имени
        Sheet sheet = getSheetGroup(sheetName, workbook);

        //ищем строку с артикулом
        int ind = getIndexRow(sheet, article);
        if (ind == -1) {
            throw new RuntimeException("Error edite article");
        }

        Row row = sheet.getRow(ind);
        //записываем строку
        for (int i = 0; i < newRow.size(); i++) {
            Cell cell = row.createCell(i);
            //записываем в ячейку данные
            cell.setCellValue(newRow.get(i));
        }
        closeBook(workbook);
    }


    private static Workbook getBook() {
        try (BufferedInputStream fis = new BufferedInputStream(new FileInputStream(new File(fileName)))) {
            return new XSSFWorkbook(fis);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private static void closeBook(Workbook workbook) {
        try (BufferedOutputStream fio = new BufferedOutputStream(new FileOutputStream(fileName))) {
            workbook.write(fio);
            workbook.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Sheet getSheetGroup(String sheetName, Workbook workbook) {

        if (workbook.getSheet(sheetName) == null) {
            workbook.createSheet(sheetName);
        }
        return workbook.getSheet(sheetName);
    }

    private static int getIndexRow(Sheet sheet, String article) {
        int ind = -1;
        for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
            //перебираем строки
            Row row = sheet.getRow(i);
            // получаем первую строку в экселе
            Cell cell = row.getCell(0);
            // проверяем на совпадение
            if (cell.toString().equals(article)) {
                ind = i;
                System.out.println(ind + " index");
            }
        }

        return ind;
    }


}
