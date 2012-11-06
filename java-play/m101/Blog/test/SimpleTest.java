
import models.Users;
import org.junit.Test;

public class SimpleTest {
    @Test
    public void pwsHashTest()
    {   
        System.out.println(" pwd, salt: " + Users.makePwHash("pwd", "salt"));
        System.out.println(" pwd, salt: " + Users.makePwHash("pwd", "salt"));
        System.out.println(" pwd, salt: " + Users.makePwHash("pwd", "salt"));
        
        System.out.println(" pwd1,null: " + Users.makePwHash("pwd1", null));        
    }
}
