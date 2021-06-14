package app.murakami.takuro.voicecookingassistant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_recipe.*

class Recipe : AppCompatActivity() {
    val realm: Realm = Realm.getDefaultInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)

        var id = intent.getStringExtra("MENU")

        var Recipedata = realm.where(RecipeData::class.java)
            .equalTo("menu", "$id")
            .findFirst();

        menuTextView2.text = Recipedata?.menu.toString()
        ingredientsTextView.text = Recipedata?.ingredients.toString()
        methodTextView.text = Recipedata?.method.toString()
    }


    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)

        val inflater = menuInflater
        //メニューのリソース選択
        inflater.inflate(R.menu.recipe_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var id = intent.getStringExtra("MENU")

        var Recipedata = realm.where(RecipeData::class.java)
            .equalTo("menu", "$id")
            .findFirst()

        when (item.getItemId()) {

            R.id.edit -> {
                edit(id)
                return true
            }

            R.id.delete -> {
                delete(Recipedata)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    fun delete(Recipedata:RecipeData?){
        Toast.makeText(applicationContext, Recipedata?.menu + "を削除しました", Toast.LENGTH_SHORT)
            .show()
        val toMainActivityIntent = Intent(this, MainActivity::class.java)

        realm.executeTransaction {
            Recipedata?.deleteFromRealm()
        }
        startActivity(toMainActivityIntent)
    }

    fun edit(id:String?){
        val toRegisterIntent = Intent(this, Register::class.java)

        toRegisterIntent.putExtra("MENU", id)

        startActivity(toRegisterIntent)
    }
}