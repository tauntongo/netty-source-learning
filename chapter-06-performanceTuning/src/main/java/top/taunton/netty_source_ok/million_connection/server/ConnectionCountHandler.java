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

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
/**
 *<p>服务器端统计客户端连接数</p>
 *@author <a href="mailto:tangduns945@gmail.com">Taunton</a>
 *@version 1.0.0
 *@since 1.0.0
 *@date Created in 2020-06-11 5:35 下午
 */
@Sharable
public class ConnectionCountHandler extends ChannelInboundHandlerAdapter {

    private final AtomicInteger connectionNum = new AtomicInteger();

    public ConnectionCountHandler() {
        //定时任务输出统计服务端连接数 每两秒输出一次
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            System.out.println("connections: " + connectionNum.get());
        }, 0, 2, TimeUnit.SECONDS);

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        connectionNum.incrementAndGet();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        connectionNum.decrementAndGet();
    }

}
