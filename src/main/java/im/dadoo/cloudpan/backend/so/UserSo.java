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

  public Result<UserDto> login(String name, String password) {
    Result<UserDto> r = new Result<>();
    r.setCode(CloudpanCode.NONAME_ERROR.getCode());
    try {
      UserPo po = this.userDao.findByName(name);
      if (StringUtils.equals(po.getName(), password)) {
        long current = System.currentTimeMillis();
        String token = DigestUtils.md5Hex(String.format("%d:%d:%d", po.getId(), current, RandomUtils.nextInt(0, 100)));
        po.setGmtExpire(current + 30*24*60*60*1000);
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
}
