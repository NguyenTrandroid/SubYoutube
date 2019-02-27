package ictandroid.youtube.com.Profile;

public class ItemHistory {
    String name;
    String linkAvatar;
    String sub;
    String channelid;

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public ItemHistory(String channelid,String name, String linkAvatar, String sub) {
        this.channelid=channelid;
        this.name = name;
        this.linkAvatar = linkAvatar;
        this.sub = sub;
    }

    public String getChannelid() {
        return channelid;
    }

    public void setChannelid(String channelid) {
        this.channelid = channelid;
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
