package com.m2lifeapps.moovdummy


import android.app.AppComponentFactory
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Camera
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import io.card.payment.CardIOActivity
import io.card.payment.CreditCard
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.view.Window.FEATURE_NO_TITLE
import androidx.appcompat.app.AppCompatActivity


/**
 * A simple [Fragment] subclass.
 */
class CardReader : AppCompatActivity() {
    val REQUEST_AUTOTEST = 100
    val REQUEST_SCAN = 11
    private lateinit var btnCamera: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_card_reader)
        btnCamera = findViewById(R.id.btnCamera)

        btnCamera.setOnClickListener{
            Reader()
        }
     }
/*
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }*/



    fun Reader() {
        val intent = Intent(this@CardReader, CardIOActivity::class.java)
            .putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true)
            .putExtra(CardIOActivity.EXTRA_SCAN_EXPIRY, true)
            .putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, true)
            .putExtra(CardIOActivity.EXTRA_REQUIRE_CARDHOLDER_NAME, true)
            .putExtra(CardIOActivity.EXTRA_LANGUAGE_OR_LOCALE, "en")
            .putExtra(CardIOActivity.EXTRA_GUIDE_COLOR, Color.GREEN)
            .putExtra(CardIOActivity.EXTRA_RETURN_CARD_IMAGE, true)
        startActivityForResult(intent, REQUEST_SCAN)

    }


    /*private lateinit var listener: SendData
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is SendData) {
            listener = context
        } else {
            throw ClassCastException(
                context.toString() + " must implement OnDogSelected."
            ) as Throwable
        }

    }*/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((requestCode == REQUEST_SCAN || requestCode == REQUEST_AUTOTEST) && data != null && data.hasExtra(
                CardIOActivity.EXTRA_SCAN_RESULT
            )
        ) {
            var resultDisplayStr: String
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                val scanResult =
                    data.getParcelableExtra<CreditCard>(CardIOActivity.EXTRA_SCAN_RESULT)

                var month: String = scanResult.expiryMonth.toString()
                var year: String = scanResult.expiryYear.toString()

                var expiredDate = month + "/" + year

                val  intent = Intent(this,CreditCard::class.java)
                intent.putExtra("holderName",scanResult.cardholderName)
                intent.putExtra("cardNumber",scanResult.cardNumber)
                intent.putExtra("CVV",scanResult.cvv)
                intent.putExtra("expiredDate",expiredDate)
                startActivity(intent)

                /*listener.CardDetail(
                    scanResult.cardholderName,
                    expiredDate,
                    scanResult.cvv,
                    scanResult.cardNumber
                )
                dialog!!.dismiss()*/

            } else {
                resultDisplayStr = "Scan was canceled."
            }
        }
    }


}
