package moe.atalanta.accounts.comm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import moe.atalanta.accounts.entity.DaoSession;

/**
 * Created by wang on 2018/8/28.
 */

public class BaseActivity extends AppCompatActivity {

    protected String TAG = getClass().getSimpleName();

    protected Context getContext(){
        return this;
    }

    protected DaoSession getDaoSession(){
        return MyApplication.getInstance().getDaoSession();
    }

    protected void makeText(Object msg){
        Toast.makeText(getContext(), String.valueOf(msg), Toast.LENGTH_SHORT).show();
    }

    protected void finishDelay(long delay){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, delay);
    }

    protected void startActivity(Class<?> cls){
        Intent intent = new Intent(getContext(), cls);
        startActivity(intent);
    }

    protected void startActivity(Class<?> cls, Bundle bundle){
        Intent intent = new Intent(getContext(), cls);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}
