package info.yangguo.dragon.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.session.SessionAutoConfiguration;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication(exclude = {SessionAutoConfiguration.class, DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class})
@ImportResource({"classpath:spring/*.xml"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
