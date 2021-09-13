package com.example.do_i_need_it.Models;

public class Products {

    private String id;
    private String name;
    private String price;
    private String uri;

    public Products() {

    }

    public Products(String id, String name, String price, String uri) {

        this.id = id;
        this.name = name;
        this.uri = uri;
        this.price = price;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrice(){
        return price;
    }

    public void setPrice(String price){
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
