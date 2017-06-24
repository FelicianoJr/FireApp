package br.com.mabelli.bijoux;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by feliciano on 29/07/16.
 */
@IgnoreExtraProperties
public class Product {

    private String title;
    private String subtitle;
    private String price;
    private String url;
    private String reference;
    private String sale;

    public Product() {

    }

    public Product(String title, String subtitle, String price, String url, String reference, String sale) {
        this.title = title;
        this.subtitle = subtitle;
        this.price = price;
        this.url = url;
        this.reference = reference;
        this.sale = sale;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getSale() {
        return sale;
    }

    public void setSale(String sale) {
        this.sale = sale;
    }


}
