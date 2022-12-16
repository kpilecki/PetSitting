package lt.codeacademy.petsitting.pojo;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Address {
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    private Long id;

    @NotBlank
    @Size( min = 2, max = 255 )
    private String street;

    @NotBlank
    @Size( min = 2, max = 255 )
    private String city;

    @NotBlank
    @Size( min = 2, max = 255 )
    private String municipality;

    @NotBlank
    @Size( min = 2, max = 255 )
    private String country;

    @NotBlank
    private double longitude;

    @NotBlank
    private double latitude;
}
