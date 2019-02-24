package ictandroid.youtube.com.Profile;

public class ItemHistory {
    String name;
    String linkAvatar;
    String sub;

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public ItemHistory(String name, String linkAvatar, String sub) {
        this.name = name;
        this.linkAvatar = linkAvatar;
        this.sub = sub;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLinkAvatar() {
        return linkAvatar;
    }

    public void setLinkAvatar(String linkAvatar) {
        this.linkAvatar = linkAvatar;
    }
}
