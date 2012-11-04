
import models.Users;
import org.junit.Test;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author CAGOJOV
 */
public class SimpleTest {
    @Test
    public void pwsHashTest()
    {
        Users users = new Users();
        
        System.out.println(" pwd, salt 1: " + Users.makePwHash("pwd", "salt"));
        System.out.println(" pwd, salt 1: " + Users.makePwHash("pwd", "salt"));
        System.out.println(" pwd, salt 1: " + Users.makePwHash("pwd", "salt"));
        System.out.println(" pwd, salt 1: " + Users.makePwHash("pwd", "salt"));
        System.out.println(" pwd, salt 1: " + Users.makePwHash("pwd", "salt"));
        System.out.println(" pwd1,null 1: " + Users.makePwHash("pwd1", null));
        
    }
    
}
