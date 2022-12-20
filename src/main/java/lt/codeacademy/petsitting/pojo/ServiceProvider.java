package lt.codeacademy.petsitting.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.validation.constraints.Size;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity
public class ServiceProvider extends Customer{

    @Lob
    @Size( min = 4, max = 1000 )
    private String about;

    @OneToOne
    private Address publicAddress;

}
