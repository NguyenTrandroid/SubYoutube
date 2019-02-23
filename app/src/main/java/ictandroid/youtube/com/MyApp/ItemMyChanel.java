package ictandroid.youtube.com.MyApp;

public class ItemMyChanel {
    private String linkIcon;
    private String nameChanel;
    private String soLuotSub;
    private String diem;
    private String chanelId;

    public String getLinkIcon() {
        return linkIcon;
    }

    public void setLinkIcon(String linkIcon) {
        this.linkIcon = linkIcon;
    }

    public String getNameChanel() {
        return nameChanel;
    }

    public void setNameChanel(String nameChanel) {
        this.nameChanel = nameChanel;
    }

    public String getSoLuotSub() {
        return soLuotSub;
    }

    public void setSoLuotSub(String soLuotSub) {
        this.soLuotSub = soLuotSub;
    }

    public String getDiem() {
        return diem;
    }

    public void setDiem(String diem) {
        this.diem = diem;
    }

    public String getChanelId() {
        return chanelId;
    }

    public void setChanelId(String chanelId) {
        this.chanelId = chanelId;
    }

    public ItemMyChanel(String linkIcon, String nameChanel, String soLuotSub, String diem, String chanelId) {
        this.linkIcon = linkIcon;
        this.nameChanel = nameChanel;
        this.soLuotSub = soLuotSub;
        this.diem = diem;
        this.chanelId = chanelId;
    }

    public ItemMyChanel() {
    }
}
