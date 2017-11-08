package org.asocframework.support.tools;

import java.util.Date;

/**
 * @author dhj
 * @version $Id: BizUtils ,v 0.1 2016/12/16 11:41 dhj Exp $
 * @name
 */
public class BizUtils {

    private static ThreadLocal<String> bizId = new InheritableThreadLocal();

    private BizUtils() {

    }

    public  static void BizInit(){
        compelInit();
    }


    public static void removeBizId(){
        bizId.remove();
    }


    public static void compelInit(){
        bizId.set(createBizId());
    }

    @Deprecated
    public static void setBizId(String id){
        bizId.set(id);
    }

    public static String getBizId(){
        try{
            String bizValue = bizId.get();
            if(bizValue==null){
                bizValue = createBizId();
                bizId.set(bizValue);
            }
            return bizValue;
        }catch (RuntimeException e){
            return "100000000000";
        }
    }

    /**
     *
     * @return
     */
    private static String createBizId(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(DateUtils.formatDate(new Date(), "yyMMddHHmmssSSSS"));
        Double random = (Math.random()*9+1)*10000000;
        stringBuilder.append(random.longValue());
        return stringBuilder.toString();
    }

}
