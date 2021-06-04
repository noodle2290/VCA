package app.murakami.takuro.voicecookingassistant

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.realm.OrderedRealmCollection
import kotlinx.android.synthetic.main.item_list.view.*

class MenuAdapter(    private val context: Context,
                      private var menuList: OrderedRealmCollection<RecipeData>?,
                      private val autoUpdate: Boolean) :
    RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    class MenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val menuTextView: TextView = view.menuTextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.item_list, parent, false)
        return MenuViewHolder(v)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val menu: RecipeData = menuList?.get(position) ?: return

        holder.menuTextView.text = menu.menu
    }

    override fun getItemCount(): Int = menuList?.size ?: 0
}