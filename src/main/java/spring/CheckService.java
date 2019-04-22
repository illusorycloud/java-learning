package spring;

import org.springframework.stereotype.Component;

/**
 * @author Administrator
 * @version 1.0.0
 * @date 2019/4/19 0019
 */
@Component
public class CheckService {
    public void checkAccess() throws Exception {
        System.out.println("进行权限校验~");
    }
}
