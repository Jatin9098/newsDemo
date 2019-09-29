package com.news.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Article implements Parcelable {
    @Expose
    @SerializedName("content")
    public String content;
    @Expose
    @SerializedName("publishedAt")
    public String publishedAt;
    @Expose
    @SerializedName("urlToImage")
    public String urlToImage;
    @Expose
    @SerializedName("url")
    public String url;
    @Expose
    @SerializedName("description")
    public String description;
    @Expose
    @SerializedName("title")
    public String title;
    @Expose
    @SerializedName("author")
    public String author;
    @Expose
    @SerializedName("source")
    public Source source;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.content);
        dest.writeString(this.publishedAt);
        dest.writeString(this.urlToImage);
        dest.writeString(this.url);
        dest.writeString(this.description);
        dest.writeString(this.title);
        dest.writeString(this.author);
        dest.writeParcelable(this.source, flags);
    }

    public Article() {
    }

    protected Article(Parcel in) {
        this.content = in.readString();
        this.publishedAt = in.readString();
        this.urlToImage = in.readString();
        this.url = in.readString();
        this.description = in.readString();
        this.title = in.readString();
        this.author = in.readString();
        this.source = in.readParcelable(Source.class.getClassLoader());
    }

    public static final Parcelable.Creator<Article> CREATOR = new Parcelable.Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel source) {
            return new Article(source);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };
}
