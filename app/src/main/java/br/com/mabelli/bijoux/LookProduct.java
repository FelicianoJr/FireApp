package br.com.mabelli.bijoux;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

/**
 * Created by feliciano on 01/07/17.
 */

public class LookProduct extends Fragment {

    private static final String EXTRA_TRANSITION_NAME = "transition_name";
    private ImageView imageView;

    public LookProduct() {
    }

    public static LookProduct newInstance(String transitionName) {
        LookProduct lookProduct = new LookProduct();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_TRANSITION_NAME, transitionName);
        lookProduct.setArguments(bundle);
        return lookProduct;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        postponeEnterTransition();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSharedElementEnterTransition(TransitionInflater
                    .from(getContext()).inflateTransition(android.R.transition.move));
        }

        imageView = new ImageView(getActivity());
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.WRAP_CONTENT));
        return imageView;
        //View rootView = inflater.inflate(R.layout.content_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String transitionName = getArguments().getString(EXTRA_TRANSITION_NAME);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imageView.setTransitionName(transitionName);
        }

        Glide.with(this)
                .load("https://beautyfashionbrasil.files.wordpress.com/2010/05/bq031.jpg")
                .centerCrop().dontAnimate().listener(new RequestListener<String
                , GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target
                    , boolean isFirstResource) {
                startPostponedEnterTransition();
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model
                    , Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                startPostponedEnterTransition();
                return false;
            }
        }).into(imageView);
    }
}
