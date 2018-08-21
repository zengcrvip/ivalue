package com.axon.market.common.domain.iscene;

/**
 * Created by DELL on 2017/1/3.
 */
public class LocationDomain
{
    private int Id ;
    private String OID ;
    private String City ;
    private String TimeSpan ;
    private String No ;
    private String Class1 ;
    private String Class2 ;
    private String Class3 ;
    private String Class4 ;
    private String SceneId ;
    private String SceneName ;
    private String ParentScene ;
    private String SceneDescribe ;
    private String ScenePosition ;
    private String Scenelong ;
    private String Scenelat ;
    private String Coverage ;
    private int IsReport ;
    private String Province ;
    private String AdminRegion ;
    private String GeoRegion ;
    private String RegionName ;
    private String NetWorkType ;
    private String Remarks ;
    private String LAC ;
    private String CI ;
    private int ULIType ;

    public LocationDomain()
    {
    }

    public LocationDomain(int id, String OID, String city, String timeSpan, String no, String class1, String class2, String class3, String class4, String sceneId, String sceneName, String parentScene, String sceneDescribe, String scenePosition, String scenelong, String scenelat, String coverage, int isReport, String province, String adminRegion, String geoRegion, String regionName, String netWorkType, String remarks, String LAC, String CI, int ULIType)
    {
        Id = id;
        this.OID = OID;
        City = city;
        TimeSpan = timeSpan;
        No = no;
        Class1 = class1;
        Class2 = class2;
        Class3 = class3;
        Class4 = class4;
        SceneId = sceneId;
        SceneName = sceneName;
        ParentScene = parentScene;
        SceneDescribe = sceneDescribe;
        ScenePosition = scenePosition;
        Scenelong = scenelong;
        Scenelat = scenelat;
        Coverage = coverage;
        IsReport = isReport;
        Province = province;
        AdminRegion = adminRegion;
        GeoRegion = geoRegion;
        RegionName = regionName;
        NetWorkType = netWorkType;
        Remarks = remarks;
        this.LAC = LAC;
        this.CI = CI;
        this.ULIType = ULIType;
    }

    public int getId()
    {
        return Id;
    }

    public void setId(int id)
    {
        Id = id;
    }

    public String getOID()
    {
        return OID;
    }

    public void setOID(String OID)
    {
        this.OID = OID;
    }

    public String getCity()
    {
        return City;
    }

    public void setCity(String city)
    {
        City = city;
    }

    public String getTimeSpan()
    {
        return TimeSpan;
    }

    public void setTimeSpan(String timeSpan)
    {
        TimeSpan = timeSpan;
    }

    public String getNo()
    {
        return No;
    }

    public void setNo(String no)
    {
        No = no;
    }

    public String getClass1()
    {
        return Class1;
    }

    public void setClass1(String class1)
    {
        Class1 = class1;
    }

    public String getClass2()
    {
        return Class2;
    }

    public void setClass2(String class2)
    {
        Class2 = class2;
    }

    public String getClass3()
    {
        return Class3;
    }

    public void setClass3(String class3)
    {
        Class3 = class3;
    }

    public String getClass4()
    {
        return Class4;
    }

    public void setClass4(String class4)
    {
        Class4 = class4;
    }

    public String getSceneId()
    {
        return SceneId;
    }

    public void setSceneId(String sceneId)
    {
        SceneId = sceneId;
    }

    public String getSceneName()
    {
        return SceneName;
    }

    public void setSceneName(String sceneName)
    {
        SceneName = sceneName;
    }

    public String getParentScene()
    {
        return ParentScene;
    }

    public void setParentScene(String parentScene)
    {
        ParentScene = parentScene;
    }

    public String getSceneDescribe()
    {
        return SceneDescribe;
    }

    public void setSceneDescribe(String sceneDescribe)
    {
        SceneDescribe = sceneDescribe;
    }

    public String getScenePosition()
    {
        return ScenePosition;
    }

    public void setScenePosition(String scenePosition)
    {
        ScenePosition = scenePosition;
    }

    public String getScenelong()
    {
        return Scenelong;
    }

    public void setScenelong(String scenelong)
    {
        Scenelong = scenelong;
    }

    public String getScenelat()
    {
        return Scenelat;
    }

    public void setScenelat(String scenelat)
    {
        Scenelat = scenelat;
    }

    public String getCoverage()
    {
        return Coverage;
    }

    public void setCoverage(String coverage)
    {
        Coverage = coverage;
    }

    public int getIsReport()
    {
        return IsReport;
    }

    public void setIsReport(int isReport)
    {
        IsReport = isReport;
    }

    public String getProvince()
    {
        return Province;
    }

    public void setProvince(String province)
    {
        Province = province;
    }

    public String getAdminRegion()
    {
        return AdminRegion;
    }

    public void setAdminRegion(String adminRegion)
    {
        AdminRegion = adminRegion;
    }

    public String getGeoRegion()
    {
        return GeoRegion;
    }

    public void setGeoRegion(String geoRegion)
    {
        GeoRegion = geoRegion;
    }

    public String getRegionName()
    {
        return RegionName;
    }

    public void setRegionName(String regionName)
    {
        RegionName = regionName;
    }

    public String getNetWorkType()
    {
        return NetWorkType;
    }

    public void setNetWorkType(String netWorkType)
    {
        NetWorkType = netWorkType;
    }

    public String getRemarks()
    {
        return Remarks;
    }

    public void setRemarks(String remarks)
    {
        Remarks = remarks;
    }

    public String getLAC()
    {
        return LAC;
    }

    public void setLAC(String LAC)
    {
        this.LAC = LAC;
    }

    public String getCI()
    {
        return CI;
    }

    public void setCI(String CI)
    {
        this.CI = CI;
    }

    public int getULIType()
    {
        return ULIType;
    }

    public void setULIType(int ULIType)
    {
        this.ULIType = ULIType;
    }
}
