package im.dadoo.cloudpan.backend.context;

import com.google.gson.Gson;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@EnableWebMvc
@EnableScheduling
@EnableAsync
@EnableAspectJAutoProxy
@PropertySource("file:server.properties")
@ComponentScan({"im.dadoo"})
public class BackendContext extends WebMvcConfigurerAdapter {

  @Resource
  private Environment env;

  @Bean(destroyMethod = "shutdown")
  public ExecutorService scheduler() {
    return Executors.newScheduledThreadPool(10);
  }

  @Bean(destroyMethod = "shutdown")
  public ExecutorService executor() {
    return Executors.newCachedThreadPool();
  }

  @Bean
  public Gson gson() {
    return new Gson();
  }

  /**
   * response body 转换器
   *
   * @param converters
   */
  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    converters.add(new StringHttpMessageConverter());
    converters.add(new GsonHttpMessageConverter());
  }

  @Bean
  public CommonsMultipartResolver multipartResolver() {
    CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
    multipartResolver.setMaxUploadSize(Integer.MAX_VALUE);
    return multipartResolver;
  }

//  @Bean(destroyMethod = "close")
//  public DataSource dataSource() {
//    HikariDataSource dataSource = new HikariDataSource();
//    dataSource.setJdbcUrl(this.env.getProperty("db.url"));
//    dataSource.setUsername(this.env.getProperty("db.username"));
//    dataSource.setPassword(this.env.getProperty("db.password"));
//    dataSource.setMaximumPoolSize(NumberUtils.toInt(this.env.getProperty("db.poolsize")));
//    dataSource.setDriverClassName("com.mysql.jdbc.Driver");
//
//    return dataSource;
//  }
//
//  @Bean
//  public NamedParameterJdbcTemplate jdbcTemplate() {
//    NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(this.dataSource());
//    return jdbcTemplate;
//  }

}

