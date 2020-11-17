package com.rs.util;

/**
 * @program: Roadstar_mixStation
 * @description:
 * @author: Saxon
 * @create: 2020-07-07 13:30
 **/

import com.xiaoleilu.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;

@Slf4j
public class DingDingMsgSendUtils {

    /**
     * 调用钉钉官方接口发送钉钉消息（新版本，需要配置安全设置）
     *
     * @param secret
     * @param textMsg
     */
    private static void dealDingDingMsgSendNew(String URLString, String secret, String textMsg) {
        Long timestamp = System.currentTimeMillis();
        String sign = getSign(secret, timestamp);
        String url = URLString +"&timestamp=" + timestamp + "&sign=" + sign;
        try {
            log.info("【发送钉钉群消息】请求参数：url = {}, textMsg = {}", url, textMsg);
            String res = HttpUtil.post(url, textMsg);
            log.info("【发送钉钉群消息】消息响应结果：" + res);
        } catch (Exception e) {
            log.warn("【发送钉钉群消息】请求钉钉接口异常，errMsg = {}", e);
        }
    }

    /**
     * 计算签名
     *
     * @param secret    密钥，机器人安全设置页面，加签一栏下面显示的SEC开头的字符
     * @param timestamp
     * @return
     */
    private static String getSign(String secret, Long timestamp) {
        try {
            String stringToSign = timestamp + "\n" + secret;
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256"));
            byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
            String sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)), "UTF-8");
            log.info("【发送钉钉群消息】获取到签名sign = {}", sign);
            return sign;
        } catch (Exception e) {
            log.error("【发送钉钉群消息】计算签名异常，errMsg = {}", e);
            return null;
        }
    }

//    public static void sendDingTalkMsg(String content) {
//
//        String textMsg = "{\n" +
//                "    \"msgtype\": \"text\", \n" +
//                "    \"text\": {\n" +
//                "        \"content\": \"【系统消息通知】\n\n" + content + "\"\n" +
//                "    }\n" +
//                "}";
//        String URLString ="https://oapi.dingtalk.com/robot/send?access_token=24e32987afb7e7af28ce8b5e16d68369e07c0c14a04d77e46ec8fe6ae708513d";
//        String secret = "SECb65d9967b0df1189449378d87258b6e476e66ce8c2c1cc5e8ed7d8c8ee717d67";
//        dealDingDingMsgSendNew(URLString,secret,textMsg);
//    }


    public static void sendRFIDDingTalkMsg(String content) {

        String textMsg = "{\n" +
                "    \"msgtype\": \"text\", \n" +
                "    \"text\": {\n" +
                "        \"content\": \"【系统消息通知】\n\n" + content + "\"\n" +
                "    }\n" +
                "}";
        String URLString ="https://oapi.dingtalk.com/robot/send?access_token=9ac24044c9520f6ae380b9b9ea8adec3da2a7a338c56e3efd36263492d40cbb2";
        String secret = "SEC569b4f2d6306a5c63a8560e0dc83afcf89744c59d15fe8f7e51f6773a7d20b6a";
        dealDingDingMsgSendNew(URLString,secret,textMsg);
    }




//    public static void main(String[] args) {
//        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//        String s1 ="测试106";
//        String mnl9 ="344446";
//        StringBuffer msg = new StringBuffer("摊铺现场打卡成功!\n\n");
//        msg.append("打卡时间：").append(sd.format(System.currentTimeMillis())).append("\n摊铺机编号：").append(s1).append("\n卡号：").append(mnl9);
//        log.info(msg.toString());
//        String r = "123456789qwertyuiop";
//        r.substring(0,8);
//    }

//    public static void main(String[] args) throws UnknownHostException, IOException {
//        // TODO Auto-generated method stub
//        //1.建立TCP连接
//        String ip="127.0.0.1";   //服务器端ip地址
//        int port=6072;        //端口号
//        Socket sck=new Socket(ip,port);
//        //2.传输内容
//        String content="这是一个java模拟客户端";
//        byte[] bstream=content.getBytes("GBK");  //转化为字节流
//        OutputStream os=sck.getOutputStream();   //输出流
//        os.write(bstream);
//        //3.关闭连接
//        sck.close();
//    }


}

