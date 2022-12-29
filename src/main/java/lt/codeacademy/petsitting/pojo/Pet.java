package lt.codeacademy.petsitting.pojo;

import com.google.cloud.storage.BlobId;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Pet {

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    private Long id;

    @NotBlank( message = "{validation.pet.name.notBlankMessage}")
    private String name;

    @NotNull( message = "{validation.pet.species.notNullMessage}")
    @Enumerated( EnumType.STRING )
    private PetType species;

    private String breed;

    @NotNull( message = "{validation.pet.size.notNullMessage}")
    @Enumerated( EnumType.STRING )
    private PetSize size;

    @NotNull( message = "{validation.pet.gender.notNullMessage}")
    @Enumerated( EnumType.STRING )
    private PetGender gender;

    @NotNull( message = "{validation.pet.birthYear.notNullMessage}")
    @Min( value = 1900 , message = "{validation.pet.birthYear.minMessage}")
    @Max( value = 3000, message = "{validation.pet.birthYear.maxMessage}")
    private int birthYear;

    @NotNull( message = "{validation.pet.neutered.notNullMessage}")
    private boolean neutered;

    @NotNull( message = "{validation.pet.chipped.notNullMessage}")
    private boolean chipped;

    @NotNull( message = "{validation.pet.vaccinated.notNullMessage}")
    private boolean vaccinated;

    @NotNull( message = "{validation.pet.houseTrained.notNullMessage}")
    private boolean houseTrained;

    @NotNull( message = "{validation.pet.friendlyWithDogs.notNullMessage}")
    private boolean friendlyWithDogs;

    @NotNull( message = "{validation.pet.friendlyWithCats.notNullMessage}")
    private boolean friendlyWithCats;

    @NotNull( message = "{validation.pet.friendlyWithKids.notNullMessage}")
    private boolean friendlyWithKids;

    @NotNull( message = "{validation.pet.friendlyWithAdults.notNullMessage}")
    private boolean friendlyWithAdults;

    @NotBlank( message = "{validation.pet.description.notBlankMessage}")
    private String description;

    private BlobId profileImageId;

}
