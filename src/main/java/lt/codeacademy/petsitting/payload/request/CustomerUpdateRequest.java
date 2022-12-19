package lt.codeacademy.petsitting.payload.request;

import lombok.Getter;
import lombok.Setter;
import lt.codeacademy.petsitting.validators.UniqueEmail;
import lt.codeacademy.petsitting.validators.UniqueUsername;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Getter
@Setter
public class CustomerUpdateRequest {


    @Size( min = 4, max = 255 )
    @UniqueUsername
    private String username;

    @Email
    @UniqueEmail
    private String email;

    private String firstName;

    private String lastName;
}
