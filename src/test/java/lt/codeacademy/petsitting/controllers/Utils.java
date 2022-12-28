package lt.codeacademy.petsitting.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import lt.codeacademy.petsitting.pojo.*;

import java.util.List;

public class Utils {

    public static String serializeObjectToJSON( Object object ) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure( SerializationFeature.WRAP_ROOT_VALUE, false );
        ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();

        return objectWriter.writeValueAsString( object );
    }

    public static Address getValidAddress(){
        return Address
                .builder()
                .street( "Arsenalo g. 5")
                .municipality( "Vilnius" )
                .city( "Vilnius" )
                .country( "LT" )
                .postCode( "01143" )
                .build();
    }

    public static Address getInValidAddress(){
        return Address
                .builder()
                .street( "")
                .municipality( "" )
                .city( "" )
                .country( "" )
                .postCode( "" )
                .build();
    }

    public static String getValidCustomerAsJson() throws JsonProcessingException {
        return serializeObjectToJSON( getValidCustomer() );
    }

    public static Customer getValidCustomer(){
        return Customer
                .builder()
                .username( "username" )
                .password( "P4ssword" )
                .firstName( "firstName" )
                .lastName( "lastName")
                .email( "test@email.com" )
                .build();
    }

    public static ServiceProvider getValidServiceProvider(){
        return ServiceProvider
                .builder()
                .username( "username" )
                .password( "P4ssword" )
                .firstName( "firstName" )
                .lastName( "lastName")
                .email( "test@email.com" )
                .about( "About Service Provider")
                .yearsOfExperience( 10 )
                .headline( "Headline" )
                .acceptedPaymentMethods(List.of( PaymentMethod.PAYPAL, PaymentMethod.CASH ))
                .skillDescription( "Skill description" )
                .build();
    }

    public static Pet getValidPet(){
        return Pet.builder()
                .name( "Cooper" )
                .species( PetType.DOG )
                .breed( "German Shepherd" )
                .size( PetSize.LARGE )
                .gender( PetGender.MALE )
                .birthYear( 2019 )
                .neutered( true )
                .chipped( true )
                .vaccinated( true )
                .houseTrained( false )
                .friendlyWithDogs( true )
                .friendlyWithCats( false )
                .friendlyWithKids( true )
                .friendlyWithAdults( true )
                .description( "Lovely dog named Cooper" )
                .build();
    }

    public static Pet getInValidPet(){
        return Pet.builder()
                .name( "" )
                .species( null )
                .breed( "" )
                .size( null )
                .gender( null )
                .birthYear( 0 )
                .description( null )
                .build();
    }

    public static String getValidPetAsJson() throws JsonProcessingException {
        return serializeObjectToJSON( getValidPet() );
    }

    public static String getInValidPetAsJson() throws JsonProcessingException {
        return serializeObjectToJSON( getInValidPet() );
    }

    public static Service getValidService(){
        return Service.builder()
                .serviceType( ServiceType.BOARDING )
                .acceptedPetSizes( List.of( PetSize.MEDIUM, PetSize.GIANT ))
                .acceptedPetTypes( List.of( PetType.REPTILE, PetType.DOG ))
                .price( 20 )
                .maxPetAge( 10 )
                .minPetAge( 1 )
                .description( "Description" )
                .build();
    }

    public static Service getInValidService(){
        return Service.builder()
                .serviceType( null )
                .acceptedPetSizes( null )
                .acceptedPetTypes( null )
                .price( 0 )
                .maxPetAge( 0 )
                .minPetAge( 0 )
                .description( null )
                .build();
    }

    public static String getValidServiceAsJson() throws JsonProcessingException {
        return serializeObjectToJSON( getValidService() );
    }

    public static String getInValidServiceAsJson() throws JsonProcessingException {
        return serializeObjectToJSON( getInValidService() );
    }
}
