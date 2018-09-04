package moe.atalanta.accounts.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Stack;

import moe.atalanta.accounts.R;
import moe.atalanta.accounts.comm.BaseActivity;
import moe.atalanta.accounts.comm.MessageEvent;
import moe.atalanta.accounts.entity.Accounts;
import moe.atalanta.accounts.entity.AccountsDao;

public class AccountsActivity extends BaseActivity {

    Button btn;
    SearchView sv;
    SwipeRefreshLayout srl;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_accounts);
        srl = findViewById(R.id.swipe_refresh_layout_0);
        lv = findViewById(R.id.lv_0);
        btn = findViewById(R.id.btn_add);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(AccountsEditActivity.class);
            }
        });
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadListView();
                srl.setRefreshing(false);
            }
        });
        sv = findViewById(R.id.sv_search);
        sv.onActionViewExpanded();
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                reloadListView();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                reloadListView();
                return true;
            }
        });
        reloadListView();
    }

    private void reloadListView() {
        final List<Accounts> list = queryList();
        lv.setAdapter(new ArrayAdapter<Accounts>(getContext(), R.layout.item, list) {
            @NonNull
            @Override
            public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View root = getLayoutInflater().inflate(R.layout.item, null);
                final Accounts a = list.get(position);
                TextView tv0 = root.findViewById(R.id.tv_0);
                tv0.setText(a.getDomain());
                tv0.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("data", list.get(position));
                        startActivity(AccountsEditActivity.class, bundle);
                    }
                });
                Button btn0 = root.findViewById(R.id.btn_0);
                btn0.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(getContext())
                                .setTitle("Sure To Delete [ " + a.getDomain() + " ] ?")
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        getDaoSession().getAccountsDao().deleteByKey(a.getId());
                                        makeText("删除成功");
                                        reloadListView();
                                    }
                                })
                                .create()
                                .show();
                    }
                });
                return root;
            }
        });
    }

    private List<Accounts> queryList(){
        AccountsDao dao = getDaoSession().getAccountsDao();
        if(sv.getQuery().length() != 0){
            return dao.queryBuilder().where(AccountsDao.Properties.Domain.like(sv.getQuery() + "%")).orderAsc(AccountsDao.Properties.Id).list();
        }
        return dao.loadAll();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMessage(MessageEvent em){
        if(em.code() == 0){
            reloadListView();
        }
    }
}
