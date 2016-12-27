package im.dadoo.cloudpan.backend.container;

import im.dadoo.cloudpan.backend.context.AbstractWebAppInitializer;
import im.dadoo.cloudpan.backend.context.BackendContext;
import org.springframework.util.CollectionUtils;

import javax.servlet.ServletContainerInitializer;
import java.util.ArrayList;
import java.util.List;

/**
 * @author codekitten
 */
public abstract class Container {

    protected ServletContainerInitializer initializer;

    public Container(final List<Class<?>> contexts) {
        this(new AbstractWebAppInitializer() {
            @Override
            protected Class<?>[] getServletConfigClasses() {
                List<Class<?>> list = new ArrayList<>();
                if (!CollectionUtils.isEmpty(contexts)) {
                    list.addAll(contexts);
                }
                return list.toArray(new Class[list.size()]);
            }
        });
    }

    public Container(ServletContainerInitializer initializer) {
        this.initializer = initializer;
        final Container that = this;
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                //程序结束时进行的操作
                try {
                    if (that != null) {
                        that.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public abstract void start() throws Exception;

    public abstract void close() throws Exception;


}

