package moe.atalanta.accounts.entity;

import moe.atalanta.accounts.comm.Encrypt;
import moe.atalanta.accounts.comm.StringUtils;

/**
 * Created by wang on 2018/9/6.
 */

public class EntityBuilder {

	public static Accounts getAccountsFromStringArrayPasswordEncrypt(String[] arr) {
		if (arr == null || arr.length == 0)
			return null;
		Accounts a = new Accounts();
		if (arr.length > 0) {
			a.setDomain(StringUtils.isBlank(arr[0]) ? "-" : arr[0]);
		}
		if (arr.length > 1) {
			a.setLabel(StringUtils.isBlank(arr[1]) ? "-" : arr[1]);
		}
		if (arr.length > 2) {
			a.setUsername(StringUtils.isBlank(arr[2]) ? "-" : arr[2]);
		}
		if (arr.length > 3) {
			a.setPassword(Encrypt.encrypt(StringUtils.isBlank(arr[3]) ? "-" : arr[3]));
		}
		if (arr.length > 4) {
			a.setRemarks(StringUtils.isBlank(arr[4]) ? "-" : arr[4]);
		}
		return a;
	}

}
