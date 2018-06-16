package com.example.star.movie4share.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.example.star.movie4share.Movie4ShareApplication;
import com.example.star.movie4share.R;
import com.example.star.movie4share.activity.MovieTypeAction;
import com.example.star.movie4share.activity.ProductDetailActivity;
import com.example.star.movie4share.adapter.ProductAdapter;
import com.example.star.movie4share.dao.ProductDao;
import com.example.star.movie4share.entity.Product;

import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FindNew extends Fragment {

    private List<Product> typeAction = new ArrayList<>();
    private List<Product> typeLove = new ArrayList<>();
    private List<Product> typeFun = new ArrayList<>();

    private OnFragmentInteractionListener mListener;

    public FindNew() {
        // Required empty public constructor
    }

    // TODO: Try if onCreated() is also okay!
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_find_new, null);
        mThread.start();
        return view;
    }

    @Override
    public void onStart(){
        Log.d("cc", "FindNewFragmentStart *star");
        TextView actionList = (TextView) getActivity().findViewById(R.id.movie_type_action_more);
        actionList.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getActivity(), MovieTypeAction.class);
                intent.putExtra("type", "action");
                getActivity().startActivity(intent);
            }
        });

        TextView loveList = (TextView) getActivity().findViewById(R.id.movie_type_love_more);
        loveList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MovieTypeAction.class);
                intent.putExtra("type", "love");
                getActivity().startActivity(intent);
            }
        });

        TextView funList = (TextView) getActivity().findViewById(R.id.movie_type_fun_more);
        funList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MovieTypeAction.class);
                intent.putExtra("type", "fun");
                getActivity().startActivity(intent);
            }
        });

        super.onStart();
    }

    public Thread mThread = new Thread(){
        @Override
        public void run(){
            super.run();

            ProductDao dao = Movie4ShareApplication.getInstances().getDaoSession().getProductDao();
            typeAction = dao.loadAll();
            typeLove = dao.loadAll();   //TODO: change the classify s
            typeFun =  dao.loadAll();

            Message msg = new Message();
            msg.what=1321;
            nHandler.sendMessage(msg);
        }

    };

    public Handler nHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1321:
                    ProductAdapter adapter1 = new ProductAdapter(
                            getActivity(),
                            R.layout.detail_product,
                            typeAction
                    );
                    ProductAdapter adapter2 = new ProductAdapter(
                            getActivity(),
                            R.layout.detail_product,
                            typeLove
                    );
                    ProductAdapter adapter3 = new ProductAdapter(
                            getActivity(),
                            R.layout.detail_product,
                            typeFun
                    );

                    GridView gridView1 = (GridView) getActivity().findViewById(R.id.grid1);
                    gridView1.setAdapter (adapter1);
                    gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Product product = typeAction.get(i);
                            Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("productId", product.getId());
                            intent.putExtras(bundle);
                            getActivity().startActivity(intent);
                        }
                    });
                    GridView gridView2 = (GridView) getActivity().findViewById(R.id.grid2);
                    gridView2.setAdapter(adapter2);
                    gridView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Product product = typeLove.get(i);
//                            Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
//                            Bundle bundle = new Bundle();
//                            bundle.putSerializable("productId", product.getId());
//                            intent.putExtras(bundle);
//                            getActivity().startActivity(intent);
                        }
                    });
                    GridView gridView3 = (GridView) getActivity().findViewById(R.id.grid3);
                    gridView3.setAdapter(adapter3);
                    gridView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Product product = typeFun.get(i);
//                            Intent intent = new Intent(getActivity(), VolunteerApply.class);
//                            Bundle bundle = new Bundle();
//                            bundle.putSerializable("volActivityId", product);
//                            intent.putExtras(bundle);
//                            getActivity().startActivity(intent);
                        }
                    });
            }
        }
    };

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
