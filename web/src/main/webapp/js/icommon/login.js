/**
 * Created by yuanfei on 2017/1/17.
 */
function login() {
    var $loginChange = $("div.login-table.show img.login-change-icon");
    var $vCode = $("div.login-table.show input.verificationText");
    var loginType = $loginChange.attr("loginType");
    if (loginType == "phone") {
        var $userPhone = $("#telephone");
        var $verificationCode = $("#verificationCode");
        var userPhone = $userPhone.val();
        var verificationCode = $verificationCode.val();
        var vCode = $vCode.val();

        if (!/^(\+86|86)?1\d{10}$/.test(userPhone)) {
            layer.tips("手机号码为空或无效", $userPhone, {time: 1000});
            $userPhone.focus();
            return;
        }

        if (verificationCode == "") {
            layer.tips($verificationCode.attr("title"), $verificationCode, {time: 1000});
            $verificationCode.focus();
            return;
        }

        if (vCode == "") {
            layer.tips($vCode.attr("title"), $vCode, {time: 1000});
            $vCode.focus();
            return;
        }

        globalRequest.loginByPhone(true, {
            phone: userPhone,
            code: verificationCode,
            vCode: $vCode.val()
        }, function (data) {
            if (data.retValue === 0) {
                window.location.href = data.url;
            } else {
                layer.alert(data.desc, {icon: 6}, function (index) {
                    $("div.login-main div.login-table.show .vCodeImg").trigger("click");
                    layer.close(index);
                });

            }
        }, function () {
            layer.alert("网络异常");
        });
    } else {
        var $userName = $("#loginName");
        var $userPwd = $("#password");
        var userName = $userName.val();
        var userPwd = $userPwd.val();
        var vCode = $vCode.val();
        if (!/^(\+86|86)?1\d{10}$/.test($userName.val())) {
            layer.tips("手机号码为空或无效", $userName, {time: 1000});
            $userName.focus();
            return;
        }
        if (userPwd == "") {
            layer.tips($userPwd.attr("title"), $userPwd, {time: 1000});
            $userPwd.focus();
            return;
        }
        if (vCode == "") {
            layer.tips($vCode.attr("title"), $vCode, {time: 1000});
            $vCode.focus();
            return;
        }

        globalRequest.loginByName(true, {user: userName, password: userPwd, vCode: vCode}, function (data) {
            if (data.retValue === 0) {
                window.location.href = data.url;
            } else {
                layer.alert(data.desc, {icon: 6}, function (index) {
                    $("div.login-main div.login-table.show .vCodeImg").trigger("click");
                    layer.close(index);
                });
            }
        }, function () {
            layer.alert("系统异常");
        });
    }
}

function getSettingData() {
    var data = $system.getProvince();
    if (data) {
        if (data === $system.PROVINCE_ENUM.GX) {
            $(".login-title").text(data + "联通存量维系管理运营平台");
            $(".province").text(data);
            $(document).attr("title", data + "联通存量维系管理运营平台")
        } else {
            $(".login-title").text(data + "联通智能营销系统");
            $(".province").text(data);
            $(document).attr("title", data + "联通智能营销平台")
        }
    }
}

$(function () {
    getSettingData()
    // Modify 局方要求浏览器提示一直挂着！
    // 浏览器兼容性
    // var userAgent = navigator.userAgent;
    // var isChrome = userAgent.indexOf("Chrome") > -1 && userAgent.indexOf("Safari") > -1 && userAgent.indexOf("Edge") === -1;
    // if (!isChrome) {
    setTimeout(function () {
        $(".login-browser").addClass("animated slideInDown").show();
    }, 800)
    // }

    $("div.login-main").on("click", "div.login-table.show img.login-change-icon", function () {
        $("div.login-main div.login-table").toggleClass("show");
        $("div.login-main").find("div.login-table.show .vCodeImg").trigger("click");
    });

    $("div.login-main").on("click", 'div.login-table.show .vCodeImg', function () {
        $(this).attr("src", "getVerifyCode?timestamp=" + (new Date()).valueOf());
    });

    $(".sendVerification").click(function () {
        var $this = $(this);
        var $telephone = $("#telephone");
        var userPhone = $telephone.val();
        if (!/^(\+86|86)?1\d{10}$/.test(userPhone)) {
            layer.tips("手机号码为空或无效", $telephone, {time: 1000});
            $telephone.focus();
            return;
        }
        $this.addClass("disabled");


        globalRequest.sendVerificationCode(true, {phone: userPhone}, function (data) {
            if (data.retValue === 0) {
                countDown($this, 60);
            } else {
                $this.text("获取验证码");
                $this.removeClass("disabled");
                layer.alert(data.desc, {icon: 6});
            }

            function countDown($this, second) {
                if (second == 0) {
                    $this.text("获取验证码");
                    $this.removeClass("disabled");
                    return;
                }
                $this.text(second + "秒后重试");
                setTimeout(function () {
                    countDown($this, second - 1)
                }, 1000);
            }
        }, function () {

        });
    });

    $("body").keydown(function () {
        if (event.keyCode == "13") {
            login();
        }
    });
    //
    //$("[logintype='phone']").click();
    //$("#loginName").val("12345678987");
    //$("#password").val("1");

})

