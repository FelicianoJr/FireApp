package br.com.mabelli.bijoux;

import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by feliciano on 01/07/17.
 */

public class ProductRecycler extends RecyclerView.ViewHolder {

    public final TextView titleView;
    public final ImageView imgView;
    public final TextView priceView;
    public final AppCompatImageButton btn;

    public ProductRecycler(View itemView) {
        super(itemView);
        titleView = (TextView) itemView.findViewById(R.id.name);
        imgView = (ImageView) itemView.findViewById(R.id.comment_photo);
        priceView = (TextView) itemView.findViewById(R.id.artist);
        btn = (AppCompatImageButton) itemView.findViewById(R.id.btnfavorite);
    }
}
