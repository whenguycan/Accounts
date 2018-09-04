package moe.atalanta.accounts.comm;

import java.util.List;

/**
 * Created by wang on 2018/8/28.
 */

public class ListUtils {

    public static boolean isBlank(List<?> list){
        return list == null || list.isEmpty();
    }

    public static boolean isNotBlank(List<?> list){
        return !isBlank(list);
    }

}
