package com.example.ecommerce;

public class Category  {
    private String Name;
    private int ImageResId;

    public Category(int ImageResId, String name) {
        this.ImageResId = ImageResId;
        this.Name = name;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getImageResId() {
        return ImageResId;
    }

    public void setImageResId(int imageResId) {
        ImageResId = imageResId;
    }
}
