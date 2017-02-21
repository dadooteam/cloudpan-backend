package im.dadoo.cloudpan.backend.util;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by codekitten on 2017/2/21.
 */
public final class MimeUtil {

  private MimeUtil() {}

  private static Map<String, String> map;

  static {
    map = new HashMap<>();
    map.put("html", "text/html");
    map.put("htm", "text/html");
    map.put("shtml", "text/html");
    map.put("css", "text/css");
    map.put("mml", "text/mathml");
    map.put("txt", "text/plain");
    map.put("wml", "text/vnd.sap.wml");

    map.put("gif", "image/gif");
    map.put("jpeg", "image/jpeg");
    map.put("jpg", "image/jpeg");
    map.put("png", "image/png");
    map.put("tif", "image/tiff");
    map.put("tiff", "image/tiff");
    map.put("ico", "image/x-icon");
    map.put("bmp", "image/x-ms-bmp");
    map.put("svg", "image/svg+xml");
    map.put("svgz", "image/svg+xml");
    map.put("webp", "image/webp");

    map.put("midi", "audio/midi");
    map.put("mid", "audio/midi");
    map.put("kar", "audio/midi");
    map.put("mp3", "audio/mpeg");
    map.put("ogg", "audio/ogg");
    map.put("m4a", "audio/x-m4a");

    map.put("3gpp", "video/3gpp");
    map.put("3gp", "video/3gp");
    map.put("mp4", "video/mp4");
    map.put("mpeg", "video/mpeg");
    map.put("mpg", "video/mpeg");
    map.put("mov", "video/quicktime");
    map.put("webm", "video/webm");
    map.put("mpeg", "video/mpeg");
    map.put("flv", "video/x-flv");
    map.put("m4v", "video/x-m4v");
    map.put("asf", "video/x-ms-asf");
    map.put("wmv", "video/x-ms-wmv");
    map.put("avi", "video/x-msvideo");

    map.put("js", "application/javascript");
    map.put("atom", "application/atom+xml");
    map.put("rss", "application/rss+xml");
    map.put("jar", "application/java-archive");
    map.put("war", "application/java-archive");
    map.put("ear", "application/java-archive");
    map.put("json", "application/json");
    map.put("doc", "application/msword");
    map.put("pdf", "application/pdf");
    map.put("ps", "application/postscript");
    map.put("rtf", "application/rtf");
    map.put("xls", "application/vnd.ms-excel");
    map.put("ppt", "application/vnd.ms-powerpoint");
    map.put("7z", "application/x-7z-compressed");
    map.put("rar", "application/x-rar-compressed");
    map.put("zip", "application/zip");
    map.put("swf", "application/x-shockwave-flash");
    map.put("xhtml", "application/xhtml+xml");
  }

  public static String getMime(String suffix) {
    String r = "application/octet-stream";
    if (StringUtils.isNotBlank(suffix)) {
      suffix = suffix.toLowerCase();
      if (map.containsKey(suffix)) {
        r = map.get(suffix);
      }
    }
    return r;
  }
}
