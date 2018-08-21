/**
 * Created by yangyang on 2016224.
 */

/**
 * ajax请求 公共方法
 *
 * @param url 接口地址
 * @param async true:异步请求 false：同步请求(阻塞线程)
 * @param paras 请求参数
 * @param sucFunc 请求成功的回调函数
 * @param errorFunc 请求错误的回调函数
 * @param beforeSendFunc 发送请求前回调函数
 * @param completeFunc 请求完成后回调函数
 * @param loading 是否显示遮罩层  注意: (默认显示 可不传参数 , 如需不显示 传入true,1 等即可不显示)
 * @description 注意：方法依赖jquery,需要提前引入、加载jquery
 */
function post(url, async, paras, sucFunc, errorFunc, beforeSendFunc, completeFunc, loading) {
    $.ajax({
        type: 'POST',
        url: url,
        async: async,
        data: JSON.stringify(paras),
        contentType: 'application/json;charset=UTF-8',
        dataType: 'json',
        cache: false,
        beforeSend: function () {
            if (!loading) {
                $html.loading(true);
            }
            if (beforeSendFunc && typeof beforeSendFunc == "function") {
                beforeSendFunc();
            }
        },
        complete: function () {
            if (!loading) {
                $html.loading(false);
            }
            if (completeFunc && typeof completeFunc == "function") {
                completeFunc();
            }
        },
        success: sucFunc,
        error: function (xhr) {
            debugger
            var sessionStatus = xhr.getResponseHeader("sessionStatus");
            var redirectUrl = xhr.getResponseHeader("redirectUrl");
            // session timeout
            if (911 == xhr.status && "timeout" == sessionStatus) {
                layer.alert('会话超时，请重新登录', function (index) {
                    window.location.href = redirectUrl;
                    layer.close(index);
                    return;
                });
            } else if (755 == xhr.status) {
                layer.alert('无该数据操作权限', function (index) {
                    layer.close(index);
                    return;
                });
            } else {
                console.log(JSON.stringify(xhr));
                if (errorFunc && typeof errorFunc == "function") {
                    errorFunc()
                }
            }
        }
    });
};

