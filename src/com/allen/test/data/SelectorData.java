package com.allen.test.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class SelectorData {
    public static final String AUTHORITY = "com.allen.test.data.SelectorData";
    //数据库版本
    public final static int VERSON=2;
    public final static String DB_NAME="selectordata.db";
    //内容类型 MIME类型
    //文件夹类型
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.user.mydata";
    //文件类型
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.user.mydata";
    
    
    private SelectorData(){};
    public static final class DataColumns implements BaseColumns{
        public final static String TBL_NAME="DataTbl";
    	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + TBL_NAME);
        //table
        //这是定义的列名，总共有3列，BaseColumns有一个隐含的_ID成员，不需要再定义
        public static final String 	TYPE = "type"; 
        public static final String 	GROUP = "group";
        public static final String 	NAME = "type"; 
        public static final String PICTURE = "picture";
        public static final String DETAIL = "detail";
        //#是一个通配符
        public final static String TBL_ITEM="DataTbl/#";
        //默认排序常量
        public static final String DEFAULT_SORT_ORDER = null;

       
    }
}
