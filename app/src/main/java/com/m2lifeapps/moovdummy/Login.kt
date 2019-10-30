package com.m2lifeapps.moovdummy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.widget.EditText
import com.github.vacxe.phonemask.PhoneMaskManager
import com.github.vacxe.phonemask.ValueListener
import kotlinx.android.synthetic.main.login1.*
import android.telephony.PhoneNumberUtils.formatNumber as formatNumber1

class Login : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login1)
        PhoneMaskManager()
            .withMask(" (###) ###-##-##")
            .withRegion("+90")
            .withValueListener(object : ValueListener {
                override fun onPhoneChanged(phone: String) {

                }
            })
            .bindTo((findViewById(R.id.etPhone) as EditText))
     }
}
