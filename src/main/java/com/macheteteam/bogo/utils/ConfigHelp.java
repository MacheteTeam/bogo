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
    @Value("${jsch.host:@null}")
    private String jschHost;

    @Value("${jsch.port:22}")
    private int jschPort = Constants.DEFAULTSSHPORY;

    @Value("${jsch.user:@null}")
    private String jschUser;

    @Value("${jsch.password:@null}")
    private String jschPassword;

    //是否使用私钥验证
    @Value("${jsch.privateKey:@null}")
    private boolean jschIsUsePrivateKey = false;

    //私钥路径
    @Value("${jsch.privateKeyPath:@null}")
    private String jschPrivateKeyPath = Constants.DEFAULTPRIKEY;

    //客户端私钥加密密码
    @Value("${jsch.privateKeyPass:@null}")
    private String jschPrivateKeyPass = "";

    public String getJschHost() {
        return jschHost;
    }

    public int getJschPort() {
        return jschPort;
    }

    public String getJschUser() {
        return jschUser;
    }

    public String getJschPassword() {
        return jschPassword;
    }

    public boolean getJschIsUsePrivateKey() {
        return jschIsUsePrivateKey;
    }

    public String getJschPrivateKeyPath() {
        return jschPrivateKeyPath;
    }

    public String getJschPrivateKeyPass() {
        return jschPrivateKeyPass;
    }
}
