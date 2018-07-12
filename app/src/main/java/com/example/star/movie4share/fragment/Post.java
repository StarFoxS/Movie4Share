package com.example.star.movie4share.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
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
    private Spinner spinner;
    private String spinnerResult;
    private Button submitBtn;
    private ArrayAdapter<String> category;
    private String strName, strShortDescription, strType, strPrice, strUrl;
    private Double doublePrice;


    // upload pics needs
    private GridView mGridView;
    private String pathImage;
    private ArrayList<HashMap<String, Object>> imageItem = new ArrayList<>();
    private Bitmap bitmap;
    private SimpleAdapter simpleAdapter;
    private String pathTakePhoto;
    private Uri imageUri;
    private long NumPhoto = 0;

    private OnFragmentInteractionListener mListener;

    public Post() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_post, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        etName = (EditText) getActivity().findViewById(R.id.post_name_edittext);
        etPrice = (EditText) getActivity().findViewById(R.id.post_price_edittext);
        etStock = (EditText) getActivity().findViewById(R.id.post_stock_edittext);
        etShortDescription = (EditText) getActivity().findViewById(R.id.post_short_des_edittext);
        etUrl = (EditText) getActivity().findViewById(R.id.post_url_edittext);

        mGridView = (GridView) getActivity().findViewById(R.id.post_pic_gridview);
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
        simpleAdapter = new SimpleAdapter(getContext(), imageItem, R.layout.grid_add_pic,
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
        mGridView.setAdapter(simpleAdapter);

        gridListener();

    }


    @Override
    public void onStart(){


        super.onStart();
    }

    private void gridListener(){
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if ( i > 0){
                    deleteDialog(i);
                    ((SimpleAdapter)mGridView.getAdapter()).notifyDataSetChanged();
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
                switch (spinnerResult){
                    case "动作片":
                        strType = "action";
                        break;
                    case "爱情片":
                        strType = "love";
                        break;
                    case "喜剧片":
                        strType = "fun";
                        break;
                }
//                strType = spinnerResult;
                strPrice = etPrice.getText().toString();
                strUrl = etUrl.getText().toString();
                if (!strPrice.isEmpty()){
                    doublePrice = Double.parseDouble(strPrice);
                } else {
                    Toast.makeText(getActivity(), "必须添加价格", Toast.LENGTH_SHORT).show();
                    return;
                }
//                if (imageItem.size() == 1){
//                    Toast.makeText(getActivity(), "必须添加图片！", Toast.LENGTH_SHORT).show();
//                    return;
//                }
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
                },2000);
            }
        });
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            Log.d("cc", "imageItem Size:" + imageItem.size());
//            for (int i = 1; i < imageItem.size(); i++){
//                String strImg = (String) imageItem.get(i).get("pathImage");
//            }
//            String strImg = (String) imageItem.get(1).get("pathImage");
            String strImg = pathImage;
            Log.i("cc", "strImg:" + strImg);
            ProductDao productDao = Movie4ShareApplication.getInstances().getDaoSession().getProductDao();
            Log.d("cc", "insert into product:" + strName + " " + strType);

            //long id, String productName, String category, double price, String description,
            //String shortDescription, String url, String urlDescription, int stockNum, int limitNum,
            //int boughtNum
            try {
                Product nProduct = new Product(50, strName, strType, doublePrice, strShortDescription,
                        strShortDescription, strImg, strUrl, 1000, 1000, 0);
                productDao.insert(nProduct);
            } catch (Exception e){
                Product nProduct = new Product(51, strName, strType, doublePrice, strShortDescription,
                        strShortDescription, strImg, strUrl, 1000, 1000, 0);
                productDao.insert(nProduct);
            }

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
                                    intent.putExtra("casefragment","refreshproduct");
                                    startActivity(intent);
                                    getActivity().finish();
                                }
                            }).setCancelable(false).show();
                    break;
                default:
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
            Log.d("cc", "pathImage:" + pathImage);
            Toast.makeText(getActivity(),pathImage,Toast.LENGTH_LONG).show();
            submitListener();
            BitmapFactory.Options cc = new BitmapFactory.Options();
            cc.inSampleSize=8;

            Bitmap addbmp=BitmapFactory.decodeFile(pathImage,cc);
            HashMap<String, Object> map = new HashMap<>();
            map.put("itemImage", addbmp);
            map.put("pathImage", pathImage);
            imageItem.add(map);
            SimpleAdapter simpleAdapter = (SimpleAdapter) mGridView.getAdapter();
            simpleAdapter.notifyDataSetChanged();
            //刷新后释放防止手机休眠后自动添加
