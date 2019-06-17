# netty的应用

- ![netty-applied-app](D:\software\dev\java\learn\netty\netty-source-analysis-learning-sample\note\img\netty-applied-app.png)

# Chapter-02

### Netty的基本组件

- NioEventLoop（核心）
  - Channel：netty自定义的Channel，是对nio中的Channel的进一步封装
  - Pipeline
  - ChannelHandler：每一次Channel需要进行数据处理就放出一个ChannelHandler来进行处理，我们可以创建多个ChannelHandler对象添加到Channel里面去，从而介入Channel的数据处理流程中去。我们的业务代码也写在这里面。
  - ByteBuffer



# Chapter-03(服务端启动&服务端启动时干了什么)

### QuickStart-启动一个Netty服务端

```java
public class StartServerDemo {

    @Test
    public void server(){
        EventLoopGroup serverGroup = new NioEventLoopGroup(1);
        EventLoopGroup childGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(serverGroup,childGroup)
                    .channel(NioServerSocketChannel.class)
                    // 对服务端Channel NioServerSocketChannel的配置，可通过多次调用设置多个
                    //.option()
                    //.option()
                    // 对服务端Channel NioServerSocketChannel的属性设置，可通过多次调用设置多个
                    //.attr()
                    //.attr()
                    //对accept到的SocketChannel的配置，每次accept到SocketChannel都会按照我们所传的配置项配置一遍
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    //对accept到的SocketChannel的属性设置，每次accept到SocketChannel都会按照我们所传的属性设置一遍
                    .childAttr(AttributeKey.newInstance("childAttr"), "childAttrValue")
                    //对NioServerSocketChannel的处理介入，这里我们传入的是一个继承了ChannelInboundHandlerAdapter的自定义handler对象
                    .handler(new ServerHandler())
                    //对accept到的SocketChannel处理介入,我们如果要写业务代码一般也就是写在handler里面了
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //可以在这这这里加上众多的handler介入对accept到的Channel的处理
                            //ch.pipeline().addLast()
                            //ch.pipeline().addLast()
                            //ch.pipeline().addAfter()
                        }
                    });

            //启动Netty服务端
            ChannelFuture channelFuture = b.bind(8848).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //优雅的关闭
            serverGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
            //childGroup.shutdownGracefully(1000,3000, TimeUnit.MILLISECONDS);
        }
    }
}
```

### 服务端启动流程

- 服务端启动核心路径

```mermaid
sequenceDiagram
new Channel()->>init():then
init()->>register():then
register()->>doBind():then
```


##### 服务端Channel的创建

- 从外部用户代码进入到具体创建这一步的流程

  - ```mermaid
    sequenceDiagram
    bind()[用户代码入口]->>initAndRegister()[初始化并注册]:then
    initAndRegister()[初始化并注册]->>newChannel()[通过反射创建服务端channel]:then
    ```

- 具体创建的流程

  - newSocket()：通过jdk来创建底层jdk channel
    - ![2019-05-26-2.1-newSocket()](D:\software\dev\java\learn\netty\netty-source-analysis-learning-sample\note\img\2019-05-26-2.1-newSocket().png)
  - AbstractNioChannel()
    - AbstractChannel()：创建id，unsafe，pipeline
    - SelectableChannel.configureBlocking(false)：jdk底层channel配置阻塞模式
    - ![2019-05-26-2.2-AbstractNioChannel()](D:\software\dev\java\learn\netty\netty-source-analysis-learning-sample\note\img\2019-05-26-2.2-AbstractNioChannel().png)
  - new NioServerSocketChannelConfig()：tcp参数配置类。通过ServerBootstrap.option()保存的参数最终会被设置到此配置对象中去



##### 服务端Channel的初始化

- 从外部用户代码进入到具体初始化这一步的流程

  - ```mermaid
    sequenceDiagram
    bind()[用户代码入口]->>initAndRegister()[初始化并注册]:then
    initAndRegister()[初始化并注册]->>newChannel()[通过反射创建服务端channel]:then
    initAndRegister()[初始化并注册]->>init()[初始化服务端channel]:then
    ```

- 具体初始化Channel流程

  - init()：初始化入口
    - set ChannelOptions，set ChannelAttrs：将业务代码中用户配置的参数&属性设置到NioServerSocketChannel中去
    - childGroup、childHandler、childOptions、childAttrs：获取到对请求Channel的各种配置，用来在后面第四步传入连接器中
    - config.handler()：配置服务端pipeline
    - add ServerBootstrapAcceptor：添加连接器（每次accept到SocketChannel后使用用户配置的childGroup、childHandler、childOptions、childAttrs作为其配置）
    - 源码：
      - ![2019-05-27-1-ServerBootstrap.init()](D:\software\dev\java\learn\netty\netty-source-analysis-learning-sample\note\img\2019-05-27-1-ServerBootstrap.init().png)

##### 注册到selector

