package lt.codeacademy.petsitting.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import lt.codeacademy.petsitting.pojo.*;

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
}
