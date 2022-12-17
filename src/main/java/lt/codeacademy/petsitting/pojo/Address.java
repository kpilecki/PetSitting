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
    @Size( min = 2, max = 255 )
    private String postCode;

    private double longitude;

    private double latitude;

    @JsonIgnore
    @OneToOne( mappedBy = "address")
    private Customer customer;

}
