package com.doyoteam.fisher.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.doyoteam.fisher.Constants;
import com.doyoteam.fisher.R;
import com.doyoteam.fisher.db.bean.City;
import com.doyoteam.fisher.db.bean.Post;
import com.doyoteam.fisher.net.HttpClient;
import com.doyoteam.fisher.net.HttpIn;
import com.doyoteam.fisher.net.config.ConfigHttpIn;
import com.doyoteam.fisher.net.config.ConfigHttpOut;
import com.doyoteam.ui.CityPopWindow;
import com.doyoteam.util.DES3Utils;
import com.doyoteam.util.Tools;

import java.util.ArrayList;
import java.util.List;

import Decoder.BASE64Encoder;

/**
 * 首页
 *
 */
public class HomeFragment extends Fragment implements View.OnClickListener{

    private CityPopWindow popWindow;
    private List<Post> postList;
    private GroupRvAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化数据
        initData(false);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        //初始化界面
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView)
    {
        //导航栏
        TextView tv_city = (TextView) rootView.findViewById(R.id.title_bar_city);//城市
        tv_city.setOnClickListener(this);
        Tools.renderBackground(getActivity(), tv_city, R.drawable.bg_material_button);
        popWindow = new CityPopWindow(this, tv_city);
        tv_city.setText(popWindow.getShowCity().name);

        rootView.findViewById(R.id.title_bar_action_image).setOnClickListener(this);

        TextView tv_search = (TextView) rootView.findViewById(R.id.title_bar_search);
        tv_search.setOnClickListener(this);
        Tools.renderBackground(getActivity(), tv_search, R.drawable.bg_material_button);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mAdapter);
    }
    //初始化数据
    private void initData(boolean needUpdateFlag)
    {
        getConfigNet();
        postList = new ArrayList<>();
        mAdapter = new GroupRvAdapter(getActivity(), postList, true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_bar_city:
//                popWindow.show();
            case R.id.title_bar_action_image:
                try {
                    check();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }
    private void check() throws Exception {
        BASE64Encoder encoder = new BASE64Encoder();
        String msg = "Hello World";
        System.out.println("【加密前】：" + msg);

        // 加密
        byte[] secretArr = DES3Utils.encryptMode(msg.getBytes());
        System.out.println("【加密后】：" + encoder.encode(secretArr));

        // 解密
        byte[] myMsgArr = DES3Utils.decryptMode(secretArr);
        System.out.println("【解密后】：" + new String(myMsgArr));
    }
    private void getConfigNet()
    {
        ConfigHttpIn request = new ConfigHttpIn();
        request.setActionListener(new HttpIn.ActionListener<ConfigHttpOut>() {
            @Override
            public void onSuccess(ConfigHttpOut result) {
                String status = result.getStatus();
                if (status.equals("SUCCESS")) {
                    Tools.showToast(result.getForumArrayList().size()+"");
                } else {
                    Tools.showToast(status);
                }
            }

            @Override
            public void onFailure(String errInfo) {
                Tools.showToast(errInfo);
            }
        });
        HttpClient.post(request);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Constants.REQUEST_CODE_CITY:
                    popWindow.updateCity((City) data.getSerializableExtra("CITY"));
                    break;
            }
        }
    }
}

