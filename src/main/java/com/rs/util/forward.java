package com.rs.util;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * @author yexin
 * @date 2020-05-09 09:12
 */
public class forward {


    public static void send( byte[] data ) {
        try {
            //定义服务器的地址，端口号，数据
            InetAddress address = InetAddress.getByName("127.0.0.1");
//            InetAddress address = InetAddress.getByName("127.0.0.1");
            int port = 6060;
            String str = "";
            //创建数据报
            DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
            //创建DatagramSocket，实现数据发送和接收
            DatagramSocket socket = new DatagramSocket();
            //向服务器端发送数据报
            socket.send(packet);
            //接收服务器响应数据
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
