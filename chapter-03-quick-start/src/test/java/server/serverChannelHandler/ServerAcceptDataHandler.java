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

package server.serverChannelHandler;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
/**
 *<p>服务端接收请求数据channelHandler</p>
 *@author <a href="mailto:tangdong@supermonkey.com.cn">tangdong</a>
 *@version
 *@since
 *@date Created in 2020-05-21 3:11 下午
 */
public class ServerAcceptDataHandler extends SimpleChannelInboundHandler<ByteBuf> {

    //计数器
    private final AtomicInteger counter = new AtomicInteger();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        //读取请求数据
        byte[] bytes = new byte[msg.readableBytes()];
        msg.readBytes(bytes);
        String reqBody = new String(bytes, StandardCharsets.UTF_8);
        System.out.println("客户端请求数据 ： " + reqBody);
        System.out.println("请求次数计数器 ： " + counter.incrementAndGet());
        //响应请求数据
        ByteBuf resp = Unpooled.copiedBuffer(("服务端已收到确认-" + counter.get() + System.getProperty("line.separator")).getBytes(StandardCharsets.UTF_8));
        ctx.writeAndFlush(resp);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("完成一次完整的客户端请求数据读取");
    }

}
