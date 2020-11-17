package com.rs;

import com.rs.util.DingDingMsgSendUtils;
import com.rs.util.forward;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author yexin
 * @date 2020-05-08 08:53
 */
@Slf4j
public class gateway {

    static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

    public static void main(String[] args) {
        // 构造一个线程池
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(2, 10, 3, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(3),
                new ThreadPoolExecutor.DiscardOldestPolicy());

        try {
            ServerSocket ss = new ServerSocket(6072);
            while (true) {
                Socket s = ss.accept();
                threadPool.execute(new receiveData(s));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    private static class receiveData implements Runnable {
        Socket s;

        public receiveData(Socket s) {
            this.s = s;
        }

        @Override
        public void run() {
            receive();
        }

        private void receive() {
            BufferedReader br = null;   //读入数据缓冲流
            PrintWriter pw = null;   //下位机客户端——>服务器端

            try {
                br = new BufferedReader(new InputStreamReader(s.getInputStream(), "GBK"));
                pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream(), "UTF-8")));

                InputStream is = s.getInputStream();
                byte[] buf = new byte[24];
                while ((is.read(buf)) != -1) {
                    String rcard = parseByte2HexStr(buf);
                    log.info(df.format(System.currentTimeMillis()) + "=====" + rcard);
                    int length = rcard.length();
                    if (rcard.contains("1234123412341234")) {
                        String str = rcard.split("1234123412341234")[1].substring(0, 8);
                        String cardNumber = "M_" + str;
                        log.info(df.format(System.currentTimeMillis()) + "=====" + cardNumber + "=========");
//                        forward.send(cardNumber.getBytes());
                        StringBuffer msg = new StringBuffer("拌合现场打卡成功!\n\n");
                        msg.append("打卡时间：").append(sdf.format(System.currentTimeMillis())).append("\n卡号：").append(str);
                        DingDingMsgSendUtils.sendRFIDDingTalkMsg(msg.toString());
                    }else if (rcard.contains("12341234")) {
                        String str = rcard.split("12341234")[1].substring(0, 8);
                        String cardNumber = "M_" + str;
                        log.info(df.format(System.currentTimeMillis()) + "=====" + cardNumber + "=========");
//                        forward.send(cardNumber.getBytes());
                        StringBuffer msg = new StringBuffer("拌合现场打卡成功!\n\n");
                        msg.append("打卡时间：").append(sdf.format(System.currentTimeMillis())).append("\n卡号：").append(str);
                        DingDingMsgSendUtils.sendRFIDDingTalkMsg(msg.toString());
                    }
                    /*读取分析完毕，将回复帧写入*/
                    pw.print("replyString");
                    pw.flush();
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            } finally {
                //在此关闭输入输出流、socket下位机连接
                try {
                    br.close();
                    pw.println(sdf.format(System.currentTimeMillis()) + "Server: socket connection close now.");
                    pw.flush();
                    pw.close();
                    s.close();
                    log.info("current socket is closed!");
                } catch (IOException e) {
                    log.warn("One socket can't be close.");
                    e.printStackTrace();
                }
            }
        }
    }


    public static String parseByte2HexStr(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs.toUpperCase();
    }

    /**
     * 两位相反再相倒
     * in：A1010100
     * out：000101A1
     */
    public static String fz(String str) {
        String fzStr = "";
        for (int i = 0; i < str.length(); i = i + 2) {
            fzStr += str.substring(str.length() - 2 - i, str.length() - i);
        }
        return fzStr;
    }


}
