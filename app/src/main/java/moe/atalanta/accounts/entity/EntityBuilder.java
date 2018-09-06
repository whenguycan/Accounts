package moe.atalanta.accounts.entity;

import moe.atalanta.accounts.comm.Encrypt;

/**
 * Created by wang on 2018/9/6.
 */

public class EntityBuilder {

	public static Accounts getAccountsFromStringArray(String[] arr) {
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
		return a;
	}

}
