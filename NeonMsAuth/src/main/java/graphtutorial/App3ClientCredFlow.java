package graphtutorial;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

import javax.mail.Session;
import javax.mail.Store;

import com.microsoft.aad.msal4j.ClientCredentialFactory;
import com.microsoft.aad.msal4j.ClientCredentialParameters;
import com.microsoft.aad.msal4j.ConfidentialClientApplication;
import com.microsoft.aad.msal4j.IAuthenticationResult;

public class App3ClientCredFlow {

	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		
		App3ClientCredFlow app3 = new App3ClientCredFlow();
	    Store store = null;
	    
	    String accessToken = app3.getAccessTokenByClientCredentialGrant();
	    String emailId = "Kaustav.Pakira@adventureconsultancysolutions.com";

	    try {
	        store = app3.connect(emailId, accessToken );
	    } catch (Exception ex) {
	        System.out.println("Exception in connecting to email " + ex.getMessage());
	        ex.printStackTrace();
	        
	    }

	    //write code to read email using javax.mail code

	

	}

	public String getAccessTokenByClientCredentialGrant()  {
        
	    String accessToken = null;
	    String clientId = "0506e1d5-040c-4abb-acda-089dfcd940b2";
	    String secret = "co48Q~Ba5oF6_U816Xs3E85stxYlknd2_HdFOaRt";
	    String authority = "https://login.microsoftonline.com/consumers/oauth2/v2.0/token";
	    String scope = "https://outlook.office365.com/.default";
	    System.out.println("Client ID : "+clientId);
	    System.out.println("Client Secret : "+secret);
	    System.out.println("Auth Server: "+authority);
	    System.out.println("Scope: "+scope);
	    
	    try {
	        
	    
	        ConfidentialClientApplication app = ConfidentialClientApplication.builder(
	                clientId,
	                ClientCredentialFactory.createFromSecret(secret))
	                .authority(authority)
	                .build();   
	        
	        // With client credentials flows the scope is ALWAYS of the shape "resource/.default", as the
	        // application permissions need to be set statically (in the portal), and then granted by a tenant administrator
	        ClientCredentialParameters clientCredentialParam = ClientCredentialParameters.builder(
	                Collections.singleton(scope))
	                .build();
	        
	        CompletableFuture<IAuthenticationResult> future = app.acquireToken(clientCredentialParam);
	        IAuthenticationResult result = future.get();
	        accessToken = result.accessToken();
	        
	    } catch(Exception e) {
	    	System.out.println("Exception in acquiring token: "+e.getMessage());
	        e.printStackTrace();
	    }
	    System.out.println("Access Token : "+accessToken);
	    return accessToken;
	}

	//This method connects to store using the access token
	public Store connect(String userEmailId, String oauth2AccessToken) throws Exception {

	    String host = "outlook.office365.com";
	    String port = "993";
	    Store store = null;
	    
	    
	    String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
	    Properties props= new Properties();

	    props.put("mail.imaps.ssl.enable", "true");
	    props.put("mail.imaps.sasl.enable", "true");
	    props.put("mail.imaps.port", port);

	    props.put("mail.imaps.auth.mechanisms", "XOAUTH2");
	    props.put("mail.imaps.sasl.mechanisms", "XOAUTH2");
	    
	    props.put("mail.imaps.auth.login.disable", "true");
	    props.put("mail.imaps.auth.plain.disable", "true");

	    props.setProperty("mail.imaps.socketFactory.class", SSL_FACTORY);
	    props.setProperty("mail.imaps.socketFactory.fallback", "false");
	    props.setProperty("mail.imaps.socketFactory.port", port);
	    props.setProperty("mail.imaps.starttls.enable", "true");

	    props.put("mail.debug", "true");
	    props.put("mail.debug.auth", "true");

	    Session session = Session.getInstance(props);
	    session.setDebug(true);
	    
	    store = session.getStore("imaps");
	    
	    System.out.println("OAUTH2 IMAP trying to connect with system properties to Host:" + host + ", Port: "+ port
	            + ", userEmailId: " + userEmailId+ ", AccessToken: " + oauth2AccessToken);
	    try {
	    
	        store.connect(host, userEmailId, oauth2AccessToken);
	        System.out.println("IMAP connected with system properties to Host:" + host + ", Port: "+ port
	            + ", userEmailId: " + userEmailId+ ", AccessToken: " + oauth2AccessToken);
	        if(store.isConnected()){
	        	System.out.println("Connection Established using imap protocol successfully !");      
	        }
	    } catch (Exception e) {
	    	System.out.println("Store.Connect failed with the errror: "+e.getMessage());
	        StringWriter sw = new StringWriter();
	        e.printStackTrace(new PrintWriter(sw));
	        String exceptionAsString = sw.toString();
	        System.out.println(exceptionAsString);
	        
	    }

	    return store;

	}

	//the main method which calls the above 2 methods.
//	public void getEmailContents() throws Exception {
//	    Store store = null;
//	    
//	    String accessToken = getAccessTokenByClientCredentialGrant();
//	    String emailId = "<email which needs to be read>";
//
//	    try {
//	        store = connect(emailId, accessToken );
//	    } catch (Exception ex) {
//	        log.error("Exception in connecting to email " + ex.getMessage());
//	        ex.printStackTrace();
//	        
//	    }
//
//	    //write code to read email using javax.mail code
//
//	}
	
	
}
