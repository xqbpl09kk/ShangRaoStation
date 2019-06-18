package lite.android.xiangwushuo.com.sr

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import lite.android.xiangwushuo.com.sr.app.router.RouteConstant
import lite.android.xiangwushuo.com.sr.app.router.RoutePath

@RoutePath(RouteConstant.APP_MERGE)
class TestMergeActivity : BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_merge)
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, TestMergeFragment()).commit()

    }
}