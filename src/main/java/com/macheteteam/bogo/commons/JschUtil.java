package com.macheteteam.bogo.commons;

import com.jcraft.jsch.*;
import com.macheteteam.bogo.utils.ConfigHelp;
import com.macheteteam.bogo.utils.StringUtils;
import org.springframework.stereotype.Component;

import java.io.*;
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

    /**
     * scp 文件上传
     * @param localFile 本地文件路径
     * @param remoteFile 目标文件路径
     */
    public static void scp(String localFile, String remoteFile){
        if (StringUtils.isNotEmpty(localFile,remoteFile)) {
            Session session = getSession();
            try {
                Channel channel = session.openChannel("exec");
                String command = "scp -t " + remoteFile;
                ((ChannelExec) channel).setCommand(command);
                channel.connect();
                OutputStream out = channel.getOutputStream();
                InputStream in = channel.getInputStream();

                if(checkAck(in)!=0){
                    System.exit(0);
                }

                File _lfile = new File(localFile);

                long filesize=_lfile.length();
                command="C0644 "+filesize+" ";
                if(localFile.lastIndexOf('/')>0){
                    command+=localFile.substring(localFile.lastIndexOf('/')+1);
                }
                else{
                    command+=localFile;
                }
                command+="\n";
                out.write(command.getBytes()); out.flush();

                if(checkAck(in)!=0){
                    System.exit(0);
                }

                FileInputStream fis;
                fis = new FileInputStream(_lfile);
                byte[] buf = new byte[1024];
                while (true) {
                    int len = fis.read(buf, 0, buf.length);
                    if (len <= 0) break;
                    out.write(buf, 0, len); //out.flush();
                }
                fis.close();
                // send '\0'
                buf[0] = 0;
                out.write(buf, 0, 1);
                out.flush();
                if(checkAck(in)!=0){
                    System.exit(0);
                }
                out.close();
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

    private static int checkAck(InputStream in) throws IOException{
        int b=in.read();
        // b may be 0 for success,
        //          1 for error,
        //          2 for fatal error,
        //          -1
        if(b==0) return b;
        if(b==-1) return b;

        if(b==1 || b==2){
            StringBuffer sb=new StringBuffer();
            int c;
            do {
                c=in.read();
                sb.append((char)c);
            } while(c!='\n');
            if(b==1){ // error
                System.out.print(sb.toString());
            }
            if(b==2){ // fatal error
                System.out.print(sb.toString());
            }
        }
        return b;
    }

}
