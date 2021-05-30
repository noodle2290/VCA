package app.murakami.takuro.voicecookingassistant

import io.realm.RealmObject
import java.time.LocalDateTime


//Realmに保存するクラス
open class RecipeData (
    open var menu:String = "",
    open var ingredients:String = "",
    open var method:String = ""
): RealmObject()