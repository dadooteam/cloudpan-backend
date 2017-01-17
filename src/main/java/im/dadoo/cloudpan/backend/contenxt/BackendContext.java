package im.dadoo.cloudpan.backend.contenxt;

import im.dadoo.cloudpan.backend.interceptor.AuthInteceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by codekitten on 2017/1/17.
 */
@Configuration
public class BackendContext extends WebMvcConfigurerAdapter {

  @Autowired
  private AuthInteceptor authInteceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(this.authInteceptor).addPathPatterns("/**")
        .excludePathPatterns("/hello", "/login", "/user");
  }
}
