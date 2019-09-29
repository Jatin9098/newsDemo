package com.news.controller.newsReadActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Parcelable;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.news.R;
import com.news.controller.Constants;
import com.news.controller.helper.CustomTabActivityHelper;
import com.news.controller.helper.CustomTabsHelper;
import com.news.model.Article;

public class NewsReadActivity extends AppCompatActivity {

    private Article article;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_read);


        initViews();

    }

    private void initViews() {

        Toolbar toolbar = findViewById(R.id.toolBar);
        TextView txtNewsTitle = findViewById(R.id.txtNewsTitle);
        TextView txtName = findViewById(R.id.txtName);
        TextView txtAuthorPublishDate = findViewById(R.id.txtAuthorPublishDate);
        TextView txtNewsDescription = findViewById(R.id.txtNewsDescription);
        ImageView imgUrl = findViewById(R.id.imgUrl);

        Bundle bundle = getIntent().getExtras();
        article = bundle.getParcelable(Constants.READ_NEWS_INFO);
        String authorName = article.author == null ? "N.A" : article.author;

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supportFinishAfterTransition();
            }
        });


        txtNewsTitle.setText(article.title);
        txtName.setText(Html.fromHtml(getResources().getString(R.string.color_format)+ article.source.name));
        txtAuthorPublishDate.setText(Html.fromHtml("Author: "+authorName+" | "+ article.publishedAt.substring(0,10)));
        txtNewsDescription.setText(article.description);
        Glide.with(this)
                .load(article.urlToImage != null ? article.urlToImage : getResources().getString(R.string.news_default))
                .centerCrop()
                .placeholder(R.drawable.news_placeholder)
                .into(imgUrl);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.news_action_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.item_like);
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (item.isChecked()) {
                    item.setIcon(getResources().getDrawable(R.drawable.ic_unlike));
                    item.setChecked(false);
                }else {
                    item.setIcon(getResources().getDrawable(R.drawable.ic_liked));
                    item.setChecked(true);
                }
                return false;
            }
        });
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.item_like) {
            // Do something
            return true;
        }
        if (id == R.id.item_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_data));
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent,"Share Via"));
            return true;
        }
        if (id == R.id.item_on_web) {
            CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
                    .addDefaultShareMenuItem()
                    .setToolbarColor(this.getResources()
                            .getColor(R.color.colorPrimary))
                    .setShowTitle(true)
                    .setCloseButtonIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_back))
                    .build();
            CustomTabsHelper.addKeepAliveExtra(this, customTabsIntent.intent);
            CustomTabActivityHelper.openCustomTab(this, customTabsIntent,
                    Uri.parse(article.url),
                    new CustomTabActivityHelper.CustomTabFallback() {
                        @Override
                        public void openUri(Activity activity, Uri uri) {

                        }
                    });
            return true;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        this.supportFinishAfterTransition();
    }
}
