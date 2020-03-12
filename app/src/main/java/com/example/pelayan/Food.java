package com.example.pelayan;

public class Food {
    public int id;
    public String name;
    public String harga;
    public String image;

    public Food(){
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getHarga(){
        return harga;
    }

    public void setHarga(String harga){
        this.harga = harga;
    }

    public String getImage(){
        return image;
    }

    public void setImage(String image){
        this.image = image;
    }

}
