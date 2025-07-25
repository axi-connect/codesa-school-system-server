package codesa.school_system_server.error;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import codesa.school_system_server.error.dto.ResponseMessage;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {
    
    @ExceptionHandler(LocalNotFoundExceptionHandler.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)

    public ResponseEntity<ResponseMessage> handleCustomException(String messageText, HttpStatus status) {
        ResponseMessage message = new ResponseMessage();
        message.setStatus(status);
        message.setMessage(messageText);
        message.setSuccessful(false);
        message.setData(null);
        return ResponseEntity.status(status).body(message);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        ResponseMessage message = new ResponseMessage();
        message.setStatus(HttpStatus.BAD_REQUEST);
        message.setMessage("Error en los campos del formulario");
        message.setSuccessful(false);
        message.setData(errors);

        return ResponseEntity.badRequest().body(message);
    }
}
