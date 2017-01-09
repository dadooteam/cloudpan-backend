package im.dadoo.cloudpan.backend.bo;

import im.dadoo.cloudpan.backend.common.constant.CloudpanConstant;
import im.dadoo.cloudpan.backend.common.dto.FileDto;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.File;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
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

  public FileDto toFileDto(File file) {
    FileDto r = null;
    if (file != null) {
      try {
        FileDto temp = new FileDto();
        temp.setGmtModify(file.lastModified());
        temp.setName(file.getName());
        temp.setPath(file.getPath());
        if (file.isDirectory()) {
          temp.setMime("");
          temp.setSize(0L);
          temp.setType(CloudpanConstant.TYPE_DIR);
        } else {
          temp.setMime(URLConnection.guessContentTypeFromStream(FileUtils.openInputStream(file)));
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
      r = files.stream().filter(file -> file != null).map(file -> this.toFileDto(file)).collect(Collectors.toList());
    }
    return r;
  }
}
