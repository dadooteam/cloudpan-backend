package im.dadoo.cloudpan.backend.so;

import im.dadoo.cloudpan.backend.dao.UserDao;
import im.dadoo.cloudpan.backend.dto.Result;
import im.dadoo.cloudpan.backend.po.UserPo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by codekitten on 2017/1/16.
 */
@Service
public class UserSo {

  @Autowired
  private UserDao userDao;

  public Result<String> login(String name, String password) {
    UserPo po = this.userDao.findByName(name);
    if (StringUtils.equals(po.getName(), password)) {
      return po.getToken();
    } else {
      return null;
    }
  }
}
