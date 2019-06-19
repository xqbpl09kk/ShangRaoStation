package lite.android.xiangwushuo.com.sr.app.router

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.Fragment
import lite.android.xiangwushuo.com.sr.BaseActivity
import lite.android.xiangwushuo.com.sr.BuildConfig
import lite.android.xiangwushuo.com.sr.app.MyApp
import java.util.*
import kotlin.collections.ArrayList

//TODO : Router
object RouteInitial : IRouter{

    private val pathMap: LinkedHashMap<String, Class<out BaseActivity>> = LinkedHashMap()

    fun init(context: Context) {
        val activities = getActivities(context)
        for (activity in activities) {
            val key = getRouteByClass(activity)
            pathMap[key] = activity
        }
    }


    fun routeByPath(path: String) {
        if (pathMap.isEmpty())
            init(MyApp.INSTANCE)
        val appCxt = MyApp.INSTANCE
        val clazz = pathMap[path] ?: throw IllegalArgumentException("没有配置$path 路径的activity")
        appCxt.startActivity(Intent(appCxt, clazz)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).apply {
            this.putExtra("extraData", Bundle().apply {
                this.putInt("index", 1)
            })
        })
    }

    fun routerByPath(context : Context , path:String ){

    }

    fun routerByPath(path : String ,
                     context : Context ?= null ,
                     requestCode : Int? = null  ,
                     bundle : Bundle ?= null ,
                     fragment : Fragment?= null){
        val cxt :Context  = when(context){
            is Activity -> {
                context
            }
            is Application ->{
                context
            }
            else -> {
                MyApp.INSTANCE
            }
        }
    }



    /**
     * 从Activity的注解中获取path
     * @param clazz : 类明
     */
    private fun getRouteByClass(clazz: Class<out BaseActivity>): String {
        val activityClass = Class.forName(clazz.name
                , false, BaseActivity::class.java.classLoader)
        for (annotation in activityClass.annotations) {
            if (annotation is RoutePath) {
                return annotation.value
            }
        }
        if(BuildConfig.DEBUG)
            throw IllegalArgumentException("请在BaseActivity中配置Route属性")
        return RouteConstant.APP_MAIN
    }


    /**
     *根据package Manager获取当前App的所有Activity
     * @param context : App Context
     */
    private fun getActivities(context: Context): ArrayList<Class<out BaseActivity>> {
        val pm = context.packageManager
        val packageInfo = pm.getPackageInfo(
                context.packageName, PackageManager.GET_ACTIVITIES)
        val activities = arrayListOf<Class<out BaseActivity>>()
        for (activityInfo in packageInfo.activities) {
            try {
                val clazzInfo =
                        Class.forName(activityInfo.name, false
                                , BaseActivity::class.java.classLoader)
                                .asSubclass(BaseActivity::class.java)
                activities.add(clazzInfo)
            } catch (t: Throwable) {
                t.printStackTrace()
                if (BuildConfig.DEBUG) {
                    throw  t
                }
            }
        }
        return activities
    }


}


