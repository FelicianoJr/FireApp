package br.com.mabelli.bijoux;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by feliciano on 29/07/16.
 */
@IgnoreExtraProperties
public class Product {

    public String uid;
    public String title;
    public String subtitle;
    public String price;
    public String url;
    public String reference;
    public String sale;

    public Product() {

    }

    public Product(String uid, String title, String subtitle,
                   String price, String url, String reference, String sale) {
        this.uid = uid;
        this.title = title;
        this.subtitle = subtitle;
        this.price = price;
        this.url = url;
        this.reference = reference;
        this.sale = sale;
    }

}
