package im.dadoo.cloudpan.backend.util;

import im.dadoo.cloudpan.backend.po.CloudFilePo;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLConnection;

/**
 * Created by codekitten on 2017/1/7.
 */
public final class CloudpanUtil {

  private static final long DIR_SIZE = 100;

  private CloudpanUtil() {}

  public static String makeSection() {
    long dir1 = RandomUtils.nextInt(1, 100);
    long dir2 = RandomUtils.nextInt(1, 100);
    long dir3 = RandomUtils.nextInt(1, 100);
    long dir4 = RandomUtils.nextInt(1, 100);
    long dir5 = RandomUtils.nextInt(1, 100);
    return String.format("%s/%s/%s/%s/%s", dir1, dir2, dir3, dir4, dir5);
  }

  public static String randomTempName() {
    return "" + System.currentTimeMillis() + RandomStringUtils.random(4);
  }

  public static boolean isDirectory(CloudFilePo po) {
    return StringUtils.isBlank(po.getMd5());
  }

  public static String md5(File file) throws Exception {
    String r = null;
    InputStream is = new BufferedInputStream(new FileInputStream(file));
    r = DigestUtils.md5DigestAsHex(is);
    is.close();
    return r;
  }

  public static String mime(File file) throws Exception {
    String r = null;
    InputStream is = new BufferedInputStream(new FileInputStream(file));
    r = URLConnection.guessContentTypeFromStream(is);
    is.close();
    return r;
  }
}
