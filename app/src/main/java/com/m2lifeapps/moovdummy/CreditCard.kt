package com.m2lifeapps.moovdummy

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.github.vacxe.phonemask.PhoneMaskManager
import com.github.vacxe.phonemask.ValueListener
import kotlinx.android.synthetic.main.creditcard.*

class CreditCard : AppCompatActivity(), SendData {

    private var cardOwner: String = ""
    private var expariedDate: String = ""
    private var cvv: String = ""
    private var cardNumber: String = ""

    override fun CardDetail(
        cardOwner: String,
        expariedDate: String,
        cvc: String,
        cardNumber: String
    ) {

        this.cardNumber = cardNumber
        this.cardOwner = cardOwner
        this.cvv = cvc
        this.expariedDate = expariedDate
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.creditcard)





        btnDevam.setOnClickListener {

        }

        PhoneMaskManager()
                .withMask("#### #### #### ####")
                .bindTo((findViewById(R.id.etCardNumber) as EditText))

            PhoneMaskManager()
                .withMask("##/##")
                .bindTo((findViewById(R.id.etExpirationDate) as EditText))

            PhoneMaskManager()
                .withMask("###")
                .bindTo((findViewById(R.id.etCVC) as EditText))

            etCVC.setText(cvv)
            etCardNumber.setText(cardNumber)
            etCardOwner.setText(cardOwner)
            etExpirationDate.setText(expariedDate)



    }
}
