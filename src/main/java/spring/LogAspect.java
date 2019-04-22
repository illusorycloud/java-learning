package spring;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author illusory
 * @version 1.0.0
 * @date 2019/4/18
 */
//切面
@Aspect
@Component
public class LogAspect {
    @Autowired
    private CheckService checkService;

    //切入点
    @Pointcut("@annotation(AdminOnly)")
//    @Pointcut("execution(public void spring.LoginService.login(void ))")
//    @Pointcut("within(spring.LoginService)")
    public void adminOnly() {

    }

    //通知 增强
    @Before(value = "adminOnly()")
    public void check() throws Exception {
        checkService.checkAccess();
    }
}
