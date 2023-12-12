package tv.newtv.demo.livedemo.data.bean

import com.google.gson.annotations.SerializedName

/*{
        "ack": "yes",
        "lc": {
        "isp_code": "1",
        "city_code": "",
        "provice_code": "TJ",
        "country_code": "CN",
        "ip": "123.150.146.2"
    },
        "client_sid": "PhJskjvKn\/MrFeL+ThbDs4XDssBScRoi422wJcnFI8w=",
        "flv_cdn_info": {
        "cdn_code": "LIVE-FLV-CDN-CNC2",
        "cdn_name": "3rdFLV网宿2"
    },
        "flv_url": {
        "flv1": "",
        "flv2": "",
        "flv3": "yangshi?group&drm=2",
        "flv4": "",
        "flv5": "http:\/\/cctvcncw.v.wscdns.com\/live\/cctv2_1\/index.m3u8?adapt=0&BR=pub",
        "flv6": ""
    },
        "hls_cdn_info": {
        "cdn_code": "LIVE-HLS-CDN-BS",
        "cdn_name": "3rdHLS白山云"
    },
        "hls_url": {
        "hls1": "http:\/\/carksc.v.kcdnvip.com\/car\/udrmcctv2_1\/index.m3u8?contentid=2820180516001",
        "hls2": "http:\/\/cctvcnch5ca.v.wscdns.com\/live\/cctv2_2\/index.m3u8?BR=hd&contentid=2820180516001",
        "hls3": "yangshi?group&drm=2",
        "hls4": "http:\/\/carksc.v.kcdnvip.com\/car\/udrmcctv2_1\/index.m3u8?contentid=2820180516001",
        "hls5": "http:\/\/cctvbsw.v.live.baishancdnx.cn\/live\/cctv2_2\/index.m3u8?adapt=0&BR=pic",
        "hls6": "http:\/\/cctvbsw.v.live.baishancdnx.cn\/live\/cctv2_1\/index.m3u8?adapt=0&BR=audio"
    },
        "hds_cdn_info": {
        "cdn_code": "LIVE-HDS-CDN-KS",
        "cdn_name": "3rdHDS金山云"
    },
        "hds_url": {
        "hds1": "",
        "hds2": "",
        "hds3": "yangshi?group&drm=2",
        "hds4": "",
        "hds5": "",
        "hds6": ""
    },
        "public": "1",
        "status": "1",
        "video_protect": "3",
        "audio_protect": "3"
    }*/
data class CntvLiveVdn(

    @SerializedName("ack")
    val ack: String? = null,

    @SerializedName("audio_protect")
    val audioProtect: String? = null,

    @SerializedName("client_sid")
    val clientSid: String? = null,

    @SerializedName("hls_cdn_info")
    val hlsCdnInfo: CntvLiveHlsCdnInfo? = null,

    @SerializedName("hls_url")
    val hlsUrl: CntvLiveHlsUrl? = null,

    @SerializedName("lc")
    var lc: CntvLiveLc? = null,

    @SerializedName("public")
    val isPublic: String? = null,

    val status: String? = null,

    @SerializedName("video_protect")
    val videoProtect: String? = null
)

data class CntvLiveHlsCdnInfo(
    @SerializedName("cdn_code")
    val cdnCode: String? = null,

    @SerializedName("cdn_name")
    val cdnName: String? = null
)

data class CntvLiveHlsUrl(
    @SerializedName("hls1")
    val hls1: String? = null,
    @SerializedName("hls2")
    val hls2: String? = null,
    @SerializedName("hls3")
    val hls3: String? = null,
    @SerializedName("hls4")
    val hls4: String? = null,
    @SerializedName("hls5")
    val hls5: String? = null,
    @SerializedName("hls6")
    val hls6: String? = null
)

data class CntvLiveLc(
    @SerializedName("city_code")
    val cityCode: String,

    @SerializedName("country_code")
    val countryCode: String,

    val ip: String,

    @SerializedName("isp_code")
    val ispCode: String,

    @SerializedName("provice_code")
    val proviceCode: String
)