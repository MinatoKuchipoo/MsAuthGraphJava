package graphtutorial;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

import javax.mail.Session;
import javax.mail.Store;

import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.microsoft.aad.msal4j.ClientCredentialFactory;
import com.microsoft.aad.msal4j.ClientCredentialParameters;
import com.microsoft.aad.msal4j.ConfidentialClientApplication;
import com.microsoft.aad.msal4j.IAuthenticationResult;
import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
import com.microsoft.graph.models.User;
import com.microsoft.graph.requests.GraphServiceClient;
import com.microsoft.graph.requests.UserCollectionPage;

import okhttp3.Request;

public class App4ClientCredFlowGraph {

	private static ClientSecretCredential _clientSecretCredential;
	private static GraphServiceClient<Request> _appClient;
	
	public static void main(String[] args) throws Exception{
		final Properties oAuthProperties = new Properties();
	    try {
	        oAuthProperties.load(App.class.getResourceAsStream("../neonOauth.properties"));
	    } catch (IOException e) {
	        System.out.println("Unable to read OAuth configuration. Make sure you have a properly formatted oAuth.properties file. See README for details.");
	        return;
	    }
		
	    ensureGraphForAppOnlyAuth(oAuthProperties);
	    
	    
	    final UserCollectionPage users = _appClient.users().buildRequest().select("displayName,mail,userPrincipalName").get();
	    for (User user: users.getCurrentPage()) {
            System.out.println("User: " + user.displayName);
            System.out.println("  ID: " + user.id);
            System.out.println("  Email: " + user.mail);
        }
        
	}


	private static void ensureGraphForAppOnlyAuth(Properties oAuthProperties) throws Exception {
	    // Ensure _properties isn't null
	    if (oAuthProperties == null) {
	        throw new Exception("Properties cannot be null");
	    }

	    if (_clientSecretCredential == null) {
	        final String clientId = oAuthProperties.getProperty("app.clientId");
	        final String tenantId = oAuthProperties.getProperty("app.tenantId");
	        final String clientSecret = oAuthProperties.getProperty("app.clientSecret");

	        _clientSecretCredential = new ClientSecretCredentialBuilder()
	            .clientId(clientId)
	            .tenantId(tenantId)
	            .clientSecret(clientSecret)
	            .build();
	    }
	    System.out.println("ClientCredential built. ");

	    if (_appClient == null) {
	    	final List<String> SCOPES = Arrays.asList("https://graph.microsoft.com/.default");
	        final TokenCredentialAuthProvider authProvider =
	            new TokenCredentialAuthProvider(SCOPES, _clientSecretCredential);

	        _appClient = GraphServiceClient.builder()
	            .authenticationProvider(authProvider)
	            .buildClient();
	        System.out.println("GraphServiceClient created ");
	    }
	}
	
	
}
