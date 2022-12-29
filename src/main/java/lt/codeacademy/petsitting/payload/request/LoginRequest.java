package lt.codeacademy.petsitting.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class LoginRequest {

    @NotBlank( message = "{validation.loginRequest.username.notBlankMessage}")
    private String username;

    @NotBlank( message = "{validation.loginRequest.password.notBlankMessage}")
    private String password;
}
