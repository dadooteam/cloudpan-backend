package im.dadoo.cloudpan.backend.bo;

import im.dadoo.cloudpan.backend.constant.CloudpanConstant;
import im.dadoo.cloudpan.backend.dto.FileDto;
import im.dadoo.cloudpan.backend.dto.UserDto;
import im.dadoo.cloudpan.backend.po.UserPo;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by codekitten on 2017/1/7.
 */
@Component
public class ConverterBo {

  private static final Logger MLOGGER = LoggerFactory.getLogger(ConverterBo.class);
  private static final Logger ELOGGER = LoggerFactory.getLogger(Exception.class);

  @Resource
  private Environment env;

  @Resource
  private ExecutorService executor;

  public FileDto toFileDto(File file) {
    FileDto r = null;
    if (file != null) {
      try {
        FileDto temp = new FileDto();
        temp.setGmtModify(file.lastModified());
        temp.setName(file.getName());
        temp.setPath(StringUtils.removePattern(StringUtils.replace(file.getPath(), "\\", "/"),
            String.format("^%s/[0-9]+/", this.env.getProperty("master.path"))));
        if (file.isDirectory()) {
          temp.setMime("");
          temp.setSize(0L);
          temp.setType(CloudpanConstant.TYPE_DIR);
        } else {
          try (InputStream is = new BufferedInputStream(FileUtils.openInputStream(file))) {
            temp.setMime(URLConnection.guessContentTypeFromStream(is));
            if (StringUtils.startsWith(temp.getMime(), "image/")) {
              try (ByteArrayOutputStream os = new ByteArrayOutputStream(1024)){
                Thumbnails.of(is).size(50, 50).outputQuality(0.5).outputFormat("jpg").toOutputStream(os);
                String code = Base64.encodeBase64URLSafeString(os.toByteArray());
                temp.setThumbnail(String.format("data:image/jpg;base64,%s", code));
              }
            }
          }
          temp.setSize(FileUtils.sizeOf(file));
          temp.setType(CloudpanConstant.TYPE_FILE);
        }
        r = temp;
      } catch (Exception e) {
        MLOGGER.error("toFileDto异常", e);
        ELOGGER.error("toFileDto异常", e);
      }
    }
    return r;
  }

  public List<FileDto> toFileDtos(List<File> files) {
    List<FileDto> r = new ArrayList<>();
    if (!CollectionUtils.isEmpty(files)) {
      r = files.stream().filter(file -> file != null).map(file -> this.executor.submit(() -> this.toFileDto(file)))
          .collect(Collectors.toList()).stream().map(future -> {
            FileDto dto = null;
            try {
              dto = future.get(10, TimeUnit.SECONDS);
            } catch (Exception e) {
              ELOGGER.error("获取文件信息超时", e);
            }
            return dto;
          }).filter(dto -> dto != null).collect(Collectors.toList());
    }
    return r;
  }

  public UserDto toUserDto(UserPo po) {
    UserDto r = null;
    if (po != null) {
      try {
        r = new UserDto();
        r.setId(po.getId());
        r.setGmtCreate(po.getGmtCreate());
        r.setName(po.getName());
        r.setPhone(po.getPhone());
        r.setToken(po.getToken());
      } catch (Exception e) {
        MLOGGER.error("toUserDto异常", e);
        ELOGGER.error("toUserDto异常", e);
      }
    }
    return r;
  }
}
