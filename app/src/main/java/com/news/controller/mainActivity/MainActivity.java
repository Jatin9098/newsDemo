package com.news.controller.mainActivity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.news.R;
import com.news.controller.CommonUtils;

import com.news.controller.Constants;
import com.news.controller.newsReadActivity.NewsReadActivity;
import com.news.model.Article;
import com.news.model.NewsModel;
import com.news.viewModel.HeadLineViewModel;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    ArrayList<Article> mArticleList;
    HeadLineAdapter headLineAdapter;
    RecyclerView rvHeadline;
    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Constants.CONTEXT = this;

        initViews();
        setupRecyclerView();

        if (CommonUtils.isConnected(this)) {
            HeadLineViewModel viewModel = ViewModelProviders.of(this).get(HeadLineViewModel.class);
            observeViewModel(viewModel);
        } else {
            CommonUtils.showAlertMessage(this, getString(R.string.no_internet_connection), getString(R.string.oops), 1);
        }

    }

    private void initViews() {
        mArticleList = new ArrayList<>();
        rvHeadline = findViewById(R.id.rvHeadline);
        mViewPager = findViewById(R.id.viewPager);
        mViewPager.setPageMargin(dpToPx(10));
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar_layout);
        collapsingToolbarLayout.setTitle(getString(R.string.top_news));
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.transparent)); // transperent color = #00000000
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.colorPrimaryDark)); //Color of your title
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    private void setupRecyclerView() {
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        if (headLineAdapter == null) {
            headLineAdapter = new HeadLineAdapter(mArticleList, this);
            rvHeadline.setLayoutManager(layoutManager);
            rvHeadline.setAdapter(headLineAdapter);
            rvHeadline.setItemAnimator(new DefaultItemAnimator());
            rvHeadline.setNestedScrollingEnabled(true);
        } else {
            headLineAdapter.notifyDataSetChanged();
        }

    }

    private void observeViewModel(HeadLineViewModel viewModel) {
        viewModel.getNewsResponseObservable()
                .observe(this, new Observer<NewsModel>() {
                    @Override
                    public void onChanged(@Nullable NewsModel newsModel) {
                        if (newsModel != null) {
                            List<Article> articles = newsModel.getArticles();
                            mArticleList.addAll(articles);

                            ArrayList<Article> articleArrayList = new ArrayList<>();
                            int lastIndex = articles.size() - 1;
                            for (int i = 0; i <= 5; i++) {
                                articleArrayList.add(articles.get(lastIndex));
                                lastIndex = lastIndex - 3;
                            }

                            ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(articleArrayList);
                            mViewPager.setAdapter(viewPagerAdapter);
                            mViewPager.setCurrentItem(2);
                            headLineAdapter.notifyDataSetChanged();
                        }
                    }
                });


    }

    private class HeadLineAdapter extends RecyclerView.Adapter<HeadLineAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<Article> articleArrayList;

        public HeadLineAdapter(ArrayList<Article> articleArrayList, MainActivity mainActivity) {
            this.articleArrayList = articleArrayList;
            this.mContext = mainActivity;
        }

        @NonNull
        @Override
        public HeadLineAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

            View view = LayoutInflater.from(mContext).inflate(R.layout.news_layout, viewGroup, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull HeadLineAdapter.MyViewHolder holder, int position) {
            Article article = articleArrayList.get(position);
            String authorName = article.author == null ? "N.A" : article.author;

            holder.txtName.setText(Html.fromHtml(getResources().getString(R.string.color_format) + article.source.name));
            holder.txtNewsTitle.setText(article.title);
            holder.txtAuthorPublishDate.setText("Author: " + authorName + " | " + article.publishedAt.substring(0, 10));
            Glide.with(mContext)
                    .load(article.urlToImage != null ? article.urlToImage : getResources().getString(R.string.news_default))
                    .centerCrop()
                    .placeholder(R.drawable.news_placeholder)
                    .into(holder.imgUrl);
        }

        @Override
        public int getItemCount() {
            return articleArrayList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TextView txtName, txtNewsTitle, txtAuthorPublishDate;
            ImageView imgUrl;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);

                txtName = itemView.findViewById(R.id.txtName);
                txtNewsTitle = itemView.findViewById(R.id.txtNewsTitle);
                txtAuthorPublishDate = itemView.findViewById(R.id.txtAuthorPublishDate);
                imgUrl = itemView.findViewById(R.id.imgUrl);

                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                startNewsReadActivity(getAdapterPosition(), articleArrayList, imgUrl, txtName, txtNewsTitle, txtAuthorPublishDate);
            }

        }
    }

    private void startNewsReadActivity(int adapterPosition, ArrayList<Article> mArticleList, View... views) {

        Intent intent = new Intent(this, NewsReadActivity.class);
        ActivityOptionsCompat options = null;
        Bundle bundle = new Bundle();
        Pair<View, String> p1 = Pair.create((View) views[0], "profile");
        Pair<View, String> p2 = Pair.create((View) views[1], "txtName");
        Pair<View, String> p3 = Pair.create((View) views[2], "txtNewsTitle");
        if (views.length == 4) {
            Pair<View, String> p4 = Pair.create((View) views[3], "txtAuthorPublishDate");
            options = (ActivityOptionsCompat) ActivityOptionsCompat.makeSceneTransitionAnimation(this, p1, p2, p3, p4);
        } else {
            options = (ActivityOptionsCompat) ActivityOptionsCompat.makeSceneTransitionAnimation(this, p1, p2, p3);
        }
        bundle.putParcelable(Constants.READ_NEWS_INFO, mArticleList.get(adapterPosition));
        intent.putExtras(bundle);
        startActivity(intent, options.toBundle());
    }

    @Override
    protected void onStop() {
        super.onStop();
        Constants.CONTEXT = null;
    }

    private class ViewPagerAdapter extends PagerAdapter {

        ArrayList<Article> arrayList;

        public ViewPagerAdapter(ArrayList<Article> arrayList) {
            this.arrayList = arrayList;

        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            Article article = arrayList.get(position);
            LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
            ViewGroup itemView = (ViewGroup) inflater.inflate(R.layout.pager_item, container, false);
            container.addView(itemView);

            ImageView ivNews = itemView.findViewById(R.id.imgUrl);
            TextView txtName = itemView.findViewById(R.id.txtName);
            TextView txtNewsTitle = itemView.findViewById(R.id.txtNewsTitle);

            txtName.setText(Html.fromHtml(getResources().getString(R.string.color_format) + article.source.name));
            txtNewsTitle.setText(article.title);
            Glide.with(MainActivity.this)
                    .load(article.urlToImage != null ? article.urlToImage : getResources().getString(R.string.news_default))
                    .centerCrop()
                    .placeholder(R.drawable.news_placeholder)
                    .into(ivNews);

            ivNews.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startNewsReadActivity(position, arrayList, ivNews, txtName, txtNewsTitle);
                }
            });

            return itemView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object view) {
            container.removeView((View) view);
        }

        @Override
        public int getCount() {
            return this.arrayList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }
    }
}
