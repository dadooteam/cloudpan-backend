package im.dadoo.cloudpan.backend.so;

import im.dadoo.cloudpan.backend.bo.ConverterBo;
import im.dadoo.cloudpan.backend.constant.CloudpanCode;
import im.dadoo.cloudpan.backend.dao.UserDao;
import im.dadoo.cloudpan.backend.dto.Result;
import im.dadoo.cloudpan.backend.dto.UserDto;
import im.dadoo.cloudpan.backend.po.UserPo;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by codekitten on 2017/1/16.
 */
@Service
public class UserSo {

  private static final Logger MLOGGER = LoggerFactory.getLogger(UserSo.class);
  private static final Logger ELOGGER = LoggerFactory.getLogger(Exception.class);

  @Autowired
  private UserDao userDao;

  @Autowired
  private ConverterBo converterBo;

  public Result<UserDto> login(String phone, String password) {
    Result<UserDto> r = new Result<>();
    r.setCode(CloudpanCode.NONAME_ERROR.getCode());
    try {
      UserPo po = this.userDao.findByPhone(phone);
      if (StringUtils.equals(po.getPassword(), password)) {
        long current = System.currentTimeMillis();
        String token = DigestUtils.md5Hex(String.format("%d:%d:%d", po.getId(), current, RandomUtils.nextInt(0, 100)));
        long gmtExpire = current + 2592000000L;
        po.setGmtExpire(gmtExpire);
        po.setToken(token);
        po = this.userDao.save(po);
        r.setData(this.converterBo.toUserDto(po));
        r.setCode(CloudpanCode.OK.getCode());
      }
    } catch (Exception e) {
      r.setMessage(e.getLocalizedMessage());
      MLOGGER.error(String.format("%d:%s", r.getCode(), e.getLocalizedMessage()));
      ELOGGER.error(Long.toString(r.getCode()), e);
    }
    r.setStatus(r.getCode() / 1_000_000);
    return r;
  }

  public Result<UserDto> auth(String token) {
    Result<UserDto> r = new Result<>();
    r.setCode(CloudpanCode.NONAME_ERROR.getCode());
    try {
      UserPo userPo = this.userDao.findByToken(token);
      if (userPo != null) {
        long gmtCurrent = System.currentTimeMillis();
        if (gmtCurrent > userPo.getGmtExpire()) {
          r = this.login(userPo.getName(), userPo.getPassword());
        } else {
          r.setData(this.converterBo.toUserDto(userPo));
        }
        r.setCode(CloudpanCode.OK.getCode());
      } else {
        r.setMessage("token已过期");
        r.setCode(CloudpanCode.FORBIDDEN.getCode());
      }
    } catch (Exception e) {
      r.setMessage(e.getLocalizedMessage());
      MLOGGER.error(String.format("%d:%s", r.getCode(), e.getLocalizedMessage()));
      ELOGGER.error(Long.toString(r.getCode()), e);
    }
    r.setStatus(r.getCode() / 1_000_000);
    return r;
  }

  public Result<UserDto> insert(String phone, String password) {
    Result<UserDto> r = new Result<>();
    r.setCode(CloudpanCode.NONAME_ERROR.getCode());
    try {
      UserPo po = new UserPo();
      po.setGmtCreate(System.currentTimeMillis());
      po.setGmtModify(po.getGmtCreate());
      po.setName(phone);
      po.setPhone(phone);
      po.setPassword(password);
      po.setToken("");
      po = this.userDao.save(po);
      if (po != null) {
        r = this.login(phone, password);
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
}
