package lt.codeacademy.petsitting.payload;

import lombok.*;
import lt.codeacademy.petsitting.pojo.PaymentMethod;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServiceProviderProfileInfo {

    @NotBlank
    @Size( min = 4, max = 1000, message = "Error: About must be between {min} and {max} characters long" )
    private String about;

    @NotBlank
    @Size( min = 4, max = 50 )
    private String headline;

    @NotNull
    private int yearsOfExperience;

    @NotBlank
    @Size( min = 4, max = 1000 )
    private String skillDescription;

    @NotEmpty
    private List<PaymentMethod> acceptedPaymentMethods;

}
