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

    @NotBlank
    private String name;

    @NotNull
    @Enumerated( EnumType.STRING )
    private PetType species;

    private String breed;

    @NotNull
    @Enumerated( EnumType.STRING )
    private PetSize size;

    @NotNull
    @Enumerated( EnumType.STRING )
    private PetGender gender;

    @NotNull
    @Min( 1900 )
    @Max( 3000 )
    private int birthYear;

    @NotNull
    private boolean neutered;

    @NotNull
    private boolean chipped;

    @NotNull
    private boolean vaccinated;

    @NotNull
    private boolean houseTrained;

    @NotNull
    private boolean friendlyWithDogs;

    @NotNull
    private boolean friendlyWithCats;

    @NotNull
    private boolean friendlyWithKids;

    @NotNull
    private boolean friendlyWithAdults;

    @NotBlank
    private String description;

    private BlobId profileImageId;

}
