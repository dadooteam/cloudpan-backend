package im.dadoo.cloudpan.backend.context;

import com.google.common.base.Splitter;
import com.google.gson.Gson;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.client.fluent.Executor;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

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
    public Executor httpExecutor() {
        return Executor.newInstance();
    }

    @Bean
    public Gson gson() {
        return new Gson();
    }
    /**
     * response body 转换器
     * @param converters
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new StringHttpMessageConverter());
        converters.add(new GsonHttpMessageConverter());
    }

}

