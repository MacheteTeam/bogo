package com.macheteteam.bogo.commons;

import com.jcraft.jsch.*;
import com.macheteteam.bogo.utils.ConfigHelp;
import com.macheteteam.bogo.utils.Constants;
import com.macheteteam.bogo.utils.SpringUtil;
import com.macheteteam.bogo.utils.StringUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;

/**
 * Created by xudi on 2017/8/12.
 */
@Component
public class JschUtil {

    private static String host;
    private static int port = Constants.DEFAULTSSHPORY;
    private static String user;
    private static String password;
    //是否使用私钥验证
    private static boolean isUsePrivateKey = false;
    //私钥路径
    private static String privateKeyPath = Constants.DEFAULTPRIKEY;
    //客户端私钥加密密码
    private static String privateKeyPass = "";

    static {
//        ConfigHelp help = SpringUtil.getBean("configHelp");
//        if (help != null) {
//        }
        //TODO 目前写死
        host = "localhost";
        port = 22;
        user = "xudi";
        password = "";
        isUsePrivateKey = true;
        privateKeyPath = "/Users/xudi/.ssh/id_rsa";
        privateKeyPass = "";
    }
    /**
     * 执行shell
     * @param command shell命令
     */
    public static void execCommand(String command, Consumer<InputStream> consumer){
        if (StringUtils.isNotEmpty(command)) {
            Session session = getSession();
            try {
                Channel channel = session.openChannel("exec");
                ((ChannelExec)channel).setCommand(command);

                InputStream in=channel.getInputStream();
                channel.connect();
                consumer.accept(in);

                channel.disconnect();
                session.disconnect();
            } catch (JSchException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static Session getSession(){
        Session session = null;
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(user, host, port);
            if(isUsePrivateKey){
                File rsa = new File(privateKeyPath);
                jsch.addIdentity(rsa.getAbsolutePath(),privateKeyPass);
            }else {
                session.setPassword(password);
            }
            //FIXME 这里直接禁用了服务器公钥检查，之后修改为检查公钥
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
        } catch (JSchException e) {
            e.printStackTrace();
        }
        return session;
    }

}
