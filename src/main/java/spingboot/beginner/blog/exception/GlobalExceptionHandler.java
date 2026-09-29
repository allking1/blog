package spingboot.beginner.blog.exception; // Chỉnh lại theo package của bạn

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;


/*
Trong các dự án lớn, việc xử lí các ngoại lệ cần có hệ thống, không phải lúc nào cũng xử lý ngay tại chỗ xảy ra ngoại lệ
Đây là cách xử lý các ngoại lệ tập trung cho toàn bộ hệ thống.
*/
@RestControllerAdvice // Áp dụng cho toàn bộ các RestController trong dự án
public class GlobalExceptionHandler {

    // 1. Đánh chặn lỗi do Tầng Service ném ra (Nghiệp vụ sai)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage()); // Lấy câu thông báo lỗi ở Service nhét vào JSON

        // Trả về mã 400 Bad Request
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // 2. Đánh chặn lỗi do Controller quét DTO (Lỗi thiếu Title, Content... do @Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        // Quét tất cả các trường bị lỗi và nhét vào Map
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        // Trả về mã 400 Bad Request
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}