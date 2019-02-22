package ictandroid.youtube.com.Campaign;

public class ItemChanel {
    private String linkIcon;
    private String nameChanel;
    private String soLuotSub;
    private String idChanel;
    private String time;
    private String doUuTien;
    private String diem;

    public ItemChanel(String linkIcon, String nameChanel, String soLuotSub, String idChanel, String time, String doUuTien, String diem) {
        this.linkIcon = linkIcon;
        this.nameChanel = nameChanel;
        this.soLuotSub = soLuotSub;
        this.idChanel = idChanel;
        this.time = time;
        this.doUuTien = doUuTien;
        this.diem = diem;
    }

    public ItemChanel() {
        soLuotSub = "0";
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

    public String getIdChanel() {
        return idChanel;
    }

    public void setIdChanel(String idChanel) {
        this.idChanel = idChanel;
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
}
