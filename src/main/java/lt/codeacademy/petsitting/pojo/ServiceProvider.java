package lt.codeacademy.petsitting.pojo;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@SuperBuilder
@NoArgsConstructor
@Entity
public class ServiceProvider extends Customer{

    @OneToOne
    private Address publicAddress;

}
