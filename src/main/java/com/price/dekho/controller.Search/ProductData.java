package com.price.dekho.controller.Search;

import java.util.List;

public class ProductData {
    private String name;
    private List<String> description;
    private String ratingStar;
    private String ratingCount;
    private float price;
    private String exchange;
    private List<String> image;
    private String link;

    public ProductData(){}

    public ProductData(String name, List<String> description, String ratingStar, String ratingCount, float price, String exchange, List<String> image, String link) {
        this.name = name;
        this.description = description;
        this.ratingStar = ratingStar;
        this.ratingCount = ratingCount;
        this.price = price;
        this.exchange = exchange;
        this.image = image;
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getDescription() {
        return description;
    }

    public void setDescription(List<String> description) {
        this.description = description;
    }

    public String getRatingStar() {
        return ratingStar;
    }

    public void setRatingStar(String ratingStar) {
        this.ratingStar = ratingStar;
    }

    public String getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(String ratingCount) {
        this.ratingCount = ratingCount;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}