import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;

public class Send {

	public static void send(String posiljatelj, String prejemnik, String message) throws ClientProtocolException, IOException, URISyntaxException {
		URI uri = new URIBuilder("http://chitchat.andrej.com/messages")
		          .addParameter("username", posiljatelj)
		          .build();
		
		
		
		String text = "{\"global\" : false, \"recipient\": \"" + prejemnik + "\", \"text\" : \"" + message + "\"  }";

		String responseBody = Request.Post(uri)
		          .bodyString(text, ContentType.APPLICATION_JSON)
		          .execute()
		          .returnContent()
		          .asString();
		
		System.out.println(responseBody);
	}

	public static void sendAll(String posiljatelj, String message) throws URISyntaxException, ClientProtocolException, IOException {
		URI uri = new URIBuilder("http://chitchat.andrej.com/messages")
		          .addParameter("username", posiljatelj)
		          .build();
		
		String text = "{\"global\" : true, \"text\" : \"" + message + "\"  }";
		
		String responseBody = Request.Post(uri)
		          .bodyString(text, ContentType.APPLICATION_JSON)
		          .execute()
		          .returnContent()
		          .asString();
		
		System.out.println(responseBody);
	}

}
