package lt.codeacademy.petsitting.payload.response;

import com.google.cloud.storage.BlobId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lt.codeacademy.petsitting.pojo.ServiceProvider;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ServiceProviderResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private BlobId profileImageId;
    private String about;
    private String headline;
    private int yearsOfExperience;
    private String skillDescription;

    public ServiceProviderResponse( ServiceProvider provider ){
        this.id = provider.getId();
        this.firstName = provider.getFirstName();
        this.lastName = provider.getLastName();
        this.profileImageId = provider.getProfileImageId();
        this.about = provider.getAbout();
        this.headline = provider.getHeadline();
        this.yearsOfExperience = provider.getYearsOfExperience();
        this.skillDescription = provider.getSkillDescription();
    }
}
