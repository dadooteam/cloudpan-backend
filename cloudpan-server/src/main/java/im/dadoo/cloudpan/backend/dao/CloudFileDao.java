package im.dadoo.cloudpan.backend.dao;

import im.dadoo.cloudpan.backend.dao.mapper.CloudFileMapper;
import im.dadoo.cloudpan.backend.po.CloudFilePo;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by codekitten on 2017/1/7.
 */
@Repository
public class CloudFileDao {

  private static final String INSERT_SQL =
      "INSERT INTO t_cloud_file(gmt_create,gmt_modify,user_id,sup_id,name,mime,section,size,md5) "
          + "VALUES(:gmt_create,:gmt_modify,:user_id,:sup_id,:name,:mime:,:section,:size,:md5)";

  private static final String UPDATE_BY_ID_SQL =
      "UPDATE t_cloud_file SET mime=:mime, size=:size, md5=:md5 WHERE id=:id";

  private static final String DELETE_BY_ID_SQL =
      "DELETE FROM t_cloud_file WHERE id=:id";

  private static final String FIND_BY_ID_SQL =
      "SELECT * FROM t_cloud_file WHERE id=:id LIMIT 1";

  private static final String FIND_BY_SUP_ID_AND_NAME_SQL =
      "SELECT * FROM t_cloud_file WHERE sup_id=:sup_id AND name=:name LIMIT 1";

  private static final String LIST_BY_MD5_SQL =
      "SELECT * FROM t_cloud_file WHERE md5=:md5 ORDER BY gmt_create DESC";

  private static final String PAGE_BY_SUP_ID_SQL =
      "SELECT * FROM t_cloud_file WHERE sup_id=:sup_id ORDER BY gmt_create DESC LIMIT :offset,:length";

  @Resource
  private NamedParameterJdbcTemplate jdbcTemplate;

  @Resource
  private CloudFileMapper cloudFileMapper;

  public long insert(CloudFilePo po) {
    KeyHolder holder = new GeneratedKeyHolder();
    MapSqlParameterSource sps = new MapSqlParameterSource();
    sps.addValue("gmt_create", po.getGmtCreate());
    sps.addValue("gmt_modify", po.getGmtModify());
    sps.addValue("user_id", po.getUserId());
    sps.addValue("sup_id", po.getSupId());
    sps.addValue("name", po.getName());
    sps.addValue("mime", po.getMime());
    sps.addValue("section", po.getSection());
    sps.addValue("size", po.getSize());
    sps.addValue("md5", po.getMd5());
    this.jdbcTemplate.update(INSERT_SQL, sps, holder);
    return holder.getKey().longValue();
  }

  public void updateById(CloudFilePo po) {
    MapSqlParameterSource sps = new MapSqlParameterSource();
    sps.addValue("id", po.getId());
    sps.addValue("mime", po.getMime());
    sps.addValue("size", po.getSize());
    sps.addValue("md5", po.getMd5());
    this.jdbcTemplate.update(UPDATE_BY_ID_SQL, sps);
  }

  public void deleteById(long id) {
    MapSqlParameterSource sps = new MapSqlParameterSource();
    sps.addValue("id", id);
    this.jdbcTemplate.update(DELETE_BY_ID_SQL, sps);
  }

  public CloudFilePo findById(long id) {
    CloudFilePo r = null;
    MapSqlParameterSource sps = new MapSqlParameterSource();
    sps.addValue("id", id);
    try {
      r = this.jdbcTemplate.queryForObject(FIND_BY_ID_SQL, sps, this.cloudFileMapper);
    } catch (Exception e) {}
    return r;
  }

  public CloudFilePo findBySupIdAndName(long supId, String name) {
    CloudFilePo r = null;
    MapSqlParameterSource sps = new MapSqlParameterSource();
    sps.addValue("sup_id", supId);
    sps.addValue("name", name);
    try {
      r = this.jdbcTemplate.queryForObject(FIND_BY_SUP_ID_AND_NAME_SQL, sps, this.cloudFileMapper);
    } catch (Exception e) {}
    return r;
  }

  public List<CloudFilePo> listByMd5(String md5) {
    MapSqlParameterSource sps = new MapSqlParameterSource();
    sps.addValue("md5", md5);
    return this.jdbcTemplate.query(LIST_BY_MD5_SQL, sps, this.cloudFileMapper);
  }

  public List<CloudFilePo> pageBySupId(long supId, long pageCount, long pageSize) {
    MapSqlParameterSource sps = new MapSqlParameterSource();
    sps.addValue("sup_id", supId);
    sps.addValue("offset", (pageCount - 1) * pageSize);
    sps.addValue("length", pageSize);
    return this.jdbcTemplate.query(PAGE_BY_SUP_ID_SQL, sps, this.cloudFileMapper);
  }

}
