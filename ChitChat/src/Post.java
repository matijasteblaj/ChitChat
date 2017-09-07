import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;

public class Post {

	public static void main(String[] args) throws URISyntaxException, ClientProtocolException, IOException {
		URI uri = new URIBuilder("http://chitchat.andrej.com/users")
		          .addParameter("username", "Matija")
		          .build();
		String responseBody = Request.Post(uri)
		                               .execute()
		                               .returnContent()
		                               .asString();
		System.out.println(responseBody);
	}
}