package meteor.serverarticle.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ListResponse {
    private String article;
    private String title;
    private String description;
    private String paper;
    private String amount;
    private String author;
    private String note;
    private LocalDateTime date;
}
