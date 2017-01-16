package im.dadoo.cloudpan.backend.dto;

/**
 * Created by codekitten on 2017/1/16.
 */
public class UserDto {

  private long id;

  private long gmtCreate;

  private String name;

  private String phone;

  private String token;

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("UserDto{");
    sb.append("id=").append(id);
    sb.append(", gmtCreate=").append(gmtCreate);
    sb.append(", name='").append(name).append('\'');
    sb.append(", phone='").append(phone).append('\'');
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

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}
