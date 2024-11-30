import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projetoappreceitas.Feed
import com.example.projetoappreceitas.R
import com.example.projetoappreceitas.Receita

class RecipeAdapter(private var receitas: MutableList<Receita>, val listener: Feed) :
    RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    private var filteredList: MutableList<Receita>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val itemLista = LayoutInflater.from(parent.context).inflate(R.layout.item_receitas, parent, false)
        return RecipeViewHolder(itemLista)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val receita = filteredList?.get(position) ?: receitas[position]
        holder.nome.text = receita.nome
        holder.descricao.text = receita.descricao
        holder.ratingBar.rating = receita.ratingBar

        Glide.with(holder.itemView.context)
            .load(receita.imagem) // URL da imagem
            .placeholder(R.drawable.placeholder) // Imagem de carregamento
            .error(R.drawable.error_image) // Imagem de erro
            .into(holder.imagem) // ImageView de destino

        holder.itemView.setOnClickListener {
            listener.onItemClick(receita)
        }

        holder.itemView.setOnClickListener {
            listener.onItemClick(receita)
        }
    }

    override fun getItemCount(): Int = filteredList?.size ?: receitas.size

    interface MyClickListener {
        fun onItemClick(receita: Receita)
    }

    fun searchDataList(searchList: MutableList<Receita>) {
        filteredList = searchList
        notifyDataSetChanged()
    }

    fun resetSearch() {
        filteredList = null
        notifyDataSetChanged()
    }

    inner class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imagem = itemView.findViewById<ImageView>(R.id.imagemReceita)
        val nome = itemView.findViewById<TextView>(R.id.nomeReceita)
        val descricao = itemView.findViewById<TextView>(R.id.descricaoReceita)
        val ratingBar = itemView.findViewById<RatingBar>(R.id.ratingBar)
    }
}