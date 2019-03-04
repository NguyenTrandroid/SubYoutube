package ictandroid.youtube.com.Viewpager;

public class ItemPage {
    int drawbale;
    int txtName;
    int content;

    public ItemPage(int drawbale, int txtName, int content) {
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

    public int getTxtName() {
        return txtName;
    }

    public void setTxtName(int txtName) {
        this.txtName = txtName;
    }

    public int getContent() {
        return content;
    }

    public void setContent(int content) {
        this.content = content;
    }
}
