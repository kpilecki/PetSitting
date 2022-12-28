package lt.codeacademy.petsitting.pojo;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Service {

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    private Long id;

    @NotNull
    private ServiceType serviceType;

    @NotBlank
    private String description;

    @NotNull
    private float price;

    @NotEmpty
    @ElementCollection( targetClass = PetType.class )
    @CollectionTable
    @Enumerated( EnumType.STRING )
    private List<PetType> acceptedPetTypes;

    @NotEmpty
    @ElementCollection( targetClass = PetSize.class )
    @CollectionTable
    @Enumerated( EnumType.STRING )
    private List<PetSize> acceptedPetSizes;

    @NotNull
    private int minPetAge;

    @NotNull
    private int maxPetAge;


}
