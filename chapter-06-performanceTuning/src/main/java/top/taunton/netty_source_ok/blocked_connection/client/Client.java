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

package top.taunton.netty_source_ok.blocked_connection.client;

import top.taunton.netty_source_ok.blocked_connection.constant.Constant;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
/**
 *<p></p>
 *@author <a href="mailto:tangduns945@gmail.com">Taunton</a>
 *@version 1.0.0
 *@since 1.0.0
 *@date Created in 2020-06-11 5:57 下午
 */
public class Client {

    private static final String SERVER_HOST = "127.0.0.1";

    public static void main(String[] args) throws Exception {
        new Client().start(Constant.PORT);
    }

    public void start(int port) throws Exception {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        final Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_REUSEADDR, true);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) {
                ch.pipeline().addLast(new FixedLengthFrameDecoder(Long.BYTES));
                ch.pipeline().addLast(ClientBusinessHandler.INSTANCE);
            }
        });

        for (int i = 0; i < 1000; i++) {
            bootstrap.connect(SERVER_HOST, port).get();
        }
    }
}
