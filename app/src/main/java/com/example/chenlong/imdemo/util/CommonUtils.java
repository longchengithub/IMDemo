package com.example.chenlong.imdemo.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ChenLong on 2017/2/20.
 */

public class CommonUtils
{
    /**
     * 静态吐司
     */
    private static Toast sToast = null;

    public static void showToast(Context context, String text)
    {
        if (sToast == null)
        {
            sToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        }
        sToast.setText(text);
        sToast.show();
    }

    /**
     * 汉字转换拼音
     *
     * @param str
     * @return
     */
    public static String hanZi2PinYin(String str)
    {
        if (TextUtils.isEmpty(str))
        {
            return "";
        }
        //1.汉字转换成字符
        char[] chars = str.toCharArray();
        //2.设置转换输出格式
        if (sFormat == null)
        {
            sFormat = new HanyuPinyinOutputFormat();
            //设置为大写输出
            sFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
            //去除音调显示
            sFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        }

        StringBuilder sb = new StringBuilder();
        for (char aChar : chars)
        {
            //去空格
            if (Character.isWhitespace(aChar))
            {
                continue;
            }

            //判断是否是汉字
            if (Character.toString(aChar).matches("[\\u4E00-\\u9FA5]"))
            {
                try
                {
                    String pinyin = PinyinHelper.toHanyuPinyinStringArray(aChar, sFormat)[0];
                    sb.append(pinyin);
                } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination)
                {
                    badHanyuPinyinOutputFormatCombination.printStackTrace();
                }
            } else  //非汉字
            {
                if (Character.isLetter(aChar))
                {
                    sb.append(Character.toUpperCase(aChar));
                } else
                {  //看不懂的东西全部替换空格
                    sb.append("");
                }
            }
        }
        return sb.toString();
    }

    private static HanyuPinyinOutputFormat sFormat;

    /**
     * 有昵称用昵称,没昵称返回jid
     *
     * @param jid
     * @param name
     * @return
     */
    public static String getNed(String jid, String name)
    {
        return TextUtils.isEmpty(name) ? jid : name;
    }

    public static String getCurrentTime()
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return format.format(new Date());
    }
}
