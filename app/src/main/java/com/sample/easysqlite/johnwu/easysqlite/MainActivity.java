package com.sample.easysqlite.johnwu.easysqlite;

import android.content.ContentValues;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;

import com.easysqlite.mylibrary.johnwu.MySQLite;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String MYTABLE = "MyTable";
    private static final String COLUMN_TITLE = "Title";
    private static final String COLUMN_SUBTITLE = "SubTitle";

    private RecyclerView recyclerView;
    private Button btn_insert;
    private MySQLite mySQLite;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findviews();
        createTable();
        initialRecycle();
    }

    private void findviews() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        btn_insert = (Button) findViewById(R.id.btn_add);

        btn_insert.setOnClickListener(this);
    }

    private void createTable() {
        mySQLite = new MySQLite.Builder(this)
                .databaseName("Mydatabase")
                .tableName(MYTABLE)
                .append(COLUMN_TITLE, MySQLite.Form.TEXT)
                .append(COLUMN_SUBTITLE, MySQLite.Form.TEXT)
                .build();
    }

    private void initialRecycle() {
        List<Article> list = new ArrayList<Article>();
        List<String> titleList = mySQLite.getStrings(COLUMN_TITLE);
        List<String> subTitleList = mySQLite.getStrings(COLUMN_SUBTITLE);
        for (int i = 0; i < titleList.size(); i++) {
            Article article = new Article(titleList.get(i),subTitleList.get(i));
            list.add(article);
        }
        MyAdapter adapter = new MyAdapter(list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private Article generateArticle() {
        int a = (int) (Math.random() * 10);
        int b = (int) (Math.random() * 10);
        Article article = new Article(Article.TITLE[a], Article.SUBTITLE[b]);
        return article;
    }

    private void insert(Article article) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, article.getTitle());
        values.put(COLUMN_SUBTITLE, article.getSubtitle());
        mySQLite.getWritableDatabase().insert(MYTABLE, null, values);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_add) {
            insert(generateArticle());
            initialRecycle();
        }
    }
}
