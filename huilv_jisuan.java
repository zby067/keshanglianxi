package com.example.bmiceshi;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

//计算汇率以及改变汇率程序
public class huilv_jisuan extends AppCompatActivity implements Runnable{
    //初始变量，设为全局，方便更改
    //英镑、欧元、韩元
    double rateDollar = 1.1, rateEuro = 1.2, rateWon = 1.3;
    Handler boxAll;

    //处理函数，将浮点数转为保留两位的浮点数
    public double changeDouble(String s) {
        double res;
        res = Double.parseDouble(String.format("%.2f", Double.parseDouble(s)));
        return res;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huilv_jisuan);
        //如果接收到数据，那么说明进行了改变汇率的操作，进行汇率的保存
        //Intent tent = getIntent();
        //if(tent.getStringExtra("rateDollar") != null) getChangeRate();

        Thread t = new Thread(this);
        t.start();
        changeRateFromNet();
    }

    private void changeRateFromNet() {
        boxAll = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if(msg.what == 5) {
                    //取出来的时候记得强转一下
                    Bundle saveNet = (Bundle) msg.obj;
                    String dollarStr = saveNet.getString("Dollar", "noneValue");
                    String EuroStr = saveNet.getString("Euro", "noneValue");
                    String WonStr = saveNet.getString("Won", "noneValue");
                    rateDollar = Double.parseDouble(dollarStr);
                    rateEuro = Double.parseDouble(EuroStr);
                    rateWon = Double.parseDouble(WonStr);
                    //Log.i(TAG, "WonStr = " + WonStr);
                    //Log.i(TAG, "EuroStr =" + EuroStr);
                    //Log.i(TAG, "dollarStr =" + dollarStr);
                }
                super.handleMessage(msg);
            }
        };
    }

    @Override
    //对返回数据的处理
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 569 && resultCode == 547) {
            rateDollar = changeDouble(data.getStringExtra("rateDollar"));
            rateEuro = changeDouble(data.getStringExtra("rateEuro"));
            rateWon = changeDouble(data.getStringExtra("rateWon"));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /*
    public void getChangeRate() {
        //对改变汇率的请求进行保存
        Intent tent = getIntent();
        Log.i(TAG, "getChangeRate: ok");
        rateDollar = changeDouble(tent.getStringExtra("rateDollar"));
        rateEuro = changeDouble(tent.getStringExtra("rateEuro"));
        rateWon = changeDouble(tent.getStringExtra("rateWon"));
    }
    */

    public void configbtu(View v) {
        //提取方法，选中代码 -> refactor() -> method
        openConfig();
    }

    //打开汇率转换界面
    private void openConfig() {
        //跳转改变汇率界面
        Intent tent = new Intent(this, rate.class);

        //传输原始数据，一是方便打印，二是处理输入为空的情况
        tent.putExtra("rateDollar", rateDollar);
        tent.putExtra("rateEuro", rateEuro);
        tent.putExtra("rateWon", rateWon);

        //跳转
        //startActivity(tent);
        startActivityForResult(tent, 569);
        //Toast.makeText(this,"这里会被执行", Toast.LENGTH_LONG).show();
        //attention:startActivityForResult()不会阻塞下方代码，也就是说Toast语句会在点击config之后直接显示
        //而不是在返回数据之后再显示

        //所以这里是否无效的呢？？？确实无效，因为之前的页面是按照startActivity()打开的，事实上是a->b->a'
        //因此不会执行这里，但是如果是在onCreate()的主函数里进行判空的话，重新打开一个页面也是可以进行判空的
        //getChangeRate();
    }

    //RMB  ->  Won / Euro / Dollar
    public void moneybtu(View v) {
        //没有转之前是范式，所以不能进行findViewById(R.id.inputmon).getText()的操作
        EditText mon = findViewById(R.id.inputmon);
        String monS = mon.getText().toString();

        //输入为空的处理，提示界面
        if(monS.length() == 0) {
            Toast.makeText(this, "请输入金额后转换", Toast.LENGTH_LONG).show();
            return;
        }

        //获取输入金额得出答案
        int id = v.getId();
        double res = 0;
        if(id == R.id.btu_dollar) res = Double.parseDouble(monS) * rateDollar;
        else if(id == R.id.btu_euro) res = Double.parseDouble(monS) * rateEuro;
        else res = Double.parseDouble(monS) * rateWon;

        TextView changemon = findViewById(R.id.monchange);
        changemon.setText(String.valueOf(String.format("%.2f", res)));

    }

    //菜单开启
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //获取菜单资源文件
        getMenuInflater().inflate(R.menu.money_main, menu);

        //是否启动
        return true;
    }

    //菜单按钮选项
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.menuchangemoney) {
            openConfig();
        }
        return super.onOptionsItemSelected(item);
    }

    //子进程
    @Override
    public void run() {
        Log.i(TAG, "run: this is childProject");

        String tarUrl = "https://www.boc.cn/sourcedb/whpj/";
        Bundle tarInfo = new Bundle();
        try {
            //有两个库都有Document类，记得导入Jsoup相关库的那个
            Document tarDoc = Jsoup.connect(tarUrl).get();
            //获取带有汇率的表格
            Element tarTableRateS = tarDoc.getElementsByTag("table").get(1);
            for(Element countryRate:tarTableRateS.getElementsByTag("tr")) {
                //coutryRates每行数据，接下来我要获取每列数据和比对
                Elements countryRateInfoTot = countryRate.getElementsByTag("td");
                //防止数组越界
                if(countryRateInfoTot.size() > 6) {
                    //name = "欧元euro"、"英镑dollar"、"韩国元Won"
                    Element countryNameOne = countryRateInfoTot.get(0);
                    Element countryRateOne = countryRateInfoTot.get(5);
                    //Log.i(TAG, "run: Name = " + countryNameOne.text() + ", Rate = " + countryRateOne.text());

                    //注意区分countryNameOne.toString()和countryNameOne.text()的区别，大致是下面的意思
                    // countryNameOne.toString() = <td>xxx</td>，所以假如使用toString()的话下面返回的是空值
                    // countryNameOne.text() = xxx
                    if((countryNameOne.text()).equals("欧元")) tarInfo.putString("Euro", countryRateOne.text());
                    else if((countryNameOne.text()).equals("英镑")) tarInfo.putString("Dollar", countryRateOne.text());
                    else if((countryNameOne.text()).equals("韩国元")) tarInfo.putString("Won", countryRateOne.text());
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        /*
        这里是通过url进行获取网页信息的数据
        URL tarUrl = null;
        try {
            //创建网页链接 -->  打开链接  -->  获取InputStream流  -->  转String
            tarUrl = new URL("https://www.boc.cn/sourcedb/whpj/");
            HttpURLConnection conn = (HttpURLConnection) tarUrl.openConnection();
            InputStream inTxt = conn.getInputStream();
            String res = readData(inTxt, "utf-8");
            Log.i(TAG, "run UrlMsg:" + res);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        */


        Message msg = boxAll.obtainMessage(5, tarInfo);
        //
        boxAll.sendMessage(msg);
    }

    //修改InputStream --> String
    public static String readData(InputStream inSream, String charsetName) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while( (len = inSream.read(buffer)) != -1 ){
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();
        outStream.close();
        inSream.close();
        return new String(data, charsetName);
    }

}