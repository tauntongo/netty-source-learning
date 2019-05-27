/*
 * Copyright (c) 2019. tangduns945@gmail.com.
 */

import io.netty.channel.DefaultSelectStrategyFactory;
import io.netty.channel.SelectStrategy;
import io.netty.channel.SelectStrategyFactory;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultEventExecutor;

import java.net.Socket;
import java.nio.channels.spi.SelectorProvider;

/**
 * <p></p>
 *
 * @Author <a href="mailto:tangduns945@gmail.com">Taunton</a>
 * @Date Created in 2019-05-12 21:23
 */
public class SocketDemo {

    public static void main(String[] args) {
        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();
        DefaultEventExecutor defaultEventExecutor = new DefaultEventExecutor();
        SelectorProvider selectorProvider = SelectorProvider.provider();
        //NioEventLoop nioEventLoop = new NioEventLoop(nioEventLoopGroup,defaultEventExecutor,selectorProvider, DefaultSelectStrategyFactory.INSTANCE,);
    }
}
