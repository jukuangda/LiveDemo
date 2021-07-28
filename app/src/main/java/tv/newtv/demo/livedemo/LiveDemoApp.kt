package tv.newtv.demo.livedemo

import android.app.Activity
import android.content.Context
import android.media.AudioManager
import android.os.Bundle
import androidx.multidex.MultiDexApplication

class LiveDemoApp : MultiDexApplication() {

    private var foregroundActivityNum: Int = 0
    val isBackground: Boolean
        get() {
            return foregroundActivityNum < 1
        }

    companion object {
        lateinit var INSTANCE: LiveDemoApp
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        appContext = applicationContext
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {

            override fun onActivityPaused(activity: Activity?) {
                foregroundActivityNum--
                if (isBackground) {
                    abandonAudioFocus()
                }
            }

            override fun onActivityResumed(activity: Activity?) {
                if (isBackground) {
                    requestAudioFocus()
                }
                foregroundActivityNum++
            }

            override fun onActivityStarted(activity: Activity?) {

            }

            override fun onActivityDestroyed(activity: Activity?) {

            }

            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
            }

            override fun onActivityStopped(activity: Activity?) {

            }

            override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {

            }
        })
    }

    //请求音频焦点
    private fun requestAudioFocus() {
        //请求焦点的参数说明：
        //AUDIOFOCUS_GAIN：想要长期占有焦点，失去焦点者stop播放和释放
        //AUDIOFOCUS_GAIN_TRANSIENT：想要短暂占有焦点，失去焦点者pause播放
        //AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK：想要短暂占有焦点，失去焦点者可以继续播放但是音量需要调低
        //AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE：想要短暂占有焦点，但希望失去焦点者不要有声音播放
        (getSystemService(Context.AUDIO_SERVICE) as AudioManager)
            .requestAudioFocus(
                audioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN
            )
    }

    //释放音频焦点
    private fun abandonAudioFocus() {
        (getSystemService(Context.AUDIO_SERVICE) as AudioManager)
            .abandonAudioFocus(audioFocusChangeListener)
    }

    private val audioFocusChangeListener by lazy {
        AudioManager.OnAudioFocusChangeListener { focusChange ->
        }
    }
}