package lite.android.xiangwushuo.com.shangraostation

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class TestMergeActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_merge)
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer , TestMergeFragment()).commit()

    }
}