var globalRequest = {
    iLogin: {
        querySystemConfig: function (async, paras, sucFunc, errorFunc) {
            post("querySystemConfig", async, paras, sucFunc, errorFunc);
        }
    },
    iTag: {
        querySchemas: function (async, paras, sucFunc, errorFunc) {
            post("querySchemas.view", async, paras, sucFunc, errorFunc);
        },
        queryTableAndViews: function (async, paras, sucFunc, errorFunc) {
            post("queryTableAndViews.view", async, paras, sucFunc, errorFunc);
        },
        queryTableColumnInfo: function (async, paras, sucFunc, errorFunc) {
            post("queryTableColumnInfo.view", async, paras, sucFunc, errorFunc);
        },
        queryAllTagsByPage: function (async, paras, sucFunc, errorFunc) {
            post("queryAllTagsByPage.view", async, paras, sucFunc, errorFunc);
        },
        addOrEditTag: function (async, paras, sucFunc, errorFunc) {
            post("addOrEditTag.view", async, paras, sucFunc, errorFunc);
        },
        deleteTag: function (async, paras, sucFunc, errorFunc) {
            post("deleteTag.view", async, paras, sucFunc, errorFunc);
        },
        queryAllTagSchemaAndNames: function (async, paras, sucFunc, errorFunc) {
            post("queryAllTagSchemaAndNames.view", async, paras, sucFunc, errorFunc);
        },
        queryRemoteServersByPage: function (async, paras, sucFunc, errorFunc) {
            post("queryRemoteServersByPage.view", async, paras, sucFunc, errorFunc);
        },
        addOrEditRemoteServer: function (async, paras, sucFunc, errorFunc) {
            post("addOrEditRemoteServer.view", async, paras, sucFunc, errorFunc);
        },
        deleteRemoteServer: function (async, paras, sucFunc, errorFunc) {
            post("deleteRemoteServer.view", async, paras, sucFunc, errorFunc);
        },
        queryAllRemoteServerNames: function (async, paras, sucFunc, errorFunc) {
            post("queryAllRemoteServerNames.view", async, paras, sucFunc, errorFunc);
        },
        testConnection: function (async, paras, sucFunc, errorFunc) {
            post("testConnection.view", async, paras, sucFunc, errorFunc);
        },
        createProperties: function (async, paras, sucFunc, errorFunc) {
            post("createProperties.view", async, paras, sucFunc, errorFunc);
        },
        updateProperty: function (async, paras, sucFunc, errorFunc) {
            post("updateProperty.view", async, paras, sucFunc, errorFunc);
        },
        deleteProperty: function (async, paras, sucFunc, errorFunc) {
            post("deleteProperty.view", async, paras, sucFunc, errorFunc);
        },
        queryAllDimensionIdAndNames: function (async, paras, sucFunc, errorFunc) {
            post("queryAllDimensionIdAndNames.view", async, paras, sucFunc, errorFunc);
        }
    },
    iModel: {
        queryModelsByPage: function (async, paras, sucFunc, errorFunc) {
            post("queryModelsByPage.view", async, paras, sucFunc, errorFunc);
        },
        queryModelsByList: function (async, paras, sucFunc, errorFunc) {
            post("queryModelsByList.view", async, paras, sucFunc, errorFunc);
        },
        queryAllPropertiesUnderCategory: function (async, paras, sucFunc, errorFunc) {
            post("queryAllPropertiesUnderCategory.view", async, paras, sucFunc, errorFunc);
        },
        queryAllPropertiesAndImportModelUnderCategory: function (async, paras, sucFunc, errorFunc) {
            post("queryAllPropertiesAndImportModelUnderCategory.view", async, paras, sucFunc, errorFunc);
        },
        addOrEditModel: function (async, paras, sucFunc, errorFunc) {
            post("addOrEditModel.view", async, paras, sucFunc, errorFunc);
        },
        deleteModel: function (async, paras, sucFunc, errorFunc) {
            post("deleteModel.view", async, paras, sucFunc, errorFunc);
        },
        queryAllRole: function (async, paras, sucFunc, errorFunc) {
            post("queryAllRole.view", async, paras, sucFunc, errorFunc);
        },
        queryAllCategoryByBody: function (async, paras, sucFunc, errorFunc) {
            post("queryAllCategoryByBody.view", async, paras, sucFunc, errorFunc);
        },
        downloadModel: function (async, paras, sucFunc, errorFunc) {
            post("downloadModel.view", async, paras, sucFunc, errorFunc);
        },
        queryMatchRuleUserCounts: function (async, paras, sucFunc, errorFunc) {
            post("queryMatchRuleUserCounts.view", async, paras, sucFunc, errorFunc);
        },
        deleteModelDownloadSetting: function (async, paras, sucFunc, errorFunc) {
            post("deleteModelDownloadSetting.view", async, paras, sucFunc, errorFunc);
        },
        copyModelDownloadSetting: function (async, paras, sucFunc, errorFunc) {
            post("copyModelDownloadSetting.view", async, paras, sucFunc, errorFunc);
        },
        queryModelsUnderMe: function (async, paras, sucFunc, errorFunc) {
            post("queryModelsUnderMe.view", async, paras, sucFunc, errorFunc);
        },
        queryModelDownloadSettingById: function (async, paras, sucFunc, errorFunc) {
            post("queryModelDownloadSettingById.view", async, paras, sucFunc, errorFunc);
        },
        insertModelDownloadSetting: function (async, paras, sucFunc, errorFunc) {
            post("insertModelDownloadSetting.view", async, paras, sucFunc, errorFunc);
        },
        updateModelDownloadSetting: function (async, paras, sucFunc, errorFunc) {
            post("updateModelDownloadSetting.view", async, paras, sucFunc, errorFunc);
        },
        deleteResourceModel: function (async, paras, sucFunc, errorFunc) {
            post("deleteResourceModel.view", async, paras, sucFunc, errorFunc);
        },
        editResourceModel: function (async, paras, sucFunc, errorFunc) {
            post("editResourceModel.view", async, paras, sucFunc, errorFunc);
        },
        queryResourceModelById: function (async, paras, sucFunc, errorFunc) {
            post("queryResourceModelById.view", async, paras, sucFunc, errorFunc);
        }
    },
    iScene: {
        addOrDeleteUrlBlacklist: function (async, paras, sucFunc, errorFunc) {
            post("addOrDeleteUrlBlacklist.view", async, paras, sucFunc, errorFunc);
        },
        addOrEditSmsSendConfig: function (async, paras, sucFunc, errorFunc) {
            post("addOrEditSmsSendConfig.view", async, paras, sucFunc, errorFunc);
        },
        deleteSmsSendConfig: function (async, paras, sucFunc, errorFunc) {
            post("deleteSmsSendConfig.view", async, paras, sucFunc, errorFunc);
        },
        getSelectPushConfig: function (async, paras, sucFunc, errorFunc) {
            post("getSelectPushConfig.view", async, paras, sucFunc, errorFunc);
        }
    },
    iSystem: {
        queryMenusByPage: function (async, paras, sucFunc, errorFunc) {
            post("queryMenusByPage.view", async, paras, sucFunc, errorFunc);
        },
        addOrEditMenu: function (async, paras, sucFunc, errorFunc) {
            post("addOrEditMenu.view", async, paras, sucFunc, errorFunc);
        },
        deleteMenu: function (async, paras, sucFunc, errorFunc) {
            post("deleteMenu.view", async, paras, sucFunc, errorFunc);
        },
        queryCurrentLevelMenu: function (async, paras, sucFunc, errorFunc) {
            post("queryCurrentLevelMenu.view", async, paras, sucFunc, errorFunc);
        },
        getInterfacePhone: function (async, paras, sucFunc, errorFunc) {
            post("getInterfacePhone.view", async, paras, sucFunc, errorFunc);
        }
    },
    iShop: {
        queryAutograph: function (async, paras, sucFunc, errorFunc) {
            post("queryAutograph.view", async, paras, sucFunc, errorFunc);
        },
        queryShopBlockBases: function (async, paras, sucFunc, erroFunc) {
            post("queryShopBlockBases.view", async, paras, sucFunc, erroFunc);
        },
        createShopUserBlackList: function (async, paras, sucFunc, errorFunc) {
            post("createShopUserBlackList.view", async, paras, sucFunc, errorFunc);
        },
        createMoreShopUserBlackList: function (async, paras, sucFunc, errorFunc) {
            post("createMoreShopUserBlackList.view", async, paras, sucFunc, errorFunc);
        },
        deleteShopUserBlackList: function (async, paras, sucFunc, errorFunc) {
            post("deleteShopUserBlackList.view", async, paras, sucFunc, errorFunc);
        },
        deleteAllShopUserBlackList: function (async, paras, sucFunc, errorFunc) {
            post("deleteAllShopUserBlackList.view", async, paras, sucFunc, errorFunc);
        },
        flushShopBlack: function (async, paras, sucFunc, erroFunc) {
            post("flushShopBlack.view", async, paras, sucFunc, erroFunc);
        },
        queryShopTaskAuditReject: function (async, paras, sucFunc, erroFunc) {
            post("queryShopTaskAuditReject.view", async, paras, sucFunc, erroFunc, null, null, true);
        },
        queryShopBlackBaseAreas: function (async, paras, sucFunc, erroFunc) {
            post("queryShopBlackBaseAreas.view", async, paras, sucFunc, erroFunc);
        },
        queryShopDailyByPage: function (async, paras, sucFunc, erroFunc) {
            post("queryShopDailyByPage.view", async, paras, sucFunc, erroFunc);
        },
        queryShopDailyTotal: function (async, paras, sucFunc, erroFunc) {
            post("queryShopDailyTotal.view", async, paras, sucFunc, erroFunc);
        },
        queryShopWeeklyTotal: function (async, paras, sucFunc, erroFunc) {
            post("queryShopWeeklyTotal.view", async, paras, sucFunc, erroFunc);
        },
        queryShopProvinceDaily: function (async, paras, sucFunc, erroFunc) {
            post("queryShopProvinceDaily.view", async, paras, sucFunc, erroFunc);
        },
        queryBusinessHallPortraitById: function (async, paras, sucFunc, erroFunc) {
            post("queryBusinessHallPortraitById.view", async, paras, sucFunc, erroFunc);
        }
    },
    iKeeper: {
        fetchAreaOverview: function (async, paras, sucFunc, errorFunc) {
            post("fetchAreaOverview.view", async, paras, sucFunc, errorFunc);
        },
        fetchChannelOverview: function (async, paras, sucFunc, errorFunc) {
            post("fetchChannelOverview.view", async, paras, sucFunc, errorFunc);
        },
        fetchActivityOverview: function (async, paras, sucFunc, errorFunc) {
            post("fetchActivityOverview.view", async, paras, sucFunc, errorFunc);
        },
        fetchFeeOverview: function (async, paras, sucFunc, errorFunc) {
            post("fetchFeeOverview.view", async, paras, sucFunc, errorFunc);
        },
        fetchAreaRank: function (async, paras, sucFunc, errorFunc) {
            post("fetchAreaRank.view", async, paras, sucFunc, errorFunc);
        },
        fetchChannelRank: function (async, paras, sucFunc, errorFunc) {
            post("fetchChannelRank.view", async, paras, sucFunc, errorFunc);
        },
        queryRootOrgByOrgType: function (async, paras, sucFunc, errorFunc) {
            post("queryRootOrgByOrgType.view", async, paras, sucFunc, errorFunc);
        },
        queryOrgListByOrgId: function (async, paras, sucFunc, errorFunc) {
            post("queryOrgListByOrgId.view", async, paras, sucFunc, errorFunc);
        },
        queryWelfareOfShopKeeper: function (async, paras, sucFunc, errorFunc) {
            post("queryWelfareOfShopKeeper.view", async, paras, sucFunc, errorFunc);
        },
        queryAllProductOfShopKeeper: function (async, paras, sucFunc, errorFunc) {
            post("queryAllProductOfShopKeeper.view", async, paras, sucFunc, errorFunc);
        },
        queryProductListByCompositId: function (async, paras, sucFunc, errorFunc) {
            post("queryProductListByCompositId.view", async, paras, sucFunc, errorFunc, null, null, true);
        },
        queryBusinessOrg: function (async, paras, sucFunc, errorFunc) {
            post("queryBusinessOrg.view", async, paras, sucFunc, errorFunc);
        },
        addOrg: function (async, paras, sucFunc, errorFunc) {
            post("addOrg.view", async, paras, sucFunc, errorFunc);
        },
        updateOrg: function (async, paras, sucFunc, errorFunc) {
            post("updateOrg.view", async, paras, sucFunc, errorFunc);
        },
        deleteOrg: function (async, paras, sucFunc, errorFunc) {
            post("deleteOrg.view", async, paras, sucFunc, errorFunc);
        },
        addProduct: function (async, paras, sucFunc, errorFunc) {
            post("addProductOfShopKeeper.view", async, paras, sucFunc, errorFunc);
        },
        updateProduct: function (async, paras, sucFunc, errorFunc) {
            post("updateProductOfShopKeeper.view", async, paras, sucFunc, errorFunc);
        },
        deleteProduct: function (async, paras, sucFunc, errorFunc) {
            post("deleteProductOfShopKeeper.view", async, paras, sucFunc, errorFunc);
        },
        createKeeperUser: function (async, paras, sucFunc, errorFunc) {
            post("createKeeperUser.view", async, paras, sucFunc, errorFunc);
        },
        updateKeeperUser: function (async, paras, sucFunc, errorFunc) {
            post("updateKeeperUser.view", async, paras, sucFunc, errorFunc);
        },
        deleteKeeperUser: function (async, paras, sucFunc, errorFunc) {
            post("deleteKeeperUser.view", async, paras, sucFunc, errorFunc);
        },
        queryKeeperUserDetail: function (async, paras, sucFunc, errorFunc) {
            post("queryKeeperUserDetail.view", async, paras, sucFunc, errorFunc);
        },
        queryUsersForKeeperUser: function (async, paras, sucFunc, errorFunc) {
            post("queryUsersForKeeperUser.view", async, paras, sucFunc, errorFunc);
        },
        giveCustWelfare: function (async, paras, sucFunc, errorFunc) {
            post("giveCustWelfare.view", async, paras, sucFunc, errorFunc);
        },
        createUserMaintain: function (async, paras, sucFunc, errorFunc) {
            post("createUserMaintain.view", async, paras, sucFunc, errorFunc);
        },
        queryUserMaintainDetail: function (async, paras, sucFunc, errorFunc) {
            post("queryUserMaintainDetail.view", async, paras, sucFunc, errorFunc);
        },
        updateUserMaintain: function (async, paras, sucFunc, errorFunc) {
            post("updateUserMaintain.view", async, paras, sucFunc, errorFunc);
        },
        deleteUserMaintain: function (async, paras, sucFunc, errorFunc) {
            post("deleteUserMaintain.view", async, paras, sucFunc, errorFunc);
        },
        queryCurrentKeeperUser: function (async, paras, sucFunc, errorFunc) {
            post("queryCurrentKeeperUser.view", async, paras, sucFunc, errorFunc);
        },
        queryProductGroupOfShopKeeper: function (async, paras, sucFunc, errorFunc) {
            post("queryProductGroupOfShopKeeper.view", async, paras, sucFunc, errorFunc);
        },
        queryKeeperTaskById: function (async, paras, sucFunc, errorFunc) {
            post("queryKeeperTaskById.view", async, paras, sucFunc, errorFunc);
        },
        queryKeeperInstDetail: function (async, paras, sucFunc, errorFunc) {
            post("queryKeeperInstDetail.view", async, paras, sucFunc, errorFunc);
        },
        querySmsSignatureAuditUsers: function (async, paras, sucFunc, errorFunc) {
            post("querySmsSignatureAuditUsers.view", async, paras, sucFunc, errorFunc);
        },
        queryKeeperTaskAudits: function (async, paras, sucFunc, errorFunc) {
            post("queryKeeperTaskAudits.view", async, paras, sucFunc, errorFunc);
        },
        createKeeperTask: function (async, paras, sucFunc, errorFunc) {
            post("createKeeperTask.view", async, paras, sucFunc, errorFunc);
        },
        updateKeeperTask: function (async, paras, sucFunc, errorFunc) {
            post("updateKeeperTask.view", async, paras, sucFunc, errorFunc);
        },
        deleteKeeperTask: function (async, paras, sucFunc, errorFunc) {
            post("deleteKeeperTask.view", async, paras, sucFunc, errorFunc);
        },
        queryKeeperTaskType: function (async, paras, sucFunc, errorFunc) {
            post("queryKeeperTaskType.view", async, paras, sucFunc, errorFunc);
        },
        queryKeeperRuleByTypeId: function (async, paras, sucFunc, errorFunc) {
            post("queryKeeperRuleByTypeId.view", async, paras, sucFunc, errorFunc);
        },
        saveKeeperTaskCustomer: function (async, paras, sucFunc, errorFunc) {
            post("saveKeeperTaskCustomer.view", async, paras, sucFunc, errorFunc);
        },
        queryWelfareByTypeId: function (async, paras, sucFunc, errorFunc) {
            post("queryWelfareByTypeId.view", async, paras, sucFunc, errorFunc);
        },
        queryOutbandPhone: function (async, paras, sucFunc, errorFunc) {
            post("queryOutbandPhone.view", async, paras, sucFunc, errorFunc);
        },
        auditKeeperTask: function (async, paras, sucFunc, errorFunc) {
            post("auditKeeperTask.view", async, paras, sucFunc, errorFunc);
        },
        auditSmsSignature: function (async, paras, sucFunc, errorFunc) {
            post("auditSmsSignature.view", async, paras, sucFunc, errorFunc);
        },
        queryProductOfShopKeeper: function (async, paras, sucFunc, errorFunc) {
            post("queryProductOfShopKeeper.view", async, paras, sucFunc, errorFunc);
        },
        addWelfareOfShopKeeper: function (async, paras, sucFunc, errorFunc) {
            post("addWelfareOfShopKeeper.view", async, paras, sucFunc, errorFunc);
        },
        updateWelfareOfShopKeeper: function (async, paras, sucFunc, errorFunc) {
            post("updateWelfareOfShopKeeper.view", async, paras, sucFunc, errorFunc);
        },
        deleteWelfareOfShopKeeper: function (async, paras, sucFunc, errorFunc) {
            post("deleteWelfareOfShopKeeper.view", async, paras, sucFunc, errorFunc);
        },
        queryAuditFailureReason: function (async, paras, sucFunc, errorFunc) {
            post("queryAuditFailureReason.view", async, paras, sucFunc, errorFunc, null, null, true);
        },
        queryWelfareById: function (async, paras, sucFunc, errorFunc) {
            post("queryWelfareById.view", async, paras, sucFunc, errorFunc);
        },
        queryProductById: function (async, paras, sucFunc, errorFunc) {
            post("queryProductById.view", async, paras, sucFunc, errorFunc);
        },
        saveDirectionalCustomer: function (async, paras, sucFunc, errorFunc) {
            post("saveDirectionalCustomer.view", async, paras, sucFunc, errorFunc);
        },
        terminateKeeperTask: function (async, paras, sucFunc, errorFunc) {
            post("terminateKeeperTask.view", async, paras, sucFunc, errorFunc);
        },
        checkProductCodeUnique: function (async, paras, sucFunc, errorFunc) {
            post("checkProductCodeUnique.view", async, paras, sucFunc, errorFunc);
        },
        checkOnlineProduct: function (async, paras, sucFunc, errorFunc) {
            post("checkOnlineProduct.view", async, paras, sucFunc, errorFunc);
        }
    },
    iScheduling: {
        queryMarketingTaskDetail: function (async, paras, sucFunc, errorFunc) {
            post("queryMarketingTaskDetail.view", async, paras, sucFunc, errorFunc);
        },
        createMarketingTask: function (async, paras, sucFunc, erroFunc) {
            post("createMarketingTask.view", async, paras, sucFunc, erroFunc);
        },
        updateMarketingTask: function (async, paras, sucFunc, erroFunc) {
            post("updateMarketingTask.view", async, paras, sucFunc, erroFunc);
        },
        deleteMarketingTask: function (async, paras, sucFunc, erroFunc) {
            post("deleteMarketingTask.view", async, paras, sucFunc, erroFunc);
        },
        executeTask: function (async, paras, sucFunc, erroFunc) {
            post("executeMarketingTask.view", async, paras, sucFunc, erroFunc);
        },
        stopTask: function (async, paras, sucFunc, erroFunc) {
            post("stopMarketingTask.view", async, paras, sucFunc, erroFunc);
        },
        querySenceRuleSmsType: function (async, paras, sucFunc, erroFunc) {
            post("querySenceRuleSmsType.view", async, paras, sucFunc, erroFunc);
        },
        sendMarketingTaskTestSms: function (async, paras, sucFunc, erroFunc) {
            post("sendMarketingTaskTestSms.view", async, paras, sucFunc, erroFunc);
        },
        queryAllModelsUnderCatalog: function (async, paras, sucFunc, erroFunc) {
            post("queryAllModelsUnderCatalog.view", async, paras, sucFunc, erroFunc);
        },
        queryAllModels: function (async, paras, sucFunc, erroFunc) {
            post("queryAllModels.view", async, paras, sucFunc, erroFunc);
        },
        checkMarketingTaskName: function (async, paras, sucFunc, erroFunc) {
            post("checkMarketingTaskName.view", async, paras, sucFunc, erroFunc);
        },
        queryUserAreas: function (async, paras, sucFunc, errorFunc) {
            post("queryUserAreas.view", async, paras, sucFunc, errorFunc);
        },
        auditMarketingTask: function (async, paras, sucFunc, errorFunc) {
            post("auditMarketingTask.view", async, paras, sucFunc, errorFunc);
        },
        viewMarketingPoolTaskDetail: function (async, paras, sucFunc, errorFunc) {
            post("viewMarketingPoolTaskDetail.view", async, paras, sucFunc, errorFunc);
        }
    },
    iOldCustomer: {
        saveOldCustomerAppointUsers: function (async, paras, sucFunc, errorFunc) {
            post("saveOldCustomerAppointUsers.view", async, paras, sucFunc, errorFunc);
        },
        saveOldCustomerBlackUsersImport: function (async, paras, sucFunc, errorFunc) {
            post("saveOldCustomerBlackUsersImport.view", async, paras, sucFunc, errorFunc);
        },
        saveOldCustomerBaseInfoImport: function (async, paras, sucFunc, errorFunc) {
            post("saveOldCustomerBaseInfoImport.view", async, paras, sucFunc, errorFunc);
        },
        createOldCustomerTask: function (async, paras, sucFunc, errorFunc) {
            post("createOldCustomerTask.view", async, paras, sucFunc, errorFunc);
        },
        previewOldCustomer: function (async, paras, sucFunc, errorFunc) {
            post("previewOldCustomer.view", async, paras, sucFunc, errorFunc);
        },
        updateOldCustomer: function (async, paras, sucFunc, errorFunc) {
            post("updateOldCustomer.view", async, paras, sucFunc, errorFunc);
        },
        executeOldCustomerTask: function (async, paras, sucFunc, errorFunc) {
            post("executeOldCustomerTask.view", async, paras, sucFunc, errorFunc);
        },
        auditOldCustomer: function (async, paras, sucFunc, errorFunc) {
            post("auditOldCustomer.view", async, paras, sucFunc, errorFunc);
        },
        deleteOldCustomer: function (async, paras, sucFunc, errorFunc) {
            post("deleteOldCustomer.view", async, paras, sucFunc, errorFunc);
        },
        getOldCustomerTaskAuditReason: function (async, paras, sucFunc, errorFunc) {
            post("getOldCustomerTaskAuditReason.view", async, paras, sucFunc, errorFunc, null, null, true);
        },
        stopTask: function (async, paras, sucFunc, errorFunc) {
            post("terminateOldCustomerTask.view", async, paras, sucFunc, errorFunc);
        },
        queryOldCustomerAudit: function (async, paras, sucFunc, errorFunc) {
            post("queryOldCustomerAudit.view", async, paras, sucFunc, errorFunc);
        }
    },
    queryMatchRuleUserCounts: function (async, paras, sucFunc, errorFunc) {
        post("queryMatchRuleUserCounts.view", async, paras, sucFunc, errorFunc);
    },
    queryAllMenus: function (async, paras, sucFunc, errorFunc) {
        post("queryAllMenus.view", async, paras, sucFunc, errorFunc);
    },
    queryAllRole: function (async, paras, sucFunc, errorFunc) {
        post("queryAllRole.view", async, paras, sucFunc, errorFunc);
    },
    queryUserAreasCode: function (async, paras, sucFunc, errorFunc) {
        post("queryUserAreasCode.view", async, paras, sucFunc, errorFunc);
    },
    startStopUser: function (async, paras, sucFunc, errorFunc) {
        post("startStopUser.view", async, paras, sucFunc, errorFunc);
    },
    queryAuditUsers: function (async, paras, sucFunc, errorFunc) {
        post("queryAuditUsers.view", async, paras, sucFunc, errorFunc);
    },
    createUser: function (async, paras, sucFunc, errorFunc) {
        post("createUser.view", async, paras, sucFunc, errorFunc);
    },
    updateUser: function (async, paras, sucFunc, errorFunc) {
        post("updateUser.view", async, paras, sucFunc, errorFunc);
    },
    queryUsersByPage: function (async, paras, sucFunc, errorFunc) {
        post("queryUsersByPage.view", async, paras, sucFunc, errorFunc);
    },
    queryAllMyCreatedSubUsers: function (async, paras, sucFunc, erroFunc) {
        post("queryAllMyCreatedSubUsers.view", async, paras, sucFunc, erroFunc);
    },
    queryUnderMeBusinessHallsByCondition: function (async, paras, sucFunc, erroFunc) {
        post("queryUnderMeBusinessHallsByCondition.view", async, paras, sucFunc, erroFunc);
    },
    batchUpdateUsersAuditUser: function (async, paras, sucFunc, erroFunc) {
        post("batchUpdateUsersAuditUser.view", async, paras, sucFunc, erroFunc);
    },
    updateRoleInfo: function (async, paras, sucFunc, errorFunc) {
        post("updateRoleInfo.view", async, paras, sucFunc, errorFunc);
    },
    deleteRole: function (async, paras, sucFunc, errorFunc) {
        post("deleteRole.view", async, paras, sucFunc, errorFunc);
    },
    createRoleInfo: function (async, paras, sucFunc, errorFunc) {
        post("createRoleInfo.view", async, paras, sucFunc, errorFunc);
    },
    queryAllPermissions: function (async, paras, sucFunc, errorFunc) {
        post("queryAllPermissions.view", async, paras, sucFunc, errorFunc);
    },
    queryRolesByPage: function (async, paras, sucFunc, erroFunc) {
        post("queryRolesByPage.view", async, paras, sucFunc, erroFunc);
    },
    querySenceSmsRuleByPage: function (async, paras, sucFunc, erroFunc) {
        post("querySenceSmsRuleByPage.view", async, paras, sucFunc, erroFunc);
    },
    deleteSenceSmsRuleById: function (async, paras, sucFunc, erroFunc) {
        post("deleteSenceSmsRuleById.view", async, paras, sucFunc, erroFunc);
    },
    querySenceClientByPage: function (async, paras, sucFunc, erroFunc) {
        post("querySenceClientByPage.view", async, paras, sucFunc, erroFunc);
    },
    querySenceRuleType: function (async, paras, sucFunc, erroFunc) {
        post("querySenceRuleType.view", async, paras, sucFunc, erroFunc);
    },
    querySenceClientTypeOne: function (async, paras, sucFunc, erroFunc) {
        post("querySenceClientTypeOne.view", async, paras, sucFunc, erroFunc);
    },
    querySenceClientTypeTwo: function (async, paras, sucFunc, erroFunc) {
        post("querySenceClientTypeTwo.view", async, paras, sucFunc, erroFunc);
    },
    querySenceTerminalByPage: function (async, paras, sucFunc, erroFunc) {
        post("querySenceTerminalByPage.view", async, paras, sucFunc, erroFunc);
    },
    createOrUpdateSceneRule: function (async, paras, sucFunc, erroFunc) {
        post("createOrUpdateSceneRule.view", async, paras, sucFunc, erroFunc);
    },
    querySenceRuleById: function (async, paras, sucFunc, erroFunc) {
        post("querySenceRuleById.view", async, paras, sucFunc, erroFunc);
    },
    queryPositionSceneByPage: function (async, paras, sucFunc, erroFunc) {
        post("queryPositionSceneByPage.view", async, paras, sucFunc, erroFunc);
    },
    queryPositonSenceType: function (async, paras, sucFunc, erroFunc) {
        post("queryPositonSenceType.view", async, paras, sucFunc, erroFunc);
    },
    queryBaseAreaType: function (async, paras, sucFunc, erroFunc) {
        post("queryBaseAreaType.view", async, paras, sucFunc, erroFunc);
    },
    queryBaseAreas: function (async, paras, sucFunc, erroFunc) {
        post("queryBaseAreas.view", async, paras, sucFunc, erroFunc);
    },
    queryBasesByPage: function (async, paras, sucFunc, erroFunc) {
        post("queryBasesByPage.view", async, paras, sucFunc, erroFunc);
    },
    queryBases: function (async, paras, sucFunc, erroFunc) {
        post("queryBases.view", async, paras, sucFunc, erroFunc);
    },
    queryPositionBaseAreas: function (async, paras, sucFunc, erroFunc) {
        post("queryPositionBaseAreas.view", async, paras, sucFunc, erroFunc);
    },
    createOrUpdatePositionScen: function (async, paras, sucFunc, erroFunc) {
        post("createOrUpdatePositionScen.view", async, paras, sucFunc, erroFunc);
    },
    deletePositionSceneById: function (async, paras, sucFunc, erroFunc) {
        post("deletePositionSceneById.view", async, paras, sucFunc, erroFunc);
    },
    queryPositionSceneById: function (async, paras, sucFunc, erroFunc) {
        post("queryPositionSceneById.view", async, paras, sucFunc, erroFunc);
    },
    queryPositionSceneType: function (async, paras, sucFunc, erroFunc) {
        post("queryPositionSceneType.view", async, paras, sucFunc, erroFunc);
    },
    queryScenePilotType: function (async, paras, sucFunc, erroFunc) {
        post("queryScenePilotType.view", async, paras, sucFunc, erroFunc);
    },
    queryPriorityLevel: function (async, paras, sucFunc, erroFunc) {
        post("queryPriorityLevel.view", async, paras, sucFunc, erroFunc);//查询场景导航任务优先级
    },
    queryPositionBaseByPage: function (async, paras, sucFunc, erroFunc) {
        post("queryPositionBaseByPage.view", async, paras, sucFunc, erroFunc);
    },
    queryPositionBaseById: function (async, paras, sucFunc, erroFunc) {
        post("queryPositionBaseById.view", async, paras, sucFunc, erroFunc);
    },
    createOrUpdatePositionBase: function (async, paras, sucFunc, erroFunc) {
        post("createOrUpdatePositionBase.view", async, paras, sucFunc, erroFunc);
    },
    deletePositionBaseById: function (async, paras, sucFunc, erroFunc) {
        post("deletePositionBaseById.view", async, paras, sucFunc, erroFunc);
    },
    queryPositionBaseImport: function (async, paras, sucFunc, erroFunc) {
        post("queryPositionBaseImport.view", async, paras, sucFunc, erroFunc);
    },
    createPositionBaseImport: function (async, paras, sucFunc, erroFunc) {
        post("createPositionBaseImport.view", async, paras, sucFunc, erroFunc);
    },
    queryPositionReportByPage: function (async, paras, sucFunc, erroFunc) {
        post("queryPositionReportByPage.view", async, paras, sucFunc, erroFunc);
    },
    getPositionReportDown: function (async, paras, sucFunc, erroFunc) {
        post("getPositionReportDown.view", async, paras, sucFunc, erroFunc);
    },
    queryPositionSummary: function (async, paras, sucFunc, erroFunc) {
        post("queryPositionSummary.view", async, paras, sucFunc, erroFunc);
    },
    sendVerificationCode: function (async, paras, sucFunc, erroFunc) {
        post("sendVerificationCode", async, paras, sucFunc, erroFunc);
    },
    loginByName: function (async, paras, sucFunc, erroFunc) {
        post("loginByName.view", async, paras, sucFunc, erroFunc);
    },
    loginByPhone: function (async, paras, sucFunc, erroFunc) {
        post("loginByPhone.view", async, paras, sucFunc, erroFunc);
    },
    loginOut: function (async, paras, sucFunc, erroFunc) {
        post("loginOut.view", async, paras, sucFunc, erroFunc);
    },
    queryTasksByPage: function (async, paras, sucFunc, erroFunc) {
        post("queryTasksByPage.view", async, paras, sucFunc, erroFunc);
    },
    queryCurrentUserInfo: function (async, paras, sucFunc, erroFunc) {
        post("queryCurrentUserInfo.view", async, paras, sucFunc, erroFunc);
    },
    queryAllProductUnderCatalog: function (async, paras, sucFunc, erroFunc) {
        post("queryAllProductUnderCatalog.view", async, paras, sucFunc, erroFunc);
    },
    queryAllCategoryUnderCatalog: function (async, paras, sucFunc, erroFunc) {
        post("queryAllCategoryUnderCatalog.view", async, paras, sucFunc, erroFunc);
    },
    queryShopTaskByPage: function (async, paras, sucFunc, erroFunc) {
        post("queryShopTaskByPage.view", async, paras, sucFunc, erroFunc);
    },
    queryContentByPage: function (async, paras, sucFunc, erroFunc) {
        post("queryContentByPage.view", async, paras, sucFunc, erroFunc);
    },
    createShopTask: function (async, paras, sucFunc, erroFunc) {
        post("createShopTask.view", async, paras, sucFunc, erroFunc);
    },
    queryShopTaskById: function (async, paras, sucFunc, erroFunc) {
        post("queryShopTaskById.view", async, paras, sucFunc, erroFunc);
    },
    queryMarketingTaskDetail: function (async, paras, sucFunc, erroFunc) {
        post("queryMarketingTaskDetail.view", async, paras, sucFunc, erroFunc);
    },
    queryShopPhone: function (async, paras, sucFunc, erroFunc) {
        post("queryShopPhone.view", async, paras, sucFunc, erroFunc);
    },
    queryShopMsgDesc: function (async, paras, sucFunc, erroFunc) {
        post("queryShopMsgDesc.view", async, paras, sucFunc, erroFunc);
    },
    updateShopTask: function (async, paras, sucFunc, erroFunc) {
        post("updateShopTask.view", async, paras, sucFunc, erroFunc);
    },
    deleteShopTaskById: function (async, paras, sucFunc, erroFunc) {
        post("deleteShopTaskById.view", async, paras, sucFunc, erroFunc);
    },
    manualShopTask: function (async, paras, sucFunc, erroFunc) {
        post("manualShopTask.view", async, paras, sucFunc, erroFunc);
    },
    getExecuteBaseByTaskId: function (async, paras, sucFunc, erroFunc) {
        post("getExecuteBaseByTaskId.view", async, paras, sucFunc, erroFunc);
    },
    stopShopTask: function (async, paras, sucFunc, erroFunc) {
        post("stopShopTask.view", async, paras, sucFunc, erroFunc);
    },
    reminderItem: function (async, paras, sucFunc, erroFunc) {
        post("reminderItem.view", async, paras, sucFunc, erroFunc);
    },
    pauseItem: function (async, paras, sucFunc, erroFunc) {
        post("pauseItem.view", async, paras, sucFunc, erroFunc);
    },
    queryShopTaskPhoneImport: function (async, paras, sucFunc, erroFunc) {
        post("queryShopTaskPhoneImport.view", async, paras, sucFunc, erroFunc);
    },
    saveAppointUsersImport: function (async, paras, sucFunc, erroFunc) {
        post("saveAppointUsersImport.view", async, paras, sucFunc, erroFunc);
    },
    saveBlackUsersImport: function (async, paras, sucFunc, erroFunc) {
        post("saveBlackUsersImport.view", async, paras, sucFunc, erroFunc);
    },
    queryShopPerNum: function (async, paras, sucFunc, errorFunc) {
        post("queryShopPerNum.view", async, paras, sucFunc, errorFunc);
    },
    checkChangeAuditUser: function (async, paras, sucFunc, erroFunc) {
        post("checkChangeAuditUser.view", async, paras, sucFunc, erroFunc);
    },
    queryAllEffectiveAccessNumbers: function (async, paras, sucFunc, erroFunc) {
        post("queryAllEffectiveAccessNumbers.view", async, paras, sucFunc, erroFunc);
    },
    queryDealShopTask: function (async, paras, sucFunc, erroFunc) {
        post("queryDealShopTask.view", async, paras, sucFunc, erroFunc);
    },
    queryAllAreas: function (async, paras, sucFunc, erroFunc) {
        post("queryAllAreas.view", async, paras, sucFunc, erroFunc);
    },
    querFixedAccessNum: function (async, paras, sucFunc, erroFunc) {
        post("querFixedAccessNum.view", async, paras, sucFunc, erroFunc);
    },
    queryShopBusinessType: function (async, paras, sucFunc, erroFunc) {
        post("queryShopBusinessType.view", async, paras, sucFunc, erroFunc);
    },
    queryShopTaskTypeCount: function (async, paras, sucFunc, erroFunc) {
        post("queryShopTaskTypeCount.view", async, paras, sucFunc, erroFunc);
    },
    queryShopTaskKPI: function (async, paras, sucFunc, erroFunc) {
        post("queryShopTaskKPI.view", async, paras, sucFunc, erroFunc);
    },
    validateTaskName: function (async, paras, sucFunc, erroFunc) {
        post("validateTaskName.view", async, paras, sucFunc, erroFunc);
    },
    queryHistoryFileById: function (async, paras, sucFunc, erroFunc) {
        post("queryHistoryFileById.view", async, paras, sucFunc, erroFunc);
    },
    queryAllDimensions: function (async, paras, sucFunc, erroFunc) {
        post("queryAllDimensions.view", async, paras, sucFunc, erroFunc);
    },
    queryCurrentUserInfoById: function (async, paras, sucFunc, erroFunc) {
        post("queryCurrentUserInfoById.view", async, paras, sucFunc, erroFunc);
    },
    queryPersonalBaseInfo: function (async, paras, sucFunc, erroFunc) {
        post("queryPersonalBaseInfo.view", async, paras, sucFunc, erroFunc);
    },
    updatePersonalBaseInfo: function (async, paras, sucFunc, erroFunc) {
        post("updatePersonalBaseInfo.view", async, paras, sucFunc, erroFunc);
    },
    queryBaseInfoById: function (async, paras, sucFunc, erroFunc) {
        post("queryBaseInfoById.view", async, paras, sucFunc, erroFunc);
    },
    checkChangeUserArea: function (async, paras, sucFunc, erroFunc) {
        post("checkChangeUserArea.view", async, paras, sucFunc, erroFunc);
    },
    queryAnalysisFlowRateByUDate: function (async, paras, sucFunc, erroFunc) {
        post("queryAnalysisFlowRateByUDate.view", async, paras, sucFunc, erroFunc);
    },
    queryAnalysisFlowByUDate: function (async, paras, sucFunc, erroFunc) {
        post("queryAnalysisFlowByUDate.view", async, paras, sucFunc, erroFunc);
    },
    queryModelById: function (async, paras, sucFunc, erroFunc) {
        post("queryModelById.view", async, paras, sucFunc, erroFunc);
    },
    queryTerminalPackageData: function (async, paras, sucFunc, erroFunc) {
        post("queryTerminalPackageData.view", async, paras, sucFunc, erroFunc);
    },
    queryBehaviorPreferences: function (async, paras, sucFunc, erroFunc) {
        post("queryBehaviorPreferences.view", async, paras, sucFunc, erroFunc);
    },
    queryStockUserDaily: function (async, paras, sucFunc, erroFunc) {
        post("queryStockUserDaily.view", async, paras, sucFunc, erroFunc);
    },
    queryBaseMonthlyReservedData: function (async, paras, sucFunc, erroFunc) {
        post("queryBaseMonthlyReservedData.view", async, paras, sucFunc, erroFunc);
    },
    queryStockUserIncomeData: function (async, paras, sucFunc, erroFunc) {
        post("queryStockUserIncomeData.view", async, paras, sucFunc, erroFunc);
    },
    queryMonthlyReservedData: function (async, paras, sucFunc, erroFunc) {
        post("queryMonthlyReservedData.view", async, paras, sucFunc, erroFunc);
    },
    checkDuplicationOfModelName: function (async, paras, sucFunc, erroFunc) {
        post("checkDuplicationOfModelName.view", async, paras, sucFunc, erroFunc);
    },
    queryDataRefreshTime: function (async, paras, sucFunc, erroFunc) {
        post("queryDataRefreshTime.view", async, paras, sucFunc, erroFunc);
    },
    delPositionBaseById: function (async, paras, sucFunc, erroFunc) {
        post("delPositionBaseById.view", async, paras, sucFunc, erroFunc);
    },
    createShopTempTaskRequ: function (async, paras, sucFunc, erroFunc) {
        post("createShopTempTask.view", async, paras, sucFunc, erroFunc);
    },
    getShopTempTaskById: function (async, paras, sucFunc, erroFunc) {
        post("queryShopTempTaskByPage.view", async, paras, sucFunc, erroFunc);
    },
    queryDailyTrafficOrder: function (async, paras, sucFunc, erroFunc) {
        post("queryDailyTrafficOrder.view", async, paras, sucFunc, erroFunc);
    },
    queryMonthlyTrafficOrder: function (async, paras, sucFunc, erroFunc) {
        post("queryMonthlyTrafficOrder.view", async, paras, sucFunc, erroFunc);
    },
    queryProvincialTrafficOrder: function (async, paras, sucFunc, erroFunc) {
        post("queryProvincialTrafficOrder.view", async, paras, sucFunc, erroFunc);
    },
    pauseTempItem: function (async, paras, sucFunc, erroFunc) {
        post("pauseTempItem.view", async, paras, sucFunc, erroFunc);
    },
    queryNewSceneClientByPage: function (async, paras, sucFunc, erroFunc) {
        post("queryNewSceneClientByPage.view", async, paras, sucFunc, erroFunc);
    },
    queryNewSceneClientTypeTwo: function (async, paras, sucFunc, erroFunc) {
        post("queryNewSceneClientTypeTwo.view", async, paras, sucFunc, erroFunc);
    },
    queryNewSceneClientTypeOne: function (async, paras, sucFunc, erroFunc) {
        post("queryNewSceneClientTypeOne.view", async, paras, sucFunc, erroFunc);
    },
    validateSceneName: function (async, paras, sucFunc, erroFunc) {
        post("validateSceneName.view", async, paras, sucFunc, erroFunc);
    },
    queryProductOrderCity: function (async, paras, sucFunc, erroFunc) {
        post("getProductOrderCityName.view", async, paras, sucFunc, erroFunc);
    },
    queryMarketAreas: function (async, paras, sucFunc, errorFunc) {
        post("queryMarketAreas.view", async, paras, sucFunc, errorFunc);
    },
    queryMarketAssessmentByPage: function (async, paras, sucFunc, erroFunc) {
        post("queryMarketAssessmentByPage.view", async, paras, sucFunc, erroFunc);
    },
    queryMarketUserDetailsByPage: function (async, paras, sucFunc, erroFunc) {
        post("queryMarketUserDetailsByPage.view", async, paras, sucFunc, erroFunc);
    },
    queryShopTaskByIdForHover: function (async, paras, sucFunc, erroFunc) {
        post("queryShopTaskById.view", async, paras, sucFunc, erroFunc, null, null, true);
    },
    queryUserDistribution: function (async, paras, sucFunc, erroFunc) {
        post("queryUserDistribution.view", async, paras, sucFunc, erroFunc, null, null, true);
    },
    queryMyTodoTaskColumn: function (async, paras, sucFunc, erroFunc) {
        post("queryMyTodoTaskColumn.view", async, paras, sucFunc, erroFunc, null, null, true);
    }
};