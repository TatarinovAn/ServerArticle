package meteor.serverarticle.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EditArticleRequest {
    private String article;
    private String title;
    private String description;
    private String paper;
    private String amount;
    private String author;
    private String note;
    private Long group;
}