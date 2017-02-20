package im.dadoo.cloudpan.backend;

import com.google.gson.Gson;
import im.dadoo.cloudpan.backend.interceptor.AuthInteceptor;
import im.dadoo.cloudpan.backend.interceptor.MdcInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by codekitten on 2016/12/27.
 */
@SpringBootApplication
public class Application extends WebMvcConfigurerAdapter {

  @Autowired
  private Environment env;

  @Autowired
  private MdcInterceptor mdcInterceptor;

  @Autowired
  private AuthInteceptor authInteceptor;

  @Bean
  public Gson gson() {
    return new Gson();
  }

  @Bean(destroyMethod = "shutdown")
  public ExecutorService executor() {
    return Executors.newCachedThreadPool();
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(this.mdcInterceptor).addPathPatterns("/**");
    registry.addInterceptor(this.authInteceptor).addPathPatterns("/**")
        .excludePathPatterns("/hello", "/login", "/user");
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/files/**").addResourceLocations(String.format("file:%s/", this.env.getProperty("master.path")));
    registry.addResourceHandler("/thumbnails/**").addResourceLocations(String.format("file:%s/", this.env.getProperty("thumbnail.path")));
  }

  public static void main(final String[] args) throws Exception {
    SpringApplication.run(Application.class, args);
  }

}
