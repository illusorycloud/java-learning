package spring;

import org.springframework.stereotype.Component;

/**
 * @author illusory
 * @version 1.0.0
 * @date 2019/4/19
 */
@Component
public class LoginService {
    @AdminOnly
    public void login() {
        System.out.println("login");
    }
}
