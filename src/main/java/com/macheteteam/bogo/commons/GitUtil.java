package com.macheteteam.bogo.commons;

import com.macheteteam.bogo.utils.CollectionUtils;
import com.macheteteam.bogo.utils.StringUtils;
import org.eclipse.jgit.api.CreateBranchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.*;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Created by xudi on 2017/8/19.
 * git操作
 */

public class GitUtil {
    //默认workspace，所有git项目所在父目录
    public static final String LOCALPATH = "/Users/xudi/tmp/";
    //git远程地址
//    public static final String GITREMOTE = "https://github.com/MacheteTeam/bogo.git";

    /**
     * 读取相应目录的.git库，创建一个Repository
     * @param repoName 项目名
     * @return
     */
    public static Repository openRepository(String repoName){
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        File file = new File(LOCALPATH + repoName+"/.git");
        try {
            return builder.setGitDir(file).build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static Repository cloneRepository(String remotePath){
        return cloneRepository(remotePath,LOCALPATH);
    }
    /**
     * 从远程clone库到本地
     * @param remotePath 远程地址 如：https://github.com/MacheteTeam/bogo.git
     * @param localPath 父目录 /Users/MacheteTeam/
     * @return clone完成创建的Repository
     */
    public static Repository cloneRepository(String remotePath,String localPath){
        String repoName = getRepoNameByUrl(remotePath);
        if(!localPath.endsWith("/")){
            localPath = localPath + "/";
        }
        localPath = localPath + repoName;
        File file = new File(localPath);
        Git result;
        try {
            result = Git.cloneRepository().setURI(remotePath).setDirectory(file).setNoCheckout(true).call();
            return result.getRepository();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取所有远程分支
     * @param repoName 项目名
     * @return
     */
    public static List<Ref> getBranchs(String repoName){
        try (Repository repository = openRepository(repoName)) {
            if (repository != null) {
                try (Git git = new Git(repository)) {
                    List<Ref> refs = git.branchList().setListMode(ListBranchCommand.ListMode.REMOTE).call();
                    if(CollectionUtils.isNotEmpty(refs)){
                        return refs;
                    }
                } catch (GitAPIException e) {
                    e.printStackTrace();
                }
            }
        }
        return Collections.emptyList();
    }

    /**
     * 切换到远程分支，相当于git checkout origin/branchName
     * @param branchName 远程分支名  如：master
     */
    public static boolean checkoutRemoteBranch(String repoName, String branchName){
        try (Repository repository = openRepository(repoName)){
            if (repository != null) {
                try (Git git = new Git(repository)) {
                    git.checkout()
                            .setName("origin/"+branchName)
                            .setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.TRACK)
                            .setStartPoint("origin/" + branchName ).call();
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }



    private static boolean validateGitUrl(String gitUrl){
        if (StringUtils.isNotEmpty(gitUrl)) {
            return gitUrl.endsWith(".git");
        }
        return false;
    }

    private static String getRepoNameByUrl(String gitUrl){
        if(validateGitUrl(gitUrl)){
            String gitStr = gitUrl.substring(gitUrl.lastIndexOf("/")+1);
            String[] subs = gitStr.split("\\.");
            if(subs.length>1){
                return subs[0];
            }
            return StringUtils.EMPTYSTRING;
        }
        return StringUtils.EMPTYSTRING;
    }


}
