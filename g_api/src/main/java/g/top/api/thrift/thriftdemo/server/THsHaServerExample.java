package g.top.api.thrift.thriftdemo.server;

import g.top.api.thrift.thriftdemo.service.TestService;
import g.top.api.thrift.thriftdemo.service.impl.TestServiceImpl;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.layered.TFramedTransport;

/**
 * Created by wanghaiguang on 2022/7/15 下午7:27
 */
public class THsHaServerExample {

    private static final int SERVER_PORT = 9123;

    public static void main(String[] args) {

        try {

            /**
             * 1. 创建Transport
             */
            //TServerSocket serverTransport = new TServerSocket(SERVER_PORT);
            TNonblockingServerSocket serverTransport = new TNonblockingServerSocket(SERVER_PORT);
            THsHaServer.Args tArgs = new THsHaServer.Args(serverTransport);

            /**
             * 2. 为Transport创建Protocol
             */
            /**
             * TSocket: 阻塞式socket；
             * TFramedTransport: 以frame为单位进行传输，非阻塞式服务中使用；
             * TFileTransport: 以文件形式进行传输；
             */
            tArgs.transportFactory(new TFramedTransport.Factory());
            /**
             * TBinaryProtocol: 二进制格式；
             * TCompactProtocol: 压缩格式；
             * TJSONProtocol: JSON格式；
             * TSimpleJSONProtocol: 提供只写的JSON协议
             */
            tArgs.protocolFactory(new TBinaryProtocol.Factory());

            /**
             * 3. 为Protocol创建Processor
             */
            /**
             * TSimpleServer: 简单的单线程服务模型，常用于测试；
             * TThreadPoolServer: 多线程服务模型，使用标准的阻塞式IO；
             * TNonBlockingServer: 多线程服务模型，使用非阻塞式IO(需要使用TFramedTransport数据传输方式);
             * THsHaServer: THsHa引入了线程池去处理，其模型读写任务放到线程池去处理，Half-sync/Half-async处理模式，Half-async是在处理IO事件上(accept/read/write io)，Half-sync用于handler对rpc的同步处理；
             */
            TProcessor tprocessor = new TestService.Processor<TestService.Iface>(new TestServiceImpl());
            tArgs.processor(tprocessor);


            /**
             * 4. 创建Server并启动
             *
             * org.apache.thrift.server.TSimpleServer - 简单的单线程服务模型，一般用于测试
             */
            //TServer server = new TSimpleServer(tArgs);
            //半同步半异步的服务模型
            TServer server = new THsHaServer(tArgs);
            System.out.println("server start ....");
            server.serve();



        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
