package xyz.kalinski.perform.activities.main.fragments.news.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.news_item_view.view.*
import xyz.kalinski.perform.R
import xyz.kalinski.perform.commons.DateHelper
import xyz.kalinski.perform.models.response.Item
import xyz.kalinski.perform.utils.inflate
import xyz.kalinski.perform.utils.loadImageCenter
import xyz.kalinski.perform.view.AutoUpdatableAdapter
import kotlin.properties.Delegates

class NewsAdapter(val listener: (Item) -> Unit) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>(), AutoUpdatableAdapter {

    var items: List<Item> by Delegates.observable(emptyList()) {
        prop, old, new ->
        autoNotify(old, new) { o, n -> o.guid == n.guid }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = NewsViewHolder(parent.inflate(R.layout.news_item_view))

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) = holder.bind(items[position], listener)

    override fun getItemCount() = items.size

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Item, listener: (Item) -> Unit) = with(itemView) {
            image.loadImageCenter(item.enclosure?.url)
            title.text = item.title
            description.text = item.description
            if (item.pubDate != null) {
                date.text = DateHelper.parseDate(DateHelper.parseString(item.pubDate!!), DateHelper.USER_FRIENDLY_FORMAT)
            }
            category.text = item.category
            itemView.setOnClickListener { listener.invoke(item) }
        }
    }

}