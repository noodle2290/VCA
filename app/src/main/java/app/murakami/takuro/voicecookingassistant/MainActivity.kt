package app.murakami.takuro.voicecookingassistant

import android.Manifest.permission.RECORD_AUDIO
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    //speechRecognizerの定義
    private var speechRecognizer : SpeechRecognizer? = null
    //委譲プロパティを使って遅延初期化
    private val realm: Realm by lazy {
        Realm.getDefaultInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //realmのデータをrecyclerview用に定義
        val menuList = readAll()

        //adapterの定義　再読み込みできるようにvarにした
        var adapter = MenuAdapter(this, menuList, object : MenuAdapter.OnItemClickListener {
            override fun onItemClick(item: RecipeData) {
                // クリック時の処理
                Toast.makeText(applicationContext, item.menu + "です", Toast.LENGTH_SHORT).show()
                val toRecipeIntent = Intent(this@MainActivity,Recipe::class.java)

                toRecipeIntent.putExtra("MENU",item.menu)

                startActivity(toRecipeIntent)
            }
        })

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter//アダプターを適用

        //起動時に音声入力許可をもらう
        val granted = ContextCompat.checkSelfPermission(this, RECORD_AUDIO)
        if (granted != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(RECORD_AUDIO), PERMISSIONS_RECORD_AUDIO)
        }

        //SpeechRecognizerをActivityで使えるようにする
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(applicationContext)

        //speechRecognizerがnullじゃなければtextViewに聞き取れた音声を表示する
        speechRecognizer?.setRecognitionListener(createRecognitionListenerStringStream { textView.text = it })

        // setOnClickListener でクリック動作を登録し、クリックで音声入力が開始するようにする
        startButton.setOnClickListener { speechRecognizer?.startListening(Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)) }

        // setOnclickListener でクリック動作を登録し、クリックで音声入力が停止するようにする
        //stopButton.setOnClickListener { speechRecognizer?.stopListening() }

        //realmのデータを全消去
        deleteButton.setOnClickListener {
                realm.executeTransaction {
                    realm.deleteAll()
            }
            Toast.makeText(applicationContext,"すべて消去しました", Toast.LENGTH_SHORT).show()
            var adapter = MenuAdapter(this, menuList, object : MenuAdapter.OnItemClickListener {
                override fun onItemClick(item: RecipeData) {
                    // クリック時の処理
                    Toast.makeText(applicationContext, item.menu + "です", Toast.LENGTH_SHORT).show()
                    val toRecipeIntent = Intent(this@MainActivity,Recipe::class.java)

                    toRecipeIntent.putExtra("MENU",item.menu)

                    startActivity(toRecipeIntent)
                }
            })
            recyclerView.adapter = adapter//ここで再読み込み
        }

        //登録画面に飛ぶ
        registerIntentButton.setOnClickListener {
            val toRegisterIntent =  Intent(this,Register::class.java)
            startActivity(toRegisterIntent)
        }
    }

    //RecognizerListenerの登録
    private fun createRecognitionListenerStringStream(onResult : (String)-> Unit) : RecognitionListener {
        return object : RecognitionListener {
            override fun onRmsChanged(rmsdB: Float) { /** 今回は特に利用しない */ }
            override fun onReadyForSpeech(params: Bundle) { onResult("メニュー名は？") }
            override fun onBufferReceived(buffer: ByteArray) { onResult("onBufferReceived") }
            override fun onPartialResults(partialResults: Bundle) { onResult("onPartialResults") }
            override fun onEvent(eventType: Int, params: Bundle) { onResult("onEvent") }
            override fun onBeginningOfSpeech() { onResult("聞き取り中") }
            override fun onEndOfSpeech() { onResult("聞き取り終了") }
            override fun onError(error: Int) { onResult("エラー") }
            override fun onResults(results: Bundle) {
                val stringArray = results.getStringArrayList(android.speech.SpeechRecognizer.RESULTS_RECOGNITION)

                if (stringArray != null) {
                    var word = stringArray[0]

                    onResult(stringArray[0].toString())


                val toRecipeIntent = Intent(this@MainActivity,Recipe::class.java)

                Toast.makeText(applicationContext, word + "です", Toast.LENGTH_SHORT).show()
                toRecipeIntent.putExtra("MENU",word)

                startActivity(toRecipeIntent)
                }
            }
        }
    }

    //アプリを閉じたらspeechRecognizerを閉じる
    override fun onDestroy() {
        super.onDestroy()
        speechRecognizer?.destroy()
        realm.close()
    }

    companion object {
        private const val PERMISSIONS_RECORD_AUDIO = 1000
    }

    //Realmのデータを読込
    fun readAll(): RealmResults<RecipeData> {
        return realm.where(RecipeData::class.java).findAll()//.sort("createdAt", Sort.ASCENDING)
    }

}

