package lt.codeacademy.petsitting.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import lt.codeacademy.petsitting.pojo.Address;
import lt.codeacademy.petsitting.pojo.Customer;
import lt.codeacademy.petsitting.pojo.ServiceProvider;

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
                .build();
    }
}
