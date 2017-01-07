package im.dadoo.cloudpan.backend.dao;

import im.dadoo.cloudpan.backend.dao.mapper.UserMapper;
import im.dadoo.cloudpan.backend.po.UserPo;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * Created by codekitten on 2017/1/7.
 */
@Repository
public class UserDao {

  private static final String FIND_BY_TOKEN_SQL =
      "SELECT * FROM t_user WHERE token=:token LIMIT 1";

  @Resource
  private NamedParameterJdbcTemplate jdbcTemplate;

  @Resource
  private UserMapper userMapper;

  public UserPo findByToken(String token) {
    UserPo r = null;
    MapSqlParameterSource sps = new MapSqlParameterSource();
    sps.addValue("token", token);
    try {
      r = this.jdbcTemplate.queryForObject(FIND_BY_TOKEN_SQL, sps, this.userMapper);
    } catch (Exception e) {}
    return r;
  }
}
