package lt.codeacademy.petsitting.payload.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lt.codeacademy.petsitting.pojo.Address;
import lt.codeacademy.petsitting.pojo.PaymentMethod;
import lt.codeacademy.petsitting.pojo.Service;
import lt.codeacademy.petsitting.pojo.ServiceProvider;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ServiceProviderViewResponse {

    private String firstName;
    private String lastName;
    private String about;
    private String headline;
    private int yearsOfExperience;
    private String skillDescription;
    private Address publicAddress;
    private List<PaymentMethod> acceptedPaymentMethods;
    private List<Service> services;

    public ServiceProviderViewResponse( ServiceProvider provider ){
        this.firstName = provider.getFirstName();
        this.lastName = provider.getLastName();
        this.about = provider.getAbout();
        this.headline = provider.getHeadline();
        this.yearsOfExperience = provider.getYearsOfExperience();
        this.skillDescription = provider.getSkillDescription();
        this.publicAddress = provider.getPublicAddress();
        this.acceptedPaymentMethods = provider.getAcceptedPaymentMethods();
        this.services = provider.getServices();
    }
}
