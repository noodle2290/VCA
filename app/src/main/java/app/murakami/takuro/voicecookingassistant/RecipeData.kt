package app.murakami.takuro.voicecookingassistant

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.time.LocalDateTime
import java.util.*


//Realmに保存するクラス
open class RecipeData (
    @PrimaryKey open var id: String = UUID.randomUUID().toString(),
    open var menu:String = "",
    open var ingredients:String = "",
    open var method:String = ""
): RealmObject()