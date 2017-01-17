package im.dadoo.cloudpan.backend.dao;


import im.dadoo.cloudpan.backend.po.UserPo;
import org.springframework.data.repository.CrudRepository;

public interface UserDao extends CrudRepository<UserPo, Long> {

  UserPo findByPhone(String phone);

  UserPo findByToken(String token);
}