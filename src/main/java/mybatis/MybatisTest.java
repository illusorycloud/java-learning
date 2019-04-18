//package mybatis;
//
//import org.apache.ibatis.io.Resources;
//import org.apache.ibatis.session.SqlSession;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.apache.ibatis.session.SqlSessionFactoryBuilder;
//import org.junit.Test;
//
//import java.io.IOException;
//import java.io.InputStream;
//
///**
// * Mybatis运行流程
// *
// * @author illusory
// * @version 1.0.0
// * @date 2019/4/16
// */
//public class MybatisTest {
//    @Test
//    public void testMybaits() throws IOException {
//        //mybatis核心配置文件
//        String resources = "mybatis-config.xml";
//        //以流的形式加载进来
//        InputStream resourceAsStream = Resources.getResourceAsStream(resources);
//        //根据配置文件创建SqlSessionFactory
//        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
//        //用SqlSessionFactory创建SqlSession
//        SqlSession sqlSession = sqlSessionFactory.openSession();
//        //SqlSession获取mapper
//        sqlSession.insert()
//        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
//        //执行CRUD操作
//        mapper.findUserByName("username");
//    }
//}
