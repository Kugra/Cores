package johan.kovalsikoski.cores

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

const val MAX_INT = 255

class MainActivity : AppCompatActivity(){

    private val random: Random by lazy { Random() }
    private val colorList = mutableListOf<ColorObject>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sb_red.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                v_color_result.setBackgroundColor(Color.rgb(progress, sb_green.progress, sb_blue.progress))
                updateTextRedLabel(progress)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) { }

            override fun onStopTrackingTouch(p0: SeekBar?) { }

        })

        sb_green.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                v_color_result.setBackgroundColor(Color.rgb(sb_red.progress, progress, sb_blue.progress))
                updateTextGreenLabel(progress)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) { }

            override fun onStopTrackingTouch(p0: SeekBar?) { }

        })

        sb_blue.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                v_color_result.setBackgroundColor(Color.rgb(sb_red.progress, sb_green.progress, progress))
                updateTextBlueLabel(progress)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) { }

            override fun onStopTrackingTouch(p0: SeekBar?) { }

        })

        btn_random_color.setOnClickListener {
            val red = generateValueForColor()
            val green = generateValueForColor()
            val blue = generateValueForColor()

            v_color_result.setBackgroundColor(Color.rgb(red, green, blue))

            sb_red.progress = red
            sb_green.progress = green
            sb_blue.progress = blue

            addColorToList(red, green, blue)

            updateTextRedLabel(red)
            updateTextGreenLabel(green)
            updateTextBlueLabel(blue)
        }

        iv_list.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }
    }

    private fun addColorToList(red: Int, green: Int, blue: Int) {
        val colorObject = ColorObject(red, green, blue)
        colorList.add(colorObject)

        saveToDatabase(convertListIntoJson(colorList))
    }

    private fun convertListIntoJson(colorList: List<ColorObject>) : String {
        return Gson().toJson(Database(colorList))
    }

    private fun saveToDatabase(json: String) {
        val sharedPreferences = getSharedPreferences("cores", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("database", json).apply()
    }

    private fun updateTextRedLabel(red: Int) {
        tv_red.text = getString(R.string.tv_bar_red, red)
    }

    private fun updateTextGreenLabel(green: Int) {
        tv_green.text = getString(R.string.tv_bar_green, green)
    }

    private fun updateTextBlueLabel(blue: Int) {
        tv_blue.text = getString(R.string.tv_bar_blue, blue)
    }

    fun generateValueForColor() = random.nextInt(MAX_INT)

}
