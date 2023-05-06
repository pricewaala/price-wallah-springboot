package com.price.dekho.controller.Search;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
        String formattedSearch = search.replace(" ", "+");
        String link = "https://www.amazon.in/s?k=" + formattedSearch;
        List<AmazonProduct> products = new ArrayList<>(1000);
        try {
            // Create a HttpClient instance
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(link);
            // Execute the HTTP request and get the response
            CloseableHttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            // Parse the HTML response using jsoup
            Document doc = Jsoup.parse(entity.getContent(), "UTF-8", link);
            while (doc == null || doc.body() == null || doc.body().children().isEmpty()) {
                doc = Jsoup.parse(entity.getContent(), "UTF-8", link);
            }
            // Close the HttpClient and response
            EntityUtils.consume(entity);
            response.close();
            httpClient.close();

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

}