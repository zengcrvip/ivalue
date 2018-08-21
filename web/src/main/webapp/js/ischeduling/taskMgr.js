var taskMgr = function () {
    var dialogHeight = 0, dataTable = {}, obj = {};
    var taskMgr_enum = {
        schedule_type: {
            single: "single",
            single_text: "自动调度",
            manu: "manu",
            manu_text: "手动调度"
        },
        isboidSale: {
            yes: "",
            no: ""
        }
    }

    obj.initialize = function (status) {
        status != 0 ? $("div.iMarket_Body .addBtn").hide() : $("div.iMarket_Body .addBtn").show();
        status != 0 ? $("div.iMarket_Body .stats").hide() : $("div.iMarket_Body .stats").show();

        if (status == 0) {
            $("div.iMarket_Body .poolStats").hide();
        } else if (status == 2) {
            $("div.iMarket_Body .addBtn").hide();
            $("div.iMarket_Body .stats").hide();
            $("div.iMarket_Body .poolStats").show();
        }

        dialogHeight = localStorage.getItem("winHeight");
        if (!dialogHeight) {
            dialogHeight = $(window).height();
            localStorage.setItem("winHeight", dialogHeight);
        }
    }

    obj.initData = function (status) {
        var options = {
            ele: $('table.taskTab'),
            ajax: {url: obj.getAjaxUrl(status), type: "POST"},
            columns: [
                {data: "name", title: "任务名称", className: "dataTableFirstColumns"},
                {
                    data: "marketType", title: "来源",
                    render: function (data, type, row) {
                        return obj.getMarketType(data);
                    }
                },
                {
                    data: "businessType", title: "业务类别",
                    render: function (data, type, row) {
                        return obj.getBusinessType(data);
                    }
                },
                {data: "startTime", title: "开始时间"},
                {data: "stopTime", title: "结束时间"},
                {data: "createTime", title: "配置时间"},
                {data: "marketSegmentNames", title: "目标用户"},
                {
                    data: "status", title: "状态", className: "centerColumns",
                    render: function (data, type, row) {
                        if (row.status == 0) {
                            return "<i class='fa'>草稿</i>";
                        } else if (row.status == 1) {
                            return "<i class='fa' style='color: green;'>待审核</i>";
                        } else if (row.status == 2 || row.status == 40) {
                            return "<i class='fa' style='color: green;'>审核成功</i>";
                        } else if (row.status == 3) {
                            return "<i class='fa' style='color: red;'>审核拒绝</i>";
                        } else if (row.status == 4) {
                            return "<i class='fa' style='color: blue;'>已暂停</i>";
                        } else if (row.status == 5) {
                            return "<i class='fa' style='color: blue;'>已失效</i>";
                        } else if (row.status == 6) {
                            return "<i class='fa' style='color: red;'>已终止</i>";
                        } else if (row.status == 20 || row.status == 30) {
                            return "<i class='fa' style='color: blue;'>营销处理中</i>";
                        } else if (row.status == 35) {
                            return "<i class='fa' style='color: green;'>营销触发成功</i>";
                        } else if (row.status == 36) {
                            return "<i class='fa' style='color: red;'>营销失败</i>";
                        }
                        else if (row.status == -1) {
                            return "<i class='fa' style='color: red;'>已删除</i>";
                        } else {
                            return "<i class='fa'>未知</i>";
                        }
                    }
                },
                {
                    title: "操作", width: "12%",
                    render: function (data, type, row) {
                        var buttons = "";
                        var editBtnHtml = "", deleteBtnHtml = "", stopHtml = "", executeHtml = "",
                            firstExecuteHtml = "",
                            viewBtnHtml = "<a  title='预览' class='viewBtn btn btn-primary btn-preview btn-sm' href='javascript:void(0)' onclick='taskMgr.viewItem(" + JSON.stringify(row) + "," + status + " )'>预览</a>";
                        if (globalConfigConstant.loginUser.id == row.createUser) {
                            editBtnHtml = "<a title='编辑'  class='editBtn btn btn-info btn-edit btn-sm' href='javascript:void(0)' onclick='taskMgr.editItem(" + JSON.stringify(row) + ")' >编辑</a>";
                            deleteBtnHtml = "<a title='删除' class='deleteBtn btn btn-danger btn-edit btn-sm' href='javascript:void(0)' onclick='taskMgr.deleteItem(" + row.id + ",\"" + row.name + "\")'>删除</a>";
                            stopHtml = "<a title='一键终止任务' class='status btn btn-warning btn-edit btn-sm ' href='javascript:void(0)' onclick='taskMgr.stopTask(" + row.id + ",\"" + row.name + "\"," + row.status + ")' >终止</a>";
                            executeHtml = "<a title='执行' class='manuBtn btn btn-success btn-edit btn-sm' href='javascript:void(0)' onclick='taskMgr.executeTask(" + row.id + ",\"" + row.name + "\")' >执行</a>";
                            firstExecuteHtml = "<a title='首次执行确认' class='manuBtn btn btn-success btn-edit btn-sm' href='javascript:void(0)' onclick='taskMgr.executeTask(" + row.id + ",\"" + row.name + "\")' >首次执行确认</a>";
                        }
                        var viewAuditHtml = "<a title='查看审批进度' class='viewAuditBtn btn btn-danger btn-edit btn-sm' href='javascript:void(0)' onclick='taskMgr.viewAudit(" + row.id + ")' >审批进度</a>";
                        var resubmitHtml = "<a title='重新提交' class='resubmitBtn btn btn-primary btn-edit btn-sm' href='javascript:void(0)' onclick='taskMgr.resubmit(" + JSON.stringify(row) + ")' >重新提交</a>";

                        if (status == "0") {    // 任务配置页面
                            buttons += viewBtnHtml;
                            if (row.status == 0 || row.status == 1 || row.status == 3) {  // 审批通过前的状态 显示删除按钮
                                buttons += deleteBtnHtml + editBtnHtml;
                            } else {  // 审批通过后的状态 显示终止按钮
                                if (row.status != 6) {
                                    buttons += stopHtml;
                                }
                            }
                        } else if (status == "2") { // 任务池页面
                            buttons = viewBtnHtml;
                            if (row.scheduleType == 'manu') {
                                if (row.status == 2) {
                                    buttons += executeHtml;
                                }
                            }
                            if (row.scheduleType == 'single') {
                                if (row.isFistStatus == 2 && row.status == 2) {
                                    buttons += firstExecuteHtml;
                                }
                            }
                        }
                        return buttons;
                    }
                },
                {data: "id", visible: false}
            ]
        };
        dataTable = $plugin.iCompaignTable(options);
    }

    obj.initEvent = function (status) {
        /**
         * 查询事件
         */
        $(".searchBtn").click(function () {
            $plugin.iCompaignTableRefresh(dataTable, obj.getAjaxUrl(status));
        })

        /**
         * 新增事件
         */
        $(".addBtn").click(function (event, params) {
            // 涉及客户群跳转到营销任务功能
            var initValue = {
                marketSegmentIds: params ? params.segmentId : "",
                marketSegmentNames: params ? params.segmentName : "",
                marketSegmentUserCounts: params ? params.lastRefreshCount : 0,
                operateType: params ? params.operateType : "create"
            };
            obj.handleTaskInitCreateHtml($("#dialogPrimary"));
            obj.handleTaskSetings($("#dialogPrimary"), initValue);

            $plugin.iModal({
                title: '新增营销任务',
                content: $("#dialogPrimary"),
                area: '750px',
                btn: []
            }, null, null, function (layero, index) {
                layero.find('.layui-layer-btn').remove();
                layero.find("div.data").attr("index", index).attr("operate", initValue.operateType);
            })
        })
    }

    /**
     * 预览 事件
     * @param task
     */
    obj.viewItem = function (task, status) {
        task["operateType"] = "show";
        obj.initShopTaskDetailDialog();
        obj.initShopTaskDetailValue(task["id"], "preview", status);
        $("#taskMgrDetailDialog").show();
        $("#shopTaskDetailDialog").hide();
        $plugin.iModal({
            title: '预览任务',
            content: $("#commonPage"),
            area: '750px',
            btn: []
        }, null, null, function (layero, index) {
            layero.find('.layui-layer-btn').remove();
            layero.find("div.data").attr("index", index).attr("operate", "update");
        })
    }

    /**
     * 初始化对话框元素内容-预览
     * @param id
     * @param type
     * @param status
     */
    obj.initShopTaskDetailValue = function (id, type, status) {
        if (type == "preview" || type == "execute") {
            if (!id || id <= 0) {
                layer.alert("未找到该数据，请稍后重试", {icon: 6});
                return;
            }

            if (status == 0) {
                globalRequest.iScheduling.queryMarketingTaskDetail(true, {id: id}, successFunc, function () {
                    layer.alert("根据ID查询炒店数据失败", {icon: 6});
                })
            } else if (status == 2) {
                globalRequest.iScheduling.viewMarketingPoolTaskDetail(true, {id: id}, successFunc, function () {
                    layer.alert("根据ID查询炒店数据失败", {icon: 6});
                })
            }
        }

        function successFunc(data) {
            var shopTaskDomainObj = data;
            var $shopTaskDetailInfo = $("#taskMgrDetailDialog div.taskMgrDetailInfo");
            // 任务基本信息
            $shopTaskDetailInfo.find(".detail_id").text(shopTaskDomainObj.id);
            $shopTaskDetailInfo.find(".detail_taskName").text(shopTaskDomainObj.name);
            $shopTaskDetailInfo.find(".detail_businessTypeSelect").text(obj.getBusinessType(shopTaskDomainObj.businessType));
            var type = shopTaskDomainObj.scheduleType;
            var scheduleType = "空";
            if (type == taskMgr_enum.schedule_type.single) {
                scheduleType = taskMgr_enum.schedule_type.single_text;
                $shopTaskDetailInfo.find(".detail_monitoringStartTime").text(shopTaskDomainObj.beginTime || "09:00");
                $shopTaskDetailInfo.find(".detail_monitoringEndTime").text(shopTaskDomainObj.endTime || "18:00");
                $shopTaskDetailInfo.find(".monitoring_row").show();
            } else if (type == taskMgr_enum.schedule_type.manu) {
                scheduleType = taskMgr_enum.schedule_type.manu_text;
            }
            $shopTaskDetailInfo.find(".detail_timerSelect").text(scheduleType);
            $shopTaskDetailInfo.find(".detail_startTime").text(shopTaskDomainObj.startTime || "空");
            $shopTaskDetailInfo.find(".detail_endTime").text(shopTaskDomainObj.stopTime || "空");
            $shopTaskDetailInfo.find(".detail_boidSale").text(shopTaskDomainObj.isBoidSale == 1 ? "是" : "否");
            $shopTaskDetailInfo.find(".detail_intervalInSeconds_title").text(shopTaskDomainObj.isBoidSale == 1 ? "分组间隔：" : "时间间隔：");
            $shopTaskDetailInfo.find(".detail_intervalInSeconds").text(shopTaskDomainObj.sendInterval ? shopTaskDomainObj.sendInterval + "天" : "空");
            // $shopTaskDetailInfo.find(".detail_repeatStrategy").text(!shopTaskDomainObj.repeatStrategy ? "空" : "营销频次剔重（" + shopTaskDomainObj.repeatStrategy + "）天");
            $shopTaskDetailInfo.find(".detail_areaNames").text(shopTaskDomainObj.areaNames || "空");
            $shopTaskDetailInfo.find(".detail_remarks").text(shopTaskDomainObj.remarks || "空");

            //
            $shopTaskDetailInfo.find(".detail_segmentNames").text(shopTaskDomainObj.marketSegmentNames || "空");
            $shopTaskDetailInfo.find(".detail_segmentCounts").text(shopTaskDomainObj.marketSegmentUserCounts || "空");
            // $shopTaskDetailInfo.find(".detail_number").text(shopTaskDomainObj.marketUserCountLimit || "空");

            $shopTaskDetailInfo.find(".marketTypeValue").hide();

            var marketTypeValue = shopTaskDomainObj.marketType;
            if (marketTypeValue == "sceneSms") {
                // 场景规则显示
                $shopTaskDetailInfo.find(".marketTypeValue").show();
                $shopTaskDetailInfo.find(".detail_marketTypeValue").text(shopTaskDomainObj.sceneSmsName || "空");
            }
            $shopTaskDetailInfo.find(".detail_marketType").text(obj.getMarketType(marketTypeValue));
            $shopTaskDetailInfo.find(".detail_AccessNumber").text(shopTaskDomainObj.accessNumber || "空");
            $shopTaskDetailInfo.find(".detail_Content").text(shopTaskDomainObj.marketContent || "空");
            $shopTaskDetailInfo.find(".detail_TestPhones").text(shopTaskDomainObj.testPhones || "空");
        }
    }

    /**
     * 初始化对话框-预览
     */
    obj.initShopTaskDetailDialog = function () {
        var $dialog = $("#taskMgrDetailDialog");
        var $panel = $(".iMarket_preview").find("div.taskMgrDetailInfo").clone();
        $dialog.find("div.taskMgrDetailInfo").remove();
        $dialog.append($panel);
    }

    /**
     * 修改 事件
     * @param task
     */
    obj.editItem = function (task) {
        task["operateType"] = "update";
        obj.handleTaskInitCreateHtml($("#dialogPrimary"), task);
        obj.handleTaskSetings($("#dialogPrimary"), task);
        $plugin.iModal({
            title: '修改营销任务',
            content: $("#dialogPrimary"),
            area: '750px',
            btn: []
        }, null, null, function (layero, index) {
            layero.find('.layui-layer-btn').remove();
            layero.find("div.data").attr("index", index).attr("operate", "update");
        })
    }

    /**
     * 重新提交 事件
     * @param task
     */
    obj.resubmit = function (task) {
        task["operateType"] = "resubmit";
        obj.handleTaskInitCreateHtml($("#dialogPrimary"), task);
        obj.handleTaskSetings($("#dialogPrimary"), task);
        $plugin.iModal({
            title: '营销任务重新提交',
            content: $("#dialogPrimary"),
            area: '750px',
            btn: []
        }, null, null, function (layero, index) {
            layero.find('.layui-layer-btn').remove();
            layero.find("div.data").attr("index", index).attr("operate", "update");
        })
    }

    /**
     * 删除 事件
     * @param id
     * @param name
     */
    obj.deleteItem = function (id, name) {
        layer.confirm('确认删除营销任务:' + name + "?", {icon: 3, title: '提示'}, function (index) {
            globalRequest.iScheduling.deleteMarketingTask(true, {id: id}, function () {
                layer.close(index);
                dataTable.ajax.reload()
                layer.msg("删除成功", {time: 1000});
            }, function () {
                layer.alert('操作数据库失败', {icon: 6});
            })
        })
    }

    /**
     * 执行 事件
     * @param id
     * @param name
     * @param status
     */
    obj.executeTask = function (id, name) {
        layer.confirm("确认启动营销任务:" + name, function (index) {
            globalRequest.iScheduling.executeTask(true, {id: id}, function (data) {
                if (data.retValue == 0) {
                    layer.close(index);
                    dataTable.ajax.reload();
                    layer.msg("启动成功", {time: 1000});
                } else {
                    layer.alert("启动失败", {icon: 6});
                }
            }), function () {
                layer.alert('操作数据库失败');
            }
        })
    }

    /**
     * 终止 事件
     * @param id
     * @param name
     */
    obj.stopTask = function (id, name) {
        layer.confirm('确认终止营销任务:' + name + "?", function (index) {
            layer.close(index);
            globalRequest.iScheduling.stopTask(true, {id: id}, function (data) {
                if (data.retValue === 0) {
                    dataTable.ajax.reload();
                    layer.msg("终止营销成功", {time: 1000});
                } else {
                    layer.alert(data.desc, {icon: 6});
                }
            }, function () {
                layer.alert('操作数据库失败');
            })
        })
    }

    /**
     * 查看审批进度 事件
     * @param id
     */
    obj.viewAudit = function (id) {
        var $dialog = $("#dialogPrimary").empty();
        $dialog.append("<table class='processTab table' style='width:100%;'></table>");

        var options = {
            ele: $dialog.find('table.processTab'),
            ajax: {url: "queryMarketingTaskAuditProgress.view?id=" + id, type: "POST"},
            paging: false,
            columns: [
                {data: "auditUserName", title: "审批人", width: "12%"},
                {
                    data: "auditResult", title: "审批结果", width: "13%",
                    render: function (data, type, row) {
                        if (data == null) {
                            return '未审批';
                        }
                        else if (data == 'approve') {
                            return '<span style=color:green>同意</span>';
                        }
                        else {
                            return '<span style=color:red>拒绝</span>'
                        }
                    }
                },
                {data: "auditTime", title: "审批时间", width: "20%"},
                {
                    data: "remarks", title: "审批说明", width: "45%",
                    render: function (data, type, row) {
                        return "<span id='remark' style='word-break:break-all; width:auto;display:block;white-space:pre-wrap;word-wrap : break-word;overflow:hidden;' title='" + row.remarks + "'>" + row.remarks + "</span>";
                    }
                }
            ]
        };
        $plugin.iCompaignTable(options);

        $plugin.iModal({
            title: '审批进度详情',
            content: $dialog,
            area: '750px',
            btn: ['关闭']
        }, function (index) {
            layer.close(index);
        })
    }

    obj.handleTaskInitCreateHtml = function ($dialog, savaValue) {
        var $all = $("div.iMarket_Content").find("div.marketJobEditInfo").clone();
        $dialog.empty().append($all);

        initAll();

        function initAll() {
            /**
             * 任务基本信息
             */
            var $marketJobName = $all.find(".marketName input.name"),           // 任务名称
                $marketBusinessTypeSelect = $all.find("select.marketBusinessTypeSelect"),   // 业务类别
                $marketTimerSelect = $all.find("select.timerSelect"),           // 调度类型
                $startTimeInput = $all.find("input.startTime"),                 // 开始时间
                $endTimeInput = $all.find("input.endTime"),                     // 结束时间
                $marketMonitoringTimeRow = $all.find(".marketMonitoringTime"),      // 监控行
                $monitoringStartTimeInput = $all.find("input.monitoringStartTime"), // 监控开始时间
                $monitoringEndTimeInput = $all.find("input.monitoringEndTime"),     // 监控结束时间
                $waveCount = $all.find("label.checkbox-inline :radio"),         // 分组营销
                $singleValueTitle = $all.find("strong.singleValueTitle"),       // 时间间隔标题
                $singleValue = $all.find("input.singleValue"),                  // 时间间隔
                $marketLimitSelect = $dialog.find("select.marketLimitSelect"),  // 剔除策略
                $marketAreaNames = $all.find("textarea.areaNames"),             // 营销地区
                $marketAreaIds = $all.find("input.areaIds"),                    // 营销地区Id
                $marketRemarks = $all.find("textarea.remarks"),                 // 任务描述
                $marketWaveCountInfo = $all.find("div.marketDescription"),      // 分组营销提示语
                /**
                 * 目标用户选择
                 */
                $marketSegmentNames = $all.find("textarea.segmentNames"),       // 客户群
                $marketSegmentIds = $all.find(".marketSegments .segmentIds"),   // 客户群Id
                $segmentCounts = $all.find(".marketSegments .segmentCounts"),   // 客户群人数
                $numberLimitInput = $all.find("input.numberLimit"),             // 限制人数
                /**
                 * 客户接触渠道
                 */
                $marketTypeSelect = $all.find("select.marketTypeSelect"),                   // 营销方式
                // 短信
                $smsAccessNumberSelect = $all.find(".sms select.smsAccessNumberSelect"),    // 接入号
                $smsMessageContent = $all.find(".sms textarea.smsMessageContent"),          // 短息内容
                $smsContentId = $all.find("input.smsContentId"),                            // 短信内容Id
                $smsMessageSelectButton = $all.find(".smsMessageSelectButton"),             // 选择按钮
                // 场景短信
                $sceneSmsAccessNumberSelect = $all.find(".sceneSms select.sceneSmsAccessNumberSelect"),  // 接入号
                $sceneSmsMessageContent = $all.find(".sceneSms textarea.sceneSmsMessageContent"),        // 短信内容
                $sceneSmsContentId = $all.find("input.sceneSmsContentId"),                               // 短信内容Id
                $sceneSmsMessageSelectButton = $all.find(".sceneSmsMessageSelectButton"),                // 选择按钮
                $sceneRuleSmsSelectTr = $all.find("div.sceneRule"),                                      // 场景规则行
                $sceneRuleSmsSelect = $all.find("select.sceneRuleSmsSelect"),                            // 场景规则
                $testPhones = $dialog.find("textarea.marketTestPhones"),        // 测试号
                $testNumbersSelectBtn = $dialog.find(".testNumbersSelectBtn"),  // 测试号 选择按钮
                $testNumbersSendBtn = $dialog.find(".testNumbersSendBtn"),      // 测试号 发送按钮
                $warnMessage = $all.find("div.warnMessage"),                    // 友情提醒

                $userGroupInfo = $all.find("div.userGroupInfo"),        // 任务基本信息
                $jobConfigureInfo = $all.find("div.jobConfigureInfo"),  // 客户接触渠道
                $channelInfo = $all.find("div.channelInfo"),            // 目标用户选择
                $jobAuditInfo = $all.find("div.auditInfo"),             // 预览

                $preStep = $all.find("span.pre"),           // 上一步
                $nextStep = $all.find("span.next"),         // 下一步
                $confirmStep = $all.find("span.confirm"),   // 确定
                $closeBtn = $all.find("span.closeBtn"),     // 关闭
                $editStep = $all.find("span.edit"),         // 返回修改
                $operateData = $all.find("div.data"),       // 记录状态
                $flowStepA = $all.find("div.flowStepContainer div.flowStep div.flowStepA"),
                $flowStepB = $all.find("div.flowStepContainer div.flowStep div.flowStepB"),
                $flowStepC = $all.find("div.flowStepContainer div.flowStep div.flowStepC"),
                $flowStepD = $all.find("div.flowStepContainer div.flowStep div.flowStepD");
            // var $flowStepE = $all.find("div.flowStepContainer div.flowStep div.flowStepE");

            initSmsAccessNumberSelect();
            initStartEndTime();
            initEvents();

            /**
             * 初始化 接入号下拉框
             */
            function initSmsAccessNumberSelect() {
                $smsAccessNumberSelect.empty();
                $sceneSmsAccessNumberSelect.empty();
                var accessNumbers = globalConfigConstant.smsAccessNumber;
                for (var i = 0; i < accessNumbers.length; i++) {
                    $smsAccessNumberSelect.append("<option value='" + accessNumbers[i].accessNumber + "' selected>" + accessNumbers[i].accessNumber + "</option>")
                    $sceneSmsAccessNumberSelect.append("<option value='" + accessNumbers[i].accessNumber + "' selected>" + accessNumbers[i].accessNumber + "</option>")
                }
            }

            /**
             * 初始化 开始、结束时间、监控开始、结束时间
             */
            function initStartEndTime() {
                var currentDate = new Date();
                var startDate = new Date(currentDate.setHours(currentDate.getHours() + 1)),
                    year = startDate.getFullYear(),
                    month = startDate.getMonth() + 1,    //js从0开始取
                    day = startDate.getDate(),
                    finishDate = new Date(currentDate.setDate(currentDate.getDate() + 30)),
                    endYear = finishDate.getFullYear(),
                    endMonth = finishDate.getMonth() + 1,    //js从0开始取
                    endDay = finishDate.getDate();
                $startTimeInput.val(year + "-" + setStandardData(month) + "-" + setStandardData(day));
                $endTimeInput.val(endYear + "-" + setStandardData(endMonth) + "-" + setStandardData(endDay));

                $monitoringStartTimeInput.val("09:00"); // 监控开始时间
                $monitoringEndTimeInput.val("18:00");   // 监控结束时间
            }

            function setStandardData(data) {
                if (data < 10) {
                    return "0" + data;
                }
                return data;
            }

            /**
             * 初始化 场景规则下拉框
             */
            function initSceneRuleSmsSelect() {
                globalRequest.iScheduling.querySenceRuleSmsType(false, {}, function (data) {
                    $sceneRuleSmsSelect.empty();
                    if (data) {
                        for (var i = 0; i < data.length; i++) {
                            if (i === 0) {
                                $sceneRuleSmsSelect.append("<option value='A' selected>B</option>".replace(/A/g, data[i].id).replace(/B/g, data[i].name));
                            } else {
                                $sceneRuleSmsSelect.append("<option value='A'>B</option>".replace(/A/g, data[i].id).replace(/B/g, data[i].name));
                            }
                        }
                    }
                }, function () {
                    layer.alert("系统异常，获取场景短信失败", {icon: 6});
                })
            }

            /**
             * 初始化 事件
             */
            function initEvents() {
                /**
                 * 调度类型 下拉框 事件
                 */
                $marketTimerSelect.change(function () {
                    $.trim($(this).val()) == taskMgr_enum.schedule_type.single ? $marketMonitoringTimeRow.show() : $marketMonitoringTimeRow.hide();
                })

                /**
                 * 选择分组营销 事件
                 */
                $waveCount.change(function () {
                    if ($(this).val() != 1) {
                        $marketWaveCountInfo.hide();
                        $singleValueTitle.text("时间间隔：");
                        return;
                    }

                    $singleValueTitle.text("分组间隔：");
                    if (isNaN($singleValue.val()) || !$singleValue.val()) {
                        $marketWaveCountInfo.hide();
                        return;
                    }
                    var timeInterval = dateUtil.getDifferenceDay($startTimeInput.val(), $endTimeInput.val());
                    if (timeInterval > 90) {
                        layer.tips("开始时间与结束时间最多相隔90天", $endTimeInput);
                        $endTimeInput.focus();
                        return;
                    }
                    if (($singleValue.val() > 1) && $singleValue.val() >= timeInterval + 1) {
                        layer.tips("触发营销间隔不能大于等于任务开始时间与结束时间的间隔", $singleValue);
                        $singleValue.focus();
                        $marketWaveCountInfo.hide();
                        return;
                    }
                    var diffDay = Math.floor(timeInterval / $singleValue.val()) + 1;
                    if (isNaN(diffDay)) {
                        $marketWaveCountInfo.hide();
                        return;
                    }
                    $all.find("span.waveCountBatch").text(diffDay);
                    $all.find("span.waveCountDay").text($singleValue.val())
                    $marketWaveCountInfo.show();
                })

                /**
                 * 分组间隔/时间间隔 鼠标失去焦点事件
                 */
                $singleValue.blur(function () {
                    var $boidSave = $all.find("label.checkbox-inline :radio:checked");
                    var boidSaveValue = $.trim($boidSave.val());
                    if (boidSaveValue != 1) {
                        $marketWaveCountInfo.hide();
                        return;
                    }
                    if (isNaN($(this).val()) || !$(this).val()) {
                        $marketWaveCountInfo.hide();
                        return;
                    }
                    var timeInterval = dateUtil.getDifferenceDay($startTimeInput.val(), $endTimeInput.val());
                    if (timeInterval > 90) {
                        layer.tips("开始时间与结束时间最多相隔90天", $endTimeInput);
                        $endTimeInput.focus();
                        return;
                    }
                    if (($singleValue.val() > 1) && $singleValue.val() >= timeInterval + 1) {
                        layer.tips("触发营销间隔不能大于等于任务开始时间与结束时间的间隔", $singleValue);
                        $singleValue.focus();
                        $marketWaveCountInfo.hide();
                        return;
                    }
                    var diffDay = Math.floor(timeInterval / $singleValue.val()) + 1;
                    if (isNaN(diffDay)) {
                        $marketWaveCountInfo.hide();
                        return;
                    }
                    $all.find("span.waveCountBatch").text(diffDay);
                    $all.find("span.waveCountDay").text($singleValue.val())
                    $marketWaveCountInfo.show();
                })

                /**
                 * 选择营销地区 事件
                 */
                $marketAreaNames.click(function () {
                    var setting = {
                        view: {
                            dblClickExpand: false,
                            selectedMulti: true,
                            txtSelectedEnable: true,
                            showLine: false
                        },
                        data: {
                            simpleData: {
                                enable: true
                            },
                            keep: {
                                parent: true,
                                leaf: true
                            }
                        },
                        check: {
                            enable: true,
                            chkStyle: "checkbox"
                        },
                        callback: {}
                    }
                    globalRequest.iScheduling.queryUserAreas(true, {}, function (data) {
                        if (data && data.length > 0) {
                            //选中的地域弹框时勾选
                            var selectIds = $marketAreaIds.val() ? $marketAreaIds.val().split(",") : [];
                            for (var i = 0; i < data.length; i++) {
                                if ((data.length == selectIds.length + 1) || selectIds.indexOf(data[i].id + "") >= 0) {
                                    data[i].checked = true;
                                }
                            }
                        }

                        $.fn.zTree.init($("#treePrimary"), setting, data);

                        $plugin.iModal({
                            title: '选择营销地区',
                            content: $("#dialogTreePrimary"),
                            area: '750px'
                        }, function (index) {
                            var zTree = $.fn.zTree.getZTreeObj("treePrimary");
                            var nodes = zTree.getCheckedNodes();
                            var areaNames = [], areaIds = [];
                            for (var i = 0; i < nodes.length; i++) {
                                if (nodes[i].id != 99999) {
                                    areaNames.push(nodes[i].name);
                                    areaIds.push(nodes[i].id);
                                }
                            }
                            $marketAreaIds.val(areaIds.join(","));
                            $marketAreaNames.val(areaNames.join(","));
                            layer.close(index);
                        })
                    }, function () {
                        layer.alert("系统异常：查询用户目录失败");
                    })
                })
                /**
                 * 营销方式 下拉框 事件
                 */
                $marketTypeSelect.change(function (event, showPage) {
                    var value = $marketTypeSelect.val();
                    if ("sms" === value) {
                        $all.find(".jobConfigureInfo .sceneSms").hide();
                        $all.find(".jobConfigureInfo .sms").show();
                        initSmsAccessNumberSelect();
                        $sceneRuleSmsSelectTr.hide();
                        $marketTimerSelect.removeAttr("disabled");
                        $all.find("label.checkbox-inline :radio[value='0']").removeAttr("disabled")
                        $all.find("label.checkbox-inline :radio[value='1']").removeAttr("disabled")
                        $testPhones.attr("warn", "true");
                    } else if ("sceneSms" === value) {
                        $all.find(".jobConfigureInfo .sms").hide();
                        $all.find(".jobConfigureInfo .sceneSms").show();
                        initSceneRuleSmsSelect();
                        $sceneRuleSmsSelectTr.show();
                        // 场景营销---调度类别改成自动调度并置灰，分组营销改为否并置灰
                        $marketTimerSelect.val("single").change();
                        $marketTimerSelect.attr("disabled", "disabled");
                        $testPhones.attr("warn", "true");
                        $all.find("label.checkbox-inline :radio[value='0']").attr("disabled", "disabled")
                        $all.find("label.checkbox-inline :radio[value='1']").attr("disabled", "disabled")
                        $all.find("label.checkbox-inline :radio[value='1']").click();
                    } else {
                        $all.find("label.checkbox-inline :radio[value='0']").removeAttr("disabled")
                        $all.find("label.checkbox-inline :radio[value='1']").removeAttr("disabled")
                        $marketTimerSelect.removeAttr("disabled");
                        $testPhones.attr("warn", "true");
                    }
                    $warnMessage.hide();

                    if (showPage) {
                        $testPhones.attr("warn", "false");
                        // $marketTimerSelect.attr("disabled", "disabled");
                    }
                })
                /**
                 * 选择测试号 事件
                 */
                $testNumbersSelectBtn.click(function () {
                    var $selectDialog = $("#dialogMiddle");
                    var $testNumberAll = $(".iMarket_Content").find("div.selectTestNumberInfo").clone();
                    $selectDialog.empty().append($testNumberAll);

                    var selectedTestNumbers = $testPhones.val() ? $testPhones.val().split(",") : [];

                    initData();
                    initEvent();

                    function initData() {
                        var options = {
                            ele: $testNumberAll.find('table.testNumberTab'),
                            ajax: {url: "queryTestPhoneNumbersByPage.view", type: "POST"},
                            columns: [
                                {
                                    data: "id", width: "10%", className: "centerColumns",
                                    render: function (data, type, row) {
                                        if (selectedTestNumbers.indexOf(row.testPhoneNumber + "") >= 0) {
                                            return '<input type="checkbox" name="id" checked value="' + row.testPhoneNumber + '"/>';
                                        }
                                        return '<input type="checkbox" name="id" value="' + row.testPhoneNumber + '"/>';
                                    }
                                },
                                {data: "testPhoneNumber", title: "号码", width: "45%", className: "centerColumns"},
                                {data: "userName", title: "号码所属用户", width: "45%", className: "centerColumns"},
                            ]
                        };
                        $plugin.iCompaignTable(options);
                    }

                    function initEvent() {
                        $testNumberAll.on("click", "input[name = id]", function () {
                            var $this = $(this);
                            if ($this.is(":checked")) {
                                selectedTestNumbers.push($this.val());
                            } else {
                                var index = selectedTestNumbers.indexOf($this.val());
                                selectedTestNumbers.splice(index, 1);
                            }
                        })
                    }

                    $plugin.iModal({
                        title: '选择测试号',
                        content: $selectDialog,
                        area: '750px'
                    }, function (index) {
                        $testPhones.val(selectedTestNumbers.join(","));
                        layer.close(index);
                    })
                })
                /**
                 * 发送测试号 事件
                 */
                $testNumbersSendBtn.click(function () {
                    var value = $marketTypeSelect.val();
                    var content = "", accessNumber = "";

                    if ("sms" === value) {
                        if (!$smsMessageContent.val()) {
                            layer.tips($smsMessageContent.attr("title"), $smsMessageContent);
                            $smsMessageContent.focus();
                            return;
                        }
                        content = $smsMessageContent.val();
                        accessNumber = $smsAccessNumberSelect.val();
                    }
                    else if ("sceneSms" === value) {
                        if (!$sceneSmsMessageContent.val()) {
                            layer.tips($sceneSmsMessageContent.attr("title"), $sceneSmsMessageContent);
                            $sceneSmsMessageContent.focus();
                            return;
                        }
                        content = $sceneSmsMessageContent.val();
                        accessNumber = $sceneSmsAccessNumberSelect.val();
                    }

                    if (!$testPhones.val()) {
                        layer.tips($testPhones.attr("title"), $testPhones);
                        $testPhones.focus();
                        return;
                    }
                    var data = obj.verifyMob($testPhones.val());
                    if (!data.ret) {
                        layer.tips(data.desc, $testPhones);
                        $testPhones.focus();
                        return;
                    }

                    //测试号短信发送提醒
                    $warnMessage.hide();
                    $testPhones.attr("warn", "false");
                    //短信发送
                    var params = {
                        testNumbers: $testPhones.val(),
                        contentId: $smsContentId.val(),
                        content: content,
                        accessNumber: accessNumber
                    };
                    globalRequest.iScheduling.sendMarketingTaskTestSms(true, params, function (data) {
                        if (data.retValue === 0) {
                            layer.msg("测试短信发送成功", {time: 1000})
                        } else {
                            layer.alert("发送失败，" + data.desc, {icon: 6});
                        }
                    }, function () {
                        layer.alert("系统异常", {icon: 6});
                    })
                })
                /**
                 * 短信 营销方式 选择短信内容 事件
                 */
                $smsMessageSelectButton.click(function () {
                    var $dialog = $("#dialogMiddle"),
                        $all = $(".iMarket_Content").find("div.selectSmsContentInfo").clone(),
                        selectedSmsContent = $smsContentId.val();
                    $dialog.empty().append($all);
                    var dataTable, url = "querySmsContentsByPage.view";
                    initData();

                    function initData() {
                        var options = {
                            ele: $dialog.find('table.smsContentTab'),
                            ajax: {url: url, type: "POST"},
                            columns: [
                                {data: "id", width: "8%", className: "hideRadio"},
                                {data: "content", title: "内容名称", width: "42%", className: "centerColumns"},
                                {data: "keywords", title: "关键词", width: "30%", className: "centerColumns"},
                                {data: "url", title: "内容地址", width: "20%", className: "centerColumns"}
                            ],
                            createdRow: function (row, data, dataIndex) {
                                if (data.id == selectedSmsContent) {
                                    $(row).addClass('selected');
                                }
                            }
                        };
                        dataTable = $plugin.iCompaignTable(options);
                    }

                    initEvent();

                    function initEvent() {
                        $dialog.find(" div.selectSmsContentInfo div.col-md-12 button.smsContentSearchBtn").click(function () {
                            var params = "searchContent=" + $.trim($dialog.find(".selectSmsContentInfo .qryContentInfo").val()) + "&key=" + $.trim($dialog.find(".selectSmsContentInfo .qryKeyInfo").val());
                            $plugin.iCompaignTableRefresh(dataTable, "querySmsContentsByPage.view" + "?" + params);
                        })

                        $dialog.find('table.smsContentTab tbody').on('click', 'tr', function () {
                            if ($(this).hasClass("selected")) {
                                $(this).removeClass("selected").siblings("tr").removeClass("selected");
                            }
                            else {
                                $(this).addClass("selected").siblings("tr").removeClass("selected");
                            }
                        })
                    }

                    $plugin.iModal({
                        title: '选择短信内容',
                        content: $dialog,
                        area: '750px'
                    }, function (index, layero) {
                        var $selectTr = layero.find('table.smsContentTab').find("tr.selected");
                        if ($selectTr.length > 0) {
                            var $tds = $selectTr.find("td");
                            var content = $tds.eq(1).text(),
                                contentId = $tds.eq(0).text(),
                                contentUrl = $tds.eq(3).text();
                            content = content.replace(/\{0}/g, contentUrl);
                            $smsContentId.val(contentId);
                            $smsMessageContent.val(content);
                        } else {
                            $smsContentId.val("");
                            $smsMessageContent.val("");
                        }
                        layer.close(index);
                    })
                })
                /**
                 * 场景短信 营销方式 选择短信内容 事件
                 */
                $sceneSmsMessageSelectButton.click(function () {
                    var $dialog = $("#dialogMiddle"),
                        $all = $(".iMarket_Content").find("div.selectSmsContentInfo").clone(),
                        selectedSmsContent = $smsContentId.val();
                    $dialog.empty().append($all);
                    var dataTable, url = "querySmsContentsByPage.view";
                    initData();

                    function initData() {
                        var options = {
                            ele: $dialog.find('table.smsContentTab'),
                            ajax: {url: url, type: "POST"},
                            columns: [
                                {data: "id", width: "8%", className: "hideRadio"},
                                {data: "content", title: "内容名称", width: "42%", className: "centerColumns"},
                                {data: "keywords", title: "关键词", width: "30%", className: "centerColumns"},
                                {data: "url", title: "内容地址", width: "20%", className: "centerColumns"}
                            ],
                            createdRow: function (row, data, dataIndex) {
                                if (data.id == selectedSmsContent) {
                                    $(row).addClass('selected');
                                }
                            }
                        };
                        dataTable = $plugin.iCompaignTable(options);
                    }

                    initEvent();

                    function initEvent() {
                        $dialog.find(" div.selectSmsContentInfo div.col-md-12 button.smsContentSearchBtn").click(function () {
                            var params = "searchContent=" + $.trim($dialog.find(".selectSmsContentInfo .qryContentInfo").val()) + "&key=" + $.trim($dialog.find(".selectSmsContentInfo .qryKeyInfo").val());
                            $plugin.iCompaignTableRefresh(dataTable, "querySmsContentsByPage.view" + "?" + params);
                        })

                        $dialog.find('table.smsContentTab tbody').on('click', 'tr', function () {
                            if ($(this).hasClass("selected")) {
                                $(this).removeClass("selected").siblings("tr").removeClass("selected");
                            }
                            else {
                                $(this).addClass("selected").siblings("tr").removeClass("selected");
                            }
                        })
                    }

                    $plugin.iModal({
                        title: '选择短信内容',
                        content: $dialog,
                        area: '750px'
                    }, function (index, layero) {
                        var $selectTr = layero.find('table.smsContentTab').find("tr.selected");
                        if ($selectTr.length > 0) {
                            var $tds = $selectTr.find("td");
                            var content = $tds.eq(1).text(),
                                contentId = $tds.eq(0).text(),
                                contentUrl = $tds.eq(3).text();
                            content = content.replace(/\{0}/g, contentUrl);
                            $sceneSmsContentId.val(contentId);
                            $sceneSmsMessageContent.val(content);
                        } else {
                            $sceneSmsContentId.val("");
                            $sceneSmsMessageContent.val("");
                        }
                        layer.close(index);
                    })
                })
                /**
                 *  选择营销用户事件
                 */
                $marketSegmentNames.click(function () {
                    var setting = {
                        check: {
                            enable: true,
                            chkStyle: 'checkbox',
                            radioType: "all"
                        },
                        view: {
                            dblClickExpand: true,
                            selectedMulti: true
                        },
                        data: {
                            simpleData: {
                                enable: true
                            }
                        },
                        callback: {
                            beforeCheck: function (treeId, treeNode, clickFlag) {
                                if (treeNode.children) {
                                    if (!verifyTreeNodeCheck(treeNode.children)) {
                                        layer.msg("最多选择3个客户群")
                                        return false;
                                    }
                                }

                                if (treeNode.checked) {
                                    return true;
                                }

                                var checkedNodes = $.fn.zTree.getZTreeObj("treePrimary").getCheckedNodes(true);
                                if (!verifyTreeNodeCheck(checkedNodes)) {
                                    layer.msg("最多选择3个客户群")
                                    return false;
                                }
                            }
                        }
                    };

                    globalRequest.iScheduling.queryAllModelsUnderCatalog(true, {}, function (data) {
                        var ids = $marketSegmentIds.val().split(",");
                        var result = [{id: '-1', pId: '-2', name: "暂无相关信息", isParent: true, nocheck: true}];
                        setParentChecked(ids, data);

                        function setParentChecked(modelIds, data) {
                            if (modelIds && modelIds.length > 0) {
                                for (var i = 0; i < modelIds.length; i++) {
                                    for (var j = 0; j < data.length; j++) {
                                        if (data[j].id == modelIds[i]) {
                                            data[j]["checked"] = true;
                                            data[j]["open"] = true;
                                            setParentChecked([data[j].pId], data);
                                            break;
                                        }
                                    }
                                }
                            }
                        }

                        result = data;

                        $.fn.zTree.init($("#treePrimary"), setting, result);

                        $plugin.iModal({
                            title: '选择客户群',
                            content: $("#dialogTreePrimary"),
                            area: '750px'
                        }, function (index) {
                            var zTree = $.fn.zTree.getZTreeObj("treePrimary");
                            var checkedNodes = zTree.getCheckedNodes(true);
                            var checkedNodesLength = checkedNodes.length;
                            if (checkedNodesLength > 0) {
                                var names = [], ids = [], counts = 0, hasSegment = false, moreNodes = 0;
                                for (var i = 0; i < checkedNodesLength; i++) {
                                    if (!checkedNodes[i].isParent) {
                                        moreNodes++;
                                        ids.push(checkedNodes[i].id);
                                        names.push(checkedNodes[i].name);
                                        counts += checkedNodes[i].element.lastRefreshCount;
                                        hasSegment = true;
                                    }
                                }
                                if (!hasSegment) {
                                    layer.msg("请选择客户群")
                                    return;
                                }
                                if (moreNodes > 3) {
                                    layer.msg("最多选择3个客户群")
                                    return;
                                }
                                $marketSegmentNames.val(names.join(","));
                                $marketSegmentIds.val(ids.join(","));
                                $segmentCounts.val(counts);
                                layer.close(index);
                            } else {
                                layer.alert("没有选择任何客户群");
                            }
                        })
                    }, function (data) {
                        layer.alert("查询客户群失败", {icon: 6});
                    })
                })
                /**
                 * 关闭 事件
                 */
                $closeBtn.click(function () {
                    var dialogIndex = $operateData.attr("index");
                    layer.close(dialogIndex);
                    if ($operateData.attr("operate") === "modelToCreate") {
                        $("#menuTree").find("a.model_content").trigger("click");
                    }
                })
                /**
                 * 上一步 事件
                 */
                $preStep.click(function () {
                    var $this = $(this);
                    if ($channelInfo.hasClass("active")) {
                        $flowStepB.find("span").removeClass("active");
                        $flowStepA.find("span").addClass("active");
                        $userGroupInfo.siblings("div.row").removeClass("active");
                        $userGroupInfo.addClass("active");

                        $this.parent().find("span").removeClass("pageB");
                        $preStep.removeClass("active");
                    } else if ($jobConfigureInfo.hasClass("active")) {
                        $flowStepC.find("span").removeClass("active");
                        $flowStepB.find("span").addClass("active");
                        $channelInfo.siblings("div.row").removeClass("active");
                        $channelInfo.addClass("active");

                        $this.parent().find("span").removeClass("pageC").addClass("pageB");
                        $nextStep.addClass("active");
                        $confirmStep.removeClass("active");
                    } else if ($jobAuditInfo.hasClass("active")) {
                        $flowStepD.find("span").removeClass("active");
                        $flowStepC.find("span").addClass("active");
                        $jobConfigureInfo.siblings("div.row").removeClass("active");
                        $jobConfigureInfo.addClass("active");
                        $this.parent().find("span").removeClass("pageC").addClass("pageB");
                        $nextStep.addClass("active");
                        $confirmStep.removeClass("active");
                    }
                })

                /**
                 * 返回修改 事件
                 */
                $editStep.click(function () {
                    $userGroupInfo.siblings("div.row").removeClass("active");
                    $userGroupInfo.addClass("active");
                    $editStep.removeClass("active");
                    $nextStep.addClass("active");
                    $flowStepA.find("span").addClass("active");
                })
                var marketJobDomain = {};
                if (savaValue) {
                    marketJobDomain = savaValue;
                }
                //营销方式

                /**
                 * 下一步 事件
                 */
                $nextStep.click(function () {
                    var $this = $(this);
                    //如果没有nextB说明下一页的按钮是第一页面的
                    //如果有nextB说明是从第二页面的
                    if ($userGroupInfo.hasClass("active")) {    // 任务基本信息
                        globalRequest.iScheduling.checkMarketingTaskName(true, {
                            id: savaValue ? savaValue.id : "",
                            name: $marketJobName.val()
                        }, function (data) {
                            if (data.retValue == 0) {
                                if (utils.valid($marketJobName, utils.is_EnglishChineseNumber, marketJobDomain, "name")
                                    && utils.valid($marketTimerSelect, utils.notEmpty, marketJobDomain, "scheduleType")
                                    && utils.valid($marketBusinessTypeSelect, utils.notEmpty, marketJobDomain, "businessType")
                                    && utils.valid($startTimeInput, utils.isDateForYMD, marketJobDomain, "startTime")
                                    && utils.valid($endTimeInput, utils.isDateForYMD, marketJobDomain, "stopTime")
                                    && utils.valid($marketLimitSelect, utils.notEmpty, marketJobDomain, "repeatStrategy")
                                    && utils.valid($singleValue, utils.isPostiveNumber, marketJobDomain, "sendInterval")
                                    && utils.valid($marketTypeSelect, utils.notEmpty, marketJobDomain, "marketType")
                                    && utils.valid($marketRemarks, utils.any, marketJobDomain, "remarks")) {
                                    // 开始、结束时间判断
                                    if ($startTimeInput.val() > $endTimeInput.val()) {
                                        layer.tips("开始时间不能大于结束时间", $endTimeInput);
                                        $endTimeInput.focus();
                                        return;
                                    }
                                    // 时间间隔判断
                                    var timeInterval = dateUtil.getDifferenceDay($startTimeInput.val(), $endTimeInput.val());
                                    if (timeInterval > 90) {
                                        layer.tips("开始时间与结束时间最多相隔90天", $endTimeInput);
                                        $endTimeInput.focus();
                                        return;
                                    }
                                    if (($singleValue.val() > 1) && $singleValue.val() >= timeInterval + 1) {
                                        layer.tips("触发营销间隔不能大于等于任务开始时间与结束时间的间隔", $singleValue);
                                        $singleValue.focus();
                                        return;
                                    }

                                    // 监控开始、结束时间判断
                                    if ($marketTimerSelect.val() == taskMgr_enum.schedule_type.single) {
                                        if ($monitoringStartTimeInput.val() > $monitoringEndTimeInput.val()) {
                                            layer.tips("监控开始时间不能大于监控结束时间", $monitoringEndTimeInput);
                                            $monitoringEndTimeInput.focus();
                                            return;
                                        }
                                        marketJobDomain["beginTime"] = $.trim($monitoringStartTimeInput.val());
                                        marketJobDomain["endTime"] = $.trim($monitoringEndTimeInput.val());
                                    }

                                    // 分组营销
                                    var $boidSave = $all.find("label.checkbox-inline :radio:checked");
                                    var boidSaveValue = $.trim($boidSave.val());
                                    if (boidSaveValue != 0 && boidSaveValue != 1) {
                                        layer.tips("分组营销数据异常，请关闭重试", $boidSave);
                                        $boidSave.focus();
                                    }
                                    marketJobDomain["isBoidSale"] = boidSaveValue;

                                    //流程步骤二图片点亮
                                    $flowStepA.find("span").removeClass("active");
                                    $flowStepB.find("span").addClass("active");
                                    //第二页面显示
                                    $channelInfo.siblings("div.row").removeClass("active");
                                    $channelInfo.addClass("active");
                                    //给所有按钮添加pageB
                                    $this.parent().find("span").addClass("pageB");
                                    $preStep.addClass("active");
                                } else {
                                    return;
                                }
                            } else {
                                layer.tips(data.desc, $marketJobName);
                                $marketJobName.focus();
                            }
                        })
                    } else if ($channelInfo.hasClass("active")) {   // 目标用户选择
                        if (utils.valid($marketJobName, utils.is_EnglishChineseNumber, marketJobDomain, "name")
                            && utils.valid($marketTimerSelect, utils.notEmpty, marketJobDomain, "scheduleType")
                            && utils.valid($marketBusinessTypeSelect, utils.notEmpty, marketJobDomain, "businessType")
                            && utils.valid($startTimeInput, utils.isDateForYMD, marketJobDomain, "startTime")
                            && utils.valid($endTimeInput, utils.isDateForYMD, marketJobDomain, "stopTime")
                            && utils.valid($marketLimitSelect, utils.notEmpty, marketJobDomain, "repeatStrategy")
                            && utils.valid($singleValue, utils.isPostiveNumber, marketJobDomain, "sendInterval")
                            && utils.valid($marketRemarks, utils.any, marketJobDomain, "remarks")

                            && utils.valid($marketSegmentNames, utils.notEmpty, marketJobDomain, "marketSegmentNames")
                            && utils.valid($marketSegmentIds, utils.notEmpty, marketJobDomain, "marketSegmentIds")
                            && utils.valid($segmentCounts, utils.isNumber, marketJobDomain, "marketSegmentUserCounts")
                            && (utils.valid($numberLimitInput, utils.any, marketJobDomain, "marketUserCountLimit") || utils.valid($numberLimitInput, utils.isPostiveNumberNotZero, marketJobDomain, "marketUserCountLimit"))
                            && utils.valid($marketAreaIds, utils.any, marketJobDomain, "areaCodes")
                            && utils.valid($marketAreaNames, utils.any, marketJobDomain, "areaNames")
                        ) {
                            if ($numberLimitInput.val() && $numberLimitInput.val() <= 0) {
                                layer.tips("限制人数必须输入大于0的正整数", $numberLimitInput);
                                $numberLimitInput.focus();
                                return;
                            }

                            //流程步骤三图片点亮
                            $flowStepB.find("span").removeClass("active");
                            $flowStepC.find("span").addClass("active");
                            //显示第三页面
                            $jobConfigureInfo.siblings("div.row").removeClass("active");
                            $jobConfigureInfo.addClass("active");
                            //给所有按钮添加pageC
                            $this.parent().find("span").removeClass("pageB").addClass("pageC");
                            //$nextStep.removeClass("active");
                            //$confirmStep.addClass("active");
                        } else {
                            return;
                        }
                    } else if ($jobConfigureInfo.hasClass("active")) {  // 客户接触渠道
                        if (utils.valid($marketJobName, utils.is_EnglishChineseNumber, marketJobDomain, "name")
                            && utils.valid($marketTimerSelect, utils.notEmpty, marketJobDomain, "scheduleType")
                            && utils.valid($marketBusinessTypeSelect, utils.notEmpty, marketJobDomain, "businessType")
                            && utils.valid($startTimeInput, utils.isDateForYMD, marketJobDomain, "startTime")
                            && utils.valid($endTimeInput, utils.isDateForYMD, marketJobDomain, "stopTime")
                            && utils.valid($marketLimitSelect, utils.notEmpty, marketJobDomain, "repeatStrategy")
                            && utils.valid($singleValue, utils.isPostiveNumber, marketJobDomain, "sendInterval")
                            && utils.valid($marketRemarks, utils.any, marketJobDomain, "remarks")

                            && utils.valid($marketSegmentNames, utils.notEmpty, marketJobDomain, "marketSegmentNames")
                            && utils.valid($marketSegmentIds, utils.notEmpty, marketJobDomain, "marketContent")
                            && utils.valid($marketSegmentIds, utils.notEmpty, marketJobDomain, "marketSegmentIds")
                            && utils.valid($segmentCounts, utils.isNumber, marketJobDomain, "marketSegmentUserCounts")
                            && (utils.valid($numberLimitInput, utils.any, marketJobDomain, "marketUserCountLimit") || utils.valid($numberLimitInput, utils.isPostiveNumberNotZero, marketJobDomain, "marketUserCountLimit"))
                            && checkMarketTypeParas()
                            && utils.valid($marketAreaIds, utils.any, marketJobDomain, "areaCodes")
                            && utils.valid($marketAreaNames, utils.any, marketJobDomain, "areaNames")

                            // && utils.valid($marketTypeSelect, utils.notEmpty, marketJobDomain, "marketType")
                            // && checkMarketTypeParas()
                            && utils.valid($testPhones, utils.any, marketJobDomain, "testPhones")
                        ) {

                            var data = obj.verifyMob($testPhones.val());
                            if (!data.ret) {
                                layer.tips(data.desc, $testPhones);
                                $testPhones.focus();
                                return;
                            }

                            if ($testPhones.attr("warn") == "true") {
                                $warnMessage.show();
                                $testPhones.attr("warn", "false");
                                return;
                            }
                            if (marketJobDomain["marketType"] === "sms") {
                                marketJobDomain['marketContent'] = $smsMessageContent.val()
                            } else if (marketJobDomain["marketType"] === "sceneSms") {
                                marketJobDomain['marketContent'] = $sceneSmsMessageContent.val()
                            }
                            $confirmStep.addClass("active");
                            // 预览
                            var $dialog = $(".previewDiv");
                            // 加载静态页面
                            var $panel = $(".iMarket_preview").find("div.taskMgrDetailInfo").clone();
                            $dialog.find("div.taskMgrDetailInfo").remove();
                            $dialog.append($panel);

                            $nextStep.removeClass("active");
                            $flowStepC.find("span").removeClass("active");
                            $flowStepD.find("span").addClass("active");
                            // 第二页面显示
                            $dialog.siblings("div.jobConfigureInfo").removeClass("active");
                            $dialog.addClass("active");

                            var $shopTaskDetailInfo = $(".previewDiv div.taskMgrDetailInfo");

                            // 任务基本信息
                            $shopTaskDetailInfo.find(".detail_taskName").text(marketJobDomain.name || "空");
                            $shopTaskDetailInfo.find(".detail_businessTypeSelect").text(obj.getBusinessType(marketJobDomain.businessType));

                            var type = marketJobDomain.scheduleType;
                            var scheduleType = "空";
                            if (type == taskMgr_enum.schedule_type.single) {
                                scheduleType = taskMgr_enum.schedule_type.single_text;
                                $shopTaskDetailInfo.find(".detail_monitoringStartTime").text(marketJobDomain.beginTime || "09:00");
                                $shopTaskDetailInfo.find(".detail_monitoringEndTime").text(marketJobDomain.endTime || "18:00");
                                $shopTaskDetailInfo.find(".monitoring_row").show();
                            } else if (type == taskMgr_enum.schedule_type.manu) {
                                scheduleType = taskMgr_enum.schedule_type.manu_text;
                            }
                            $shopTaskDetailInfo.find(".detail_timerSelect").text(scheduleType);
                            $shopTaskDetailInfo.find(".detail_startTime").text(marketJobDomain.startTime || "空");
                            $shopTaskDetailInfo.find(".detail_endTime").text(marketJobDomain.stopTime || "空");
                            $shopTaskDetailInfo.find(".detail_boidSale").text(marketJobDomain.isBoidSale == 1 ? "是" : "否");
                            $shopTaskDetailInfo.find(".detail_intervalInSeconds_title").text(marketJobDomain.isBoidSale == 1 ? "分组间隔：" : "时间间隔：");
                            // $shopTaskDetailInfo.find(".detail_repeatStrategy").text(!marketJobDomain.repeatStrategy ? "空" : "营销频次剔重（" + marketJobDomain.repeatStrategy + "）天");
                            $shopTaskDetailInfo.find(".detail_intervalInSeconds").text(marketJobDomain.sendInterval ? marketJobDomain.sendInterval + "天" : "空");
                            $shopTaskDetailInfo.find(".detail_areaNames").text(marketJobDomain.areaNames || "空");
                            $shopTaskDetailInfo.find(".detail_remarks").text(marketJobDomain.remarks || "空");


                            // 目标用户选择
                            $shopTaskDetailInfo.find(".detail_segmentNames").text(marketJobDomain.marketSegmentNames || "空");
                            $shopTaskDetailInfo.find(".detail_segmentCounts").text(marketJobDomain.marketSegmentUserCounts || "空");
                            // $shopTaskDetailInfo.find(".detail_number").text(parseInt(marketJobDomain.marketUserCountLimit) || "空");

                            // 客户接触渠道
                            $shopTaskDetailInfo.find(".marketTypeValue").hide();
                            var marketType = "空";
                            var marketTypeValue = marketJobDomain.marketType;
                            if (marketTypeValue == "sms") {
                                marketType = "短信";
                            } else if (marketTypeValue == "sceneSms") {
                                marketType = "场景短信";
                                // 场景规则显示
                                $shopTaskDetailInfo.find(".marketTypeValue").show();
                                $shopTaskDetailInfo.find(".detail_marketTypeValue").text($(".sceneRuleSmsSelect").find("option:selected").text());
                            }

                            $shopTaskDetailInfo.find(".detail_marketType").text(marketType);
                            $shopTaskDetailInfo.find(".detail_AccessNumber").text(marketJobDomain.accessNumber || "空");
                            $shopTaskDetailInfo.find(".detail_Content").text(marketJobDomain.marketContent || "空");
                            $shopTaskDetailInfo.find(".detail_TestPhones").text(marketJobDomain.testPhones || "空");
                        } else {
                            return;
                        }
                    }
                })
                /**
                 * 确定 事件
                 */
                $confirmStep.click(function () {
                    var type = $operateData.attr("operate");
                    var dialogIndex = $operateData.attr("index");
                    if (utils.valid($marketJobName, utils.is_EnglishChineseNumber, marketJobDomain, "name")
                        && utils.valid($marketTimerSelect, utils.notEmpty, marketJobDomain, "scheduleType")
                        && utils.valid($marketBusinessTypeSelect, utils.notEmpty, marketJobDomain, "businessType")
                        && utils.valid($startTimeInput, utils.isDateForYMD, marketJobDomain, "startTime")
                        && utils.valid($endTimeInput, utils.isDateForYMD, marketJobDomain, "stopTime")
                        && utils.valid($marketLimitSelect, utils.notEmpty, marketJobDomain, "repeatStrategy")
                        && utils.valid($singleValue, utils.isPostiveNumber, marketJobDomain, "sendInterval")
                        && utils.valid($marketAreaIds, utils.any, marketJobDomain, "areaCodes")
                        && utils.valid($marketAreaNames, utils.any, marketJobDomain, "areaNames")
                        && utils.valid($marketRemarks, utils.any, marketJobDomain, "remarks")

                        && utils.valid($marketSegmentNames, utils.notEmpty, marketJobDomain, "marketSegmentNames")
                        && utils.valid($marketSegmentIds, utils.notEmpty, marketJobDomain, "marketSegmentIds")
                        && (utils.valid($numberLimitInput, utils.any, marketJobDomain, "marketUserCountLimit") || utils.valid($numberLimitInput, utils.isPostiveNumberNotZero, marketJobDomain, "marketUserCountLimit"))

                        && utils.valid($marketTypeSelect, utils.notEmpty, marketJobDomain, "marketType")
                        && checkMarketTypeParas()
                        && utils.valid($testPhones, utils.any, marketJobDomain, "testPhones")
                    ) {
                        // 监控开始、结束时间
                        if ($marketTimerSelect.val() == taskMgr_enum.schedule_type.single) {
                            marketJobDomain["beginTime"] = $.trim($monitoringStartTimeInput.val());
                            marketJobDomain["endTime"] = $.trim($monitoringEndTimeInput.val());
                        } else {
                            marketJobDomain["beginTime"] = "09:00";
                            marketJobDomain["endTime"] = "18:00";
                        }
                        marketJobDomain["marketUserCountLimit"] = parseInt(marketJobDomain["marketUserCountLimit"]) || "";
                        delete marketJobDomain.operateType;

                        if (type === "create" || type === "modelToCreate") {
                            globalRequest.iScheduling.createMarketingTask(true, marketJobDomain, function (data) {
                                if (data.retValue === 0) {
                                    dataTable.ajax.reload();
                                    layer.close(dialogIndex);
                                    layer.msg("创建成功", {time: 1000});
                                    if (type === "modelToCreate") {
                                        $("#menuTree").find("a.model_content").trigger("click");
                                    }
                                } else {
                                    layer.alert(data.desc, {icon: 6});
                                }
                            })
                        } else if (type === "update" || type === "resubmit") {
                            marketJobDomain["id"] = savaValue.id;
                            globalRequest.iScheduling.updateMarketingTask(true, marketJobDomain, function (data) {
                                if (data.retValue === 0) {
                                    dataTable.ajax.reload();
                                    layer.close(dialogIndex);
                                    layer.msg("更新成功", {time: 1000});
                                } else {
                                    layer.alert(data.desc, {icon: 6});
                                }
                            })
                        } else if (type === "show") {
                            layer.close(dialogIndex);
                        } else {
                            layer.alert("系统异常", {icon: 6});
                        }
                    }
                })

                function checkMarketTypeParas() {
                    if (marketJobDomain["marketType"] == "sms") {
                        return utils.valid($smsAccessNumberSelect, utils.is_EnglishChineseNumber, marketJobDomain, "accessNumber")
                            && utils.valid($smsMessageContent, utils.notEmpty, marketJobDomain, "marketContent")
                            && utils.valid($smsContentId, utils.any, marketJobDomain, "marketContentId")
                    } else if (marketJobDomain["marketType"] == "sceneSms") {
                        return utils.valid($sceneSmsAccessNumberSelect, utils.is_EnglishChineseNumber, marketJobDomain, "accessNumber")
                            && utils.valid($sceneSmsMessageContent, utils.notEmpty, marketJobDomain, "marketContent")
                            && utils.valid($sceneSmsContentId, utils.any, marketJobDomain, "marketContentId")
                            && utils.valid($sceneRuleSmsSelect, utils.notEmpty, marketJobDomain, "marketTypeValue")
                    } else {
                        return true;
                    }
                }

                function verifyTreeNodeCheck(treeNodes) {
                    if (treeNodes) {
                        var i = 0, treeLength = treeNodes.length;
                        if (treeLength > 0) {
                            var moreNodes = 0;
                            for (i; i < treeLength; i++) {
                                if (!treeNodes[i].isParent) {
                                    moreNodes++;
                                }
                            }
                            if (moreNodes >= 3) {
                                return false;
                            }
                        }
                    }
                    return true;
                }
            }
        }
    }

    obj.handleTaskSetings = function ($dialog, initValue) {
        if (!$dialog || !initValue) {
            return;
        }
        /**
         * 任务基本信息
         */
        var $marketJobId = $dialog.find("input.id"),                        // 任务Id
            $marketJobName = $dialog.find("input.name"),                    // 任务名称
            $marketBusinessTypeSelect = $dialog.find("select.marketBusinessTypeSelect"),   // 业务类别
            $marketTimerSelect = $dialog.find("select.timerSelect"),        // 调度类型
            $startTimeInput = $dialog.find("input.startTime"),              // 开始时间
            $endTimeInput = $dialog.find("input.endTime"),                  // 结束时间
            $marketMonitoringTimeRow = $dialog.find(".marketMonitoringTime"),      // 监控行
            $monitoringStartTimeInput = $dialog.find("input.monitoringStartTime"), // 监控开始时间
            $monitoringEndTimeInput = $dialog.find("input.monitoringEndTime"),     // 监控结束时间
            $singleValueInput = $dialog.find("input.singleValue"),          // 时间间隔
            $marketLimitSelect = $dialog.find("select.marketLimitSelect"),  // 剔除策略
            $marketAreaNames = $dialog.find("textarea.areaNames"),          // 营销地区
            $marketAreaIds = $dialog.find("input.areaIds"),                 // 营销地区Id
            $mark = $dialog.find("textarea.remarks"),                       // 任务描述
            /**
             * 目标用户选择
             */
            $marketSegmentNames = $dialog.find(".marketSegments textarea.segmentNames"),    // 客户群
            $marketSegmentCounts = $dialog.find(".marketSegments .segmentCounts"),                // 客户群人数
            $marketSegmentIds = $dialog.find(".marketSegments .segmentIds"),                // 客户群Id
            $numberLimitInput = $dialog.find("input.numberLimit"),                          // 限制人数
            /**
             * 客户接触渠道
             */
            $marketTypeSelect = $dialog.find("select.marketTypeSelect"),            // 营销方式
            // 短信
            $smsAccessNumberSelect = $dialog.find("select.smsAccessNumberSelect"),  // 接入号
            $smsContent = $dialog.find("textarea.smsMessageContent"),               // 短息内容
            $smsContentId = $dialog.find(".marketType input.smsContentId"),         // 短信内容Id
            // 场景短信
            $sceneSmsAccessNumberSelect = $dialog.find(".sceneSms select.sceneSmsAccessNumberSelect"),  // 接入号
            $sceneSmsMessageContent = $dialog.find(".sceneSms textarea.sceneSmsMessageContent"),        // 短信内容
            $sceneSmsContentId = $dialog.find("input.sceneSmsContentId"),                               // 短信内容Id
            $sceneRuleSmsSelect = $dialog.find("select.sceneRuleSmsSelect"),                            // 场景规则
            $testPhones = $dialog.find("textarea.marketTestPhones"),    // 测试号

            $infoConfigBody = $dialog.find("div.infoConfigBody"),
            $editStep = $dialog.find("div.step").find("span.edit"),
            $nextStep = $dialog.find("div.step").find("span.next"),
            $closeBtn = $dialog.find("div.step").find("span.closeBtn"),
            $confirmStep = $dialog.find("div.step").find("span.confirm"),
            $preStep = $dialog.find("div.step").find("span.pre"),
            $flowStepA = $dialog.find("div.flowStep").find("div.flowStepA"),
            $flowStepC = $dialog.find("div.flowStep").find("div.flowStepC"),
            $flowStepD = $dialog.find("div.flowStep").find("div.flowStepD");
        if (initValue.operateType != "create" && initValue.operateType != "modelToCreate") {
            /**
             * 任务基本信息
             */
            $marketJobId.val(initValue.id);
            $marketJobName.val(initValue.name);
            $marketBusinessTypeSelect.val(initValue.businessType);
            $marketTimerSelect.val(initValue.scheduleType);
            if (initValue.scheduleType == taskMgr_enum.schedule_type.single) {
                $monitoringStartTimeInput.val(initValue.beginTime);
                $monitoringEndTimeInput.val(initValue.endTime);
                $marketMonitoringTimeRow.show();
            }
            $startTimeInput.val(initValue.startTime);
            $endTimeInput.val(initValue.stopTime);
            $singleValueInput.val(initValue.sendInterval);
            $marketLimitSelect.val(initValue.repeatStrategy);
            $marketAreaNames.val(initValue.areaNames);
            $marketAreaIds.val(initValue.areaCodes);
            $mark.val(initValue.remarks);
            // 分组营销
            $dialog.find("label.checkbox-inline :radio[value='" + initValue.isBoidSale + "']").click();

            /**
             * 目标用户选择
             */
            $marketSegmentNames.val(initValue.marketSegmentNames);
            $marketSegmentCounts.val(initValue.marketSegmentUserCounts);
            $marketSegmentIds.val(initValue.marketSegmentIds);
            initMarketUserCountLimit();
            /**
             * 客户接触渠道
             */
            $marketTypeSelect.val(initValue.marketType);
            initTypeBody();
            $testPhones.val(initValue.testPhones);

            /**
             * 营销方式 下拉框
             */
            function initTypeBody() {
                $marketTypeSelect.trigger("change", [true]);
                switch (initValue.marketType) {
                    case "sms":
                        $smsAccessNumberSelect.val(initValue.accessNumber);
                        $smsContentId.val(initValue.marketSmsContentId);
                        $smsContent.val(initValue.marketContent);
                        break;
                    case "sceneSms":
                        $sceneSmsAccessNumberSelect.val(initValue.accessNumber);
                        $sceneRuleSmsSelect.val(initValue.marketTypeValue);
                        $sceneSmsContentId.val(initValue.marketSmsContentId);
                        $sceneSmsMessageContent.val(initValue.marketContent);
                        $sceneRuleSmsSelect.trigger('chosen:updated');
                        break;
                }
            }

            function initMarketUserCountLimit() {
                if (parseInt(initValue.marketUserCountLimit) > 0) {
                    $numberLimitInput.val(initValue.marketUserCountLimit);
                }
            }
        }


        if (initValue.operateType == "create" || initValue.operateType == "modelToCreate") {
            $dialog.find("label.checkbox-inline :radio[value='0']").click();
            $flowStepA.find("span").addClass("active");
            $infoConfigBody.find("div.row").removeClass("active");
            $infoConfigBody.find("div.userGroupInfo").addClass("active");
            $marketSegmentNames.val(initValue.marketSegmentNames);
            $marketSegmentIds.val(initValue.marketSegmentIds);
            $marketSegmentCounts.val(initValue.marketSegmentUserCounts);
            $closeBtn.css("display", "inline-block");
            return;
        }
        else if (initValue.operateType == "update") { // 编辑时，默认选中完成状态
            $marketJobName.attr("disabled", "disabled");
            $flowStepA.find("span").addClass("active");
            $infoConfigBody.find("div.row").removeClass("active");
            $infoConfigBody.find("div.userGroupInfo").addClass("active");
            $nextStep.siblings().removeClass("active");
            $nextStep.addClass("active");
            $closeBtn.css("display", "inline-block");
            $dialog.find("label.checkbox-inline :radio[value='0']").attr("disabled", "disabled")
            $dialog.find("label.checkbox-inline :radio[value='1']").attr("disabled", "disabled")
            $marketTypeSelect.attr("disabled", "disabled");
        } else if (initValue.operateType == "resubmit") {
            $flowStepC.siblings().find("span").removeClass("active");
            $flowStepC.find("span").addClass("active");
            $infoConfigBody.find("div.row").removeClass("active");
            $infoConfigBody.find("div.jobConfigureInfo").addClass("active");
            $confirmStep.siblings().removeClass("active");
            $confirmStep.addClass("active");
            $confirmStep.text("重新提交");
            $preStep.addClass("active");
            $preStep.parent().find("span").addClass("pageB");
            $closeBtn.css("display", "inline-block");
        } else if (initValue.operateType == "show") {// 查看信息时
            $infoConfigBody.find("div.row").removeClass("active");
            $infoConfigBody.find("div.finishInfo").addClass("active");
            $editStep.siblings().removeClass("active");
            $editStep.addClass("active");
            $editStep.text("查看详情");
            $confirmStep.remove();
            $testPhones.attr("warn", "false");
            $closeBtn.css("display", "inline-block");
            $infoConfigBody.find(".numberLimit").unbind("click");
            $infoConfigBody.find("button.btn").remove();
            $infoConfigBody.find("input").attr("disabled", "disabled");
            $infoConfigBody.find("textarea").attr("disabled", "disabled");
            $infoConfigBody.find("select").attr("disabled", "disabled");
            $infoConfigBody.find(".buttons").remove();
            $infoConfigBody.find("#level").css("background-color", "#eee");
        } else if (initValue.operateType == "audit") {
            $flowStepD.siblings().find("span").removeClass("active");
            $flowStepD.find("span").addClass("active");
            $infoConfigBody.find("div.auditInfo").addClass("active").siblings().removeClass("active");
            $confirmStep.addClass("active").siblings().removeClass("active");
            $testPhones.attr("warn", "false");
            $closeBtn.css("display", "inline-block");
        }

    }

    /**
     * 获取业务类型
     * @param type
     * @returns {*}
     */
    obj.getBusinessType = function (_type) {
        var type = parseInt(_type)
        switch (type) {
            case 1:
                return "互联网综合业务";
            case 2:
                return "内容营销";
            case 3:
                return "流量经营";
            case 4:
                return "APP场景营销";
            default:
                return "未知";
        }
    }

    /**
     * 获取营销方式
     * @param type
     * @returns {*}
     */
    obj.getMarketType = function (type) {
        switch (type) {
            case "sms":
                return "周期任务";
            case "sceneSms":
                return "场景任务";
            case "jxhscene":
                return "精细化实时任务";
            case "jxhsms":
                return "精细化周期任务";
            default:
                return "未知";
        }
    }

    /**
     * 获取dataTable请求地址
     * @param status
     * @returns {string} AjaxUrl
     */
    obj.getAjaxUrl = function (status) {
        var taskUrl = "queryMarketingTasksByPage.view",
            taskPoolUrl = "queryMarketingTaskPoolByPage.view",
            name = $.trim($(".queryName").val()),
            stats = status == 0 ? $.trim($(".stats").val()) : $.trim($(".poolStats").val()),
            marketType = $.trim($(".queryMarketType").val()),
            businessType = $.trim($(".queryMarketBusinessType").val()),
            param = '?name=' + encodeURIComponent(name) + '&status=' + stats + '&marketType=' + marketType + '&businessType=' + businessType;
        return (status == 0) ? (taskUrl + param) : (taskPoolUrl + param);
    }

    /**
     * 验证手机号格式
     * @param array 手机号数组
     * @returns {{ret: boolean, desc: string}}
     */
    obj.verifyMob = function (_array) {
        var result = {ret: true, desc: ''}
        if (_array) {
            var array = _array.split(',');
            for (var i = 0; i < array.length; i++) {
                if (!$.trim(array[i])) {
                    result.ret = false;
                    result.desc = "手机号不能为空";
                    break
                }
                if (!/^1[3|4|5|6|7|8|9]\d{9}$/.test($.trim(array[i]))) {
                    result.ret = false;
                    result.desc = "[" + array[i] + "] 手机号格式不正确";
                    break
                }
            }
        }
        return result;
    }

    return obj;

}()

function onLoadBody(status) {
    taskMgr.initialize(status);
    taskMgr.initData(status);
    taskMgr.initEvent(status);
}