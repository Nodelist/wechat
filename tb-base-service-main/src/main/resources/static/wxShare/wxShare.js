// 封装 ajax 请求
function ajaxRequest(url, method, getData) {
    var xhr = new XMLHttpRequest()
    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4) {
            if (xhr.status == 200) {
                var msg = xhr.responseText
                var result = JSON.parse(msg)
                getData(result)
                return result
            } else {
                alert('响应失败！')
            }
        }
    }
    xhr.open(method, url, true)
    xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xhr.send(null)
}
let url = "http://192.168.0.36:8022/tb/wechatShare/getJsApiTicket?url=" + encodeURIComponent(location.href.split("#")[0])
    // document.querySelector('#request').onclick = function() {
ajaxRequest(url, 'GET', res => {
    wx.config({
        debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
        appId: res.data.appId, // 必填，公众号的唯一标识
        timestamp: res.data.timestamp, // 必填，生成签名的时间戳
        nonceStr: res.data.nonceStr, // 必填，生成签名的随机串
        signature: res.data.signature, // 必填，签名
        jsApiList: ['chooseImage', 'updateAppMessageShareData', 'updateTimelineShareData'] // 必填，需要使用的JS接口列表
    });

    wx.ready(function() {
        // document.querySelector('#checkJsApi').onclick = function() {

        alert("验证成功")
        wx.checkJsApi({
            jsApiList: ['chooseImage', 'updateAppMessageShareData', 'updateTimelineShareData'], // 需要检测的JS接口列表，所有JS接口列表见附录2,
            success: function(res) {
                // 以键值对的形式返回，可用的api值true，不可用为false
                // 如：{"checkResult":{"chooseImage":true},"errMsg":"checkJsApi:ok"}
                alert(JSON.stringify(res))
                if (res.checkResult.chooseImage) {

                }
            }
        });
        // };

        // 自定义“分享给朋友”及“分享到QQ”按钮的分享内容
        wx.updateAppMessageShareData({
            title: window.title,
            desc: window.desc,
            link: window.link,
            imgUrl: window.imgUrl,
            trigger: function(res) {
                alert('用户点击发送给朋友');
            },
            success: function(res) {
                alert('已分享');
            },
            cancel: function(res) {
                alert('已取消');
            },
            fail: function(res) {
                alert(JSON.stringify(res));
            }
        });
        // };

        // 自定义“分享到朋友圈”及“分享到QQ空间”按钮的分享内容
        wx.updateTimelineShareData({
            title: window.title,
            desc: window.desc,
            link: window.link,
            imgUrl: window.imgUrl,
            trigger: function(res) {
                // alert('用户点击发送给朋友');
                alert('已点击');
            },
            success: function(res) {
                alert('已分享');
            },
            cancel: function(res) {
                alert('已取消');
            },
            fail: function(res) {
                alert(JSON.stringify(res));
            }
        });
    });

    wx.error(function(res) {
        // config 验证失败执行的回调
        alert(JSON.stringify(res))
    })
})