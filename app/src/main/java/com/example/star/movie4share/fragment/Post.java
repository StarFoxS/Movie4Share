package com.example.star.movie4share.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.star.movie4share.Movie4ShareApplication;
import com.example.star.movie4share.R;
import com.example.star.movie4share.activity.SellerActivity;
import com.example.star.movie4share.dao.ProductDao;
import com.example.star.movie4share.entity.Product;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Post extends Fragment {

    private final int IMAGE_OPEN = 1;
    private final int TAKE_PHOTO = 2;
    private int phoneNum = 0;

    private EditText etName;
    private EditText etPrice;
    private EditText etStock;
    private EditText etShortDescription;
    private EditText etUrl;
    private GridView gridView;
    private Spinner spinner;
    private String spinnerResult;
    private Button submitBtn;
    private Bitmap bitmap;
    private SimpleAdapter simpleAdapter;
    private ArrayAdapter<String> category;
    private ArrayList<HashMap<String, Object>> imageItem = new ArrayList<>();
    private String strName, strShortDescription, strType, strPrice, strUrl;
    private Double doublePrice;
    private String pathImage;

    private OnFragmentInteractionListener mListener;

    public Post() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        return view;
    }

    @Override
    public void onStart(){
        etName = (EditText) getActivity().findViewById(R.id.post_name_edittext);
        etPrice = (EditText) getActivity().findViewById(R.id.post_price_edittext);
        etStock = (EditText) getActivity().findViewById(R.id.post_stock_edittext);
        etShortDescription = (EditText) getActivity().findViewById(R.id.post_short_des_edittext);
        etUrl = (EditText) getActivity().findViewById(R.id.post_url_edittext);

        gridView = (GridView) getActivity().findViewById(R.id.post_pic_gridview);
        spinner = (Spinner) getActivity().findViewById(R.id.post_category_spinner);
        String[] mCategory = getResources().getStringArray(R.array.spinnerString);
        category = new ArrayAdapter<String>(getContext(), R.layout.spinner, mCategory);
        spinner.setAdapter(category);
        spinnerListener();

        submitBtn = (Button) getActivity().findViewById(R.id.post_submit);
        submitListener();

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.add_pic);
        HashMap<String, Object> map = new HashMap<>();
        map.put("itemImage", bitmap);
        map.put("pathImage", "add_pic");
        imageItem.add(map);
        simpleAdapter = new SimpleAdapter(getActivity(), imageItem, R.layout.grid_add_pic,
                new String[] {"itemImage"}, new int[]{R.id.imageView1});
        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object o, String s) {
                if (view instanceof ImageView && o instanceof Bitmap) {
                    ImageView i = (ImageView) view;
                    i.setImageBitmap((Bitmap) o);
                    return true;
                }
                return false;
            }

        });
        gridView.setAdapter(simpleAdapter);
        gridListener();

        super.onStart();
    }

    private void gridListener(){
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (imageItem.size() == 8) {
                    Toast.makeText(getActivity(), "最多只能上传9张照片！", Toast.LENGTH_SHORT).show();
                }
                else if (position == 0){
                    AddImageDialog();
                }
            }
        });
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if ( i > 0){
                    DeleteDialog(i);
                    ((SimpleAdapter)gridView.getAdapter()).notifyDataSetChanged();
                }

                return false;
            }
        });
    }

    private void spinnerListener(){
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String[] spinnerAll = getResources().getStringArray(R.array.spinnerString);
                spinnerResult = spinnerAll[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                spinnerResult = "";
            }
        });
    }

    private void submitListener(){
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strName = etName.getText().toString();
                strShortDescription = etShortDescription.getText().toString();
                strType = spinnerResult;
                strPrice = etPrice.getText().toString();
                strUrl = etUrl.getText().toString();
                if (!strPrice.isEmpty()){
                    doublePrice = Double.parseDouble(strPrice);
                } else {
                    Toast.makeText(getActivity(), "必须添加价格", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (imageItem.size() == 1){
                    Toast.makeText(getActivity(), "必须添加图片！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (strShortDescription.isEmpty()){
                    Toast.makeText(getActivity(), "必须添加描述！", Toast.LENGTH_SHORT).show();
                    return;
                }


                final ProgressDialog progressDialog = new ProgressDialog(getActivity(),
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("新品发布中...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new Thread(runnable).start();
                        progressDialog.dismiss();
                    }
                },3000);
            }
        });
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            String strImg = (String) imageItem.get(0).get("pathImage");

            //long id, String productName, String category, double price, String description,
            //String shortDescription, String url, String urlDescription, int stockNum, int limitNum,
            //int boughtNum
            Product nProduct = new Product(50, strName, strType, doublePrice, strShortDescription,
                    strShortDescription, strImg, strUrl, 1000, 1000, 0);
            ProductDao productDao = Movie4ShareApplication.getInstances().getDaoSession().getProductDao();
            productDao.insert(nProduct);

            Message msg = new Message();
            msg.what = 133;
            nHandler.sendMessage(msg);

        }
    };

    public Handler nHandler = new Handler(){
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            switch (msg.what){
                case 133:
                    new  AlertDialog.Builder(getActivity())
                            .setTitle("消息" )
                            .setMessage("新品发布成功！" )
                            .setPositiveButton("确定",new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int ii) {
                                    Intent intent = new Intent(getActivity(), SellerActivity.class);
                                    startActivity(intent);
                                }
                            }).setCancelable(false).show();
                    break;
                case 134:
                    break;
            }
        }
    };

    @Override
    public void onResume(){
        super.onResume();
        //获取传递的处理图片在onResume中显示
        //适配器动态显示图片
        if(!TextUtils.isEmpty(pathImage)){
            Toast.makeText(getActivity(),pathImage,Toast.LENGTH_LONG).show();
            BitmapFactory.Options cc = new BitmapFactory.Options();
            cc.inSampleSize=8;

            Bitmap addbmp=BitmapFactory.decodeFile(pathImage,cc);
            HashMap<String, Object> map = new HashMap<>();
            map.put("itemImage", addbmp);
            map.put("pathImage", pathImage);
            imageItem.add(map);
            SimpleAdapter simpleAdapter = (SimpleAdapter) gridView.getAdapter();
            simpleAdapter.notifyDataSetChanged();
            //刷新后释放防止手机休眠后自动添加
            pathImage = null;
        }
    }

    protected void AddImageDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Picture");
        builder.setIcon(R.drawable.ic_launcher_foreground);
        builder.setCancelable(false);
        builder.setItems(new String[] {"本地文件","拍摄","Cancel"},
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        switch(which) {
                            case 0: //本地相册
                                dialog.dismiss();
                                if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                                    Intent intent = new Intent(Intent.ACTION_PICK,
                                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    startActivityForResult(intent, IMAGE_OPEN);
                                } else {
                                    Log.i("DEBUG_TAG", "user do not have this permission!");

                                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                                            Manifest.permission.READ_EXTERNAL_STORAGE)) {

                                        // Show an explanation to the user *asynchronously* -- don't block
                                        // this thread waiting for the user's response! After the user
                                        // sees the explanation, try again to request the permission.
                                        Log.i("DEBUG_TAG", "we should explain why we need this permission!");
                                    } else {

                                        // No explanation needed, we can request the permission.
                                        Log.i("DEBUG_TAG", "==request the permission==");

                                        try {
                                            ActivityCompat.requestPermissions(getActivity(),
                                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                                    1122);
                                        }catch (Throwable e){
                                            e.printStackTrace();
                                        }

                                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                                        // app-defined int constant. The callback method gets the
                                        // result of the request.
                                    }
                                }

                                //通过onResume()刷新数据
                                break;
                            case 1: //手机相机
                                dialog.dismiss();
                                if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)) {
                                    //has permission, do operation directly

                                    File outputImage = new File(Environment.getExternalStorageDirectory(), "suishoupai_image"+String.valueOf(phoneNum)+".jpg");
                                    phoneNum++;
                                    if (phoneNum >= 10){
                                        phoneNum = 0;
                                    }
                                    String pathTakePhoto = outputImage.toString();
                                    try {
                                        if(outputImage.exists()) {
                                            outputImage.delete();
                                        }
                                        outputImage.createNewFile();
                                    } catch(Exception e) {
                                        e.printStackTrace();
                                    }
                                    Uri imageUri = Uri.fromFile(outputImage);
                                    Intent intentPhoto = new Intent("android.media.action.IMAGE_CAPTURE"); //拍照
                                    intentPhoto.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                    try {
                                        startActivityForResult(intentPhoto, TAKE_PHOTO);
                                    }catch(Throwable e){
                                        e.printStackTrace();
                                    }
                                } else {
                                    //do not have permission
                                    Log.i("DEBUG_TAG", "user do not have this permission!");

                                    // Should we show an explanation?
                                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                                            Manifest.permission.CAMERA)) {

                                        // Show an explanation to the user *asynchronously* -- don't block
                                        // this thread waiting for the user's response! After the user
                                        // sees the explanation, try again to request the permission.
                                        Log.i("DEBUG_TAG", "we should explain why we need this permission!");
                                    } else {

                                        // No explanation needed, we can request the permission.
                                        Log.i("DEBUG_TAG", "==request the permission==");

                                        try {
                                            ActivityCompat.requestPermissions(getActivity(),
                                                    new String[]{Manifest.permission.CAMERA},
                                                    3344);
                                        }catch (Throwable e){
                                            e.printStackTrace();
                                        }

                                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                                        // app-defined int constant. The callback method gets the
                                        // result of the request.
                                    }
                                }
                                break;
                            case 2: //取消添加
                                dialog.dismiss();
                                break;
                            default:
                                break;
                        }
                    }
                });
        //显示对话框
        builder.create().show();
    }


    protected void DeleteDialog(final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("确定删除这张图片?");
        builder.setTitle("提示");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                imageItem.remove(position);
                simpleAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


    // TODO: Rename method, update argument and hook method into UI event
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
