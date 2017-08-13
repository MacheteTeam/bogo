package com.macheteteam.bogo.commons;

import com.jcraft.jsch.*;
import com.macheteteam.bogo.utils.ConfigHelp;
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
            session = jsch.getSession(ConfigHelp.jschUser, ConfigHelp.jschHost, ConfigHelp.jschPort);
            if(ConfigHelp.jschIsUsePrivateKey){
                File rsa = new File(ConfigHelp.jschPrivateKeyPath);
                jsch.addIdentity(rsa.getAbsolutePath(),ConfigHelp.jschPassword);
            }else {
                session.setPassword(ConfigHelp.jschPassword);
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
