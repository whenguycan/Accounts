package moe.atalanta.accounts.comm;

import android.os.Environment;

import java.io.File;

/**
 * Created by wang on 2018/9/5.
 */

public class Comm {

	public static final String BUNDLE_KEY_DATA = "data";

	public static final String CR = "\r";
	public static final String LF = "\n";

	public static final String ACCOUNTS_LINK_SEPARATOR = "=";

	public static final String MY_FILES_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Tencent" + File.separator + "QQfile_recv";

}
