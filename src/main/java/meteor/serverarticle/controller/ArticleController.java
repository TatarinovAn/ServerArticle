package meteor.serverarticle.controller;

import lombok.RequiredArgsConstructor;
import meteor.serverarticle.dto.ArticleRequest;
import meteor.serverarticle.dto.EditArticleRequest;
import meteor.serverarticle.dto.ListResponse;
import meteor.serverarticle.exception.InputDataException;
import meteor.serverarticle.exception.ServerException;
import meteor.serverarticle.repository.ArticleRepository;
import meteor.serverarticle.service.ArticleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;


import static meteor.serverarticle.utils.Const.*;

import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@EnableMethodSecurity
@RequiredArgsConstructor
@RestController
public class ArticleController {


    private final ArticleRepository articleRepository;
    private final ArticleService articleService;
    Logger logger = LoggerFactory.getLogger(ArticleService.class);

    // получение списка артикулов
    @PreAuthorize("hasAnyAuthority('ROLE_VIEW','ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/list")
    public List<ListResponse> getAllArticle(@RequestParam(NUMBER) Long number) {
        try {
            var articles = articleRepository.findAllByGrouper(number);
            return articles.stream()
                    .map(o -> new ListResponse(o.getArticle(), o.getTitle(), o.getDescription(), o.getPaper(), o.getAmount(),
                            o.getAuthor(), o.getNote(), o.getDate())).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error get list");
            throw new InputDataException("Error get list");
        }
    }

    // добавление артикула
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping("/article")
    public ResponseEntity<?> addArticle(@RequestBody ArticleRequest articleRequest) {

            articleService.uploadArticle(articleRequest);
            return ResponseEntity.ok(HttpStatus.resolve(201));

    }

    // редактирование артикула
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @PutMapping("/article")
    public ResponseEntity<?> editArticle(
            @RequestBody EditArticleRequest editArticleRequest) {

        articleService.editArticleService(editArticleRequest);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    //удаление файла
    @PreAuthorize("hasAuthority('ROLE_ADMIN')") //удаление только для администратора
    @DeleteMapping("/article")
    public ResponseEntity<?> deleteArticle(@RequestParam(ARTICLE_PARAM) String article,
                                    @RequestParam(ARTICLE_PARAM) Long group) {

        articleService.deleteArticleService(article, group);
        return ResponseEntity.ok(HttpStatus.OK);
    }

}
