import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;

import java.io.IOException;

public class Get {
    public static String get() throws ClientProtocolException, IOException{
            String text = Request.Get("http://chitchat.andrej.com/users")
                                  .execute()
                                  .returnContent().asString();
            return text;
        }
    }