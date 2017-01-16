package im.dadoo.cloudpan.backend;

import com.google.gson.Gson;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Created by codekitten on 2016/12/27.
 */
@SpringBootApplication
public class Application {

  @Bean
  public Gson gson() {
    return new Gson();
  }


  public static void main(final String[] args) throws Exception {
    SpringApplication.run(Application.class, args);
  }

}
