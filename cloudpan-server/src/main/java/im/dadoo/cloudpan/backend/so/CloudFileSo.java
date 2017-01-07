package im.dadoo.cloudpan.backend.so;

import im.dadoo.cloudpan.backend.common.constant.CloudpanCode;
import im.dadoo.cloudpan.backend.common.dto.Result;
import im.dadoo.cloudpan.backend.dao.CloudFileDao;
import im.dadoo.cloudpan.backend.po.CloudFilePo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.Calendar;

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
  private CloudFileDao cloudFileDao;

  /**
   * 创建文件夹
   * @param userId 用户id
   * @param supId 上级文件夹id
   * @param name
   * @return
   */
  public Result<CloudFilePo> insert(long userId, long supId, String name) {
    Result<CloudFilePo> r = new Result<>();
    r.setCode(CloudpanCode.OK.getCode());
    try {
      CloudFilePo sup = this.cloudFileDao.findById(supId);
      //sup为空，或者sup不是文件夹，都不允许
      if (sup == null || StringUtils.isNotBlank(sup.getMd5())) {
        r.setCode(CloudpanCode.SUP_NOT_FOUND.getCode());
        throw new Exception(CloudpanCode.SUP_NOT_FOUND.getName());
      }
      if (sup.getUserId() != userId) {
        r.setCode(CloudpanCode.FORBIDDEN.getCode());
        throw new Exception(CloudpanCode.FORBIDDEN.getName());
      }
      CloudFilePo temp = this.cloudFileDao.findBySupIdAndName(supId, name);
      if (temp != null) {
        r.setCode(CloudpanCode.NAME_EXIST.getCode());
        throw new Exception(CloudpanCode.NAME_EXIST.getName());
      }
      CloudFilePo po = new CloudFilePo();
      po.setGmtCreate(System.currentTimeMillis());
      po.setGmtModify(po.getGmtCreate());
      po.setUserId(userId);
      po.setSupId(supId);
      po.setName(name);
      po.setMime("");
      po.setSize(0L);
      po.setMd5("");
      long id = this.cloudFileDao.insert(po);

      r.setData(this.cloudFileDao.findById(id));
      r.setCode(CloudpanCode.OK.getCode());
    } catch (Exception e) {
      r.setMessage(e.getLocalizedMessage());
      MLOGGER.error(String.format("%d:%s", r.getCode(), e.getLocalizedMessage()));
      ELOGGER.error(Long.toString(r.getCode()), e);
    }
    r.setStatus(r.getCode() / 1_000_000);
    return r;
  }

  public Result<CloudFilePo> insert(long userId, long supId, MultipartFile file) {
    Result<CloudFilePo> r = new Result<>();
    r.setCode(CloudpanCode.OK.getCode());
    try {
      CloudFilePo sup = this.cloudFileDao.findById(supId);
      if (sup == null || StringUtils.isNotBlank(sup.getMd5())) {
        r.setCode(CloudpanCode.SUP_NOT_FOUND.getCode());
        throw new Exception(CloudpanCode.SUP_NOT_FOUND.getName());
      }
      if (sup.getUserId() != userId) {
        r.setCode(CloudpanCode.FORBIDDEN.getCode());
        throw new Exception(CloudpanCode.FORBIDDEN.getName());
      }
      CloudFilePo temp = this.cloudFileDao.findBySupIdAndName(supId, file.getOriginalFilename());
      if (temp != null) {
        r.setCode(CloudpanCode.NAME_EXIST.getCode());
        throw new Exception(CloudpanCode.NAME_EXIST.getName());
      }

      CloudFilePo po = new CloudFilePo();
      po.setGmtCreate(System.currentTimeMillis());
      po.setGmtModify(po.getGmtCreate());
      po.setUserId(userId);
      po.setSupId(supId);
      po.setName(file.getOriginalFilename());
      po.setMime();
      po.setSize(0L);
      po.setMd5("");
      long id = this.cloudFileDao.insert(po);

      r.setData(this.cloudFileDao.findById(id));
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
