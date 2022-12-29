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

    @NotNull( message = "{validation.service.serviceType.notNullMessage}")
    private ServiceType serviceType;

    @NotBlank( message = "{validation.service.description.notBlankMessage}")
    private String description;

    @NotNull( message = "{validation.service.price.notNullMessage}")
    private float price;

    @NotEmpty( message = "{validation.service.acceptedPetTypes.notEmptyMessage}")
    @ElementCollection( targetClass = PetType.class )
    @CollectionTable
    @Enumerated( EnumType.STRING )
    private List<PetType> acceptedPetTypes;

    @NotEmpty( message = "{validation.service.acceptedPetSizes.notEmptyMessage}")
    @ElementCollection( targetClass = PetSize.class )
    @CollectionTable
    @Enumerated( EnumType.STRING )
    private List<PetSize> acceptedPetSizes;

    @NotNull( message = "{validation.service.minPetAge.notNullMessage}")
    private int minPetAge;

    @NotNull( message = "{validation.service.maxPetAge.notNullMessage}")
    private int maxPetAge;

}
