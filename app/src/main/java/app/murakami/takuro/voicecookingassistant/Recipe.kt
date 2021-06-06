package app.murakami.takuro.voicecookingassistant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_recipe.*

class Recipe : AppCompatActivity() {
    val realm: Realm = Realm.getDefaultInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)

        var id = intent.getStringExtra("MENU")

        var Recipedata = realm.where(RecipeData::class.java)
                .equalTo("menu","$id")
                .findFirst();

        menuTextView2.text = Recipedata?.menu.toString()
        ingredientsTextView.text = Recipedata?.ingredients.toString()
        methodTextView.text = Recipedata?.method.toString()
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}