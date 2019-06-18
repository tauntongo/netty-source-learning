/*
 * Copyright (c) 2019. tangduns945@gmail.com.
 */

import io.netty.channel.nio.NioEventLoop;
import io.netty.util.internal.StringUtil;
import org.junit.Test;

import java.util.Locale;

/**
 * <p></p>
 *
 * @author <a href="mailto:tangduns945@gmail.com">Taunton</a>
 * @date Created in 2019-06-15 0:03
 */
public class SimpleTest {

    @Test
    public void testGetClass(){
        Class<NioEventLoop> nioEventLoopClass = NioEventLoop.class;
        String s = StringUtil.simpleClassName(nioEventLoopClass);
        System.out.println(s);
        String s1 = s.toLowerCase(Locale.US);
        System.out.println(s1);
    }

}
