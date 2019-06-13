package lite.android.xiangwushuo.com.shangraostation

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast

import lite.android.xiangwushuo.com.shangraostation.dummy.DummyContent
import kotlinx.android.synthetic.main.activity_item_list.*
import kotlinx.android.synthetic.main.item_list_content.view.*
import kotlinx.android.synthetic.main.item_list.*

/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [ItemDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class ItemListActivity : AppCompatActivity() {
    private val colorSpan: ForegroundColorSpan by lazy { ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorPrimary)) }
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPane: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)

        setSupportActionBar(toolbar)
        toolbar.title = title

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        if (item_detail_container != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }

        setupRecyclerView(item_list)
//        test()
        val str = "这里是一条评论" +
                "<a href=\"/pages/mine/ucenter/index?code=501&user_id="+
                "7F7317581R0E69113848693554\">@五月、微暖</a>评论解释了这里是一条评论" +
                "<a href=\"/pages/mine/ucenter/index?code=501&user_id="+
                "7F7317581R0E69113848693554\">"

        matchAtText(str)
        textView.movementMethod = LinkMovementMethod.getInstance()
        regexMatchAtText(str)
    }



    fun regexMatchAtText(expression :String){
        val TAG = "ItemListActivity"
        val pattern = ".*<a href=\"/pages/mine/ucenter/index?code=501&user_id.*"
//        val expression = "1.5"
        val regex = Regex(pattern)
        val containsMatchInResult= regex.containsMatchIn(expression)
        Log.e(TAG , "containsMatchInResult is $containsMatchInResult")
        val findResult = regex.find(expression )
        Log.e(TAG , "findResult value is ${findResult?.value}")

        val matchesResult = regex.matches(expression)
        Log.e(TAG , "matchesResult is $matchesResult")

        val matchEntire = regex.matchEntire(expression)
        Log.e(TAG , "matchEntireResult is $matchEntire")

        val splitResult = regex.split(expression )
        Log.e(TAG , "splitResult is $splitResult")
    }



    private fun matchAtText(matchStr: String): SpannableStringBuilder {
        val sub = "[<a href=\"/pages/mine/ucenter/index?code=501&user_id=]"
        val regex = Regex(sub)
        val result = regex.matches(matchStr)

//        Log.e("ItemListActivity" , "match result is $result")
        val array = matchStr.split(sub)
        val stringBuilder = SpannableStringBuilder()
        for (item in array) {
            if (item.contains("</a>")) {
                try {
                    val userId = item.substring(0, 25)
                    val userName = item.substring(28, item.indexOf("</a>"))
                    val spannable = SpannableStringBuilder(userName)
                    val clickSpan = object : ClickableSpan() {
                        override fun onClick(widget: View) {
                            startActivity(Intent(this@ItemListActivity , TestMergeActivity::class.java))
                            Toast.makeText(this@ItemListActivity, "userId is $userId", Toast.LENGTH_SHORT).show()
                        }

                        override fun updateDrawState(ds: TextPaint) {
                            ds.color = ContextCompat.getColor(this@ItemListActivity, R.color.colorPrimary)
                        }
                    }
                    spannable.setSpan(clickSpan, 0, userName.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    textView.append(spannable)
                    textView.append(item.substring(item.indexOf("</a>") + 4 , item.length))
                } catch (t: Throwable) {
                    t.printStackTrace()
                    textView.append(item)
                }
            } else {
                textView.append(item)
            }

        }
        return stringBuilder
    }

    private fun test() {
        val testString = "aaaabbbbbcccccdddddaaaahhh"
        val split = "cc"
        val array = testString.split(split)
        System.out.print(array)
        Log.e("ItemListActivity", "current array is $array")
        val spannableStringBuilder = SpannableStringBuilder()
        for (index in 0..10) {
            val st = "hao$index"
            val style = SpannableStringBuilder(st)
            val colorSpan = ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorPrimary))
            style.setSpan(colorSpan, 0, st.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            spannableStringBuilder.append(style)
            spannableStringBuilder.append("、、")
        }
        textView.text = spannableStringBuilder
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = SimpleItemRecyclerViewAdapter(this, DummyContent.ITEMS, twoPane)
    }

    class SimpleItemRecyclerViewAdapter(private val parentActivity: ItemListActivity,
                                        private val values: List<DummyContent.DummyItem>,
                                        private val twoPane: Boolean) :
            RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        private val onClickListener: View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->
                val item = v.tag as DummyContent.DummyItem
                if (twoPane) {
                    val fragment = ItemDetailFragment().apply {
                        arguments = Bundle().apply {
                            putString(ItemDetailFragment.ARG_ITEM_ID, item.id)
                        }
                    }
                    parentActivity.supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.item_detail_container, fragment)
                            .commit()
                } else {
                    val intent = Intent(v.context, ItemDetailActivity::class.java).apply {
                        putExtra(ItemDetailFragment.ARG_ITEM_ID, item.id)
                    }
                    v.context.startActivity(intent)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_list_content, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = values[position]
            holder.idView.text = item.id
            holder.contentView.text = item.content

            with(holder.itemView) {
                tag = item
                setOnClickListener(onClickListener)
            }
        }

        override fun getItemCount() = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val idView: TextView = view.id_text
            val contentView: TextView = view.content
        }
    }
}
