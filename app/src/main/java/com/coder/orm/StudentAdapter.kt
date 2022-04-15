package com.coder.orm

import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.coder.orm.databinding.StudentItemBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class StudentAdapter(val listStudent : List<Student>) : RecyclerView.Adapter<StudentAdapter.ViewHolder>() {

    companion object {
        val STUDENT = "student"
    }

    class ViewHolder(val binding: StudentItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = StudentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listStudent.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            tvID.text = listStudent[position].id.toString()
            tvNama.text = listStudent[position].nama
            tvEmail.text = listStudent[position].email

            ivEdit.setOnClickListener {
                val intentKeEditActivity = Intent(it.context, EditActivity::class.java)

                intentKeEditActivity.putExtra(STUDENT, listStudent[position])
                it.context.startActivity(intentKeEditActivity)
            }

            ivDelete.setOnClickListener {
                AlertDialog.Builder(it.context).setPositiveButton("Ya") { p0, p1 ->
                    val mDb = StudentDatabase.getInstance(holder.itemView.context)

                    GlobalScope.async {
                        val result = mDb?.studentDao()?.deleteStudent(listStudent[position])

                        (holder.itemView.context as MainActivity).runOnUiThread {
                            if (result != 0) {
                                Toast.makeText(it.context, "Data ${listStudent[position].nama} berhasil dihapus", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(it.context, "Data ${listStudent[position].nama} gagal dihapus", Toast.LENGTH_SHORT).show()
                            }
                        }

                        (holder.itemView.context as MainActivity).fetchData()
                    }
                }.setNegativeButton("Tidak") { p0, p1 ->
                    p0.dismiss()
                }
                    .setMessage("Apakah Anda Yakin ingin menghapus data ${listStudent[position].nama}").setTitle("Konfirmasi Hapus").create().show()
            }
        }
    }
}