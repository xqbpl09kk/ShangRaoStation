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
class MainActivity : AppCompatActivity() {

    private val arrays = arrayOf("itemList", "Hook", "DataBinding")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>( ) {
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
                            var bundle :Bundle ? = null
                            val clazz = when(adapterPosition){
                                0 -> {
                                    bundle = Bundle()
                                    ItemDetailActivity::class.java
                                }
                                1 -> JavaHookActivity::class.java
                                2 -> MDataBindActivity::class.java
                                else -> MainActivity::class.java
                            }
                            val activityIntent= Intent(actCxt , clazz)
                            bundle?.let {
                                activityIntent.putExtra("bundle" , it )
                            }
                            actCxt.startActivity(activityIntent)

                            RouteInitial.routeByPath(RouteConstant.APP_MAIN)
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
        recyclerView.layoutManager = LinearLayoutManager(this)

    }


}
