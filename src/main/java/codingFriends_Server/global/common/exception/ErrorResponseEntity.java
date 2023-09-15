package codingFriends_Server.global.common.exception;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseEntity;

@Data
@Builder
public class ErrorResponseEntity {
    private int status;
    private String message;

    public static ResponseEntity<ErrorResponseEntity> toResponseEntity(CustomException e) {
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(ErrorResponseEntity.builder()
                        .status(e.getHttpStatus().value())
                        .message(e.getInfo())
                        .build());
    }
}
