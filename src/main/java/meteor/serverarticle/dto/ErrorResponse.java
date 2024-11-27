package meteor.serverarticle.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



//Data transfer object Error
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ErrorResponse {
    private Integer id;
    private String massage;
}
