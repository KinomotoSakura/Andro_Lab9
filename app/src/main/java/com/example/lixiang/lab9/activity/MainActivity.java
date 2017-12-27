package com.example.lixiang.lab9.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.lixiang.lab9.R;
import com.example.lixiang.lab9.adapter.CardAdapter;
import com.example.lixiang.lab9.factory.ServiceFactory;
import com.example.lixiang.lab9.model.Github;
import com.example.lixiang.lab9.service.GithubService;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private EditText editText;
    private CardAdapter cardAdapter;
    private Button clearButton;
    private Button fetchButton;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setListener();
    }

    private void initView() {
        recyclerView = ((RecyclerView)findViewById(R.id.user_recycler_view));
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setSmoothScrollbarEnabled(true);
        recyclerView.setLayoutManager(manager);
        cardAdapter = new CardAdapter();
        recyclerView.setAdapter(this.cardAdapter);
        progressBar = ((ProgressBar)findViewById(R.id.progress));
        clearButton = ((Button)findViewById(R.id.button_clear));
        fetchButton = ((Button)findViewById(R.id.button_fetch));
        editText = ((EditText)findViewById(R.id.search_text));
    }

    private void showWait() {
        System.out.println("show wait");
        this.progressBar.setVisibility(View.VISIBLE);
    }

    private void removeWait() {
        System.out.println("remove wait");
        this.progressBar.setVisibility(View.GONE);
    }

    private void setListener() {
        this.clearButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MainActivity.this.cardAdapter.clear();
            }
        });
        this.fetchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String USER = MainActivity.this.editText.getText().toString();
                showWait();
                ((GithubService) ServiceFactory.getmRetrofit("https://api.github.com").create(GithubService.class))
                        .getUser(USER)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<Github>() {
                            public final void onCompleted() {
                                System.out.println("完成传输");
                                removeWait();
                            }
                            public void onError(Throwable e) {
                                Toast.makeText(MainActivity.this, e.hashCode() + "请确认用户存在", Toast.LENGTH_SHORT).show();
                                removeWait();
                            }
                            public void onNext(Github github) {
                                cardAdapter.addData(github);
                            }
                });
            }
        });
        this.cardAdapter.setOnItemClickListener(new CardAdapter.OnItemClickListener() {
            public void onClick(int pos) {
                Intent intent = new Intent(MainActivity.this, ReposActivity.class);
                intent.putExtra("user", cardAdapter.getItem(pos).getLogin());
                startActivity(intent);
            }
            public void onLongClick(int pos) {
                cardAdapter.removeItem(pos);
            }
        });
    }
}
