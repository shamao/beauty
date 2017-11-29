package beauty.louise.com.ui.main;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import beauty.louise.com.BuildConfig;
import beauty.louise.com.R;
import beauty.louise.com.bean.MCoverBean;
import beauty.louise.com.bean.MFuncBean;
import beauty.louise.com.bean.MImageBean;
import beauty.louise.com.bean.MTagBean;
import beauty.louise.com.bean.helper.MCoverList;
import beauty.louise.com.bean.helper.MFuncList;
import beauty.louise.com.bean.helper.MTagList;
import beauty.louise.com.view.UIGradientTopBar;
import beauty.louise.com.view.provider.BannerProvider;
import beauty.louise.com.view.provider.CoverProvider;
import beauty.louise.com.view.provider.FuncProvider;
import beauty.louise.com.view.provider.ImageProvider;
import beauty.louise.com.view.provider.TagProvider;
import butterknife.BindView;
import butterknife.OnClick;
import me.drakeet.multitype.MultiTypeAdapter;
import buykee.com.common.base.BaseActivity;
import buykee.com.common.bean.MolColumnBean;
import buykee.com.common.bean.MolLayoutBean;
import buykee.com.common.bean.MolSeparatorBean;
import buykee.com.common.utils.ActivityUtils;
import buykee.com.common.utils.DisplayUtils;
import buykee.com.common.utils.Logger;
import buykee.com.common.view.provider.ColumnProvider;
import buykee.com.common.view.provider.SeparatorProvider;
import pub.devrel.easypermissions.EasyPermissions;


public class MainActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {
    @BindView(R.id.gradient_bar)
    UIGradientTopBar mGradientTopBar;
    @BindView(R.id.swipe)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;
    MultiTypeAdapter mAdapter;
    LinearLayoutManager mLayoutManager;

    private List<Object> mData;

    @Override
    public int getLayoutId() {
        return R.layout.ac_main;
    }

    @Override
    public void initData() {
        mData = new ArrayList<>();

        List<MCoverBean> coverList = new ArrayList<>();
        coverList.add(new MCoverBean("http://static.cosmeapp.com/product/201709/21/09/57/59c31c82d54e4535.jpg", 2, 1));
        coverList.add(new MCoverBean("http://static.cosmeapp.com/product/201709/15/18/51/59bbb0c372f23674.jpg", 2, 1));
        coverList.add(new MCoverBean("http://static.cosmeapp.com/product/201709/22/10/11/59c47154775f1691.jpg", 2, 1));
        mData.add(MCoverList.newInstance(coverList));

        MolColumnBean labColumn =
                new MolColumnBean().withTitle("Lab实验室", null).withDesc("入口 >>", "http://lsd.design.com/lab/main");
        mData.add(labColumn);


        MolLayoutBean separatorLayoutBean = MolLayoutBean.getInstance()
                .withBgResId(R.color.default_grey_bg).withLayoutParams(0, DisplayUtils.dip2px(this, 10));
        MolSeparatorBean separatorBean = new MolSeparatorBean().withLayout(separatorLayoutBean);
        mData.add(separatorBean);

        List<MFuncBean> funcList = new ArrayList<>();
        funcList.add(new MFuncBean());
        funcList.add(new MFuncBean());
        funcList.add(new MFuncBean());
        funcList.add(new MFuncBean());
        funcList.add(new MFuncBean());
        funcList.add(new MFuncBean());
        funcList.add(new MFuncBean());
        funcList.add(new MFuncBean());
        mData.add(MFuncList.newInstance(funcList));

        List<MTagBean> tagList = new ArrayList<>();

        tagList.add(new MTagBean("日历控件").withSchema("/main/calendar"));
        tagList.add(new MTagBean("今日话题"));
        tagList.add(new MTagBean("小小屋"));
        tagList.add(new MTagBean("今日话题"));
        tagList.add(new MTagBean("小小屋"));
        tagList.add(new MTagBean("今日话题"));
        tagList.add(new MTagBean("小小屋"));
        tagList.add(new MTagBean("今日话题"));
        tagList.add(new MTagBean("小小屋"));
        mData.add(MTagList.newInstance(tagList));

        mAdapter = new MultiTypeAdapter(mData);
        mAdapter.register(MCoverList.class, new BannerProvider());
        mAdapter.register(MFuncList.class, new FuncProvider());
        mAdapter.register(MTagList.class, new TagProvider());
        mAdapter.register(MCoverBean.class, new CoverProvider());
        mAdapter.register(MImageBean.class, new ImageProvider());
        mAdapter.register(MolColumnBean.class, new ColumnProvider());
        mAdapter.register(MolSeparatorBean.class, new SeparatorProvider());
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initListener() {
        super.initListener();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 800);
            }
        });

        mGradientTopBar.setOnTopBarClickListener(new UIGradientTopBar.OnTopBarClickListener() {
            @Override
            public void onLeftClick(View view) {

            }

            @Override
            public void onRightClick(View view) {
                if (!BuildConfig.isBuildModule) {
                    ActivityUtils.startActivity(MainActivity.this, Intent.ACTION_VIEW, "lab://main", 0);
                } else {
                }
            }
        });
    }

    @OnClick(R.id.permission_tv)
    void onPermissionClick(View view) {
        EasyPermissions.requestPermissions(this, null, 1, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                           Manifest.permission.CAMERA);
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Logger.d(mTag, "onPermissionsGranted", requestCode, perms);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Logger.d(mTag, "onPermissionsGranted", requestCode, perms);

    }

    @Override
    public boolean shouldShowRequestPermissionRationale(@NonNull String permission) {
        return super.shouldShowRequestPermissionRationale(permission);
    }
}
