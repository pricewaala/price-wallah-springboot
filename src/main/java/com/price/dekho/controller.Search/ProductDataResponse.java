package com.price.dekho.controller.Search;

public class ProductDataResponse {
    private ProductData productData;

    public ProductDataResponse() {}

    public ProductDataResponse(ProductData productData) {
        this.productData = productData;
    }

    public ProductData getProductData() {
        return productData;
    }

    public void setProductData(ProductData productData) {
        this.productData = productData;
    }
}
