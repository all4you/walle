import com.ngnis.walle.core.auth.TokenFactory;
import org.junit.Test;

/**
 * @author houyi
 */
public class TokenFactoryTest {

    @Test
    public void test() {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsYXN0TG9naW4iOiIyMDIwLTAzLTE5IDIzOjUwOjI3IiwidG9rZW5JZCI6IjVlNzM5NGMzNzdjODkyY2E4NDRmNmNiNCIsImFjY2Vzc0tleSI6Im03NGhzY05TWlBWV28zdEsiLCJleHAiOjE1ODQ2MzY2MjcsImdtdENyZWF0ZSI6IjIwMjAtMDMtMTIgMDg6MTY6MjMiLCJpYXQiOjE1ODQ2MzMwMjcsInVzZXJJZCI6NiwiYWNjb3VudCI6ImhvdXlpIn0.EvjQ0oN6ovKqx2isHXXZYKeMhn5BA3tS8mR5DPA8jaA";
        TokenFactory tokenFactory = new TokenFactory();
        boolean isValid = tokenFactory.validToken(token);
        String tokenId = tokenFactory.getTokenId(token);
        Long userId = tokenFactory.getUserId(token);
        System.out.println("isValid=" + isValid);
        System.out.println("tokenId=" + tokenId);
        System.out.println("userId=" + userId);
    }

}
