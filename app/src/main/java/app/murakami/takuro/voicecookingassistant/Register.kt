package app.murakami.takuro.voicecookingassistant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_register.*
import java.time.LocalDateTime
import java.util.*

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
            val toRegisterIntent =  Intent(this,MainActivity::class.java)
            startActivity(toRegisterIntent)
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
            //val recipedata: RecipeData = it.createObject(RecipeData::class.java)
            val recipedata = it.createObject(RecipeData::class.java, UUID.randomUUID().toString())
            recipedata.menu = menu
            recipedata.ingredients = ingredients
            recipedata.method = method
        }

        Toast.makeText(applicationContext,"保存しました",Toast.LENGTH_SHORT).show()

    }
}