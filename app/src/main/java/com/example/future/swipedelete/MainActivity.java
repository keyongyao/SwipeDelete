package com.example.future.swipedelete;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<String> listdata = new ArrayList<>();
    Button btn_block;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        setter();
    }

    private void setter() {
        listView.setAdapter(new MyAdapter());
        listView.requestDisallowInterceptTouchEvent(true);
        // TODO: 2016/10/26   Bug ListView 无法获取点击事件 解决：有 ListView 有content 直接给 content 设置监听事件
      /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "点击了 "+listdata.get(position), Toast.LENGTH_SHORT).show();
            }
        });*/
        btn_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "我是按钮，我被点击了", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initData() {
        for (int i = 0; i < 50; i++) {
            listdata.add("你是煞笔" + i);
        }
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.lv_wipedel_list);
        btn_block = (Button) findViewById(R.id.btn_block);
    }

    private static class Holder {
        TextView tv_content, tv_del, tv_call;
        SwipeDeleteItem item;
        LinearLayout ll_content;

        public Holder(View convertView) {
            tv_content = (TextView) convertView.findViewById(R.id.tv_swipdel_content);
            tv_call = (TextView) convertView.findViewById(R.id.tv_swipeDelete_call);
            tv_del = (TextView) convertView.findViewById(R.id.tv_swipeDelete_del);
            item = (SwipeDeleteItem) convertView.findViewById(R.id.sdi_swipeDel_item);
            ll_content = (LinearLayout) convertView.findViewById(R.id.ll_content);
        }

        public static Holder getHolder(View convertView) {
            Holder holder = (Holder) convertView.getTag();
            if (holder == null) {
                holder = new Holder(convertView);
                convertView.setTag(holder);
            }
            return holder;
        }
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return listdata.size();
        }

        @Override
        public String getItem(int position) {
            return listdata.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(MainActivity.this, R.layout.swipedelete_layout, null);

            }
            Holder holder = Holder.getHolder(convertView);
            holder.tv_content.setText(getItem(position));
            holder.tv_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "call " + getItem(position), Toast.LENGTH_SHORT).show();
                }
            });
            holder.tv_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "del " + getItem(position), Toast.LENGTH_SHORT).show();
                }
            });
            holder.item.setOnOpenStateChangeListener(new SwipeDeleteItem.OnOpenStateChangeListener() {
                @Override
                public void onOpen() {
                    Toast.makeText(MainActivity.this, "删除菜单打开了 " + getItem(position), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onClose() {
                    Toast.makeText(MainActivity.this, "删除菜单关闭了 " + getItem(position), Toast.LENGTH_SHORT).show();
                }
            });
            holder.ll_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "点击了" + getItem(position), Toast.LENGTH_SHORT).show();
                }
            });

            return convertView;
        }


    }

   /* @Override
    public boolean onTouchEvent(MotionEvent event) {
        return listView.onInterceptTouchEvent(event);
      //  return super.onTouchEvent(event);
    }*/
}
