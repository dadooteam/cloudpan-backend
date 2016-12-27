package im.dadoo.cloudpan.backend.container;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import im.dadoo.cloudpan.backend.constant.Constant;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.PathHandler;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.ServletContainerInitializerInfo;
import io.undertow.servlet.util.ImmediateInstanceFactory;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContainerInitializer;
import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

/**
 * @author codekitten
 * @since 1.0
 */
public final class UndertowContainer extends Container {

    private static final Logger MLOGGER = LoggerFactory.getLogger(UndertowContainer.class);

    private Undertow server;

    public UndertowContainer(ServletContainerInitializer initializer) {
        super(initializer);
    }

    public UndertowContainer(List<Class<?>> contexts) {
        super(contexts);
    }

    @Override
    public void start() throws Exception {
        //处理配置文件
        Properties prop = new Properties();
        prop.load(Files.newReader(new File("server.properties"), Charsets.UTF_8));

        String name = prop.getProperty("app.name");
        String host = prop.getProperty("service.host");
        int port = NumberUtils.toInt(prop.getProperty("service.port"));

        //创建web容器
        ServletContainerInitializerInfo scii =
                new ServletContainerInitializerInfo(ServletContainerInitializer.class,
                        new ImmediateInstanceFactory<>(this.initializer),
                        new HashSet<>());

        DeploymentInfo di = Servlets.deployment()
                .addServletContainerInitalizer(scii)
                .setClassLoader(UndertowContainer.class.getClassLoader())
                .setContextPath("/")
                .setDeploymentName(name + ".war");

        DeploymentManager dm = Servlets.defaultContainer().addDeployment(di);
        dm.deploy();

        HttpHandler handler = dm.start();
        PathHandler path = Handlers.path(Handlers.redirect("/"))
                .addPrefixPath("/", handler);

        this.server = Undertow.builder()
                .addHttpListener(port, host)
                .setHandler(path)
                .setServerOption(UndertowOptions.IDLE_TIMEOUT, Constant.IDLE_TIMEOUT)
                .setServerOption(UndertowOptions.NO_REQUEST_TIMEOUT, Constant.KEEP_ALIVE_TIMEOUT)
                .setServerOption(UndertowOptions.ENABLE_HTTP2, true)
                .build();
        this.server.start();
    }

    @Override
    public void close() throws Exception {
        this.server.stop();
    }

}

