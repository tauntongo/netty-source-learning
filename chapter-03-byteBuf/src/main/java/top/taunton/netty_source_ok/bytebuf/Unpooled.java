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

package top.taunton.netty_source_ok.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

/**
 * <p>非池化ByteBuf测试</p>
 *
 * @author <a href="mailto:tangduns945@gmail.com">Taunton</a>
 * @date Created in 2019-07-28 19:37
 */
public class Unpooled {
    
    @Test
    public void testOthers(){
        int[] arr = new int[10];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = i;
        }
        int[] arr2 = {5, 5, 5};
        System.arraycopy(arr2, 0, arr, 0, 3);
        StringBuilder sb = new StringBuilder();
        for (int i : arr) {
            sb.append(i);
        }
        System.out.println(sb.toString());
    }

    /**
     * ByteBuf主要API使用
     */
    @Test
    public void testBasicUsage(){
        UnpooledByteBufAllocator allocator = UnpooledByteBufAllocator.DEFAULT;
        ByteBuf byteBuf = allocator.heapBuffer(1024);
        String writedStr =  "hello world!I'm your father.";
        //写数据
        byteBuf.writeBytes(writedStr.getBytes());
        byteBuf.setBytes(0, "H".getBytes());
        //ByteBuf byteBuf1 = allocator.heapBuffer(248);
        //byteBuf1.writeBytes("BBB".getBytes());
        //byteBuf.setBytes(0, byteBuf1, 0, byteBuf1.writerIndex());

        System.out.println("readerIndex = " + byteBuf.readerIndex());
        System.out.println("writerIndex = " + byteBuf.writerIndex());
        //标记writerIndex位置
        byteBuf.markWriterIndex();
        //清除readerIndex和writerIndex 置为0
        byteBuf.clear();
        System.out.println("after clear readerIndex = " + byteBuf.readerIndex());
        System.out.println("after clear writerIndex = " + byteBuf.writerIndex());
        //重置writerIndex到mark标记的索引位置
        byteBuf.resetWriterIndex();
        System.out.println("after resetWriterIndex writerIndex = " + byteBuf.writerIndex());


        //读数据
        byte[] bytes = new byte[byteBuf.writerIndex()-byteBuf.readerIndex()];
        byteBuf.readBytes(bytes);
        System.out.println("read val = " + new String(bytes, StandardCharsets.UTF_8));
        System.out.println("after read readerIndex = " + byteBuf.readerIndex());
        System.out.println("after read writerIndex = " + byteBuf.writerIndex());

        //标记readerIndex位置
        byteBuf.markReaderIndex();
        //清除readerIndex和writerIndex 置为0
        byteBuf.clear();
        System.out.println("after clear readerIndex = " + byteBuf.readerIndex());
        System.out.println("after clear writerIndex = " + byteBuf.writerIndex());
        //重置readerIndex到mark标记的索引位置
        //会抛出异常 因readerIndex不能大于writerIndex
        //byteBuf.resetReaderIndex();
        //System.out.println("after resetReaderIndex readerIndex = " + byteBuf.readerIndex());

        //TEST discardReadBytes
        //byte[] bytes1 = new byte[2];
        //byteBuf.readBytes(bytes1);
        //System.out.println("read val = " + new String(bytes1, StandardCharsets.UTF_8));
        //System.out.println("after read readerIndex = " + byteBuf.readerIndex());
        //System.out.println("after read writerIndex = " + byteBuf.writerIndex());
        //byteBuf.discardReadBytes();
        //byte[] bytes2 = new byte[byteBuf.writerIndex()-byteBuf.readerIndex()];
        //byteBuf.readBytes(bytes2);
        //System.out.println("after discard read val = " + new String(bytes2, StandardCharsets.UTF_8));
        //System.out.println("after discard read readerIndex = " + byteBuf.readerIndex());
        //System.out.println("after discard read writerIndex = " + byteBuf.writerIndex());


    }


    /**
     * 测试Unsafe&堆内 ByteBuf
     */
    @Test
    public void testUnsafeHeap(){}

    /**
     * 测试Unsafe&堆外 ByteBuf
     */
    @Test
    public void testUnsafeDirect(){

    }

    /**
     * 测试非Unsafe&堆内 ByteBuf
     */
    @Test
    public void testHeap(){

    }

    /**
     * 测试非Unsafe&堆外 ByteBuf
     */
    @Test
    public void testDirect(){

    }

}
