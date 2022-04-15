package com.coder.orm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.coder.orm.databinding.ActivityEditBinding
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class EditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditBinding

    var mDb: StudentDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mDb = StudentDatabase.getInstance(this)

        val objectStudent = intent.getParcelableExtra<Student>("student")

        binding.etNamaStudent.setText(objectStudent?.nama)
        binding.etEmailStudent.setText(objectStudent?.email)

        btnSave.setOnClickListener {
            objectStudent?.nama = etNamaStudent.text.toString()
            objectStudent?.email = etEmailStudent.text.toString()

            GlobalScope.async {
                val result = mDb?.studentDao()?.updateStudent(objectStudent!!)

                runOnUiThread {
                    if(result!=0){
                        Toast.makeText(this@EditActivity,"Sukses mengubah ${objectStudent?.nama}", Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(this@EditActivity,"Gagal mengubah ${objectStudent?.nama}", Toast.LENGTH_LONG).show()
                    }

                    finish()
                }
            }
        }
    }
}