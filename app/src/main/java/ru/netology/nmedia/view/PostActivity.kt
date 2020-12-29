package ru.netology.nmedia.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.PostAcitivtyBinding

class PostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = PostAcitivtyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setContent(binding.postTextEditText)

        binding.postTextEditText.requestFocus()
        binding.addPostFab.setOnClickListener {
            val intent = Intent()
            if (binding.postTextEditText.text.isNullOrBlank()) {
                setResult(Activity.RESULT_CANCELED, intent)
            } else {
                intent.putExtra(Intent.EXTRA_TEXT, binding.postTextEditText.text.toString())
                setResult(Activity.RESULT_OK, intent)
            }
            finish()
        }
    }

    private fun setContent(contentEditText: EditText) {
        contentEditText.setText(intent.getStringExtra(Intent.EXTRA_TEXT))
    }


}