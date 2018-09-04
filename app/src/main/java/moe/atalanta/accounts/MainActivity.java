package moe.atalanta.accounts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

import moe.atalanta.accounts.activity.AccountsActivity;
import moe.atalanta.accounts.comm.BaseActivity;
import moe.atalanta.accounts.comm.Encrypt;
import moe.atalanta.accounts.comm.ListUtils;
import moe.atalanta.accounts.comm.StringUtils;
import moe.atalanta.accounts.entity.Users;
import moe.atalanta.accounts.entity.UsersDao;

public class MainActivity extends BaseActivity {

    EditText etUser;
    EditText etPass;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUsers();
        etUser = findViewById(R.id.et_username);
        etPass = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUser.getText().toString();
                String password = etPass.getText().toString();
                if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
                    makeText("username or password is null");
                    return;
                }
                List<Users> list = getDaoSession().getUsersDao().queryBuilder().where(UsersDao.Properties.Username.eq(username)).list();
                if (ListUtils.isBlank(list)) {
                    makeText("user not found");
                    return;
                }
                boolean eq = Encrypt.checkPassword(password, list.get(0).getPassword());
                if (!eq) {
                    makeText("password error");
                    return;
                        }
                Intent intent = new Intent(getContext(), AccountsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initUsers() {
        getDaoSession().getUsersDao().deleteAll();
        Users users = new Users(null, "atalanta", Encrypt.encodePassword("lovehotel"));
        getDaoSession().getUsersDao().save(users);
    }
}
