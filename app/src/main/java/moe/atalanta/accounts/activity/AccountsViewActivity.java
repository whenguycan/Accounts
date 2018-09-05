package moe.atalanta.accounts.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import moe.atalanta.accounts.R;
import moe.atalanta.accounts.comm.BaseActivity;
import moe.atalanta.accounts.comm.Comm;
import moe.atalanta.accounts.comm.Encrypt;
import moe.atalanta.accounts.comm.MessageEvent;
import moe.atalanta.accounts.comm.StringUtils;
import moe.atalanta.accounts.entity.Accounts;

public class AccountsViewActivity extends BaseActivity {

	TextView tvDomain;
	TextView tvLabel;
	TextView tvUser;
	TextView tvPass;
	TextView tvRemarks;

	Button btnEdit;
	Accounts accounts;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accounts_view);
		EventBus.getDefault().register(this);

		accounts = (Accounts) getIntent().getSerializableExtra(Comm.BUNDLE_KEY_DATA);

		tvDomain = findViewById(R.id.tv_edit_domain);
		tvLabel = findViewById(R.id.tv_edit_label);
		tvUser = findViewById(R.id.tv_edit_username);
		tvPass = findViewById(R.id.tv_edit_password);
		tvRemarks = findViewById(R.id.tv_edit_remarks);

		if (accounts != null) {
			tvDomain.setText(accounts.getDomain());
			tvLabel.setText(accounts.getLabel());
			tvUser.setText(accounts.getUsername());
			if (StringUtils.isNotBlank(accounts.getPassword()))
				tvPass.setText(Encrypt.decrypt(accounts.getPassword()));
			tvRemarks.setText(accounts.getRemarks());
		}

		btnEdit = findViewById(R.id.btn_edit);
		btnEdit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putSerializable(Comm.BUNDLE_KEY_DATA, accounts);
				startActivity(AccountsEditActivity.class, bundle);
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onEventMessage(MessageEvent me) {
		if (MessageEvent.AccountsViewActivityFinish.equals(me)) {
			finish();
		}
	}
}
