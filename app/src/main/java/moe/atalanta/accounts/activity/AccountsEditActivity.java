package moe.atalanta.accounts.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.greenrobot.eventbus.EventBus;

import moe.atalanta.accounts.R;
import moe.atalanta.accounts.comm.BaseActivity;
import moe.atalanta.accounts.comm.MessageEvent;
import moe.atalanta.accounts.entity.Accounts;

public class AccountsEditActivity extends BaseActivity {

    EditText etDomain;
    EditText etLabel;
    EditText etUser;
    EditText etPass;
    Button btnSave;
    Accounts accounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts_edit);

        etDomain = findViewById(R.id.et_edit_domain);
        etLabel = findViewById(R.id.et_edit_label);
        etUser = findViewById(R.id.et_edit_username);
        etPass = findViewById(R.id.et_edit_password);
        btnSave = findViewById(R.id.btn_save);

        accounts = (Accounts) getIntent().getSerializableExtra("data");
        if(accounts != null) {
            etDomain.setText(accounts.getDomain());
            etLabel.setText(accounts.getLabel());
            etUser.setText(accounts.getUsername());
            etPass.setText(accounts.getPassword());
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isBlank(etDomain) || isBlank(etLabel) || isBlank(etUser) || isBlank(etPass)){
                    makeText("请全部填写");
                    return;
                }
                new AlertDialog.Builder(getContext())
                        .setTitle("测试")
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Accounts a = new Accounts();
                                if(accounts != null)
                                    a.setId(accounts.getId());
                                a.setDomain(etDomain.getText().toString());
                                a.setLabel(etLabel.getText().toString());
                                a.setUsername(etUser.getText().toString());
                                a.setPassword(etPass.getText().toString());
                                getDaoSession().getAccountsDao().save(a);
                                makeText("保存成功");
                                EventBus.getDefault().post(new MessageEvent(0));
                                finishDelay(500);
                            }
                        })
                        .create()
                        .show();
            }
        });
    }

    private boolean isBlank(EditText et){
        return et == null || et.getText().length() == 0;
    }
}
