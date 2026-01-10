package com.jsdc.lsoa.constants;

public interface CommonConstants {

    /**
     * 逻辑删除 是
     */
    Integer ISDEL_YES = 1;

    /**
     * 逻辑删除 否
     */
    Integer ISDEL_NO = 0;


    /**
     * 字典 开票金额配置
     */
    String INVOICE_PROCESS_AMOUNT_KEY = "invoiceAmount";

    /**
     * 字典 文件存储盘符
     */
    String FILE_DISK_SYMBOL_KEY = "diskSymbol";

    /**
     * 字典 文件存储路径
     */
    String FILE_DISK_PATH_KEY = "diskPath";

    /**
     * 字典 文件访问域名
     */
    String FILE_DOMAIN_KEY = "domain";

    /**
     * 字典 上传格式设置
     */
    String FILE_TYPE_CONFIG_KEY = "fileTypeConfig";

    /**
     * 字典 上传格式设置
     */
    String FILE_MAX_SIZE_KEY = "maxSize";

    /**
     * 字典 文件类型
     */
    String FILE_TYPE_KEY = "fileType";

    /**
     * 头像文件类型
     */
    String[] AVATAR_FILE_TYPE = new String[]{".png", ".jpg", ".jpeg"};

    //字典
    String allDictMapPrefix = "prefix_dict_map";
    //返回是全部的数据
    String allDictListPrefix = "prefix_dict_list";
    //返回是单个类型的list
    String dictListPrefix = "prefix_dict_list_oneType_";
    String allDictKeyPrefix = "prefix_dict_key";


    /**
     * 普通律师
     */
    String NORMAL_SOLICITOR = "ls";

    /**
     * 主任
     */
    String DIRECTOR = "zr";

    /**
     * 主任
     */
    String ADMIN = "admin1";

    /**
     * 顾问客户管理员
     */
    String GWKHGLY = "gwkhgly";
    /**
     * 行政
     */
    String XINGZHENG = "012";

}
