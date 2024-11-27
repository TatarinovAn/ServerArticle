package meteor.serverarticle.repository;

import meteor.serverarticle.entity.Part;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Part, Long> {

    //вывести список артикулов по номеру группы на экран
    List<Part> findAllByGrouper(Long number);


//лист элементов, объединеных в группу и отсортированы по id в обратном порядке
    List<Part> findAllByGrouperOrderByIdDesc(Long number);


    //удалить элемент из бд
    void deleteByArticle(String article);

    Optional<Part> findByArticle(String article);


    //редактирования таблицы бд

    //void updateTitleAndDescriptionAndPaperAndAmountAndAuthorAndNoteByArticle(String article, String title, String description,
//                                                                            String paper, String amount, String author,
//                                                                            String note);
//    private String title;
//    private String description;
//    private String paper;
//    private String amount;
//    private String author;
//    private String note;
}
//findByArticleAndIdOrderByGrouperDesc
