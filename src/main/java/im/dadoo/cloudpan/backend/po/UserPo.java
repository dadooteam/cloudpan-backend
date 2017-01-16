package im.dadoo.cloudpan.backend.po;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by codekitten on 2017/1/7.
 */
@Entity(name = "User")
@Table(name = "t_user", schema = "cloudpan")
public class UserPo implements Serializable {

  @Id
  @GeneratedValue
  private long id;

  @Column(name = "gmt_create", nullable = false)
  private long gmtCreate;

  @Column(name = "gmt_modify", nullable = false)
  private long gmtModify;

  @Column(name = "name", nullable = false, length = 20, unique = true)
  private String name;

  @Column(name = "phone", nullable = false, length = 20, unique = true)
  private String phone;

  @Column(name = "password", nullable = false, length = 30)
  private String password;

  @Column(name = "token", nullable = false, length = 64)
  private String token;

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("UserPo{");
    sb.append("id=").append(id);
    sb.append(", gmtCreate=").append(gmtCreate);
    sb.append(", gmtModify=").append(gmtModify);
    sb.append(", name='").append(name).append('\'');
    sb.append(", phone='").append(phone).append('\'');
    sb.append(", password='").append(password).append('\'');
    sb.append(", token='").append(token).append('\'');
    sb.append('}');
    return sb.toString();
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public long getGmtCreate() {
    return gmtCreate;
  }

  public void setGmtCreate(long gmtCreate) {
    this.gmtCreate = gmtCreate;
  }

  public long getGmtModify() {
    return gmtModify;
  }

  public void setGmtModify(long gmtModify) {
    this.gmtModify = gmtModify;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}
