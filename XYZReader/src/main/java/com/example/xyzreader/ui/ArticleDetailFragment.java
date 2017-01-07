package com.example.xyzreader.ui;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.xyzreader.R;
import com.example.xyzreader.data.ArticleLoader;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A fragment representing a single Article detail screen. This fragment is
 * either contained in a {@link MainActivity} in two-pane mode (on
 * tablets) or a {@link ArticleDetailActivity} on handsets.
 */
public class ArticleDetailFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>,RequestListener<String,GlideDrawable> {
    private static final String TAG = "ArticleDetailFragment";

    public static final String ARG_ITEM_ID = "item_id";
    private static final float PARALLAX_FACTOR = 1.25f;


    private long articleId;
   @BindView(R.id.iv_article_detail_image)ImageView imgArticlePhoto;
    @BindView(R.id.tv_article_body)TextView tvArticleBody;
    @BindView(R.id.toolbar_article_detail)Toolbar toolbar;
    @BindView(R.id.tv_article_info)TextView tvArticleInfo;
    @BindView(R.id.tv_article_title)TextView tvArticleTitle;
    @BindView(R.id.article_detail_body_headers)LinearLayout articleHeaders;
    @BindView(R.id.article_detail_progressbar)ProgressBar progressBar;
    private RequestListener glideListener=this;





    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ArticleDetailFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
//Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_article_detail,container,false);

        Intent intent=getActivity().getIntent();
        articleId=intent.getLongExtra(MainActivity.EXTRAS_ARTICLE_ID,0);

        getLoaderManager().initLoader(0,null,this);
        ButterKnife.bind(this,rootView);

        //get palette color from MainActivity
        if (intent.hasExtra(MainActivity.EXTRAS_PALETTE_COLOR)) {
            int paletteColor=intent.getIntExtra(MainActivity.EXTRAS_PALETTE_COLOR,0);
            if (paletteColor!=0){
                articleHeaders.setBackgroundColor(paletteColor);
            }
        }
// home button navigation
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        return rootView;
    }







    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        if (articleId>0)
        return ArticleLoader.newInstanceForItemId(getActivity(), articleId);
        else
            return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {


        if (cursor == null && !cursor.moveToFirst())
            return;

        String photoUrl=cursor.getString(ArticleLoader.Query.PHOTO_URL);
        Glide.with(getContext())
                .load(photoUrl)
                .dontAnimate()
                .listener(glideListener)
                .into(imgArticlePhoto);
                String val_article_title=cursor.getString(ArticleLoader.Query.TITLE);
        long val_article_date=cursor.getLong(ArticleLoader.Query.PUBLISHED_DATE);
        String val_article_author=cursor.getString(ArticleLoader.Query.AUTHOR);
        String val_article_body=cursor.getString(ArticleLoader.Query.BODY);

        tvArticleBody.setText(Html.fromHtml(val_article_body).toString());
        tvArticleBody.setText(
                new SimpleDateFormat("MMM dd,yyyy", Locale.ENGLISH).format(new Date(val_article_date)).toString()+
                        "by"+
                        val_article_author

        );
        tvArticleTitle.setText(val_article_title);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }



    @Override
    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
      progressBar.setVisibility(View.INVISIBLE);
        return false;
    }

    @Override
    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
        progressBar.setVisibility(View.INVISIBLE);

        return false;
    }
    @OnClick(R.id.article_detail_fab_share)
void onFabClick(){
        String content=tvArticleBody.getText().toString();
        if (content!=null&&!content.isEmpty()){
            Intent sendIntent=new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,content);
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent,getResources().getText(R.string.send_to)));
        }
    }
}
