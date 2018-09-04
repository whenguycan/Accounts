package moe.atalanta.accounts.comm;

/**
 * Created by wang on 2018/8/30.
 */

public class MessageEvent {

    private int code;

    private String message;

    public MessageEvent(int code){
        this.code = code;
    }

    public MessageEvent(int code, String message){
        this.code = code;
        this.message = message;
    }

    public int code(){
        return code;
    }

    public String message(){
        return message;
    }

}
