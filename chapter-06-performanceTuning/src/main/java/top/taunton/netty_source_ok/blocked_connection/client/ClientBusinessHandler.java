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

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
/**
 *<p></p>
 *@author <a href="mailto:tangduns945@gmail.com">Taunton</a>
 *@version 1.0.0
 *@since 1.0.0
 *@date Created in 2020-06-11 5:59 下午
 */
@Sharable
public class ClientBusinessHandler extends SimpleChannelInboundHandler<ByteBuf> {
    public static final ChannelHandler INSTANCE = new ClientBusinessHandler();

    private static final AtomicLong beginTime = new AtomicLong(0);
    private static final AtomicLong totalResponseTime = new AtomicLong(0);
    private static final AtomicInteger totalRequest = new AtomicInteger(0);

    public static final Thread THREAD = new Thread(() -> {
        try {
            while (true) {
                long duration = System.currentTimeMillis() - beginTime.get();
                if (duration != 0) {
                    System.out.println("qps: " + 1000 * totalRequest.get() / duration + ", " + "avg response time: " + ((float) totalResponseTime.get()) / totalRequest.get());
                    Thread.sleep(2000);
                }
            }

        } catch (InterruptedException ignored) {
        }
    });

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.executor().scheduleAtFixedRate(() -> {

            ByteBuf byteBuf = ctx.alloc().ioBuffer();
            byteBuf.writeLong(System.currentTimeMillis());
            ctx.channel().writeAndFlush(byteBuf);

        }, 0, 1, TimeUnit.SECONDS);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
        totalResponseTime.addAndGet(System.currentTimeMillis() - msg.readLong());
        totalRequest.incrementAndGet();

        if (beginTime.compareAndSet(0, System.currentTimeMillis())) {
            THREAD.start();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // ignore
    }
}
