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

package top.taunton.netty_source_ok.client.channelHandler;

import java.nio.charset.StandardCharsets;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
/**
 *<p>存在粘包问题的客户端handler</p>
 *@author <a href="mailto:tangduns945@gmail.com">Taunton</a>
 *@version
 *@since
 *@date Created in 2020-05-21 2:50 下午
 */
public class ClientSendAndAcceptDataHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf req = null;
        for (int i = 0; i < 100; i++) {
            //直接这样发送小数据包会发生粘包 因为小于TCP缓冲区大小，会堆积后TCP将多次写入TCP缓冲区中的数据一次性发送出去
            req = Unpooled.copiedBuffer(("你是谁" + System.getProperty("line.separator")).getBytes(StandardCharsets.UTF_8));
            //req = UnpooledByteBufAllocator.DEFAULT.buffer(1024*1024).writeBytes(("我爱你" + System.getProperty("line.separator")).getBytes(StandardCharsets.UTF_8));
            //ctx.writeAndFlush(req);
            ctx.pipeline().writeAndFlush(req);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        byte[] bytes = new byte[msg.readableBytes()];
        msg.readBytes(bytes);
        String resp = new String(bytes, StandardCharsets.UTF_8);
        System.out.println("服务端响应数据 ： " + resp);
    }
}
