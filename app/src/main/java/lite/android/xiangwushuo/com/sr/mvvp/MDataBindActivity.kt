package lite.android.xiangwushuo.com.sr.mvvp;

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import lite.android.xiangwushuo.com.sr.R
import lite.android.xiangwushuo.com.sr.app.router.RoutePath
import lite.android.xiangwushuo.com.sr.databinding.ActivityMdbBinding

/**
 * 文 件 名: MDataBindActivity
 * 创 建 人: XiaQiBo
 * 创建日期: 2019/6/13 14:34
 * 邮   箱: bob.xia@xiangwushuo.com
 * 文件说明：TODO
 */
@RoutePath("data_binding")
class MDataBindActivity : AppCompatActivity() {

    lateinit var dataBinding: ActivityMdbBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mdb)
        dataBinding  = DataBindingUtil.setContentView(this , R.layout.activity_mdb)
        val user = User("haoren yisheng ping an ", 1001)
        dataBinding.userZS = user
    }



}
