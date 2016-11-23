package uestc.arbc;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import uestc.arbc.background.CloudManage;
import uestc.arbc.background.ManageApplication;

/**
 * ready to work
 * Created by CK on 2016/11/6.
 */

public class BeforeWorkActivity extends Activity {

    private final static String TAG = "BeforeWorkActivity";

    private CheckBox checkBoxHeatBoardSwitch;
    private ImageButton imageButtonHeatFL;
    private ImageButton imageButtonHeatFR;
    private ImageButton imageButtonHeatBL;
    private ImageButton imageButtonHeatBR;

    private CheckBox checkBoxRawBoxIgnite;
    private ImageButton imageButtonIgniteFL;
    private ImageButton imageButtonIgniteFR;
    private ImageButton imageButtonIgniteBL;
    private ImageButton imageButtonIgniteBR;

    private int rawNum;
    private boolean heatBoardFL;
    private boolean heatBoardFR;
    private boolean heatBoardBL;
    private boolean heatBoardBR;
    private boolean rawBoxIgniteFL;
    private boolean rawBoxIgniteFR;
    private boolean rawBoxIgniteBL;
    private boolean rawBoxIgniteBR;
    private int customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beforework);
        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int i) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                    }
                }, 1000);
            }
        });
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        init();//初始化
    }

    private void init() {

        //为艾草数选择框填充数据
        Spinner spinnerRawNum = (Spinner) findViewById(R.id.spinnerRawNum);
        if (null != spinnerRawNum) {
            String arr[] = new String[]{"1盒", "2盒", "3盒", "4盒", "5盒"};
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arr);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerRawNum.setAdapter(adapter);
            spinnerRawNum.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.i(TAG,"the raw num is:" + i);
                    rawNum = i;
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    Log.i(TAG,"the raw num is:" + 1);
                    rawNum = 1;
                }
            });
        }

        //为“开启”按钮设置按下行为
        Button buttonOpen = (Button)findViewById(R.id.buttonOpen);
        if (null != buttonOpen) {
            buttonOpen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    open();
                }
            });
        }

        //为“关闭”按钮设置按下行为
        ImageButton imageButtonCancel = (ImageButton)findViewById(R.id.imageButtonCancel);
        if (null != imageButtonCancel) {
            imageButtonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }

        checkBoxHeatBoardSwitch = (CheckBox)findViewById(R.id.checkBoxHeatBoardSwitch);
        imageButtonHeatFL = (ImageButton)findViewById(R.id.imageButtonHeatFL);
        imageButtonHeatFR = (ImageButton)findViewById(R.id.imageButtonHeatFR);
        imageButtonHeatBL = (ImageButton)findViewById(R.id.imageButtonHeatBL);
        imageButtonHeatBR = (ImageButton)findViewById(R.id.imageButtonHeatBR);

        checkBoxRawBoxIgnite = (CheckBox)findViewById(R.id.checkBoxRawBoxIgnite);
        imageButtonIgniteFL = (ImageButton)findViewById(R.id.imageButtonIgniteFL);
        imageButtonIgniteFR = (ImageButton)findViewById(R.id.imageButtonIgniteFR);
        imageButtonIgniteBL = (ImageButton)findViewById(R.id.imageButtonIgniteBL);
        imageButtonIgniteBR = (ImageButton)findViewById(R.id.imageButtonIgniteBR);

        setState(imageButtonHeatFL,true);
        setState(imageButtonHeatFR,true);
        setState(imageButtonHeatBL,true);
        setState(imageButtonHeatBR,true);
        setState(imageButtonIgniteFL,true);
        setState(imageButtonIgniteFR,true);
        setState(imageButtonIgniteBL,true);
        setState(imageButtonIgniteBR,true);

        checkBoxHeatBoardSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox)view).isChecked()) {
                    setState(imageButtonHeatFL,true);
                    setState(imageButtonHeatFR,true);
                    setState(imageButtonHeatBL,true);
                    setState(imageButtonHeatBR,true);
                } else {
                    setState(imageButtonHeatFL,false);
                    setState(imageButtonHeatFR,false);
                    setState(imageButtonHeatBL,false);
                    setState(imageButtonHeatBR,false);
                }
            }
        });
        imageButtonHeatFL.setOnClickListener(heatListener);
        imageButtonHeatFR.setOnClickListener(heatListener);
        imageButtonHeatBL.setOnClickListener(heatListener);
        imageButtonHeatBR.setOnClickListener(heatListener);

        checkBoxRawBoxIgnite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox)view).isChecked()) {
                    setState(imageButtonIgniteFL,true);
                    setState(imageButtonIgniteFR,true);
                    setState(imageButtonIgniteBL,true);
                    setState(imageButtonIgniteBR,true);
                } else {
                    setState(imageButtonIgniteFL,false);
                    setState(imageButtonIgniteFR,false);
                    setState(imageButtonIgniteBL,false);
                    setState(imageButtonIgniteBR,false);
                }
            }
        });
        imageButtonIgniteFL.setOnClickListener(igniteListener);
        imageButtonIgniteFR.setOnClickListener(igniteListener);
        imageButtonIgniteBL.setOnClickListener(igniteListener);
        imageButtonIgniteBR.setOnClickListener(igniteListener);

    }

    private void setState(ImageButton imageButton, Boolean state) {
        if (state) {
            imageButton.setBackgroundResource(R.color.colorYellow);
            imageButton.setTag(true);
        } else {
            imageButton.setBackgroundResource(R.color.colorTransparent);
            imageButton.setTag(false);
        }
    }

    View.OnClickListener heatListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if ((Boolean) view.getTag()) {
                setState((ImageButton) view,false);
                checkBoxHeatBoardSwitch.setChecked(false);
            } else {
                setState((ImageButton) view,true);
                if ((Boolean)imageButtonHeatFL.getTag()&&(Boolean)imageButtonHeatFR.getTag()&&(Boolean)imageButtonHeatBL.getTag()&&(Boolean)imageButtonHeatBR.getTag()) {
                    checkBoxHeatBoardSwitch.setChecked(true);
                }
            }
        }
    };

    View.OnClickListener igniteListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if ((Boolean)view.getTag()) {
                setState((ImageButton) view,false);
                checkBoxRawBoxIgnite.setChecked(false);
            } else {
                setState((ImageButton) view,true);
                if ((Boolean)imageButtonIgniteFL.getTag()&&(Boolean)imageButtonIgniteFR.getTag()&&(Boolean)imageButtonIgniteBL.getTag()&&(Boolean)imageButtonIgniteBR.getTag()) {
                    checkBoxRawBoxIgnite.setChecked(true);
                }
            }
        }
    };


    private JSONObject getStartSetting() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("rawNum",rawNum);
            jsonObject.put("heatboardFL",(boolean)imageButtonHeatFL.getTag());
            jsonObject.put("heatboardFR",(boolean)imageButtonHeatFR.getTag());
            jsonObject.put("heatboardBL",(boolean)imageButtonHeatBL.getTag());
            jsonObject.put("heatboardBR",(boolean)imageButtonHeatBR.getTag());
            jsonObject.put("rawBoxIgniteFL",(boolean)imageButtonIgniteFL.getTag());
            jsonObject.put("rawBoxIgniteFR",(boolean)imageButtonIgniteFR.getTag());
            jsonObject.put("rawBoxIgniteBL",(boolean)imageButtonIgniteBL.getTag());
            jsonObject.put("rawBoxIgniteBR",(boolean)imageButtonIgniteBR.getTag());
            jsonObject.put("customer",customer);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return jsonObject;
    }

    public void open() {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonData;
        try {
            jsonObject.put("token",ManageApplication.getInstance().getCloudManage().getDeviceID());
            jsonObject.put("require","PAD_StartMachine");
            jsonData = getStartSetting();
            if (null == jsonData) {
                Log.i(TAG,"启动数据获取失败");
                return;
            }
            jsonObject.put("data",jsonData);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i(TAG,"启动数据时JSONException");
            return;
        }

        new StartAsyncTask().execute(jsonObject);
    }

    class StartAsyncTask extends AsyncTask<JSONObject,Integer,Integer> {
        JSONObject jsonObjectResponse;
        ProgressDialog dialog = new ProgressDialog(BeforeWorkActivity.this);
        @Override
        protected void onPreExecute() {

            dialog.setTitle("启动提示");
            dialog.setMessage("正在启动...");
            dialog.setCancelable(false);
            dialog.show();
        }


        @Override
        protected Integer doInBackground(JSONObject... jsonObjects) {
            JSONObject jsonObject = jsonObjects[0];
            CloudManage cloudManage = ((ManageApplication)getApplication()).getCloudManage();
            jsonObjectResponse = cloudManage.upload(jsonObject);
            if (null == jsonObjectResponse) {
                return -2;//-2表示上传出错，没有得到服务器回应
            } else {
                int errorCode;
                try {
                    errorCode = jsonObjectResponse.getInt("errorCode");
                } catch (JSONException e) {
                    e.printStackTrace();
                    return -2;
                }
                return errorCode;
            }
        }

        @Override
        protected void onPostExecute(Integer errorCode) {
            dialog.dismiss();
            switch (errorCode) {
                case -2:
                    Toast.makeText(BeforeWorkActivity.this,"启动失败，与服务器通信异常",Toast.LENGTH_LONG).show();
                    break;
                case -1:
                    try {
                        String msg = jsonObjectResponse.getString("message");
                        Toast.makeText(BeforeWorkActivity.this,msg,Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(BeforeWorkActivity.this,"启动失败，获取的服务器数据异常",Toast.LENGTH_LONG).show();
                    }
                    break;
                case 0:
                    Toast.makeText(BeforeWorkActivity.this,"启动成功",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent();
                    intent.setClass(BeforeWorkActivity.this,WorkMainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                default:
                    break;
            }
        }
    }
}
