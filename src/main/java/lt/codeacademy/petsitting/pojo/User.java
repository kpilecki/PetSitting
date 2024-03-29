package lt.codeacademy.petsitting.pojo;

import lombok.*;
import lombok.experimental.SuperBuilder;
import lt.codeacademy.petsitting.validators.UniqueEmail;
import lt.codeacademy.petsitting.validators.UniqueUsername;

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
@Table( name = "users" )
public class User {

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    private Long id;

    @NotBlank( message = "{validation.user.username.notBlankMessage}")
    @Size( min = 4, max = 255, message = "{validation.user.username.sizeMessage}")
    @UniqueUsername
    private String username;

    @NotBlank( message = "{validation.user.password.notBlankMessage}")
    @Size( min = 4, max = 255, message = "{validation.user.password.sizeMessage}")
    private String password;

    @NotBlank( message = "{validation.user.email.notBlankMessage}")
    @Email
    @UniqueEmail
    private String email;

    @ManyToMany( fetch = FetchType.LAZY )
    @JoinTable( name = "user_roles",
                joinColumns = @JoinColumn( name = "user_id" ),
                inverseJoinColumns = @JoinColumn( name = "role_id" ))
    private Set<Role> roles = new HashSet<>();


}
