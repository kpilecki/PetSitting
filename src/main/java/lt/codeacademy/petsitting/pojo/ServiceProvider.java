package lt.codeacademy.petsitting.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity
public class ServiceProvider extends Customer{

    @OneToOne
    private Address publicAddress;

}
