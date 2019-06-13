package lite.android.xiangwushuo.com.shangraostation;

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.app.ListFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * 文 件 名: TestMergeFragment
 * 创 建 人: XiaQiBo
 * 创建日期: 2019/6/11 16:42
 * 邮   箱: bob.xia@xiangwushuo.com
 * 文件说明：TODO
 */
public class TestMergeFragment : ListFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_test_merge , container,false  )
    }



}
