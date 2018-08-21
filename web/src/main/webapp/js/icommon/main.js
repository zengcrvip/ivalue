/**
 * Created by Administrator on 2016/12/21.
 */
$(function () {
    initialize();
    getSettingData();
    initUserInfo();
    initCommonHTML();
    initMenu();
    initGlobalConstant();
    initEvent();

    function initialize() {
        var winHeight = $(window).height(),
            navbarHeight = $(".top-navbar").height(),
            locationHeight = $("#location").height(),
            coreFrameHeight = winHeight - navbarHeight - locationHeight;
        if (!localStorage.getItem("winHeight")) {
            localStorage.setItem("winHeight", winHeight);
        }
        $("#coreFrame").css("min-height", coreFrameHeight + "px");
        $("nav.navbar-side div.sidebar-collapse").css("height", (winHeight - navbarHeight - 22) + "px");
    }

    function getSettingData() {
        var data = $system.getProvince();
        if (data) {
            if (data === $system.PROVINCE_ENUM.GX) {
                $(document).attr("title", data + "联通存量维系管理运营平台")
                $(".navbar-brand span").text("存量维系平台")
            } else {
                $(document).attr("title", data + "联通智能营销平台")
            }
        }
    }

    function initCommonHTML() {
        $("#commonPage").load("pages/icommon/shopTaskDetail.html?time" + new Date().getTime())
    }

    function initMenu() {
        globalRequest.queryAllMenus(true, {}, function (data) {
            $('#menuTree').menu(data);
            //加载home页面
            $("#menuTree").find("a.home").trigger("click")
        });
    }

    //初始化加载当前登录用户及其数据权限
    function initUserInfo() {
        globalRequest.queryCurrentUserInfo(false, {}, function (data) {
            globalConfigConstant.dataPermissionList = data["dataPermission"];
            globalConfigConstant.loginUser = data["loginUser"];
            $("#loginUser").text(data["loginUser"].name);
            $("#loginUser").attr("title", data["loginUser"].name);
        });
    }

    function initGlobalConstant() {
        globalRequest.queryAllDimensions(true, {}, function (data) {
            if (data["dimensionDetail"]) {
                globalConfigConstant.dimensionDetail = data["dimensionDetail"];
                globalConfigConstant.dimension = data["dimension"];
            }
        });

        globalRequest.queryAllEffectiveAccessNumbers(true, {}, function (data) {
            globalConfigConstant.smsAccessNumber = data;
        }, function () {
            layer.alert("系统异常", {icon: 6});
        });
    }

    function initEvent() {
        //页面收缩按钮
        $("#sideNav").click(function () {
            if ($(this).hasClass('closed')) {
                $('.navbar-side').animate({left: '0px'});
                $(this).removeClass('closed');
                $('#page-wrapper').animate({'margin-left': '260px'});

            }
            else {
                $(this).addClass('closed');
                $('.navbar-side').animate({left: '-260px'});
                $('#page-wrapper').animate({'margin-left': '0px'});
            }
        });

        // 点击右上角菜单按钮
        $(".dropDown-button").click(function (e) {
            e.stopPropagation();
            var $dropDownContent = $(this).siblings(".dropDown-content");
            $(".dropDown-content").not($dropDownContent).each(function () {
                $(this).animate({height: 0, overflow: 'hidden'}, 150);
            });
            if ($dropDownContent.height() == 0) {
                var contentHeight = $dropDownContent.hide().css("height", "auto").height();
                $dropDownContent.css("height", "0").show();
                $dropDownContent.animate({height: contentHeight, overflow: 'auto'}, 350);
                $(this).addClass("active");
            } else {
                $dropDownContent.animate({height: 0, overflow: 'hidden'}, 150);
                $(this).removeClass("active");
            }
        });
        $(document).click(function () {
            $(".dropDown-content").animate({height: 0, overflow: 'hidden'}, 150);
            $(".dropDown-content").siblings(".dropDown-button").removeClass("active");
        });

        //账号退出登录
        $("#loginOut").click(function () {
            layer.confirm('确认退出？', {icon: 3, title: '提示'}, function (index) {
                layer.close(index);
                globalRequest.loginOut(false, {}, function (data) {
                    window.location.href = data.url;
                });
            });
        });

        $("#personalCenter").click(function () {
            $("#location").find("ol.breadcrumb").empty().append("个人信息");
            $("#location").find(".page-header").text("个人中心");

            $("#coreFrame").load("icommon/personalInfo.requestHtml?time" + new Date().getTime(), function (response, sts, xhr) {
                if (xhr.status == 200) {
                    onLoadBody(status);
                } else if (xhr.status == 911) {
                    var redirectUrl = xhr.getResponseHeader("redirectUrl");
                    layer.alert('会话超时，请重新登录', function (index) {
                        window.location.href = redirectUrl;
                        layer.close(index);
                        return;
                    });
                }
            });
        });
    }

});