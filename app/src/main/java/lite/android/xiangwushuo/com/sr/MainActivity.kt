package lite.android.xiangwushuo.com.sr;

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import lite.android.xiangwushuo.com.sr.app.router.RouteConstant
import lite.android.xiangwushuo.com.sr.app.router.RouteInitial
import lite.android.xiangwushuo.com.sr.app.router.RoutePath
import lite.android.xiangwushuo.com.sr.mvvp.MDataBindActivity

/**
 * 文 件 名: MainActivity
 * 创 建 人: XiaQiBo
 * 创建日期: 2019/6/13 15:29
 * 邮   箱: bob.xia@xiangwushuo.com
 * 文件说明：TODO
 */

@RoutePath(RouteConstant.APP_MAIN)
class MainActivity : BaseActivity() {

    private val arrays = arrayOf(
            RouteConstant.APP_ITEM_DETAIL,
            RouteConstant.APP_HOOK,
            RouteConstant.APP_DATA_BIND)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RecyclerView(this).apply {
            setContentView(this)
            adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
                val actCxt  = this@MainActivity
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                    val textView = TextView(actCxt).apply {
                        layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT)
                        gravity = Gravity.CENTER
                        setPadding(30,30,30,30)
                    }
                    return object : RecyclerView.ViewHolder(textView) {
                        init {
                            itemView.setOnClickListener {
                                RouteInitial.routeByPath(arrays[adapterPosition])
                            }
                        }
                    }
                }

                override fun getItemCount(): Int {
                    return arrays.size
                }

                override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                    (holder.itemView as? TextView)?.text = arrays[position]
                }
            }
            this.layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }
}
