package com.tenlee.cloudplayer.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tenlee.cloudplayer.R;
import com.tenlee.cloudplayer.activity.MainActivity;
import com.tenlee.cloudplayer.adapter.NetMusicAdapter;
import com.tenlee.cloudplayer.entity.Song;
import com.tenlee.cloudplayer.utils.AppUtils;
import com.tenlee.cloudplayer.utils.NetMusicUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class NetMusicListFragment extends Fragment implements AdapterView.OnItemClickListener,
                                                    View.OnClickListener{

    private MainActivity mainActivity;
    private ListView listView_net_music;
    private LinearLayout load_layout;
    private LinearLayout ll_search_btn_container;
    private LinearLayout ll_search_container;
    private ImageButton search_ImageButton;
    private EditText et_search_content;

    private ArrayList<Song> searchResults = new ArrayList<Song>();
    private NetMusicAdapter netMusicAdapter;
    private int page = 1;//搜索音乐的页码

    public NetMusicListFragment() { }

    public static NetMusicListFragment newInstance() {
        NetMusicListFragment fragment = new NetMusicListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_net_music_list, container, false);

        listView_net_music = (ListView) view.findViewById(R.id.listView_net_music);
        load_layout = (LinearLayout) view.findViewById(R.id.load_layout);
        ll_search_btn_container = (LinearLayout) view.findViewById(R.id.ll_search_btn_container);
        ll_search_container = (LinearLayout) view.findViewById(R.id.ll_search_container);
        search_ImageButton = (ImageButton) view.findViewById(R.id.ib_search_btn);
        et_search_content = (EditText) view.findViewById(R.id.et_search_content);

        listView_net_music.setOnItemClickListener(this);
        listView_net_music.setVisibility(View.VISIBLE);
        ll_search_btn_container.setOnClickListener(this);
        search_ImageButton.setOnClickListener(this);
        loadNetMusic();
        return view;
    }

    private void loadNetMusic() {
        new LoadNetDataTask().execute(NetMusicUtils.BAIDU_MUSIC_DAYHOT_URL);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) getActivity();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_search_btn_container:
                ll_search_btn_container.setVisibility(View.GONE);
                ll_search_container.setVisibility(View.VISIBLE);
                break;
            case R.id.ib_search_btn:
                searchMusic();
                break;
        }
    }

    private void searchMusic() {
        AppUtils.hideInputMethod(et_search_content);


        ll_search_btn_container.setVisibility(View.VISIBLE);
        ll_search_container.setVisibility(View.GONE);
        String key = et_search_content.getText().toString();
        if (TextUtils.isEmpty(key)) {
            Toast.makeText(mainActivity, "请输入关键字", Toast.LENGTH_SHORT).show();
            return;
        }
        load_layout.setVisibility(View.VISIBLE);

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.GET,
                "http://musicuu.com/search/song?key=" + key +"&type=bd",
                new RequestCallBack<String>() {
                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {

                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        searchResults = gsonReader(responseInfo.result);

                        ArrayList<Song> sr = netMusicAdapter.getSongList();
                        sr.clear();
                        sr.addAll(searchResults);
                        netMusicAdapter.notifyDataSetChanged();

                        load_layout.setVisibility(View.GONE);
                        listView_net_music.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                    }
                });

    }

    /**
     * 列表项的单击事件
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position >= netMusicAdapter.getSongList().size() || position < 0) return;
        showDownloadDialog(position);
    }

    private void showDownloadDialog(final int position) {
        DownloadDialogFragment downloadDialogFragment = DownloadDialogFragment.newInstance(
                netMusicAdapter.getSongList().get(position));
    }

    class LoadNetDataTask extends AsyncTask<String, Integer, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            load_layout.setVisibility(View.VISIBLE);
            listView_net_music.setVisibility(View.GONE);
            searchResults.clear();
        }

        @Override
        protected Integer doInBackground(String... params) {
            searchResults = NetMusicUtils.getDayHotMusic(params[0]);
            return 1;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if(integer == 1) {
                netMusicAdapter = new NetMusicAdapter(mainActivity, searchResults);
                listView_net_music.setAdapter(netMusicAdapter);
            }
            load_layout.setVisibility(View.GONE);
            listView_net_music.setVisibility(View.VISIBLE);
        }
    }

    public ArrayList<Song> gsonReader(String json) {
        Gson gson = new Gson();
        InputStream in = new ByteArrayInputStream(json.getBytes());
        Reader r = new InputStreamReader(in);
        Type type = new TypeToken<ArrayList<Song>>(){}.getType();
        ArrayList<Song> list = gson.fromJson(r, type);
//        System.out.println("--------------------------");
//        for (int i = 0; i < list.size(); i++) {
//            System.out.println(list.get(i));
//        }
        return list;
    }
}
