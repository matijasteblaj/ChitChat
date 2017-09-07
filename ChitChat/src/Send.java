import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;

public class Send {

	public static void main(String[] args) throws ClientProtocolException, IOException, URISyntaxException {
		URI uri = new URIBuilder("http://chitchat.andrej.com/messages")
		          .addParameter("username", "Matija")
		          .build();

		  String message = "{\"global\" : false,\"recipient\": \"Anja\", \"text\" : \"Test test 123\"  }";

		  String responseBody = Request.Post(uri)
		          .bodyString(message, ContentType.APPLICATION_JSON)
		          .execute()
		          .returnContent()
		          .asString();

		  System.out.println(responseBody);
	}

}
