package com.homechart.app.commont;

/**
 * @author allen .
 * @version v1.0 .
 * @date 2017/6/1.
 * @file UrlConstants.java .
 * @brief url地址整合 .
 */
public class UrlConstants {

    public static final String USER_LOGIN = "https://api.idcool.com.cn/user/account/login";//用户登录
    public static final String JUDGE_MOBILE = "https://api.idcool.com.cn/security/verification/mobile";//验证用户手机号
    public static final String JIYAN_GETPARAM = "https://api.idcool.com.cn/security/verification/gt";  //获取极验验证参数
    public static final String SEND_MOBILE = "https://api.idcool.com.cn/security/verification/sendCaptcha";  //发送验证码
    public static final String REGISTER_MOBILE = "https://api.idcool.com.cn/user/account/signup";  // 验证验证码
    public static final String USER_LOGIN_BYYOUMENG = "https://api.idcool.com.cn/user/account/connect";  // 友盟登陆后，调用自己的后台登陆
    public static final String RESET_PASSWORD = "https://api.idcool.com.cn/user/account/resetPwd";  // 重置密码
    public static final String USER_INFO = "https://api.idcool.com.cn/user/user/getUserInfo";  // 获取用户信息
    public static final String FENSI_LIST = "https://api.idcool.com.cn/user/user/fansList";  // 获取粉丝列表
    public static final String GUANZHU_LIST = "https://api.idcool.com.cn/user/user/followList";  // 获取关注列表
    public static final String CITY_LIST = "https://api.idcool.com.cn/base/location/getList";  // 城市列表
    public static final String SHOUCANG_LIST = "https://api.idcool.com.cn/collect/single/list";  // 获取收藏列表
    public static final String SHAIJIA_LIST = "https://api.idcool.com.cn/single/single/list";  // 获取晒家列表
    public static final String SAVE_INFO = "https://api.idcool.com.cn/user/user/setUserInfo";  // 保存用户个人资料
    public static final String PUT_FILE = "https://api.idcool.com.cn/user/user/uploadAvatar";  // 上传头像
    public static final String PUT_IMAGE = "https://api.idcool.com.cn/base/image/uploadPicture";  // 上传图片
    public static final String BUNDLE_MOBILE = "https://api.idcool.com.cn/user/account/bindMobile";  // 绑定手机号码
    public static final String DELETE_SHOUCANG = "https://api.idcool.com.cn/collect/single/remove";  // 删除收藏列表
    public static final String DELETE_SHAIJIA = "https://api.idcool.com.cn/single/single/delete";  // 删除晒家列表
    public static final String ISSUE_BACK = "https://api.idcool.com.cn/base/feedback/add";  // 问题反馈
    public static final String MESSAGE_LIST = "https://api.idcool.com.cn/user/account/noticeList";  // 通知列表
    public static final String HOT_WORDS = "https://api.idcool.com.cn/search/search/hotWords";  // 搜索热词
    public static final String UNREADER_MSG = "https://api.idcool.com.cn/user/account/newNoticeNum";  // 未读消息数
    public static final String RECOMMEND_LIST = "https://api.idcool.com.cn/home/index/recommend";  // 获取首页推荐列表
    public static final String SEARCH_LIST = "https://api.idcool.com.cn/search/search/single";  // 获取搜索列表
    public static final String TAG_DATA = "https://api.idcool.com.cn/base/tag/getFilterTag";  // 获取筛选标签
    public static final String TUIJIAN_TAG = "https://api.idcool.com.cn/base/tag/getRecommendTag";  // 获取推荐标签
    public static final String GUANZHU = "https://api.idcool.com.cn/user/user/followMe";  //关注某个用户
    public static final String QUXIAO_GUANZHU = "https://api.idcool.com.cn/user/user/cancelFollow";  //取消关注某个用户
    public static final String COLOR_LIST = "https://api.idcool.com.cn/base/color/getFilterColor";  //获取颜色列表
    public static final String DOING_ACTIVITY = "https://api.idcool.com.cn/activity/activity/getDoingList";  //进行中的活动列表
    public static final String FABU = "https://api.idcool.com.cn/single/single/publish";  //用户发布晒家图片
    public static final String ITEM_FABU = "https://api.idcool.com.cn/single/single/getItemInfo";  //发布详情
    public static final String ADD_ZAN = "https://api.idcool.com.cn/like/single/add";  //对单张图片点赞
    public static final String REMOVE_ZAN = "https://api.idcool.com.cn/like/single/cancel";  //取消对单张图片点赞
    public static final String ADD_SHOUCANG = "https://api.idcool.com.cn/collect/single/add";  //收藏单张图片
    public static final String REMOVE_SHOUCANG = "https://api.idcool.com.cn/collect/single/remove";  //删除收藏的单图
    public static final String IMAGE_EDIT = "https://api.idcool.com.cn/single/single/modify";  //编辑晒家图片
    public static final String PING_LIST = "https://api.idcool.com.cn/comment/single/list";  //获取单图评论列表
    public static final String PING_REPLY = "https://api.idcool.com.cn/comment/single/reply";  //回复单图评论
    public static final String PING_IMAGE = "https://api.idcool.com.cn/comment/single/add";  //单图评论
    public static final String LIKE_CAI = "https://api.idcool.com.cn/single/single/recommend";  //猜你喜欢
    public static final String ACTIVITY_IMAGE = "https://api.idcool.com.cn/activity/activity/getSingleList";  //参与活动的图片列表
    public static final String ACTIVITY_DETAILS = "https://api.idcool.com.cn/activity/activity/getInfo";  //获取活动详情
    public static final String ADD_SHARED = "https://api.idcool.com.cn/base/share/updateShareNum";  //增加分享数
    public static final String SEARCH_ARTICLE = "https://api.idcool.com.cn/search/search/article";  //搜索文章列表

}
