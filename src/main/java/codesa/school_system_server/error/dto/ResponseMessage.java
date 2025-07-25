package codesa.school_system_server.error.dto;

import org.springframework.http.HttpStatus;

public class ResponseMessage {
    private HttpStatus status;
    private String message;
    private Object data;
    private boolean successful;

    public ResponseMessage() {}

    public ResponseMessage(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
        this.successful = false;
        this.data = null;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }
}
