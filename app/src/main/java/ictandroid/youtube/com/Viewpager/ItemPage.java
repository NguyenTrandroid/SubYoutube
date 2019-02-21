package ictandroid.youtube.com.Viewpager;

public class ItemPage {
    int drawbale;
    String txtName;
    String content;

    public ItemPage(int drawbale, String txtName, String content) {
        this.drawbale = drawbale;
        this.txtName = txtName;
        this.content = content;
    }

    public int getDrawbale() {
        return drawbale;
    }

    public void setDrawbale(int drawbale) {
        this.drawbale = drawbale;
    }

    public String getTxtName() {
        return txtName;
    }

    public void setTxtName(String txtName) {
        this.txtName = txtName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
