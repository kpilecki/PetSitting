package lt.codeacademy.petsitting.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity
public class ServiceProvider extends Customer{

    @Lob
    @Size( min = 4, max = 1000 )
    private String about;

    private String headline;

    private int yearsOfExperience;

    @Lob
    @Size( min = 4, max = 1000 )
    private String skillDescription;

    @OneToOne
    private Address publicAddress;

    @ElementCollection( targetClass = PaymentMethod.class )
    @CollectionTable
    @Enumerated( EnumType.STRING )
    private List<PaymentMethod> acceptedPaymentMethods;

    @OneToMany( cascade = CascadeType.ALL )
    private List<Order> sales;

    @OneToMany( cascade = CascadeType.ALL )
    private List<Service> services;






}
