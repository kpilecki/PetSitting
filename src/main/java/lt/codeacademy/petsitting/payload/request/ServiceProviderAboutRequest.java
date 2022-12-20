package lt.codeacademy.petsitting.payload.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceProviderAboutRequest {

    @NotBlank
    @Size( min = 4, max = 1000, message = "Error: About must be between {min} and {max} characters long")
    String about;
}
