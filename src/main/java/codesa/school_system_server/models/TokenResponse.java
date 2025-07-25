package codesa.school_system_server.models;
import com.fasterxml.jackson.annotation.JsonProperty;

public record TokenResponse (
    @JsonProperty("access_token")
    String accessToken,
    @JsonProperty("token_type")
    String tokenType
){

}
