package moe.atalanta.accounts.comm;

/**
 * Created by wang on 2018/8/30.
 */

public class MessageEvent {

	/**
	 * MessageEvent 操作码，code为4位16进制字符串，范围：0000-FFFF
	 * 前2位为Activity标识，后2位为操作码，0000-00FF为保留操作码，暂不使用
	 */
	public static final MessageEvent AccountsActivityReload = new MessageEvent("0101", "");
	public static final MessageEvent AccountsViewActivityFinish = new MessageEvent("0201", "");

	private String code;
	private String message;

	public MessageEvent(String code) {
		this.code = code;
	}

	public MessageEvent(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String code() {
		return code;
	}

	public String message() {
		return message;
	}

	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof MessageEvent))
			return false;
		MessageEvent me = (MessageEvent) obj;
		return this.code.equals(me.code);
	}
}
