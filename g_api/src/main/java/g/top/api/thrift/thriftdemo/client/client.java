package g.top.api.thrift.thriftdemo.client;

import g.top.api.thrift.thriftdemo.service.TestService;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.apache.thrift.transport.layered.TFramedTransport;

/**
 * Created by wanghaiguang on 2022/7/15 下午7:29
 */
public class client {
    public static void main(String[] args) {

        try {

            TTransport transport = new TFramedTransport(new TSocket("127.0.0.1", 9123, 3000));
            TProtocol protocol = new TBinaryProtocol(transport);

            TestService.Client client = new TestService.Client(protocol);
            transport.open();

            System.out.println(client.sayHello("测试"));

            transport.close();

        }  catch (TException e) {

        }
    }
}
