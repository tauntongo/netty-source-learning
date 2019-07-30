/*
 * Copyright (c) 2019 tangduns945@gmail.com. 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package bytebuf;

import io.netty.buffer.PooledByteBufAllocator;
import org.junit.Test;

/**
 * <p>测试池化ByteBuf，这里我们主要关注点在池化上，另外两个区分点是否Unsafe以及堆内外就不测试了</p>
 *
 * @author <a href="mailto:tangduns945@gmail.com">Taunton</a>
 * @date Created in 2019-07-28 19:36
 */
public class Pooled {
    
    @Test
    public void testSubPageMemory(){
        PooledByteBufAllocator allocator = PooledByteBufAllocator.DEFAULT;
        allocator.directBuffer(16);
    }
    
    @Test
    public void testTinyCache(){
        
    }
    
    
    
}
