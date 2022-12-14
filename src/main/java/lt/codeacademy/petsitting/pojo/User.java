package lt.codeacademy.petsitting.pojo;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    private Long id;

    @NotBlank
    @Size( min = 4, max = 255 )
    private String username;

    @NotBlank
    @Size( min = 4, max = 255 )
    private String password;

    @NotBlank
    @Email
    private String email;

    @ManyToMany( fetch = FetchType.LAZY )
    @JoinTable( name = "user_roles",
                joinColumns = @JoinColumn( name = "user_id" ),
                inverseJoinColumns = @JoinColumn( name = "role_id" ))
    private Set<Role> roles = new HashSet<>();


}
