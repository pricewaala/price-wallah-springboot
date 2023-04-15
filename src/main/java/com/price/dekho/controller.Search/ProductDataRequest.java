package com.price.dekho.controller.Search;

public class ProductDataRequest {
    private String link;

    public ProductDataRequest(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
