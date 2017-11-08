package org.asocframework.support.tools;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @author dhj
 * @version $Id: MoneyUtil ,v 0.1 2016/10/31 17:18 dhj Exp $
 * @name
 */
public class MoneyUtils {


    public static String changeY2F(String amount){
        String currency =  amount.replaceAll("\\$|\\￥|\\,", "");  //处理包含, ￥ 或者$的金额
        int index = currency.indexOf(".");
        int length = currency.length();
        Long amLong = 0l;
        if(index == -1){
            amLong = Long.valueOf(currency+"00");
        }else if(length - index >= 3){
            amLong = Long.valueOf((currency.substring(0, index+3)).replace(".", ""));
        }else if(length - index == 2){
            amLong = Long.valueOf((currency.substring(0, index+2)).replace(".", "")+0);
        }else{
            amLong = Long.valueOf((currency.substring(0, index+1)).replace(".", "")+"00");
        }
        return amLong.toString();
    }

    /**
     * 分转元，都会有两位小数
     * @param amount
     * @return
     */
    public static String changeF2Y(Long amount){
        BigDecimal amountY = BigDecimal.valueOf(amount).divide(new BigDecimal(100));
        DecimalFormat format = new DecimalFormat("0.00");
        return format.format(amountY);
    }
}
