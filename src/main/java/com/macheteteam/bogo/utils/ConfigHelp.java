package com.macheteteam.bogo.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Created by xudi on 2017/8/13.
 */
@Configuration
@Component(value = "configHelp")
@PropertySource("classpath:config.properties")
public class ConfigHelp {
    public static String jschHost;

    public static int jschPort = Constants.DEFAULTSSHPORY;

    public static String jschUser;

    public static String jschPassword;

    //是否使用私钥验证
    public static boolean jschIsUsePrivateKey = false;

    //私钥路径
    public static String jschPrivateKeyPath = Constants.DEFAULTPRIKEY;

    //客户端私钥加密密码
    public static String jschPrivateKeyPass = "";

    @Value("${jsch.host:@null}")
    public void setJschHost(String jschHost) {
        ConfigHelp.jschHost = jschHost;
    }

    @Value("${jsch.port:22}")

    public void setJschPort(int jschPort) {
        ConfigHelp.jschPort = jschPort;
    }
    @Value("${jsch.user:@null}")

    public void setJschUser(String jschUser) {
        ConfigHelp.jschUser = jschUser;
    }
    @Value("${jsch.password:@null}")

    public void setJschPassword(String jschPassword) {
        ConfigHelp.jschPassword = jschPassword;
    }
    @Value("${jsch.privateKey:@null}")
    public void setJschIsUsePrivateKey(boolean jschIsUsePrivateKey) {
        ConfigHelp.jschIsUsePrivateKey = jschIsUsePrivateKey;
    }
    @Value("${jsch.privateKeyPath:@null}")
    public void setJschPrivateKeyPath(String jschPrivateKeyPath) {
        ConfigHelp.jschPrivateKeyPath = jschPrivateKeyPath;
    }
    @Value("${jsch.privateKeyPass:@null}")
    public void setJschPrivateKeyPass(String jschPrivateKeyPass) {
        ConfigHelp.jschPrivateKeyPass = jschPrivateKeyPass;
    }
}
