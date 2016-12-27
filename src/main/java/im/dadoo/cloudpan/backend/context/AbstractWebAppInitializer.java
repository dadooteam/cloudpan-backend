package im.dadoo.cloudpan.backend.context;

import com.google.common.base.Charsets;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.filter.HttpPutFormContentFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.Set;

/**
 * 代替web.xml
 *
 * @author shuwen
 * @since 1.0
 */
public abstract class AbstractWebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer implements ServletContainerInitializer {

    @Override
    public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
        super.onStartup(ctx);
    }

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[] {};
    }

    @Override
    protected abstract Class<?>[] getServletConfigClasses();

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    protected Filter[] getServletFilters() {
        CharacterEncodingFilter ceFilter = new CharacterEncodingFilter();
        ceFilter.setEncoding("UTF-8");
        ceFilter.setForceEncoding(true);

        HiddenHttpMethodFilter hhmFilter = new HiddenHttpMethodFilter();
        HttpPutFormContentFilter putFilter = new HttpPutFormContentFilter();
        putFilter.setCharset(Charsets.UTF_8);

        return new Filter[]{ceFilter, hhmFilter, putFilter};

    }

}

