import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;

public class Delete {

	public static String delete(String username) throws URISyntaxException, ClientProtocolException, IOException {
		URI uri = new URIBuilder("http://chitchat.andrej.com/users")
		          .addParameter("username", username)
		          .build();
		String responseBody = Request.Delete(uri)
		                               .execute()
		                               .returnContent()
		                               .asString();
		return responseBody;
	}
}