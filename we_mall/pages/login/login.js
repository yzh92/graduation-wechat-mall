var http = require("../../utils/http.js");
var request = require("../../utils/http.js");
Page({
  data: {

  },

  onGotUserInfo: function (res) {
    http.updateUserInfo();
    wx.navigateBack({
      delta: 1
    })
  },

   getUserProfile(e){
    let userInfo;
     wx.getUserProfile({
      desc: '用于完善会员资料', // 声明获取用户个人信息后的用途，后续会展示在弹窗中，请谨慎填写
      success: (res) => {
        console.log(res);
        userInfo = res.userInfo;
        console.log(userInfo);
      }
    })

  }

})