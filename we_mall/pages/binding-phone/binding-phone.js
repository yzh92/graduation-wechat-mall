// pages/binding-phone/binding-phone.js

var http = require("../../utils/http.js");
var config = require("../../utils/config.js");

Page({

  /**
   * 页面的初始数据
   */
  data: {
    phonenum:'',
    code:'',
    isShow:false
  },

  getCodeNumber:function(){
    if (this.data.phoneNumber == "" ) {
      wx.showToast({
        title: '请输入手机号',
        icon: "none"
      })
      return;
    }
    var params = {
      url: "/p/sms/send",
      method: "POST",
      data: {
        // phonenum: this.data.phonenum,
        // code: this.data.code
      },
      callBack: (res) => {
        this.setData({
          phonenum: this.data.phonenum,
          code: this.data.code
        });
      }
    };
    http.request(params);
  },
  onPhoneInput:function(e){
    this.setData({
      phonenum: e.detail.value
    });
  },
  onCodeInput: function (e) {
    this.setData({
      code: e.detail.value
    });
    
    if(e.detail.value.length == 6){
      this.setData({
        isShow:true
      })
    }


  }
})