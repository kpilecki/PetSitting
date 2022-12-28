package lt.codeacademy.petsitting.payload.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lt.codeacademy.petsitting.pojo.ServiceProvider;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ServiceProviderListResponse {

    private List<ServiceProviderResponse> providers = new ArrayList<>();

    public ServiceProviderListResponse( List<ServiceProvider> providers ){
        providers.forEach( v -> this.providers.add( new ServiceProviderResponse( v ) ));
    }
}
