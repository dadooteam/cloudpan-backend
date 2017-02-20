package im.dadoo.cloudpan.backend.so;

import im.dadoo.cloudpan.backend.bo.ConverterBo;
import im.dadoo.cloudpan.backend.constant.CloudpanCode;
import im.dadoo.cloudpan.backend.dto.FileDto;
import im.dadoo.cloudpan.backend.dto.Result;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;

/**
 * Created by codekitten on 2017/1/7.
 */
@Service
public class FileSo {

  private static final Logger MLOGGER = LoggerFactory.getLogger(FileSo.class);
  private static final Logger ELOGGER = LoggerFactory.getLogger(Exception.class);

  @Autowired
  private Environment env;

  @Autowired
  private ConverterBo converterBo;

  public Result<FileDto> upload(long userId, String path, MultipartFile file) {
    Result<FileDto> r = new Result<>();
    r.setCode(CloudpanCode.NONAME_ERROR.getCode());
    try {
      Assert.isTrue(!file.isEmpty());
      String newPath = String.format("%s/%d/%s", this.env.getProperty("master.path"), userId, file.getOriginalFilename());
      if (StringUtils.isNotBlank(path)) {
        newPath = String.format("%s/%d/%s/%s", this.env.getProperty("master.path"), userId, path, file.getOriginalFilename());
      }
      File newFile = new File(newPath);
      //必须文件不存在才能上传
      if (!newFile.exists()) {
        //强制创建文件夹
        FileUtils.forceMkdir(newFile.getParentFile());
        //创建新文件
        if (newFile.createNewFile()) {
          file.transferTo(newFile);
        }
        r.setData(this.converterBo.toFileDto(newFile, userId));
      } else {
        r.setCode(CloudpanCode.NAME_EXIST.getCode());
        throw new Exception(String.format("文件名%s已存在", file.getOriginalFilename()));
      }
      r.setCode(CloudpanCode.OK.getCode());
    } catch (Exception e) {
      r.setMessage(e.getLocalizedMessage());
      MLOGGER.error(String.format("%d:%s", r.getCode(), e.getLocalizedMessage()));
      ELOGGER.error(Long.toString(r.getCode()), e);
    }
    r.setStatus(r.getCode() / 1_000_000);
    return r;
  }

  public Result<FileDto> mkdir(long userId, String path, String name) {
    Result<FileDto> r = new Result<>();
    r.setCode(CloudpanCode.NONAME_ERROR.getCode());
    try {
      Assert.hasText(name);
      String newPath = String.format("%s/%d/%s", this.env.getProperty("master.path"), userId, name);
      if (StringUtils.isNotBlank(path)) {
        newPath = String.format("%s/%d/%s/%s", this.env.getProperty("master.path"), userId, path, name);
      }
      File newFile = new File(newPath);
      //必须文件不存在才能上传
      if (!newFile.exists()) {
        //强制创建文件夹
        FileUtils.forceMkdir(newFile);
        r.setData(this.converterBo.toFileDto(newFile, userId));
      } else {
        r.setCode(CloudpanCode.NAME_EXIST.getCode());
        throw new Exception(String.format("文件夹名%s已存在", name));
      }
      r.setCode(CloudpanCode.OK.getCode());
    } catch (Exception e) {
      r.setMessage(e.getLocalizedMessage());
      MLOGGER.error(String.format("%d:%s", r.getCode(), e.getLocalizedMessage()));
      ELOGGER.error(Long.toString(r.getCode()), e);
    }
    r.setStatus(r.getCode() / 1_000_000);
    return r;
  }

  public Result<Object> delete(long userId, String path) {
    Result<Object> r = new Result<>();
    r.setCode(CloudpanCode.NONAME_ERROR.getCode());
    try {
      String fullPath = String.format("%s/%d", this.env.getProperty("master.path"), userId);
      if (StringUtils.isNotBlank(path)) {
        fullPath = String.format("%s/%d/%s", this.env.getProperty("master.path"), userId, path);
      }
      File file = new File(fullPath);
      FileUtils.deleteQuietly(file);
      r.setCode(CloudpanCode.OK.getCode());
    } catch (Exception e) {
      r.setMessage(e.getLocalizedMessage());
      MLOGGER.error(String.format("%d:%s", r.getCode(), e.getLocalizedMessage()));
      ELOGGER.error(Long.toString(r.getCode()), e);
    }
    r.setStatus(r.getCode() / 1_000_000);
    return r;
  }

  public Result<List<FileDto>> list(long userId, String path) {
    Result<List<FileDto>> r = new Result<>();
    r.setCode(CloudpanCode.NONAME_ERROR.getCode());
    try {
      String fullPath = String.format("%s/%d", this.env.getProperty("master.path"), userId);
      if (StringUtils.isNotBlank(path)) {
        fullPath = String.format("%s/%d/%s", this.env.getProperty("master.path"), userId, path);
      }
      File file = new File(fullPath);
      if (file.isDirectory()) {
        r.setData(this.converterBo.toFileDtos(Arrays.asList(file.listFiles()), userId));
      }
      r.setCode(CloudpanCode.OK.getCode());
    } catch (Exception e) {
      r.setMessage(e.getLocalizedMessage());
      MLOGGER.error(String.format("%d:%s", r.getCode(), e.getLocalizedMessage()));
      ELOGGER.error(Long.toString(r.getCode()), e);
    }
    r.setStatus(r.getCode() / 1_000_000);
    return r;
  }

  public Result<String> preview(long userId, String path) {
    Result<String> r = new Result<>();
    r.setCode(CloudpanCode.NONAME_ERROR.getCode());
    try {
      String fullPath = String.format("%s/%d/%s", this.env.getProperty("master.path"), userId, path);
      File file = new File(fullPath);
      if (file.exists() && file.isFile()) {
        try (BufferedInputStream is = new BufferedInputStream(FileUtils.openInputStream(file))) {
          String mime = URLConnection.guessContentTypeFromStream(is);
          if (StringUtils.startsWith(mime, "image/")) {
            try (ByteArrayOutputStream os = new ByteArrayOutputStream(20480)){
              Thumbnails.of(is)
                  .scale(1.0)
                  .outputQuality(NumberUtils.toDouble(this.env.getProperty("preview.quality"), 0.5))
                  .outputFormat("jpg")
                  .toOutputStream(os);
              String base64 = Base64.encodeBase64String(os.toByteArray());
              r.setData(String.format("data:image/jpeg;base64,%s", base64));
              r.setCode(CloudpanCode.OK.getCode());
            }
          }
        }
      }
    } catch (Exception e) {
      r.setMessage(e.getLocalizedMessage());
      MLOGGER.error(String.format("%d:%s", r.getCode(), e.getLocalizedMessage()));
      ELOGGER.error(Long.toString(r.getCode()), e);
    }
    r.setStatus(r.getCode() / 1_000_000);
    return r;
  }

  public boolean thumbnail(long userId, String path, OutputStream os) {
    boolean r = false;
    try {
      String fullPath = String.format("%s/%d/%s", this.env.getProperty("master.path"), userId, path);
      File file = new File(fullPath);
      if (file.exists() && file.isFile()) {
        FileDto fileDto = this.converterBo.toFileDto(file, userId);
        if (StringUtils.startsWith(fileDto.getMime(), "image/")) {
          Thumbnails.of(file)
              .size(30, 30)
              .outputQuality(0.4)
              .outputFormat("jpg")
              .toOutputStream(os);
          r = true;
        }
      }
    } catch (Exception e) {

    }
    return r;
  }


}
