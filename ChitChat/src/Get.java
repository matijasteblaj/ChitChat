import org.apache.http.client.fluent.Request;

import java.io.IOException;

/**
 * Hello ChitChat!
 */
public class Get {
    public static void main(String[] args) {
        try {
            String text = Request.Get("http://chitchat.andrej.com/users")
                                  .execute()
                                  .returnContent().asString();
            System.out.println(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}