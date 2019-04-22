package spring;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Administrator
 * @version 1.0.0
 * @date 2019/4/19 0019
 */
public class AspectTest {
    @Autowired
    private LoginService loginService;

    @Test
    public void testLogin() {
        loginService.login();
    }


}
