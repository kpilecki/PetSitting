package lt.codeacademy.petsitting.bootstrap;

import lt.codeacademy.petsitting.pojo.*;
import lt.codeacademy.petsitting.services.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Profile( "default")
@Component
public class DataLoader implements CommandLineRunner {

    private final UserService userService;

    private final RoleService roleService;

    private final AddressService addressService;

    private final CustomerService customerService;

    private final ServiceProviderService serviceProviderService;

    private final PasswordEncoder encoder;

    public DataLoader(UserService userService, RoleService roleService, AddressService addressService, CustomerService customerService, ServiceProviderService serviceProviderService, PasswordEncoder encoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.addressService = addressService;
        this.customerService = customerService;
        this.serviceProviderService = serviceProviderService;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args){
       if( userService.findAll().size() == 0 ){
           loadData();
       }

    }

    private void loadData(){
        Role customerRole = roleService.save( new Role( UserRoles.ROLE_CUSTOMER ) );
        Role providerRole = roleService.save( new Role( UserRoles.ROLE_SERVICE_PROVIDER ));

        Address customerAddress = addressService.save(
                Address.builder()
                        .street( "23 Customer Street" )
                        .city( "London" )
                        .municipality( "London" )
                        .country( "UK" )
                        .postCode( "SW12 0KR" )
                        .build()
        );

        Customer customer = Customer.builder()
                .username( "customer ")
                .password( encoder.encode( "Password" ) )
                .email( "customer@email.com" )
                .firstName( "John" )
                .lastName( "Doe" )
                .roles( Set.of( customerRole ) )
                .address( customerAddress )
                .build();
        customerService.save( customer );

        Address providerPersonalAddress = addressService.save(
                Address.builder()
                        .street( "34 Provider Street" )
                        .city( "London" )
                        .municipality( "London" )
                        .country( "UK" )
                        .postCode( "SE12 9OK" )
                        .build()
        );

        Address providerPublicAddress = addressService.save(
                Address.builder()
                        .street( "43 Provider Public Street" )
                        .city( "London" )
                        .municipality( "London" )
                        .country( "UK" )
                        .postCode( "SW13 0HR" )
                        .build()
        );

        ServiceProvider provider = ServiceProvider.builder()
                .username( "provider ")
                .password( encoder.encode( "Password" ) )
                .email( "provider@email.com" )
                .firstName( "Peter" )
                .lastName( "Griffin" )
                .roles( Set.of( customerRole, providerRole ) )
                .address( providerPersonalAddress )
                .publicAddress( providerPublicAddress )
                .build();

        serviceProviderService.save( provider );
    }
}
