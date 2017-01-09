//package im.dadoo.cloudpan.backend.dao.mapper;
//
//import im.dadoo.cloudpan.backend.po.UserPo;
//import org.springframework.jdbc.core.RowMapper;
//import org.springframework.stereotype.Component;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
///**
// * Created by codekitten on 2017/1/7.
// */
//@Component
//public class UserMapper implements RowMapper<UserPo> {
//
//  @Override
//  public UserPo mapRow(ResultSet rs, int i) throws SQLException {
//    UserPo po = new UserPo();
//    po.setId(rs.getLong("id"));
//    po.setGmtCreate(rs.getLong("gmt_create"));
//    po.setGmtModify(rs.getLong("gmt_modify"));
//    po.setName(rs.getString("name"));
//    po.setPhone(rs.getString("phone"));
//    po.setPassword(rs.getString("password"));
//    po.setToken(rs.getString("token"));
//
//    return po;
//  }
//
//}
