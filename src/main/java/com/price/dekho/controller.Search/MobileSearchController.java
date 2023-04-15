package com.price.dekho.controller.Search;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
public class MobileSearchController {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    private static final String API_URL = "http://20.100.169.29:8000/v12/amazon/%s?page=%d&page_size=%d";
    private static final String PRODUCT_DATA_API_URL = "http://20.100.169.29:8000/product-data";


    private final RestTemplate restTemplate = new RestTemplate();

//    @GetMapping("/amazon/{search_query}")
//    public List<ProductData> searchAmazonProducts(@PathVariable String search_query,
//                                                  @RequestParam(defaultValue = "1") int page,
//                                                  @RequestParam(defaultValue = "5") int page_size) {
//        String url = String.format(API_URL, search_query, page, page_size);
//        ResponseEntity<List<String>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<String>>() {});
//        List<String>listOfUrl=response.getBody();
//        System.out.println("size is "+listOfUrl.size());
//
//        List<CompletableFuture<ProductData>> futures = new ArrayList<>();
//        for(String link : listOfUrl){
//            CompletableFuture<ProductData> future = CompletableFuture.supplyAsync(() -> callProductDataApi(link));
//            futures.add(future);
//        }
//
//        return futures.stream().map(CompletableFuture::join).collect(Collectors.toList());
//    }
//
//    private ProductData callProductDataApi(String link) {
//        RestTemplate restTemplate = new RestTemplate();
//        ProductDataRequest request = new ProductDataRequest(link);
//        return restTemplate.postForObject(PRODUCT_DATA_API_URL, request, String.class);
//    }

    @GetMapping("/scrape")
    public String scrapeWebsite(@RequestParam("url") String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements elements = doc.select("a.a-link-normal.s-no-outline");
        for (Element element : elements) {
            Element aTag = element.getElementsByTag("a").first();
            String hrefValue = aTag.attr("href");

            Document document= Jsoup.connect("https://www.amazon.in/"+hrefValue).get();
            Element elem = document.selectFirst("span.a-size-large product-title-word-break");
            while (elem ==null){
                 document= Jsoup.connect("https://www.amazon.in/"+hrefValue).get();
                 elem = document.selectFirst("span.a-size-large product-title-word-break");

                 if (elem !=null){
                     System.out.println(elem);
                 }
            }
            System.out.println(elem);
        }
        return null;
    }


}