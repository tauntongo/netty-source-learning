/*
 * Copyright (c) 2019. tangduns945@gmail.com.
 */

import io.netty.channel.nio.NioEventLoop;
import io.netty.util.internal.StringUtil;
import org.junit.Test;

import java.util.Locale;

/**
 * <p></p>
 *
 * @author <a href="mailto:tangduns945@gmail.com">Taunton</a>
 * @date Created in 2019-06-15 0:03
 */
public class SimpleTest {

    @Test
    public void testGetClass(){
        Class<NioEventLoop> nioEventLoopClass = NioEventLoop.class;
        String s = StringUtil.simpleClassName(nioEventLoopClass);
        System.out.println(s);
        String s1 = s.toLowerCase(Locale.US);
        System.out.println(s1);
    }

    @Test
    public void testAssert(){
        String str = null;
        assert str != null;
        System.out.println("assert str == null successful");

    }

    @Test
    public void testNanoTime(){
        long currentNanoTime = System.nanoTime();
        long currentTimeMillis = System.currentTimeMillis();
        System.out.println(currentNanoTime);
        System.out.println(currentTimeMillis);
        Class<SimpleTest> simpleTestClass = SimpleTest.class;
        boolean assignableFrom = simpleTestClass.isAssignableFrom(SimpleTest.class);
    }

    // 'wakenUp.compareAndSet(false, true)' is always evaluated
    // before calling 'selector.wakeup()' to reduce the wake-up
    // overhead. (Selector.wakeup() is an expensive operation.)
    //
    // However, there is a race condition in this approach.
    // The race condition is triggered when 'wakenUp' is set to
    // true too early.
    //
    // 'wakenUp' is set to true too early if:
    // 1) Selector is waken up between 'wakenUp.set(false)' and
    //    'selector.select(...)'. (BAD)
    // 2) Selector is waken up between 'selector.select(...)' and
    //    'if (wakenUp.get()) { ... }'. (OK)
    //
    // In the first case, 'wakenUp' is set to true and the
    // following 'selector.select(...)' will wake up immediately.
    // Until 'wakenUp' is set to false again in the next round,
    // 'wakenUp.compareAndSet(false, true)' will fail, and therefore
    // any attempt to wake up the Selector will fail, too, causing
    // the following 'selector.select(...)' call to block
    // unnecessarily.
    //
    // To fix this problem, we wake up the selector again if wakenUp
    // is true immediately after selector.select(...).
    // It is inefficient in that it wakes up the selector for both
    // the first case (BAD - wake-up required) and the second case
    // (OK - no wake-up required).



}
