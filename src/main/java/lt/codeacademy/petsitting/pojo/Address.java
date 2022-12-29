package lt.codeacademy.petsitting.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Address {
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    private Long id;

    @NotBlank( message = "{validation.address.street.notBlankMessage}" )
    @Size( min = 2, max = 255, message = "{validation.address.street.sizeMessage}" )
    private String street;

    @NotBlank( message = "{validation.address.city.notBlankMessage}")
    @Size( min = 2, max = 255, message = "{validation.address.city.sizeMessage}")
    private String city;

    @NotBlank( message = "{validation.address.municipality.notBlankMessage}")
    @Size( min = 2, max = 255, message = "{validation.address.municipality.sizeMessage}")
    private String municipality;

    @NotBlank( message = "{validation.address.country.notBlankMessage}")
    @Size( min = 2, max = 255, message = "{validation.address.country.sizeMessage}")
    private String country;

    @NotBlank( message = "{validation.address.postCode.notBlankMessage}")
    @Size( min = 2, max = 255, message = "{validation.address.postCode.sizeMessage}")
    private String postCode;

    private double longitude;

    private double latitude;

    @JsonIgnore
    @OneToOne( mappedBy = "address")
    private Customer customer;

}
