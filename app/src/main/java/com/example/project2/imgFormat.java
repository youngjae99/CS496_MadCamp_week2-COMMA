package com.example.project2;

public class imgFormat {
    String imgPath;
    private boolean selected;

    public imgFormat(String imgPath, boolean selected) {
        this.imgPath = imgPath;
        this.selected = selected;
    }
    public String getImgPath() {
        return imgPath;
    }
    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
