package im.dadoo.cloudpan.backend.so;

import im.dadoo.cloudpan.backend.bo.ConverterBo;
import im.dadoo.cloudpan.backend.bo.FileBo;
import im.dadoo.cloudpan.backend.common.constant.CloudpanCode;
import im.dadoo.cloudpan.backend.common.constant.CloudpanConstant;
import im.dadoo.cloudpan.backend.common.dto.FileDto;
import im.dadoo.cloudpan.backend.common.dto.Result;
import im.dadoo.cloudpan.backend.dao.CloudFileDao;
import im.dadoo.cloudpan.backend.po.CloudFilePo;
import im.dadoo.cloudpan.backend.util.CloudpanUtil;
import org.apache.commons.fileupload.util.mime.MimeUtility;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.utils.HttpClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.List;

/**
 * Created by codekitten on 2017/1/7.
 */
@Service
public class CloudFileSo {

  private static final Logger MLOGGER = LoggerFactory.getLogger(CloudFileSo.class);
  private static final Logger ELOGGER = LoggerFactory.getLogger(Exception.class);

  @Resource
  private Environment env;

  @Resource
  private ConverterBo converterBo;

  @Resource
  private CloudFileDao cloudFileDao;

  @Resource
  private FileBo fileBo;

  public Result<FileDto> insert(long userId, String path, MultipartFile file) {
    Result<FileDto> r = new Result<>();
    r.setCode(CloudpanCode.NONAME_ERROR.getCode());
    try {
      Assert.isTrue(!file.isEmpty());
      String newPath = String.format("%s/%d/%s/%s", this.env.getProperty("master.path"), userId, path, file.getOriginalFilename());
      File newFile = new File(newPath);
      //必须文件不存在才能上传
      if (!newFile.exists()) {
        //强制创建文件夹
        FileUtils.forceMkdir(newFile.getParentFile());
        //创建新文件
        if (newFile.createNewFile()) {
          file.transferTo(newFile);
        }
        FileDto fileDto = new FileDto();
        fileDto.setGmtModify(newFile.lastModified());
        fileDto.setName(newFile.getName());
        fileDto.setSize(newFile.length());
        fileDto.setPath(newFile.getAbsolutePath());
        fileDto.setType(CloudpanConstant.TYPE_FILE);
        InputStream is = FileUtils.openInputStream(newFile);
        fileDto.setMime(URLConnection.guessContentTypeFromStream(is));
        IOUtils.closeQuietly(is);
        r.setData(fileDto);
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

  public Result<FileDto> insert(long userId, String path, String name) {
    Result<FileDto> r = new Result<>();
    r.setCode(CloudpanCode.NONAME_ERROR.getCode());
    try {
      String newPath = String.format("%s/%d/%s/%s", this.env.getProperty("master.path"), userId, path, name);
      File newFile = new File(newPath);new File()
      //必须文件不存在才能上传
      if (!newFile.exists()) {
        //强制创建文件夹
        FileUtils.forceMkdir(newFile);
        FileDto fileDto = new FileDto();
        fileDto.setGmtModify(newFile.lastModified());
        fileDto.setName(newFile.getName());
        fileDto.setSize(FileUtils.sizeOfDirectory(newFile));
        fileDto.setPath(newFile.getAbsolutePath());
        fileDto.setType(CloudpanConstant.TYPE_DIR);
        fileDto.setMime("");
        r.setData(fileDto);
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

  public Result<Object> deleteById(long userId, long id) {
    Result<Object> r = new Result<>();
    r.setCode(CloudpanCode.NONAME_ERROR.getCode());
    try {
      CloudFilePo po = this.cloudFileDao.findById(id);
      if (po != null) {
        //必须在本人文件夹内操作
        if (po.getUserId() != userId) {
          r.setCode(CloudpanCode.FORBIDDEN.getCode());
          throw new Exception(CloudpanCode.FORBIDDEN.getName());
        }
        if (CloudpanUtil.isDirectory(po)) {
          //如果是文件夹，则递归删除
          List<CloudFilePo> pos = this.cloudFileDao.pageBySupId(id, 1L, Long.MAX_VALUE);
          pos.stream().filter(sub -> sub != null).forEach(sub -> this.deleteById(userId, sub.getId()));
        } else {
          //不是文件夹，删除当前文件
          //如果文件存在共用情况，则不删除实体文件
          List<CloudFilePo> pos = this.cloudFileDao.listByMd5(po.getMd5());
          if (pos.size() < 2) {
            this.fileBo.delete(po);
          }
        }
        this.cloudFileDao.deleteById(id);
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

  public Result<List<CloudFilePo>> pageBySupId(long userId, long supId, long pageCount, long pageSize) {
    Result<List<CloudFilePo>> r = new Result<>();
    r.setCode(CloudpanCode.NONAME_ERROR.getCode());
    try {
      CloudFilePo sup = this.cloudFileDao.findById(supId);
      if (sup == null) {
        r.setCode(CloudpanCode.FORBIDDEN.getCode());
        throw new Exception(CloudpanCode.FORBIDDEN.getName());
      }
      if (sup.getUserId() != userId) {
        r.setCode(CloudpanCode.FORBIDDEN.getCode());
        throw new Exception(CloudpanCode.FORBIDDEN.getName());
      }
      r.setData(this.cloudFileDao.pageBySupId(supId, pageCount, pageSize));
      r.setCode(CloudpanCode.OK.getCode());
    } catch (Exception e) {
      r.setMessage(e.getLocalizedMessage());
      MLOGGER.error(String.format("%d:%s", r.getCode(), e.getLocalizedMessage()));
      ELOGGER.error(Long.toString(r.getCode()), e);
    }
    r.setStatus(r.getCode() / 1_000_000);
    return r;
  }


}
