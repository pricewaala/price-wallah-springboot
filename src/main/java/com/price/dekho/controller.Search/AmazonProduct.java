package com.price.dekho.controller.Search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AmazonProduct {
    public String title;
    public String url;
    public String price;
    public String image;
}
