package com.example.contentprovider;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contentprovider.dao.StudentDao;
import com.example.contentprovider.entities.Student;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private Button btnAdd;
    private Button btnDel;
    private Button btnUpdate;
    private Button btnQuery;
    private RecyclerView rlv;

    private List<Student> mItemList = new ArrayList<>();
    private ItemAdapter mItemAdapter;
    private StudentDao mStudentDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        btnAdd = (Button) findViewById(R.id.btn_add);
        btnDel = (Button) findViewById(R.id.btn_del);
        btnUpdate = (Button) findViewById(R.id.btn_update);
        btnQuery = (Button) findViewById(R.id.btn_query);

        btnAdd.setOnClickListener(this);
        btnDel.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        btnQuery.setOnClickListener(this);
        rlv = (RecyclerView) findViewById(R.id.rlv);
        rlv.setLayoutManager(new LinearLayoutManager(this));
        mItemAdapter = new ItemAdapter(mItemList);
        rlv.setAdapter(mItemAdapter);

        mStudentDao = MyApplication.getmDaoSession().getStudentDao();
        List<Student> students = mStudentDao.loadAll();
        mItemList.addAll(students);
        mItemAdapter.notifyDataSetChanged();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                Student student = new Student(null, "钟奋韬", 11, 1, false);
                mStudentDao.insertOrReplace(student);
                break;
            case R.id.btn_del:
                Student student1 = mItemList.get(0);
                mStudentDao.delete(student1);
                break;
            case R.id.btn_update:
                Student student2 = mItemList.get(0);
                student2.setAge(1111);
                mStudentDao.update(student2);
                break;
            case R.id.btn_query:
                List<Student> students = mStudentDao.loadAll();
                mItemList.clear();
                mItemList.addAll(students);
                mItemAdapter.notifyDataSetChanged();
                break;
        }
    }

    private class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
        private List<Student> itemList = new ArrayList<>();

        public ItemAdapter(List<Student> itemList) {
            this.itemList = itemList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            TextView textView = new TextView(getApplicationContext());
            RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textView.setPadding(10,20,10,10);
            textView.setLayoutParams(layoutParams);
            ViewHolder viewHolder = new ViewHolder(textView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Student student = itemList.get(position);
            holder.tvView.setText(student.toString());
        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvView = (TextView) itemView;
            }
        }
    }
}