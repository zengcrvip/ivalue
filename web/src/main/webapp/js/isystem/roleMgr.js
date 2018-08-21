function onLoadBody() {
    var url = "queryRolesByPage.view", dataTable;

    initData();
    initEvent();

    function initData() {
        var options = {
            ele: $('table.roleTab'),
            ajax: {url: url, type: "POST"},
            columns: [
                {data: "name", title: "名称", width: "20%", className: "dataTableFirstColumns"},
                {data: "createUserName", title: "创建人", width: "20%"},
                {data: "createTime", title: "创建时间", width: "20%"},
                {
                    data: "type", title: "角色类型", width: "20%",
                    render: function (data, type, row) {
                        switch (data) {
                            case 1:
                                return "普通角色";
                            case 2:
                                return "炒店角色";
                        }
                    }
                },
                {
                    title: "操作", width: "20%", className: "centerColumns",
                    render: function (data, type, row) {
                        return "<a type='button' class='editBtn btn btn-info btn-sm btn-edit' href='#' onclick='editItem(" + JSON.stringify(row) + ")' ><i class='fa fa-edit'></i></a>" +
                            "<a type='button' class='btn btn-danger btn-sm btn-delete' href='#' onclick='deleteItem(" + row.id + ")' ><i class='fa fa-trash-o'></i></a>";
                    }
                },
                {data: "permissionMenuIds", visible: false},
                {data: "homePageId", visible: false},
                {data: "permissionIdNames", visible: false},
                {data: "permissionMenuNames", visible: false},
                {data: "permissionDataIds", visible: false},
                {data: "permissionDataNames", visible: false},
                {data: "id", visible: false}
            ]
        };
        dataTable = $plugin.iCompaignTable(options);
    }

    function initEvent() {
        $("div.roleRefreshBtn").click(function (e, condition) {
            dataTable.ajax.url(url + (condition ? condition : ""));
            dataTable.ajax.reload();
        });

        $(".searchBtn").click(function () {
            var name = $("div.conditionSearch").find(".searchName").val();
            var condition = "?name=" + encodeURIComponent(name);
            globalLocalRefresh.refreshRoleList(condition);
        });

        $(".addBtn").click(function () {
            htmlHandle.handleRole($('#dialogPrimary'))
            layer.open({
                type: 1,
                shade: 0.3,
                title: "角色新增",
                offset: '70px',
                area: ['750px', '500px'],
                content: $('#dialogPrimary'),
                closeBtn: 0,
                btn: ["确定", "取消"],
                yes: function (index, layero) {
                    layero.find("div.handleBtn").attr({"index": index, "operate": "create"}).trigger("click");
                },
                cancel: function (index, layero) {
                    layer.close(index);
                }
            });
        });
    }
}

function editItem(data) {
    htmlHandle.handleRole($('#dialogPrimary'), data)
    layer.open({
        type: 1,
        shade: 0.3,
        title: "角色修改",
        offset: '70px',
        area: ['750px', '500px'],
        content: $('#dialogPrimary'),
        closeBtn: 0,
        btn: ["确定", "取消"],
        yes: function (index, layero) {
            layero.find("div.handleBtn").attr({"index": index, "operate": "update"}).trigger("click");
        },
        cancel: function (index, layero) {
            layer.close(index);
        }
    });
}
function deleteItem(id) {
    layer.confirm("确定删除？", {icon: 3, title: '提示'}, function () {
        globalRequest.deleteRole(true, {"id": id}, function (data) {
            if (data.retValue === 0) {
                globalLocalRefresh.refreshRoleList();
                layer.msg("删除成功");
            } else {
                layer.alert("删除失败," + data.desc);
            }
        }, function () {
            layer.alert("系统异常");
        })
    });
}