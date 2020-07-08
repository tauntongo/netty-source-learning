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

package top.taunton.netty_source_ok.million_connection.server;

import top.taunton.netty_source_ok.million_connection.constant.Constant;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
/**
 *<p>
 *     因为一条tcp连接是以源IP:源端口 + 目标IP:目标端口 作为唯一标识。因此我们只需要一台客户机和一台服务机就可以模拟出百万连接
 *     一台客户机大概可用的端口有六万个左右，服务机我们开一百个端口，60000 * 100 可以模拟出六百万连接
 * </p>
 *@author <a href="mailto:tangduns945@gmail.com">Taunton</a>
 *@version 1.0.0
 *@since 1.0.0
 *@date Created in 2020-06-11 5:25 下午
 */
public class Server {

    public static void main(String[] args) {
        new Server().start(Constant.BEGIN_PORT, Constant.N_PORT);
    }

    public void start(int beginPort, int nPort) {
        System.out.println("server starting....");

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.childOption(ChannelOption.SO_REUSEADDR, true);

        bootstrap.childHandler(new ConnectionCountHandler());


        for (int i = 0; i < nPort; i++) {
            int port = beginPort + i;
            bootstrap.bind(port).addListener((ChannelFutureListener) future -> {
                System.out.println("bind success in port: " + port);
            });
        }
        System.out.println("server started!");
    }

}
