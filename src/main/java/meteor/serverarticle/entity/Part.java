package meteor.serverarticle.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

//id serial primary key not null,
//article varchar UNIQUE not null,
//title varchar not null,
//description varchar,
//paper varchar,
//amount varchar,
//author varchar,
//note varchar,
//grouper int not null,
//date timestamp not null


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "articles")
public class Part {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "article")
    private String article;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "paper")
    private String paper;

    @Column(name = "amount")
    private String amount;

    @Column(name = "author")
    private String author;

    @Column(name = "note")
    private String note;

    @Column(name = "grouper")
    private Long grouper;

    @Column(name = "date")
    private LocalDateTime date;

//    public Part(String article, String title, String description, String paper, String author, String note, Long grouper) {
//        this.article = article;
//        this.title = title;
//        this.description = description;
//        this.paper = paper;
//        this.author = author;
//        this.note = note;
//        this.grouper = grouper;
//        this.date = LocalDateTime.now();
//
//        //проверить правильность заполнения таблицы
////        String[] strArray = article.split("\\D+");
////        this.grouper = Long.parseLong(strArray[1]);
//    }

    public Part(String newArticle, String title, String description, String paper, String amount, String author, String note, Long grouper) {
        this.article = newArticle;
        this.title = title;
        this.description = description;
        this.paper = paper;
        this.amount = amount;
        this.author = author;
        this.note = note;
        this.grouper = grouper;
        this.date = LocalDateTime.now();
    }
}
