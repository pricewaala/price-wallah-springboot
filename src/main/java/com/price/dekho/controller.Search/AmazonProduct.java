package com.price.dekho.controller.Search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AmazonProduct {
    private String title;
    private String url;
    private String price;
    private String image;
    private ArrayList<String> description;

}
