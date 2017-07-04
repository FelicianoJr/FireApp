package br.com.mabelli.bijoux;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

/**
 * Created by feliciano on 01/07/17.
 */

public class RecentProduct extends ProductControlllerFragment {

    public RecentProduct(){}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        Query recentProduct = databaseReference.child("product/");
                //.orderByChild("type").equalTo("necklace");
        return recentProduct;
    }
}