//            pathImage = null;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        //打开图片
        if(resultCode== Activity.RESULT_OK && requestCode==IMAGE_OPEN) {
            Uri uri = data.getData();
            if (!TextUtils.isEmpty(uri.getAuthority())) {
                //查询选择图片
                Cursor cursor = getActivity().getContentResolver().query(
                        uri,
                        new String[] { MediaStore.Images.Media.DATA },
                        null,
                        null,
                        null);
                //返回 没找到选择图片
                if (null == cursor) {
                    Log.i("FIND PIC","can't find pic");
                    return;
                }
                //光标移动至开头 获取图片路径
                cursor.moveToFirst();
                pathImage = cursor.getString(cursor
                        .getColumnIndex(MediaStore.Images.Media.DATA));
                //向处理活动传递数据
                //Toast.makeText(this, path, Toast.LENGTH_SHORT).show();

            } else {
                pathImage = uri.getPath();

            }
        }  //end if 打开图片
        //获取图片

        //拍照
        if(resultCode==Activity.RESULT_OK && requestCode==TAKE_PHOTO) {
            Intent intent = new Intent("com.android.camera.action.CROP"); //剪裁
            intent.setDataAndType(imageUri, "image/*");
            intent.putExtra("scale", true);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            //广播刷新相册
            Intent intentBc = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intentBc.setData(imageUri);
            getActivity().sendBroadcast(intentBc);
            //向处理活动传递数据
            pathImage = pathTakePhoto;
        }
    }

    protected void AddImageDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("添加照片");
        builder.setIcon(R.drawable.ic_menu_gallery);
        builder.setCancelable(true);
        builder.setItems(new String[] {"本地文件","拍照","放弃添加"},
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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
                                    Log.i("DEBUG_TAG", "user do not have this permission!");

                                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                                            Manifest.permission.CAMERA)) {

                                        Log.i("DEBUG_TAG", "we should explain why we need this permission!");
                                    } else {

                                        Log.i("DEBUG_TAG", "==request the permission==");

                                        try {
                                            ActivityCompat.requestPermissions(getActivity(),
                                                    new String[]{Manifest.permission.CAMERA},
                                                    3344);
                                        }catch (Throwable e){
                                            e.printStackTrace();
                                        }

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
//        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
//        builder.setTitle("hahaha");
//        builder.create().show();
    }

    protected void deleteDialog(final int position){
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


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1122: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent intent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, IMAGE_OPEN);
                    Log.i("DEBUG_TAG", "user granted the permission!");

                } else {
                    Log.i("DEBUG_TAG", "user denied the permission!");
                }
                return;
            }

            case 3344: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    File outputImage = new File(Environment.getExternalStorageDirectory(), "suishoupai_image"+String.valueOf(NumPhoto)+".jpg");
                    NumPhoto++;
                    if (NumPhoto >= 10) NumPhoto = 0;
                    pathTakePhoto = outputImage.toString();
                    try {
                        if(outputImage.exists()) {
                            outputImage.delete();
                        }
                        outputImage.createNewFile();
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                    imageUri = Uri.fromFile(outputImage);
                    Intent intentPhoto = new Intent("android.media.action.IMAGE_CAPTURE"); //拍照
                    intentPhoto.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    try {
                        startActivityForResult(intentPhoto, TAKE_PHOTO);
                    }catch(Throwable e){
                        e.printStackTrace();
                    }

                } else {

                    Log.i("DEBUG_TAG", "user denied the permission!");
                }
                return;
            }

        }
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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
