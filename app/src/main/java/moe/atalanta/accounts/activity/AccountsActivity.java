package moe.atalanta.accounts.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import moe.atalanta.accounts.R;
import moe.atalanta.accounts.comm.BaseActivity;
import moe.atalanta.accounts.comm.Comm;
import moe.atalanta.accounts.comm.Encrypt;
import moe.atalanta.accounts.comm.MessageEvent;
import moe.atalanta.accounts.entity.Accounts;
import moe.atalanta.accounts.entity.AccountsDao;

public class AccountsActivity extends BaseActivity {

	Button btnAdd;
	Button btnBackup;
	Button btnRecover;
	Button btnImport;
	Button btnSync;

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
		btnAdd = findViewById(R.id.btn_add);
		btnBackup = findViewById(R.id.btn_backup);
		btnRecover = findViewById(R.id.btn_recover);
		btnImport = findViewById(R.id.btn_import);
		btnSync = findViewById(R.id.btn_sync);
		btnAdd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(AccountsEditActivity.class);
			}
		});
		btnBackup.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				backup();
			}
		});
		btnRecover.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				recover();
			}
		});
		btnImport.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(getContext())
						.setTitle("确定导入吗？")
						.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {

							}
						})
						.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								import1();
								reloadListView();
							}
						})
						.create()
						.show();
			}
		});
		btnSync.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				sync();
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
						bundle.putSerializable(Comm.BUNDLE_KEY_DATA, list.get(position));
						startActivity(AccountsViewActivity.class, bundle);
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

	private List<Accounts> queryList() {
		AccountsDao dao = getDaoSession().getAccountsDao();
		if (sv.getQuery().length() != 0) {
			return dao.queryBuilder().where(AccountsDao.Properties.Domain.like(sv.getQuery() + "%")).orderAsc(AccountsDao.Properties.Id).list();
		}
		return dao.loadAll();
	}

	private void recover() {

	}

	private void backup() {

	}

	private void import1() {
		File file = new File(getMyFilesDir(), "import.txt");
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			List<Accounts> list = new ArrayList<>();
			String line;
			while ((line = reader.readLine()) != null) {
				list.add(getAccountsFromStringArray(line.split("/")));
			}
			getDaoSession().getAccountsDao().saveInTx(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Accounts getAccountsFromStringArray(String[] arr) {
		if (arr == null || arr.length == 0)
			return null;
		Accounts a = new Accounts();
		if (arr.length > 0)
			a.setDomain(arr[0]);
		if (arr.length > 1)
			a.setLabel(arr[1]);
		if (arr.length > 2)
			a.setUsername(arr[2]);
		if (arr.length > 3)
			a.setPassword(Encrypt.encrypt(arr[3]));
		if (arr.length > 4)
			a.setRemarks(arr[4]);
		Log.e(TAG, "getAccountsFromStringArray: " + Encrypt.encrypt(arr[3]));
		return a;
	}

	private void sync() {

	}

	private String getMyFilesDir() {
		File root = Environment.getExternalStorageDirectory();
		return root.getAbsolutePath() + File.separator + "Tencent" + File.separator + "QQfile_recv";
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onEventMessage(MessageEvent me) {
		if (MessageEvent.AccountsActivityReload.equals(me)) {
			reloadListView();
		}
	}
}
