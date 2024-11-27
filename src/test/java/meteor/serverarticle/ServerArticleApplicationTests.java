package meteor.serverarticle;

import meteor.serverarticle.utils.GetNewArticle;
import meteor.serverarticle.utils.XExcelBook;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class ServerArticleApplicationTests {

    @Test
    void contextLoads() {
    }


    @Test
    void getNewArticleTest() {
        String test = GetNewArticle.getNewArticle("KK25GBZ", 25L);
        Assertions.assertEquals(test, "KK25GCA");
    }

    @Test
    void getNewArticleTestNull() {
        String test = GetNewArticle.getNewArticle("", 363L);
        Assertions.assertEquals(test, "KK363P");
    }

    @Test
    void createExcelFileTest() throws IOException {
        List<String> list = new ArrayList<>();
        list.add("KK25P");
        list.add("Табличка");
        XExcelBook.writeToXLS(list, 25L);
        File file = new File("Nonstandard.xlsx");
        Assertions.assertTrue(file.exists());
    }

    @Test
    void deletedExcelFileTest() throws IOException {
        XExcelBook.deleteToXLS(25L, "KK25P");
        File file = new File("Nonstandard.xlsx");
        Assertions.assertTrue(file.exists());
    }

    @Test
    void editeExcelFileTest() throws IOException {
        List<String> list = new ArrayList<>();
        list.add("KK25P");
        list.add("Табличка после редактирования");
        XExcelBook.editeToXLS(25L, "KK25P", list);
      //  XExcelBook.deleteToXLS(25, "KK25P");
        File file = new File("Nonstandard.xlsx");
        Assertions.assertTrue(file.exists());
    }


}