- 从外部用户代码进入到具体注册这一步

  - ``` mermaid
    sequenceDiagram
    bind()[用户代码入口]->>initAndRegister()[初始化并注册]:then
    initAndRegister()[初始化并注册]->>register()[将channel注册到selector]:then
    ```

  - 具体注册流程：

    - AbstractChannel.register(channel)：入口
      - this.eventLoop  =  eventLoop：绑定线程，服务端对请求的接收轮询就是在通过此线程进行的，因此eventLoop是netty的核心
      - register0()：实际注册
        - doRegister()：调用jdk底层注册
        - invokeHandlerAddedIfNeeded()：回调执行handler中的handlerAdded(ChannelHandlerContext ctx)
        - fireChannelRegistered()：传播事件，可以让如我们添加的自定义handler感知
    - 部分源码流程：
      - ![2019-06-02-01AbstractChanel.register](D:\software\dev\java\learn\netty\netty-source-analysis-learning-sample\note\img\2019-06-02-01AbstractChanel.register.png)
      - ![2019-06-02-02-Abstractchannel.regist0](D:\software\dev\java\learn\netty\netty-source-analysis-learning-sample\note\img\2019-06-02-02-Abstractchannel.regist0.png)






##### 服务端口的绑定

- 从外部用户代码进入到具体绑定这一步

  - ``` mermaid
    sequenceDiagram
    bind()[用户代码入口]->>doBind0()[绑定并传播channelActive事件]:then
    ```

- 具体绑定步骤

  - AbstractChannel.AbstractUnsafe.bind()：入口
    - doBind()
      - javaChannel().bind()：调用jdk底层nio接口ServerSocketChannel绑定端口
    - pipeline.fireChannelActive()：传播channelActive事件
  - 部分步骤源码：
    - ![2019-06-04-01-bind](D:\software\dev\java\learn\netty\netty-source-analysis-learning-sample\note\img\2019-06-04-01-bind.png)

# Chapter-04(NioEventLoop)

### 概述

- NioEventLoop是Netty的核心组件，运行时一切活动都在NioEventLoop中进行

### NioEventLoopGroup继承层级结构

### NioEventLoop的继承层级结构



### NioEventLoop创建

- new **NioEventLoopGroup**(),默认创建cup核数*2个NioEventLoop，可在构造方法传入自定义
  - 事件执行器-**EventExecutor**(即NioEventLoop，NioEventLoop是其实现类)数组
    - new EventExecutor[nThreads]数组，长度默认cpu核数*2
  - 任务执行器-**ThreadPerTaskExecutor**
    -  执行存放在MpscQueue队列中中的Task
    -  每次execute(Runnable command)执行任务时，都会创建一个线程实体（FastThreadLocalThread，继承了Thread，对ThreadLocal做了优化）
    -  通过ThreadFactory来创建线程，每次创建一个NioEventLoopGroup对象时在NioEventLoopGroup的构造方法中会new 一个**DefaultThreadFactory**()传入到ThreadPerTaskExecutor构造方法中
    -  创建的线程名称命名规则nioEventLoop-nioEventLoop在nioEventLoop-1-xx（1：声明创建NioEventLoopGroup对象的次序，每创建一次自增1，xx：在每个DefaultThreadFactory对象中每创建一个线程自增1）
  - 创建NioEventLoop对象
    - 循环前面创建的EventExecutor数组，通过newChild（threadPerTaskExecutor，args）创建出来的NioEventLoop对象赋值给每个数组元素
    - newChild
      - new NioEventLoop()
        - 将前面创建的ThreadPerTaskExecutor对象赋值到NioEventLoop对象的executor变量
        - 创建一个**MpscQueue**（Multi Producer Single Consumer Queue，多生产者单消费者，如果以服务端为主视角，那么服务端的NioEventLoop线程即Consumer，外部访问线程即Producer）队列，默认大小Integer.MAX_VALUE（可通过设置系统变量io.netty.eveltLoop.maxPendingTasks来自定义），赋值给NioEventLoop对象的taskQueue变量，这是一个优先队列PriorityQueue，NioEventLoop执行任务都是从此队列取
        - 创建Selector：通过SelectProvider对象.openSelector()来获取一个选择器，赋值给NioEventLoop对象的selector变量
  - 创建NioEventLoop选择器：chooserFactory.newChooser()，用来在有外部请求时从NioEventLoop数组中挑选一个NioEventLoop来处理请求
    - isPowerOfTwo：NioEventLoop数组长度是否为2的n次幂，是则创建new PowerOfTwoEventExecutorChooser(executors)，否则创建new GenericEventExecutorChooser(executors)
    - PowerOfTwoEventExecutorChooser选择NioEventLoop规则：index++ & (length-1)
    - GenericEventExecutorChooser选择NioEventLoop规则：abs(index++ % length)

### NioEventLoop启动

### 三个问题

1. 默认情况下Netty服务端启多少线程？何时启动？
   1. 默认启动cpu核数*2个线程
   2. 服务端启动绑定端口会启动一个NioEventLoop（在parentGroup中选）；新连接接入通过chooser绑定一个NioEventLoop（在childGroup中选）
2. Netty是如何解决jdk空轮询bug的？
3. Netty如何保证异步串行无锁化？



# Chapter-05

# Chapter-06

