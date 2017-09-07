import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;

public class Delete {

	public static void main(String[] args) throws URISyntaxException, ClientProtocolException, IOException {
		URI uri = new URIBuilder("http://chitchat.andrej.com/users")
		          .addParameter("username", "SteblajM15")
		          .build();
		String responseBody = Request.Delete(uri)
		                               .execute()
		                               .returnContent()
		                               .asString();
		System.out.println(responseBody);
	}
}