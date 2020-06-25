package com.desencrypt.textcryptor

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        btnHex.setOnClickListener {
            if (edtPlainText.text!!.trim().isNotBlank() && !isHex(edtPlainText.text!!.toString().trim())){
                val hexString = plainTextTo64BitsHex(edtPlainText.text.toString())
                edtPlainText.setText(hexString.toUpperCase())
            }else{
                Toast.makeText(this, "Plain text can't be blank or Hex value",Toast.LENGTH_SHORT).show()
            }

            if (edtKey.text!!.trim().length==8 && edtKey.text!!.trim().isNotBlank() && !isHex(edtKey.text!!.toString().trim())){
                val keyHexString = plainTextTo16BitsHex(edtKey.text.toString())
                edtKey.setText(keyHexString.toUpperCase())
            }else{
                Toast.makeText(this, "Key must be 8 and not be Hex value", Toast.LENGTH_SHORT).show()
            }
        }

        btnEncode.setOnClickListener {
            val plainTextInHex = edtPlainText.text!!.trim().toString()
            val keyInHex = edtKey.text!!.trim().toString()
            if (isHex(plainTextInHex) && isHex(keyInHex)){
                edtResult.setText(DES.encrypt(keyInHex,plainTextInHex))
            }else{
                Toast.makeText(this, "Input Data must be Hex value.",Toast.LENGTH_LONG).show()
            }
        }

        btnDecode.setOnClickListener {
            val encodedValue = edtResult.text!!.trim().toString()
            val keyInHex = edtKey.text!!.trim().toString()
            if (isHex(encodedValue) && isHex(keyInHex)){
                edtResult.setText(DES.decrypt(keyInHex, encodedValue))
            }else{
                Toast.makeText(this, "Encoded Value and Key must be Hex or must not be Empty",Toast.LENGTH_LONG).show()
            }
        }

        btnPlainText.setOnClickListener {
            val decodedValue = edtResult.text!!.trim().toString()
            if (isHex(decodedValue)){
                edtResult.setText(HexToString(decodedValue))
            }else{
                Toast.makeText(this, "Decoded Value must be Hex or must not be Empty",Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun Encode(inputData: String, key: String): String{
        val pl = inputData.trim()
        val x = pl.length
        var Res = ""

       // val encode= DesEncode(this)
       // encode.big_key = encode.hex_to_bin(key.toUpperCase())

        val size = x/16
        var ind = 0
        for (i in 0 until size) {
            var h = ""
            var j = 0
            while (j < 16) {
                if (ind < x) {
                    if (pl.toCharArray()[ind] != ' ') {
                        h += pl.toCharArray()[ind]
                    } else {
                        j--
                    }
                    ind++
                } else {
                    h += "A"
                }
                j++
            }
            println("h >>>$h")
            //encode.plain = encode.hex_to_bin(h.toUpperCase())
            //encode.hex_plain = h.toUpperCase()
            //encode.encode()
            //Res = encode.bin_to_hex(encode.cipher!!)!!
            Res = DES.encrypt(key, h)
        }
        return Res
    }


    private fun isHex(str: String): Boolean{
        return str.trim().matches("[0-9A-Fa-f]+".toRegex());
    }

    private fun plainTextTo64BitsHex(plainText: String): String {
        var textInputInHex:String = StringToHex(plainText.toByteArray())

        //According to the Algorithm Explanation, Add Cariage Return '0D' and Line feed '0A'
        textInputInHex+="0D0A"

        //Check the hexadecimal length and make to be multiple of 16. If require any bit, add '0'.
        val textInputInHexLength = textInputInHex.length
        val requireExtraLength = 16 - (textInputInHexLength%16)
        if (requireExtraLength != 0 && requireExtraLength !=16){
            for (i in 0 until requireExtraLength) textInputInHex += "0"
        }
        return textInputInHex
    }

    private fun plainTextTo16BitsHex(plainText: String): String{
        var textInputKeyInHex = StringToHex(plainText.toByteArray())
        var key = ""

        //According to the DES Algorithm, make the key to be 16 Hexadecimals
        if (textInputKeyInHex.length>16){
            key +=textInputKeyInHex.substring(0, 15)
        }else if (textInputKeyInHex.length<16){
            val requireExtraLength = 16 - textInputKeyInHex.length
            for (i in 0 until requireExtraLength) textInputKeyInHex+=0
            key=textInputKeyInHex
        }else{
            key = textInputKeyInHex
        }
        return key
    }

    private fun StringToHex(ba: ByteArray): String {
        val stringBuilder = StringBuilder();
        for (i in 0..(ba.size-1)) stringBuilder.append(String.format("%x", ba[i]))
        return stringBuilder.toString()
    }

    private fun HexToString(hex: String): String {
        val stringBuilder = StringBuilder()
        var i = 0
        while (i < hex.length) {
            stringBuilder.append(hex.substring(i, i + 2).toInt(16).toChar())
            i += 2
        }
        return stringBuilder.toString()
    }



}
