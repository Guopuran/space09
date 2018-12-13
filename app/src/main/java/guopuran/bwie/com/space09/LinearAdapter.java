package guopuran.bwie.com.space09;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import guopuran.bwie.com.space09.bean.Bean;

public class LinearAdapter extends RecyclerView.Adapter <RecyclerView.ViewHolder> {
    private List<Bean.ResultBean.DataBean> list;
    private Context context;
    private final int ITEM_SAN=0;
    private final int ITEM_TWO=ITEM_SAN+1;
    public LinearAdapter(Context context) {
        this.context = context;
        list=new ArrayList<>();
    }
    //下拉刷新

    public void setList(List<Bean.ResultBean.DataBean> slist) {
        list.clear();
        if (slist!=null){
            list.addAll(slist);
        }
        notifyDataSetChanged();
    }

    //上拉加载
    public void addList(List<Bean.ResultBean.DataBean> slist) {
        if (slist!=null){
            list.addAll(slist);
        }
        notifyDataSetChanged();
    }
    public Bean.ResultBean.DataBean getitem(int position){
        return list.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (getitem(position).isPan()){
            return ITEM_SAN;
        }else{
            return ITEM_TWO;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i==ITEM_TWO){
            View view = LayoutInflater.from(context).inflate(R.layout.item_two, viewGroup, false);
            return new TwoViewHolder(view);
        }else{
            View view = LayoutInflater.from(context).inflate(R.layout.item_three, viewGroup, false);
            return new ThreeViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        int type = getItemViewType(i);
        switch (type){
            case ITEM_TWO:
                TwoViewHolder holder= (TwoViewHolder) viewHolder;
                holder.er_title.setText(getitem(i).getTitle());
                ImageLoader.getInstance().displayImage(getitem(i).getThumbnail_pic_s(),holder.one_image);
                ImageLoader.getInstance().displayImage(getitem(i).getThumbnail_pic_s02(),holder.two_image);
                holder.one_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(v,i);
                    }
                });
                holder.two_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(v,i);
                    }
                });
                break;
            case ITEM_SAN:
                ThreeViewHolder holder1= (ThreeViewHolder) viewHolder;
                holder1.textView.setText(getitem(i).getTitle());
                ImageLoader.getInstance().displayImage(getitem(i).getThumbnail_pic_s(),holder1.yi_image);
                ImageLoader.getInstance().displayImage(getitem(i).getThumbnail_pic_s02(),holder1.er_image);
                ImageLoader.getInstance().displayImage(getitem(i).getThumbnail_pic_s03(),holder1.san_image);
                holder1.yi_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(v,i);
                    }
                });
                holder1.er_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(v,i);
                    }
                });
                holder1.san_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(v,i);
                    }
                });
                break;
            default:break;
        }
        //长按删除
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemlongClickListener!=null){
                    onItemlongClickListener.onItemlongClick(v,i);
                }
                return true;
            }
        });
    }
    //透明度改变
    public void getdata(View v,int position){
        ObjectAnimator alpha=ObjectAnimator.ofFloat(v,"alpha",1f,0f,1f);
        alpha.setDuration(2000);
        alpha.setRepeatCount(0);
        alpha.start();
        notifyItemChanged(position);
    }

    //删除
    public void del(int position){
        list.remove(position);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    class TwoViewHolder extends RecyclerView.ViewHolder {
        private ImageView one_image;
        private ImageView two_image;
        private TextView er_title;
        public TwoViewHolder(@NonNull View itemView) {
            super(itemView);
            one_image=itemView.findViewById(R.id.one_image);
            two_image=itemView.findViewById(R.id.two_image);
            er_title=itemView.findViewById(R.id.er_title);
        }
    }
    class ThreeViewHolder extends RecyclerView.ViewHolder {
        private ImageView yi_image;
        private ImageView er_image;
        private ImageView san_image;
        private TextView textView;
        public ThreeViewHolder(@NonNull View itemView) {
            super(itemView);
            yi_image=itemView.findViewById(R.id.yi_image);
            er_image=itemView.findViewById(R.id.er_image);
            san_image=itemView.findViewById(R.id.san_image);
            textView=itemView.findViewById(R.id.title);
        }
    }
    public onItemClickListener onItemClickListener;
    public void setonItemClickListener(onItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }
    public interface onItemClickListener{
        void onItemClick(View v, int position);
    }
    public LinearAdapter.onItemlongClickListener onItemlongClickListener;
    public void setonItemlongClickListener(LinearAdapter.onItemlongClickListener onItemlongClickListener){
        this.onItemlongClickListener=onItemlongClickListener;
    }
    public interface onItemlongClickListener{
        void onItemlongClick(View v,int i);
    }
}
