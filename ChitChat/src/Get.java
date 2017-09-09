import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;

import java.io.IOException;

/**
 * Hello ChitChat!
 */
/**public class Get {
    public static void main(String[] args) throws ClientProtocolException, IOException{
            String text = Request.Get("http://chitchat.andrej.com/users")
                                  .execute()
                                  .returnContent().asString();
            System.out.println(text);
        }
    }*/

public class Get {
    public static String get() throws ClientProtocolException, IOException{
            String text = Request.Get("http://chitchat.andrej.com/users")
                                  .execute()
                                  .returnContent().asString();
            return(text);
        }
    }