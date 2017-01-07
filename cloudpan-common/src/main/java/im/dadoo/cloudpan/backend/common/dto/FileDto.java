package im.dadoo.cloudpan.backend.common.dto;

/**
 * Created by codekitten on 2016/12/27.
 */
public class FileDto {

    private String name;

    private boolean isDirectory;

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("FileDto{");
        sb.append("name='").append(name).append('\'');
        sb.append(", isDirectory=").append(isDirectory);
        sb.append('}');
        return sb.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public void setDirectory(boolean directory) {
        isDirectory = directory;
    }
}
