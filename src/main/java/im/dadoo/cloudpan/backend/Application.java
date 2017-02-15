package im.dadoo.cloudpan.backend;

import com.google.gson.Gson;
import im.dadoo.cloudpan.backend.interceptor.AuthInteceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by codekitten on 2016/12/27.
 */
@SpringBootApplication
public class Application extends WebMvcConfigurerAdapter {

  @Autowired
  private Environment env;

  @Autowired
  private AuthInteceptor authInteceptor;

  @Bean
  public Gson gson() {
    return new Gson();
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(this.authInteceptor).addPathPatterns("/**")
        .excludePathPatterns("/hello", "/login", "/user");
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/files/**").addResourceLocations(String.format("file:%s/", this.env.getProperty("master.path")));
  }

  public static void main(final String[] args) throws Exception {
    SpringApplication.run(Application.class, args);
  }

}
