package com.example.lixiang.lab9.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lixiang.lab9.R;
import com.example.lixiang.lab9.activity.MainActivity;
import com.example.lixiang.lab9.model.Github;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李翔 on 2017/12/25.
 */

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
    private OnItemClickListener itemClickListener = null;
    private List<Github> items = new ArrayList();

    public void addData(Github github) {
        this.items.add(github);
        notifyDataSetChanged();
    }

    public void clear() {
        this.items.clear();
        notifyDataSetChanged();
    }

    public Github getItem(int pos) {
        return (Github)this.items.get(pos);
    }

    public int getItemCount() {
        return this.items.size();
    }

    public void onBindViewHolder(final ViewHolder view, int pos) {

        final Github github = (Github)this.items.get(pos);
        view.login.setText(github.getLogin());
        view.id.setText("id: " + github.getId());
        view.blog.setText("blog: " + github.getBlog());
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Bitmap ava_pic = getURLimage(github.getAvatar_url());
                view.avatar.post(new Runnable() {
                    public void run() {
                        view.avatar.setImageBitmap(ava_pic);
                    }
                });
            }
        }).start();
        if (this.itemClickListener != null) {
            view.itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    CardAdapter.this.itemClickListener.onClick(view.getAdapterPosition());
                }
            });
            view.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                public boolean onLongClick(View v) {
                    CardAdapter.this.itemClickListener.onLongClick(view.getAdapterPosition());
                    return false;
                }
            });
        }
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false));
    }

    public void removeItem(int pos) {
        this.items.remove(pos);
        notifyItemRemoved(pos);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.itemClickListener = onItemClickListener;
    }

    public static abstract interface OnItemClickListener {
        void onClick(int pos);
        void onLongClick(int pos);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView blog;
        public TextView id;
        public TextView login;
        public ImageView avatar;

        public ViewHolder(View view) {
            super(view);
            this.login = ((TextView)view.findViewById(R.id.login));
            this.id = ((TextView)view.findViewById(R.id.id));
            this.blog = ((TextView)view.findViewById(R.id.blog));
            avatar = ((ImageView)view.findViewById(R.id.avatar));
        }
    }

    public Bitmap getURLimage(String url) {
        Bitmap bmp = null;
        try {
            URL myurl = new URL(url);
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
            conn.setConnectTimeout(6000);//设置超时
            conn.setDoInput(true);
            conn.setUseCaches(false);//不缓存
            conn.connect();
            InputStream is = conn.getInputStream();//获得图片的数据流
            bmp = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }
}
