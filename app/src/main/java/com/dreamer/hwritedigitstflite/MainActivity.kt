package com.dreamer.hwritedigitstflite


import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import butterknife.BindView
import butterknife.ButterKnife.bind

import butterknife.OnClick
import com.dreamer.hwritedigitstflite.R.id.btn_detect
import com.nex3z.fingerpaintview.FingerPaintView
import java.io.IOException


class MainActivity : AppCompatActivity() {
    //целимся в нужные объекты на Main_активити
    @BindView(R.id.fpv_paint)
    lateinit var mFpvPaint: FingerPaintView

    @BindView(R.id.tv_prediction)
    lateinit var mTvPrediction: TextView

    @BindView(R.id.tv_probability)
    lateinit var mTvProbability: TextView

    @BindView(R.id.tv_timecost)
    lateinit var mTvTimeCost: TextView
    private var mClassifier: Classifier? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bind(this)
        init()
    }

    @OnClick(btn_detect)
    fun onDetectClick() {
        if (mClassifier == null) {
            Log.e(
                LOG_TAG,
                "onDetectClick(): Classifier is not initialized"
            )
            return
        } else if (mFpvPaint.isEmpty) {
            Toast.makeText(this, R.string.please_write_a_digit, Toast.LENGTH_SHORT).show()
            return
        }
        val image = mFpvPaint.exportToBitmap(
            Classifier.IMG_WIDTH, Classifier.IMG_HEIGHT
        )
        val result = mClassifier!!.classify(image)
        renderResult(result)
    }

    @OnClick(R.id.btn_clear)
    fun onClearClick() {
        mFpvPaint.clear()
        mTvPrediction.setText(R.string.empty)
        mTvProbability.setText(R.string.empty)
        mTvTimeCost.setText(R.string.empty)
    }

    private fun init() {
        try {
            mClassifier = Classifier(this)
        } catch (e: IOException) {
            Toast.makeText(this, R.string.failed_to_create_classifier, Toast.LENGTH_LONG).show()
            Log.e(
                LOG_TAG,
                "init(): Failed to create Classifier",
                e
            )
        }
    }

    private fun renderResult(result: Result) {
        mTvPrediction.text = result.number.toString()
        mTvProbability.text = result.probability.toString()
        mTvTimeCost.text = String.format(
            getString(R.string.timecost_value),
            result.timeCost
        )
    }

    companion object {
        private val LOG_TAG = MainActivity::class.java.simpleName
    }
}


