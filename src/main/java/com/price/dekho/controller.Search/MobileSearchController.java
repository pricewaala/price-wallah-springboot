package com.price.dekho.controller.Search;

import okhttp3.Request;
import okhttp3.Response;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
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

import java.io.BufferedReader;
import java.io.IOException;
import okhttp3.OkHttpClient;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
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

    @GetMapping("/amazon/scrape/v2")
    public List<AmazonProduct> scrapeWebsiteAmazonV2(@RequestParam("search") String search) throws IOException {
        String link = "https://www.amazon.in/s?k=" + search;
        List<AmazonProduct> products = new ArrayList<>(1000);

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpGet request = new HttpGet(link);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                // Get the HTML content of the web page
                String html = EntityUtils.toString(response.getEntity());

                // Parse the HTML content with Jsoup
                Document doc = Jsoup.parse(html);

                Elements elements = doc.select("div.sg-row");

                // Print the text content of each h2 tag to the console
                for (int i = 0; i < elements.size(); i++) {
                    String title = null, url = null, price = null, image = null;
                    AmazonProduct amazonProduct = new AmazonProduct();

                    Element href = elements.get(i).selectFirst("div.sg-col-inner");

                    assert href != null;
                    Element href3=href.selectFirst("h2.a-size-mini.a-spacing-none.a-color-base.s-line-clamp-2");

                    System.out.println(href3);
//                    url = "https://www.amazon.in" + href.attr("href");
//
//                    Elements elementPrice = doc.select("span.a-price-whole");
//                    amazonProduct.setPrice(elementPrice.get(i).text());
//                    int size = products.size();
//                    products.add(amazonProduct);
//
//                    Elements elementImage = doc.select("div.a-section.aok-relative.s-image-fixed-height");
//                    if (i >= elementImage.size()) {
//                        Element img = elementImage.get(0).selectFirst("img");
//                        image = img.attr("src");
//                    } else {
//                        Element img = elementImage.get(i).selectFirst("img");
//                        image = img.attr("src");
//                    }
                    amazonProduct.setImage(image);
                    amazonProduct.setUrl(url);
                    amazonProduct.setTitle(title);
//                    products.set(i, amazonProduct);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return products;
    }

    //final okhttp
    @GetMapping("/amazon/scrape/v3")
    public List<AmazonProduct> scrapeWebsiteAmazonV3(@RequestParam("search") String search) throws IOException {
        String link = "https://www.amazon.in/s?k=" + search;
        List<AmazonProduct> products = new ArrayList<>(1000);

        scrapAmazonWrapper(link, products);

        while (products.isEmpty())
            scrapAmazonWrapper(link,products);

        return products;
    }

    private static void scrapAmazonWrapper(String link, List<AmazonProduct> products) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(link)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String html = response.body().string();

            // Parse the HTML content with Jsoup
            Document doc = Jsoup.parse(html);

            Elements elements = doc.select("div.s-card-container.s-overflow-hidden.aok-relative.puis-wide-grid-style");

            System.out.println(elements.size());

            for (int i = 0; i < elements.size(); i++) {
                AmazonProduct amazonProduct = new AmazonProduct();

                Element spanName = elements.get(i).selectFirst("span.a-size-medium.a-color-base.a-text-normal");
                if (spanName !=null)
                    amazonProduct.setTitle(spanName.text());

                Element divPrice=elements.get(i).selectFirst("div.a-section.a-spacing-none.a-spacing-top-micro.s-price-instructions-style");
                if (divPrice !=null){
                    Element divHrefLink=divPrice.selectFirst("a.a-size-base.a-link-normal.s-underline-text");
                    if (divHrefLink!=null)
                        amazonProduct.setUrl("https://www.amazon.in"+divHrefLink.attr("href"));
                    Element spanPrice=divPrice.selectFirst("span.a-price-whole");
                    if (spanPrice !=null)
                        amazonProduct.setPrice(spanPrice.text());
                }
                Element divImage=elements.get(i).selectFirst("div.a-section.aok-relative.s-image-fixed-height");
                if (divImage !=null) {
                    Element img=divImage.selectFirst("img.s-image");
                    if (img!=null)
                     amazonProduct.setImage(img.attr("src"));
                }

                products.add(amazonProduct);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @GetMapping("/reliance/scrape/v1")
    public List<AmazonProduct> scrapeWebsiteRelianceV3(@RequestParam("search") String search) throws IOException {
        String link = "https://www.reliancedigital.in/search?q=" + search;
        List<AmazonProduct> products = new ArrayList<>(1000);

        scrapRelianceWrapper(link, products);

        while (products.isEmpty())
            scrapRelianceWrapper(link,products);

        return products;
    }

    private static void scrapRelianceWrapper(String link, List<AmazonProduct> products) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(link)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String html = response.body().string();

            // Parse the HTML content with Jsoup
            Document doc = Jsoup.parse(html);

            Elements elements = doc.select("li.grid.pl__container__sp");

            System.out.println(elements.size());

            for (int i = 0; i < elements.size(); i++) {
                AmazonProduct amazonProduct = new AmazonProduct();

                Element spanName = elements.get(i).selectFirst("p.sp__name");
                if (spanName !=null)
                    amazonProduct.setTitle(spanName.text());

                Element divPrice=elements.get(i).selectFirst("div.slider-text");
                if (divPrice !=null){
                    Element priceSpan=divPrice.selectFirst("span.TextWeb__Text-sc-1cyx778-0");
                    if (priceSpan !=null)
                        amazonProduct.setPrice(priceSpan.text());
                }

                Element divLinkAndImage=elements.get(i).selectFirst("div.sp.grid");
                if (divLinkAndImage != null){
                    Element hrefLink=divLinkAndImage.selectFirst("a");
                    if (hrefLink!=null)
                        amazonProduct.setUrl("https://www.reliancedigital.in"+hrefLink.attr("href"));

                    Element imageLink=divLinkAndImage.selectFirst("img");
                    if (imageLink != null){
                        amazonProduct.setImage("https://www.reliancedigital.in"+imageLink.attr("data-srcset"));
                    }
                }

                products.add(amazonProduct);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @GetMapping("/flipkart/scrape/v1")
    public List<AmazonProduct> scrapeWebsiteFlipkart(@RequestParam("search") String search) throws IOException {
        String link = "https://www.flipkart.com/search?q=" + search;
        List<AmazonProduct> products = new ArrayList<>(1000);

        scrapFlipkartWrapper(link, products);

        while (products.isEmpty())
            scrapFlipkartWrapper(link,products);

        return products;
    }

    private static void scrapFlipkartWrapper(String link, List<AmazonProduct> products) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(link)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String html = response.body().string();

            // Parse the HTML content with Jsoup
            Document doc = Jsoup.parse(html);

            Elements elements = doc.select("div._13oc-S");

            System.out.println(elements.size());

            for (int i = 0; i < elements.size(); i++) {
                AmazonProduct amazonProduct = new AmazonProduct();
                ArrayList<String>descriptionList=new ArrayList<>();

                Element divName = elements.get(i).selectFirst("div._4rR01T");
                if (divName !=null)
                    amazonProduct.setTitle(divName.text());

                Element divPrice=elements.get(i).selectFirst("div._30jeq3._1_WHN1");
                if (divPrice !=null){
                        amazonProduct.setPrice(divPrice.text());
                }

                Element imageSrc=elements.get(i).selectFirst("img._396cs4");
                if (imageSrc !=null){
                    amazonProduct.setImage(imageSrc.attr("src"));
                }

                Element hrefLink=elements.get(i).selectFirst("a._1fQZEK");
                if (hrefLink !=null){
                    amazonProduct.setUrl("https://www.flipkart.com"+hrefLink.attr("href"));
                }

                Element description=elements.get(i).selectFirst("ul._1xgFaf");
                if (description != null){
                    Elements listOfDescriptions=description.select("li.rgWa7D");
                    for (Element list: listOfDescriptions){
                        descriptionList.add(list.text());
                    }
                    amazonProduct.setDescription(descriptionList);
                }

                products.add(amazonProduct);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}