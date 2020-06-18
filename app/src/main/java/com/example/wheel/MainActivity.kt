package com.example.wheel

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    var t_rpm: TextView? = null
    var image_rpm : ImageView? = null
    var btn_rpm : Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //TASK 1 = Rpm of the wheel
        t_rpm = findViewById(R.id.rpm_text) as TextView
        //TASK 1 = The image of a wheel
        image_rpm = findViewById(R.id.rpm_image) as ImageView
        //TASK 1 = A button that says “updates speed”
        btn_rpm = findViewById(R.id.rpm_btn) as Button
        btn_rpm!!.setOnClickListener {
            //TASK 2 = Disable the “update speed” button
            btn_rpm!!.isEnabled=false
            btn_rpm!!.alpha=0.5f
            //TASK 3 = Animate the wheel to spin at the “random” RPM
            val animation = AnimationUtils.loadAnimation(this, R.anim.rotation_animation)
            image_rpm!!.startAnimation(animation)
            //TASK 2 = Show a snackbar that says “FETCHING RPM”
            val snack = Snackbar.make(root_layout,"FETCHING RPM",Snackbar.LENGTH_INDEFINITE)
            val textView = snack.view.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
                textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER)
            else
                textView.setGravity(Gravity.CENTER_HORIZONTAL)
            snack.show()
            //TASK 2 = The application must make an API call to fetch the RPM
            getspeed()
        }
    }

    fun getspeed() {
        // instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)
        val url: String = "https://csrng.net/csrng/csrng.php?min=0&max=100"

        // request a string response from the provided URL.
        val stringReq = StringRequest(Request.Method.GET, url,
            Response.Listener<String> { response ->

                // get array value
                var result = response.toString()
                val json: JSONArray = JSONArray(result)
                for (i in 0 until json.length()) {
                    var jsobject: JSONObject = json.getJSONObject(i)
                    //TASK 3 = Get the “random” field from the response
                    val speeds: String = jsobject.getString("random")
                    //update rpm text
                    rpm_text.setText(speeds + " RPM")
                    //stop animation
                    image_rpm!!.clearAnimation()
                    //TASK 3 = Show a snackbar that says “SPEED UPDATED”
                    val snack = Snackbar.make(root_layout,"SPEED UPDATED",Snackbar.LENGTH_INDEFINITE)
                    val textView = snack.view.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
                        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER)
                    else
                        textView.setGravity(Gravity.CENTER_HORIZONTAL)
                    snack.show()
                    //TASK 3 = Enable the “update speed” button
                    btn_rpm!!.isEnabled = true
                    btn_rpm!!.isClickable = true
                    btn_rpm!!.alpha = 1f
                }

            },
            Response.ErrorListener {
                //TASK 3 = Show a snackbar that says “UNABLE TO UPDATE SPEED”
                val snack = Snackbar.make(root_layout,"UNABLE TO UPDATE SPEED",Snackbar.LENGTH_INDEFINITE)
                val textView = snack.view.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
                    textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER)
                else
                    textView.setGravity(Gravity.CENTER_HORIZONTAL)
                snack.show()
                // stop animation
                image_rpm!!.clearAnimation()
                //TASK 3 = Enable the “update speed” button
                btn_rpm!!.isEnabled = true
                btn_rpm!!.isClickable = true
                btn_rpm!!.alpha = 1f
            })
        queue.add(stringReq)
    }
}
