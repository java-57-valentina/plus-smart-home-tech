package ru.yandex.practicum.commerce.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private String message;
    private String reason;
    private HttpStatus status;

    @JsonFormat(pattern = DATE_TIME_FORMAT)
    private LocalDateTime timestamp;
}
