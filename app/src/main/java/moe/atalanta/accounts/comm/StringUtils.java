package moe.atalanta.accounts.comm;

/**
 * Created by wang on 2018/8/28.
 */

public class StringUtils {

	public static boolean isBlank(String str) {
		return str == null || str.isEmpty();
	}

	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}

}
