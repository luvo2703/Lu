package com.example.duan_oder_doan.model;

public class SanPham_Favorite {
    private String img_product;
    private String name_product;
    private String price_product;
    private String note_product;

    public SanPham_Favorite() {
    }

    public SanPham_Favorite( String img_product, String name_product, String price_product, String note_product) {
        this.img_product = img_product;
        this.name_product = name_product;
        this.price_product = price_product;
        this.note_product = note_product;
    }


    public String getImg_product() {
        return img_product;
    }

    public void setImg_product(String img_product) {
        this.img_product = img_product;
    }

    public String getName_product() {
        return name_product;
    }

    public void setName_product(String name_product) {
        this.name_product = name_product;
    }

    public String getPrice_product() {
        return price_product;
    }

    public void setPrice_product(String price_product) {
        this.price_product = price_product;
    }

    public String getNote_product() {
        return note_product;
    }

    public void setNote_product(String note_product) {
        this.note_product = note_product;
    }
}
