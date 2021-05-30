package app.murakami.takuro.voicecookingassistant

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration
//アプリを起動したら最初に通るクラス
class RealmRecipeApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        //Realmを初期化し使いやすくしている
        Realm.init(this)
        val realmConfig =  RealmConfiguration.Builder()
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(realmConfig)
    }
}