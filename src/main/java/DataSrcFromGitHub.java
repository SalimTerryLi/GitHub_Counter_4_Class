import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.OkUrlFactory;
import org.kohsuke.github.*;
import org.kohsuke.github.extras.OkHttpConnector;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class DataSrcFromGitHub {

    private Properties loginInfo;
    private File cacheDir;
    private GitHub github;
    private Cache cache;

    public DataSrcFromGitHub(String username, String password, File cacheDir) {
        loginInfo = new Properties();
        loginInfo.setProperty("login", username);
        loginInfo.setProperty("password", password);
        this.cacheDir = cacheDir;
    }

    public boolean connect() {
        cache = new Cache(cacheDir, 100 * 1024 * 1024);
        try {
            github = GitHubBuilder.fromProperties(loginInfo)
                    .withConnector(new OkHttpConnector(new OkUrlFactory(new OkHttpClient().setCache(cache))))
                    .build();
            System.out.println("GitHub RateLimit remaining "+github.getRateLimit().remaining);
            return true;
        } catch (IOException err) {
        }
        System.out.println("connect failed!");
        return false;
    }

    public List<GHCommit> getCommitListByUser(GHRepository repo, String specUser) {
        try {
            List<GHCommit> commits = repo.listCommits().asList();
            List<GHCommit> ret = new ArrayList<GHCommit>();
            for (GHCommit commit : commits) {
                if (commit.getCommitShortInfo().getAuthor().getName().equals(specUser)) {
                    ret.add(commit);
                }
            }
            return ret;
        } catch (IOException err) {
            System.out.println("ERROR!");
        }
        return new ArrayList<GHCommit>();
    }

    public GHRepository getRepository(String repoName){
        try {
            return github.getRepository(repoName);
        }catch(IOException err){}
        return null;
    }
}
