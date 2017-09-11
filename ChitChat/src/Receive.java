import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;

public class Receive {

	public static void main(String[] args) throws ClientProtocolException, IOException, URISyntaxException {
		URI uri = new URIBuilder("http://chitchat.andrej.com/messages")
		          .addParameter("username", "Matija2")
		          .build();

		  String responseBody = Request.Get(uri)
		          .execute()
		          .returnContent()
		          .asString();

		  System.out.println(responseBody);
	}
	
	/**
	public static String[] receive(String username) throws ClientProtocolException, IOException, URISyntaxException {
		URI uri = new URIBuilder("http://chitchat.andrej.com/messages")
		          .addParameter("username", username)
		          .build();

		  String responseBody = Request.Get(uri)
		          .execute()
		          .returnContent()
		          .asString();

		  return responseBody;
	}*/
}
