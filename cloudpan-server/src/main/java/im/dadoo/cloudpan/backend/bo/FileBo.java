package im.dadoo.cloudpan.backend.bo;

import im.dadoo.cloudpan.backend.dao.CloudFileDao;
import im.dadoo.cloudpan.backend.po.CloudFilePo;
import im.dadoo.cloudpan.backend.util.CloudpanUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * Created by codekitten on 2017/1/7.
 */
@Component
public class FileBo {

  @Resource
  private Environment env;

  @Resource
  private CloudFileDao cloudFileDao;

  public File cache(MultipartFile file) throws Exception {
    File r = null;
    if (file != null && !file.isEmpty()) {
      r = new File(this.env.getProperty("master.path") + "/" + System.currentTimeMillis() + RandomStringUtils.random(4));
      file.transferTo(r);
    }
    return r;
  }

  public void save(CloudFilePo po, File temp) throws Exception {
    String dirPath = String.format("%s/%s", this.env.getProperty("master.path"), po.getSection());
    new File(dirPath).mkdirs();
    String path = String.format("%s/%s/%s", this.env.getProperty("master.path"), po.getSection(), po.getMd5());
    File file = new File(path);
    if (file.exists()) {
      file.delete();
    }
    temp.renameTo(file);
  }

  public boolean delete(CloudFilePo po) {
    boolean r = false;
    if (po != null && !CloudpanUtil.isDirectory(po)) {
      String path = String.format("%s/%s/%s", this.env.getProperty("master.path"), po.getSection(), po.getMd5());
      File file = new File(path);
      if (file != null && file.exists()) {
        file.delete();
      }
      r = true;
    }
    return r;
  }
}
