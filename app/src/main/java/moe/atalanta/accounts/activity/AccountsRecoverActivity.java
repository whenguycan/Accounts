package moe.atalanta.accounts.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import moe.atalanta.accounts.R;
import moe.atalanta.accounts.comm.BaseActivity;
import moe.atalanta.accounts.comm.Comm;
import moe.atalanta.accounts.comm.Encrypt;
import moe.atalanta.accounts.comm.MessageEvent;
import moe.atalanta.accounts.comm.StringUtils;
import moe.atalanta.accounts.entity.Accounts;
import moe.atalanta.accounts.entity.AccountsDao;
import moe.atalanta.accounts.entity.EntityBuilder;

public class AccountsRecoverActivity extends BaseActivity {

	ListView lv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accounts_recover);

		File root = new File(Comm.MY_FILES_DIR);
		final List<String> list = new ArrayList<>();
		if (root != null && root.exists() && root.isDirectory()) {
			File[] files = root.listFiles();
			if (files != null && files.length != 0) {
				for (File file : files) {
					String filename = file.getName();
					if (filename.indexOf("backup") == 0) {
						list.add(filename);
					}
				}
			}
		}
		Collections.sort(list, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				long t1 = Long.parseLong(o1.replace("backup", "").replace(".txt", ""));
				long t2 = Long.parseLong(o2.replace("backup", "").replace(".txt", ""));
				return (int) (t2 - t1);
			}
		});
		lv = findViewById(R.id.lv_recover_files);
		lv.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, list));
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
				new AlertDialog.Builder(getContext())
						.setTitle("数据将被覆盖，确定导入备份 [ " + list.get(position) + " ] 吗？")
						.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {

							}
						})
						.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								recover(list.get(position));
								makeText("备份恢复成功");
								EventBus.getDefault().post(MessageEvent.AccountsActivityReload);
								finishDelay(500);
							}
						})
						.create()
						.show();
			}
		});
	}

	private void recover(String filename) {
		File file = new File(Comm.MY_FILES_DIR, filename);
		if (file == null || !file.exists())
			Log.e(TAG, "recover: file not exist: filename: " + filename);
		String sql = "update " + getDaoSession().getAccountsDao().getTablename() + " set " + AccountsDao.Properties.OnUse.columnName + " = " + Accounts.ON_USE_NO;
		getDaoSession().getAccountsDao().getDatabase().execSQL(sql);
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			List<Accounts> list = new ArrayList<>();
			String line;
			while ((line = reader.readLine()) != null) {
				if(StringUtils.isNotBlank(line)){
					String x = Encrypt.decrypt(line.replace(Comm.CRLF, ""));
					list.add(EntityBuilder.getAccountsFromStringArray(x.split(Comm.ACCOUNTS_LINK_SEPARATOR)));
				}
			}
			getDaoSession().getAccountsDao().saveInTx(list);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
