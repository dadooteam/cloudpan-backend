package im.dadoo.cloudpan.backend;

import im.dadoo.cloudpan.backend.container.Container;
import im.dadoo.cloudpan.backend.container.UndertowContainer;
import im.dadoo.cloudpan.backend.context.BackendContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by codekitten on 2016/12/27.
 */
public class App
{
    public static void main(final String[] args) throws Exception {
        List<Class<?>> contexts = new ArrayList<>();
        contexts.add(BackendContext.class);
        Container container = new UndertowContainer(contexts);
        container.start();
    }
}

