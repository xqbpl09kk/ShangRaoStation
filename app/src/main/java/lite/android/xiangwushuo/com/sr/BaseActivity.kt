package lite.android.xiangwushuo.com.sr;

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import lite.android.xiangwushuo.com.sr.app.MyApp

/**
 * 文 件 名: BaseActivity
 * 创 建 人: XiaQiBo
 * 创建日期: 2019/6/13 16:22
 * 邮   箱: bob.xia@xiangwushuo.com
 * 文件说明：TODO
 */
public class BaseActivity  :AppCompatActivity(){
    protected val appCxt = MyApp.INSTANCE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}
