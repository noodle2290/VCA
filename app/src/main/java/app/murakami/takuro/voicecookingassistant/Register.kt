package app.murakami.takuro.voicecookingassistant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_register.*
import java.time.LocalDateTime

class Register : AppCompatActivity() {

    val realm: Realm = Realm.getDefaultInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        registerButton.setOnClickListener {
            val menu = menuEditText.text.toString()
            val ingredients = ingredientsEditText.text.toString()
            val method = methodEditText.text.toString()
            save(menu,ingredients,method)
        }
    }

    //アプリを閉じるとRealmも閉じる
    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    fun save(menu:String, ingredients:String,method:String){
        //保存する処理

        realm.executeTransaction{
            val recipedata: RecipeData = it.createObject(RecipeData::class.java)
            recipedata.menu = menu
            recipedata.ingredients = ingredients
            recipedata.method = method
        }

        Toast.makeText(applicationContext,"保存しました",Toast.LENGTH_SHORT)

    }
}