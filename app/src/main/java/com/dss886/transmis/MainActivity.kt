package com.dss886.transmis

import android.content.Intent
import android.net.Uri
import com.dss886.transmis.base.BaseSwitchActivity
import com.dss886.transmis.listen.CallActivity
import com.dss886.transmis.listen.SmsActivity
import com.dss886.transmis.plugin.PluginActivity
import com.dss886.transmis.plugin.PluginManager
import com.dss886.transmis.utils.Constants
import com.dss886.transmis.utils.TransmisManager
import com.dss886.transmis.view.IConfig
import com.dss886.transmis.view.SectionConfig
import com.dss886.transmis.view.SwitchConfig
import com.dss886.transmis.view.TextButtonConfig

class MainActivity : BaseSwitchActivity() {

    override fun getToolbarTitle(): String {
        return getString(R.string.app_name)
    }

    override fun showToolbarBackIcon(): Boolean {
        return false
    }

    override fun getConfigs(): List<IConfig> {
        return mutableListOf<IConfig>().apply {
            add(SwitchConfig("总开关", Constants.SP_GLOBAL_ENABLE))
            add(SectionConfig("监听内容"))
            add(TextButtonConfig("短信", showRightArrow = true).apply {
                clickAction = {
                    startActivity(Intent(this@MainActivity, SmsActivity::class.java))
                }
                resumeAction = {
                    content = if (TransmisManager.isSmsEnable()) "开" else "关"
                }
            })
            add(TextButtonConfig("未接电话", showRightArrow = true).apply {
                clickAction = {
                    startActivity(Intent(this@MainActivity, CallActivity::class.java))
                }
                resumeAction = {
                    content = if (TransmisManager.isMissingCallEnable()) "开" else "关"
                }
            })
            add(SectionConfig("提醒插件"))
            PluginManager.plugins.forEach { plugin ->
                add(TextButtonConfig(plugin.getName(), showRightArrow = true).apply {
                    clickAction = {
                        PluginActivity.start(this@MainActivity, plugin)
                    }
                    resumeAction = {
                        content = if (plugin.isEnable()) "开" else "关"
                    }
                })
            }
            add(SectionConfig("关于"))
            add(TextButtonConfig("使用帮助") {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Constants.URL_README)))
            })
            add(TextButtonConfig("检查更新", "当前版本 v" + BuildConfig.VERSION_NAME) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Constants.URL_RELEASE)))
            })
            add(TextButtonConfig("开源许可", "GNU v3.0") {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Constants.URL_LICENSE)))
            })
        }
    }

}