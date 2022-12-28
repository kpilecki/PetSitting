package lt.codeacademy.petsitting.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.cloud.storage.BlobId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Entity
public class Customer extends User{

    private String firstName;
    private String lastName;
    private BlobId profileImageId;

    @JsonIgnore
    @OneToOne
    private Address address;

    @JsonIgnore
    @OneToMany( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    private List<Pet> pets;

    @OneToMany
    private List<Order> orders;

}
