package enrichers;

import booking.administration.model.ClientProfile;
import booking.administration.model.ClientType;
import org.glassfish.jersey.client.ClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class AdministrationGateway {
    ClientConfig config;
    Client client;
    URI baseURI;
    WebTarget serviceTarget;
    final Logger logger = LoggerFactory.getLogger(getClass());
    public AdministrationGateway() {
        // code to access webservice
        config = new ClientConfig();
        client = ClientBuilder.newClient(config);
        baseURI = UriBuilder.fromUri("http://localhost:8080/administration/rest/client").build();
        serviceTarget = client.target(baseURI);
    }
    public ClientProfile getClientProfile(int clientID){
        ClientProfile accountType = null;
        Builder builder = serviceTarget.path(String.valueOf(clientID)).request().accept(MediaType.APPLICATION_JSON);
        Response response = builder.get();
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            accountType = response.readEntity(ClientProfile.class);
        }
        return accountType;
    }
}
