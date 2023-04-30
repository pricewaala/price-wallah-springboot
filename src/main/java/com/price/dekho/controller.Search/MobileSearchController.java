package com.price.dekho.controller.Search;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class MobileSearchController {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/amazon/scrape/v1")
    public List<AmazonProduct> scrapeWebsiteAmazon(@RequestParam("search") String search) throws IOException {
        String link = "https://www.amazon.in/s?k="+search;
        List<AmazonProduct> products = new ArrayList<>(1000);
        try {
            // Get the HTML content of the web page
            Document doc = Jsoup.connect(link).get();
            while (doc == null) {
                System.out.println("------------------------trying ------------------------------------");
                doc = Jsoup.connect(link).get();
            }

            Elements elements = doc.select("h2.a-size-mini.a-spacing-none.a-color-base.s-line-clamp-2");

            // Print the text content of each h2 tag to the console
            for (int i=0;i<elements.size();i++) {
                String title = null,url = null,price = null,image=null;
                AmazonProduct amazonProduct=new AmazonProduct();

                Element href = elements.get(i).selectFirst("a");
                title = href.text();

                url = "https://www.amazon.in"+href.attr("href");

                Elements elementPrice = doc.select("span.a-price-whole");
                amazonProduct.setPrice(elementPrice.get(i).text());
                int size=products.size();
                products.add(amazonProduct);

                Elements elementImage = doc.select("div.a-section.aok-relative.s-image-fixed-height");
                if(i>=elementImage.size())
                {
                     Element img=elementImage.get(0).selectFirst("img");
                    image = img.attr("src");
                }
                else {
                    Element img = elementImage.get(i).selectFirst("img");
                    image = img.attr("src");
                }
                amazonProduct.setImage(image);
                amazonProduct.setUrl(url);
                amazonProduct.setTitle(title);
                products.set(i,amazonProduct);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return products;
    }

//    @GetMapping("/scrape")
//    public String scrapeWebsite(@RequestParam("name") String name) throws IOException {
//            String link = "https://www.amazon.in/s?k=iphone+14&crid=25DXNV78CDVV8&sprefix=iphone+1%2Caps%2C573&ref=nb_sb_noss_2";
//            try {
//                // Get the HTML content of the web page
//                Document doc = Jsoup.connect(link).get();
//                while (doc == null) {
//                    System.out.println("------------------------trying ------------------------------------");
//                    doc = Jsoup.connect(link).get();
//                }
//
//                Elements elements = doc.select("h2.a-size-mini.a-spacing-none.a-color-base.s-line-clamp-2");
//
//
//                int count = 0;
//                // Print the text content of each h2 tag to the console
//                for (Element element : elements) {
//                    count++;
//                    Element href = element.selectFirst("a");
//                    String text = href.text();
//                    String url = href.attr("href");
//                    System.out.println(text + " - " + url);
//                    System.out.println(element.text());
//
//                }
//
//                Elements elements1=doc.select("span.a-price-whole");
//                for (Element element : elements1) {
//                    System.out.println(element.text());
//
//                }
//
//                System.out.println("Abhinav" + count);
//
//
//                // Print the HTML content to the console
////            System.out.println(doc.html());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        return "success";
//    }


}