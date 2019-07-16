/*
 * Copyright (c) 2019. tangduns945@gmail.com.
 */

import childChannelHandler.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import org.junit.Test;

/**
 * <p></p>
 *
 * @Author <a href="mailto:tangduns945@gmail.com">Taunton</a>
 * @Date Created in 2019-05-26 16:27
 */
public class ExceptionChannelHandlerTestDemo {

    @Test
    public void server(){
        final EventLoopGroup serverGroup = new NioEventLoopGroup(1);
        final EventLoopGroup childGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(serverGroup,childGroup)
                    .channel(NioServerSocketChannel.class)
                    // 对服务端Channel NioServerSocketChannel的配置，可通过多次调用设置多个
                    //.option()
                    //.option()
                    // 对服务端Channel NioServerSocketChannel的属性设置，可通过多次调用设置多个
                    //.attr()
                    //.attr()
                    //对accept到的SocketChannel的配置，每次accept到SocketChannel都会按照我们所传的配置项配置一遍
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    //对accept到的SocketChannel的属性设置，每次accept到SocketChannel都会按照我们所传的属性设置一遍
                    .childAttr(AttributeKey.newInstance("childAttr"), "childAttrValue")

                    //为新连接设置ChannelHandler，我们如果要写业务代码一般也就是写在handler里面了。
                    //请注意：这是一个比较特殊的ChannelHandler抽象实现类，因为其本身并没有任何业务处理代码，当这个ChannelHandler的handlerAdded
                    //方法因fireHandlerAdded事件触发而被执行时，在handlerAdded方法中会调用initChannel(ChannelHandlerContext ctx)方法，
                    // 而在initChanel中会调用我们实现的initChanel(T ch)
                    //方法，然后这个特殊ChannelHandler再将自己本身从pipeline中移除，不再参与到后续的pipeline逻辑链的处理中来。
                    //因此我们可以看出这个ChannelHandler纯粹就是为了扩展自定义操作而生，自己本身没有任何作用
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            //可以在这这这里加上众多的handler介入对accept到的Channel的处理
                            ch.pipeline().addLast(new InboundHandlerA());

                            ch.pipeline().addLast(new InboundHandlerThrowExceptionB());
                            ch.pipeline().addLast(new InboundHandlerB());
                            ch.pipeline().addLast(new InboundHandlerC());
                            ch.pipeline().addLast(new OutboundHandlerA());
                            ch.pipeline().addLast(new OutboundHandlerB());
                            ch.pipeline().addLast(new OutboundHandlerC());
                            //对于异常的处理，通常的做法是在最后添加一个统一的异常处理器，从而进行异常统一处理
                            ch.pipeline().addLast(new ExceptionCaughtHandler());
                        }
                    });

            //启动Netty服务端
            ChannelFuture channelFuture = b.bind(8848).sync();
            System.out.println("bind port sync over");
            //serverGroup.next().execute(() -> {
            //    System.out.println("test server eventLoop execute");
            //});
            channelFuture.channel().closeFuture().sync();
            System.out.println("channelFuture sync over");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //优雅的关闭
            serverGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
            //childGroup.shutdownGracefully(1000,3000, TimeUnit.MILLISECONDS);
        }
    }
}
