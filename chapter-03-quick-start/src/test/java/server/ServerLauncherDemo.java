/*
 *
 *  * Copyright (c) 2020 tangduns945@gmail.com.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package server;

import java.nio.charset.StandardCharsets;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.AttributeKey;
import server.serverChannelHandler.BossChannelOutboundHandler;
import server.serverChannelHandler.ChildAcceptDataNonStickyHandler;
import server.serverChannelHandler.BossChannelInboundHandler;
import server.serverChannelHandler.ChildChannelOutboundHandler;
import org.junit.Test;

/**
 * <p>netty服务端</p>
 *
 * @author <a href="mailto:tangduns945@gmail.com">Taunton</a>
 * @date Created in 2019-05-26 16:27
 */
public class ServerLauncherDemo {

    @Test
    public void server(){
        final EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        final EventLoopGroup childGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup,childGroup)
                    .channel(NioServerSocketChannel.class)
                    // 对服务端Channel NioServerSocketChannel的配置，可通过多次调用设置多个
                     //请求句柄数组积压长度
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    //.option()
                    //.option()
                    // 对服务端Channel NioServerSocketChannel的属性设置，可通过多次调用设置多个
                    //.attr()
                    //.attr()
                    //对accept到的SocketChannel的配置，主要是和底层TCP读写相关的配置，每次accept到SocketChannel都会按照我们所传的配置项配置一遍
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    //对accept到的SocketChannel的属性设置，为了可以在客户端channel绑定一些自定义的属性如秘钥、存活时间之类的，
                    // 每次accept到SocketChannel都会按照我们所传的属性设置一遍
                    .childAttr(AttributeKey.newInstance("childAttr"), "childAttrValue")

                    //为服务端设置ChannelHandler这里我们传入的是一个继承了ChannelInboundHandlerAdapter的自定义handler对象
                    //我们对于netty处理流程中接入一般都是在handler中实现，netty已经定义了基本的ChannelHandler接口、抽象类、以及众多实现类，
                    //我们既可以使用既有的ChannelHandler实现，亦可实现或继承自定义ChannelHandler。
                    //.handler(new BossChannelInboundHandler())
                    .handler(new ChannelInitializer<NioServerSocketChannel>() {
                        @Override
                        protected void initChannel(NioServerSocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new BossChannelInboundHandler());
                            ch.pipeline().addLast(new BossChannelOutboundHandler());
                        }
                    })

                    //为新连接设置ChannelHandler，我们如果要写业务代码一般也就是写在handler里面了。
                    //请注意：这是一个比较特殊的ChannelHandler抽象实现类，因为其本身并没有任何业务处理代码，当这个ChannelHandler的handlerAdded
                    //方法因fireHandlerAdded事件触发而被执行时，在handlerAdded方法中会调用initChannel(ChannelHandlerContext ctx)方法，
                    // 而在initChanel中会调用我们实现的initChanel(T ch)
                    //方法，然后这个特殊ChannelHandler再将自己本身从pipeline中移除，不再参与到后续的pipeline逻辑链的处理中来。
                    //因此我们可以看出这个ChannelHandler纯粹就是为了扩展自定义操作而生，自己本身没有任何作用
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            //可以在这这这里加上众多的handler介入对accept到的Channel的处理
                            //存在粘包问题
                            //ch.pipeline().addLast(new ServerAcceptDataHandler());
                            //解决了粘包问题
                            ch.pipeline().addLast(new LineBasedFrameDecoder(1024*1024));
                            ch.pipeline().addLast(new StringDecoder(StandardCharsets.UTF_8));
                            ch.pipeline().addLast(new ChildAcceptDataNonStickyHandler());
                            //ch.pipeline().addLast()
                            //ch.pipeline().addLast()
                            //ch.pipeline().addAfter()
                        }
                    });

            //启动Netty服务端
            //sync() 作用：阻塞主线程直到bind future被回调到 done
            ChannelFuture channelFuture = b.bind(8848).addListener(f -> System.out.println("netty服务启动成功！！！")).sync();
            //serverGroup.next().execute(() -> {
            //    System.out.println("test server eventLoop execute");
            //});
            //sync 阻塞主线程直到channel close future被回调到 done
            channelFuture.channel().closeFuture().addListener(f -> System.out.println("netty服务关闭！！！")).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //优雅的关闭
            bossGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
            //childGroup.shutdownGracefully(1000,3000, TimeUnit.MILLISECONDS);
        }
    }
}
