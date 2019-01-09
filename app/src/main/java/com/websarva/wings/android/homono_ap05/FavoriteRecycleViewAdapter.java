package com.websarva.wings.android.homono_ap05;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class FavoriteRecycleViewAdapter extends RecyclerView.Adapter<FavoriteViewHolder> {

    private List<RowData> list;
    private View.OnClickListener listener;
    private FavoriteSearch _fs;

    public FavoriteRecycleViewAdapter(FavoriteSearch favoriteSearch, List<RowData> list){
        this.list= list;
        _fs=favoriteSearch;
    }
    @Override
    public FavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent,false);
        FavoriteViewHolder viewHolder = new FavoriteViewHolder(inflate);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(FavoriteViewHolder holder, final int position) {
        holder.titleView.setText(list.get(position).getFavoriteTitle());
        holder.linearLayout.setId(holder.getAdapterPosition());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int clickpos=position;
                listener.onClick(view);
                Log.i("☆確認クリック", "ぽじしょん"+ clickpos);
                //Log.i("☆確認クリック", "ポジションから店名"+list.get(clickpos).getFavoriteTitle());
                //String storename = list.get(clickpos).getFavoriteTitle();
                String placeid =list.get(clickpos).getPlaceid();
                String lat =list.get(clickpos).getLat();
                String lng =list.get(clickpos).getLng();
                Log.i("☆確認クリック", "id:"+placeid+lat+lng);

                _fs.MoveSearchResult(placeid,lat,lng);
            }
        });
    }
    public void setOnItemClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
//https://github.com/Hachimori/AndroidSamples/blob/master/RecyclerViewSample/app/src/main/java/com/example/hachimori/recyclerviewsample/RecyclerViewSampleFragment.java
//    // スワイプされたときの挙動を定義
//    ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
//        @Override
//        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//            return false;
//        }
//
//        @Override
//        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//
//            // 横にスワイプされたら要素を消す
//            int swipedPosition = viewHolder.getAdapterPosition();
//            FavoriteRecycleViewAdapter adapter =  mRecyclerView.getAdapter();
//            adapter.remove(swipedPosition);
//        }
//    };
       //(new ItemTouchHelper(callback)).attachToRecyclerView(mRecyclerView);

    public void remove(int position){
        //ここに削除
        //positionの名前をとったらdatabase照合
        //発見したら削除
        //成功したら　
        //list.remove(position);
        //notifyItemRemoved(position);
    }
}
