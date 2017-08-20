package com.macheteteam.bogo.controller;

import com.macheteteam.bogo.commons.GitUtil;
import com.macheteteam.bogo.utils.CmdUtils;
import com.macheteteam.bogo.utils.JsonUtils;
import org.eclipse.jgit.lib.Ref;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by xudi on 2017/8/20.
 */
@RestController
@RequestMapping("/git")
public class GitController {

    @RequestMapping("/clone")
    public String cloneRemoteRepo(@RequestParam("remoteAddress")String remoteAddress){
        if(GitUtil.cloneRepository(remoteAddress)!=null){
            return "success";
        }
        return "failure";
    }

    /**
     * 获取远程分支列表
     * @param repoName
     * @return
     */
    @RequestMapping("/getRemoteBranchs")
    public String getRemoteBranchs(@RequestParam("repoName") String repoName) {
        List<String> result;
        List<Ref> list = GitUtil.getBranchs(repoName);
        result = list.stream().map((s)->s.getName().substring(s.getName().lastIndexOf("/")+1)).collect(Collectors.toList());
        return JsonUtils.toString(result);
    }

    /**
     * 切换分支并打包
     * @param repoName
     * @param branchName
     * @return
     */
    @RequestMapping("/checkout")
    public String checkoutRemote(@RequestParam("repoName") String repoName,@RequestParam("branchName") String branchName) {
        if (GitUtil.checkoutRemoteBranch(repoName, branchName)) {
            //mvn打包，运行
            CmdUtils.execShell("pwd");
//            CmdUtils.execShell("mvn clean package");
            return "success";
        }
        return "failure";
    }



}
