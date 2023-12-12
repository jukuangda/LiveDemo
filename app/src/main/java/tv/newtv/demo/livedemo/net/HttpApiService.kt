package tv.newtv.demo.livedemo.net

import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import retrofit2.http.Url
import tv.newtv.demo.livedemo.data.bean.CntvLiveVdn
import tv.newtv.demo.livedemo.util.Constants

interface HttpApiService {
    @GET
    fun getCntvLiveVdn(
        @Url url: String,
        @Query(value = "channel", encoded = true) channel: String?,
        @Query("tsp") tsp: String?,
        @Query("vc") vc: String?,
        @Query("uid") uid: String?,
        @Query("vtokenpos") vtokenpos: String?,
        @Query("vtoken") vtokenUrl: String?,
        @Header("vtoken") vtokenHeader: String?,
        @Query("client") client: String = "androidapp",
        @Query("im") im: String = "1",
        @Query("vn") vn: String = Constants.CNTV_LIVE_VERSION_CODE,
        @Query("wlan") wlan: String = ""
    ): Flow<CntvLiveVdn>
}