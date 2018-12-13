package guopuran.bwie.com.space09;

import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.HashMap;
import java.util.Map;

import guopuran.bwie.com.space09.bean.Bean;
import guopuran.bwie.com.space09.presenter.IpresenterImpl;
import guopuran.bwie.com.space09.view.IView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,IView {
    private String url="http://v.juhe.cn/toutiao/index?type=&key=83a69c67e9272f816e42450ef0eb50ee";
    private IpresenterImpl mIpresenterImpl;
    private ImageView imageView;
    private TextView text_name;
    private Button button;
    private XRecyclerView xRecyclerView;
    private int page;
    private LinearAdapter linearAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPresenter();
        initView();
    }
    //互绑
    private void initPresenter() {
        mIpresenterImpl=new IpresenterImpl(this);
    }
    //解绑

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mIpresenterImpl.Deatch();
    }

    private void initView() {
        //获取资源ID
        imageView = findViewById(R.id.image);
        text_name = findViewById(R.id.text_name);
        button = findViewById(R.id.button_next);
        xRecyclerView = findViewById(R.id.xrecy);
        imageView.setOnClickListener(this);
        button.setOnClickListener(this);
        initData();
    }

    private void initData() {
        page=1;
        //获得布局管理器
        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(MainActivity.this);
        //方向
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置布局管理器
        xRecyclerView.setLayoutManager(linearLayoutManager);
        //支持下拉刷新
        xRecyclerView.setPullRefreshEnabled(true);
        //支持上拉加载
        xRecyclerView.setLoadingMoreEnabled(true);
        //增加、删除动画
        xRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //设置适配器
        linearAdapter = new LinearAdapter(this);
        xRecyclerView.setAdapter(linearAdapter);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {//刷新
                page=1;
                moreurl();
            }

            @Override
            public void onLoadMore() {//上拉加载
                moreurl();
            }
        });
        moreurl();
        linearAdapter.setonItemClickListener(new LinearAdapter.onItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                linearAdapter.getdata(v,position);
            }
        });
        linearAdapter.setonItemlongClickListener(new LinearAdapter.onItemlongClickListener() {
            @Override
            public void onItemlongClick(View v, final int i) {
                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this)
                        .setMessage("确定要删除吗");


                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        linearAdapter.del(i);
                    }
                });
                builder.show();
            }
        });
    }
    public void moreurl(){
        mIpresenterImpl.requestter(url,new HashMap<String, String>(),Bean.class);
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.button_next:
                UMShareAPI umShareAPI=UMShareAPI.get(MainActivity.this);
                umShareAPI.getPlatformInfo(MainActivity.this, SHARE_MEDIA.QQ, new UMAuthListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                        Log.i("TAG",map+"");
                        //"name" -> "小可爱"
                        //"profile_image_url" -> "http://thirdqq.qlogo.cn/qqapp/100424468/F3DAC6013A97C040BF23E80201525752/100"
                        String name = map.get("name");
                        String profile_image_url = map.get("profile_image_url");
                        text_name.setText(name);
                        ImageLoader.getInstance().displayImage(profile_image_url,imageView);
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ScaleAnimation scaleAnimation=new ScaleAnimation(1.0f,2.0f,1.0f,2.0f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
                                scaleAnimation.setDuration(2000);
                                imageView.startAnimation(scaleAnimation);
                            }
                        });
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media, int i) {

                    }
                });
                break;
            case R.id.image:

                break;
            default:break;
        }
    }
    @Override
    public void getdata(Object object) {
        Bean bean= (Bean) object;
        if (page==1){
            linearAdapter.setList(bean.getResult().getData());
        }else{
            linearAdapter.addList(bean.getResult().getData());
        }
        page++;
        xRecyclerView.refreshComplete();
        xRecyclerView.loadMoreComplete();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(MainActivity.this).onActivityResult(requestCode,resultCode,data);
    }
}
