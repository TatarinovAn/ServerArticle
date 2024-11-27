package meteor.serverarticle.service;


import lombok.Data;
import lombok.RequiredArgsConstructor;
import meteor.serverarticle.dto.ArticleRequest;
import meteor.serverarticle.dto.EditArticleRequest;
import meteor.serverarticle.entity.Part;
import meteor.serverarticle.exception.ServerException;
import meteor.serverarticle.repository.ArticleRepository;

import meteor.serverarticle.utils.GetNewArticle;
import meteor.serverarticle.utils.XExcelBook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Data
@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    Logger logger = LoggerFactory.getLogger(ArticleService.class);
    //получить предыдущий артикул
    public String getOldArticle(Long grouper) {
        var res = articleRepository.findAllByGrouperOrderByIdDesc(grouper);
        if (res.isEmpty()) {
            return "";
        }
        return res.stream().findFirst().get().getArticle();
    }
// запись артикула в бд
    @Transactional
    public synchronized void uploadArticle( ArticleRequest articleRequest) {
        List<String> list = new ArrayList<>();
        String title = articleRequest.getTitle();
        String description = articleRequest.getDescription();
        String paper = articleRequest.getPaper();
        String amount = articleRequest.getAmount();
        String date = LocalDateTime.now().toString();
        String author = articleRequest.getAuthor();
        String note = articleRequest.getNote();
        Long grouper = articleRequest.getGroup();

        try{
            String oldArticle = getOldArticle(grouper);
            String newArticle = GetNewArticle.getNewArticle(oldArticle, grouper);
            articleRepository.save(new Part(newArticle, title, description, paper, amount, author, note, grouper));
            list.add(newArticle);
            list.add(title);
            list.add(description);
            list.add(paper);
            list.add(amount);
            list.add(date);
            list.add(author);
            list.add(note);
            XExcelBook.writeToXLS(list, grouper);
            logger.info(newArticle + " successfully created");
        } catch (Exception e) {
            logger.error("Error upload Article");
            throw new ServerException("Error upload Article");
        }
    }

//удаление артикула
    @Transactional
    public void deleteArticleService (String article, Long group) {
        try {

            articleRepository.deleteByArticle(article);
            XExcelBook.deleteToXLS(group, article);
            logger.info(article + " successfully deleted");
        } catch (Exception e) {
            logger.error("Error deleted Article");
            throw new ServerException("Error deleted Article");
        }
    }

    public void editArticleService(EditArticleRequest editArticleRequest) {
        List<String> list = new ArrayList<>();
        list.add(editArticleRequest.getArticle());
        list.add(editArticleRequest.getTitle());
        list.add(editArticleRequest.getDescription());
        list.add(editArticleRequest.getAmount());
        list.add(editArticleRequest.getPaper());
        list.add(LocalDateTime.now().toString());
        list.add(editArticleRequest.getAuthor());
        list.add(editArticleRequest.getNote());
        Long grouper = editArticleRequest.getGroup();

        try {
            String article = editArticleRequest.getArticle();
            var part = getPart(article);
            part.setTitle(editArticleRequest.getTitle());
            part.setDescription(editArticleRequest.getDescription());
            part.setPaper(editArticleRequest.getPaper());
            part.setAuthor(editArticleRequest.getAuthor());
            part.setAmount(editArticleRequest.getAmount());
            part.setNote(editArticleRequest.getNote());
            articleRepository.save(part);
            XExcelBook.editeToXLS(grouper, article, list);
            logger.info(article + " successfully edited");
        } catch (Exception e) {
            logger.error("Error edited Article");
            throw new ServerException("Error edited Article");
        }

    }

    private Part getPart(String article) {

        var part = articleRepository.findByArticle(article);
        if(part.isPresent()) {
            return part.get();
        } else {
            throw new RuntimeException("No article ");
        }
    }


}
