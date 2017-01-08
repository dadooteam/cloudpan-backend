package im.dadoo.cloudpan.backend.dao.mapper;

import im.dadoo.cloudpan.backend.po.CloudFilePo;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by codekitten on 2017/1/7.
 */
@Component
public class CloudFileMapper implements RowMapper<CloudFilePo> {

  @Override
  public CloudFilePo mapRow(ResultSet rs, int i) throws SQLException {
    CloudFilePo po = new CloudFilePo();
    po.setId(rs.getLong("id"));
    po.setGmtCreate(rs.getLong("gmt_create"));
    po.setGmtModify(rs.getLong("gmt_modify"));
    po.setUserId(rs.getLong("user_id"));
    po.setSupId(rs.getLong("sup_id"));
    po.setName(rs.getString("name"));
    po.setMime(rs.getString("mime"));
    po.setSection(rs.getString("section"));
    po.setSize(rs.getLong("size"));
    po.setMd5(rs.getString("md5"));

    return po;
  }

}
