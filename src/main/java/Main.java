import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHRepository;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        if(args[0].equals("-h")){
            System.out.println("GitHub Commit Counter for C/CPP Class.");
            System.out.println("usage: java -jar this.jar username password");
            System.out.println("default xlsx=~/students.xlsx, default cache=~/tmp");
            return;
        }
        DataSrcFromXLS xls=new DataSrcFromXLS("students.xlsx");
        if(!xls.isValid){return;}   // Exit if ~/students.xlsx not here.

        DataSrcFromGitHub github = new DataSrcFromGitHub(args[0]
                , args[1]
                , new File(System.getProperty("user.home") + System.getProperty("file.separator") + "tmp"));

        if (github.connect()) {
            for(int i=1;i<=xls.getCount();i++) {
                System.out.println("Name:"+xls.getStuInfo(i).Name+" ID:"+xls.getStuInfo(i).ID+" GitHub Account:"+xls.getStuInfo(i).Account);
                GHRepository repo=github.getRepository(xls.getStuInfo(i).Account+"/hello-world");
                if(repo!=null) {
                    List<GHCommit> commits = github.getCommitListByUser(repo, xls.getStuInfo(i).Account);
                    for (GHCommit commit : commits) {
                        try {
                            System.out.println("\tDate: " + commit.getCommitShortInfo().getCommitDate().toString()
                                    + "\t Added " + commit.getLinesAdded()
                                    + "\t Deleted " + commit.getLinesDeleted());
                        } catch (IOException err) {
                        }
                    }
                }else{
                    System.out.println("Repo not found!");
                }
            }
        }
    }
}
