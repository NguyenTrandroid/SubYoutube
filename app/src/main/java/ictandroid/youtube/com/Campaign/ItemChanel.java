package ictandroid.youtube.com.Campaign;

public class ItemChanel {
    private String linkIcon;
    private String nameChanel;
    private String soLuotSub;
    private String userId;
    private String time;
    private String doUuTien;
    private String diem;
    private String chanelId;

    public ItemChanel() {
    }

    public ItemChanel(String linkIcon, String nameChanel, String soLuotSub, String userId, String time, String doUuTien, String diem, String chanelId) {
        this.linkIcon = linkIcon;
        this.nameChanel = nameChanel;
        this.soLuotSub = soLuotSub;
        this.userId = userId;
        this.time = time;
        this.doUuTien = doUuTien;
        this.diem = diem;
        this.chanelId = chanelId;
    }

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDoUuTien() {
        return doUuTien;
    }

    public void setDoUuTien(String doUuTien) {
        this.doUuTien = doUuTien;
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
}
