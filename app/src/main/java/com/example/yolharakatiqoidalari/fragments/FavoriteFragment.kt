package com.example.yolharakatiqoidalari.fragments

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.example.yolharakatiqoidalari.R
import com.example.yolharakatiqoidalari.adapters.ItemClick
import com.example.yolharakatiqoidalari.adapters.ShowRulesAdapter
import com.example.yolharakatiqoidalari.database.AppDataBase
import com.example.yolharakatiqoidalari.database.Rules
import com.example.yolharakatiqoidalari.databinding.*
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
class FavoriteFragment : Fragment(), ItemClick {

    private lateinit var editClickDialog: EditRuleDialogItemBinding
    private lateinit var context: FragmentActivity
    private lateinit var arrayAdapter: ArrayAdapter<String>
    private lateinit var selectedImagePathForEdit: String
    lateinit var binding: FragmentFavoriteBinding
    lateinit var appDataBase: AppDataBase
    lateinit var list: ArrayList<Rules>
    lateinit var rules: Rules
    private val tabTitles =
        arrayListOf("Ogohlantiruvchi", "Imtiyozli", "Ta'qiqlovchi", "Buyuruvchi")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = FragmentFavoriteBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context = requireActivity()
        appDataBase = AppDataBase.getInstance(context)
        loadData()
    }

    private fun loadData() {
        list = ArrayList()
        val mlist = appDataBase.Dao().getAllRules()
        mlist.forEach{
            if (it.favourite.equals("true")){
                list.add(it)
            }
        }
        binding.favoriteRv.adapter = ShowRulesAdapter(list, this)
    }

    override fun itemClick(rules: Rules) {
        val customDialog = AlertDialog.Builder(context).create()
        val itemClickDialog = ShowInfoAboutRuleDialogItemBinding.inflate(layoutInflater)
        customDialog.setView(itemClickDialog.root)
        customDialog.setCancelable(false)
        customDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        Glide.with(context).load(rules.imagePath).into(itemClickDialog.imageOfRule)
        itemClickDialog.ruleName.text = rules.ruleName
        itemClickDialog.aboutRule.text = rules.aboutRule

        itemClickDialog.closeDialog.setOnClickListener {
            customDialog.dismiss()
        }

        customDialog.show()
    }

    override fun editClick(rules: Rules) {
        val customDialog = AlertDialog.Builder(context).create()
        editClickDialog = EditRuleDialogItemBinding.inflate(layoutInflater)
        customDialog.setView(editClickDialog.root)
        customDialog.setCancelable(false)
        customDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        Glide.with(context).load(rules.imagePath).into(editClickDialog.imageOfRule)
        editClickDialog.TIENameOfRule.setText(rules.ruleName)
        editClickDialog.TIEAboutOfRule.setText(rules.aboutRule)
        editClickDialog.ACTVTypeOfRule.setText(rules.typeOfRule)

        // Spinner Adapter
        arrayAdapter = ArrayAdapter(context, R.layout.spinner_item, tabTitles)
        editClickDialog.ACTVTypeOfRule.setAdapter(arrayAdapter)

        editClickDialog.closeDialog.setOnClickListener {
            customDialog.dismiss()
        }

        editClickDialog.imageOfRule.setOnClickListener {
            pickImageFromGalleryForEdit()
        }

        editClickDialog.save.setOnClickListener {
            rules.ruleName = editClickDialog.TIENameOfRule.text.toString().trim()
            rules.aboutRule = editClickDialog.TIEAboutOfRule.text.toString().trim()
            rules.typeOfRule = editClickDialog.ACTVTypeOfRule.text.toString().trim()
            rules.imagePath = selectedImagePathForEdit

            appDataBase.Dao().edit(rules)
            loadData()
            Toast.makeText(context, "Saqlandi", Toast.LENGTH_SHORT).show()
            customDialog.dismiss()
        }
        customDialog.show()
    }

    override fun deleteClick(rules: Rules) {
        val customDialog = AlertDialog.Builder(context).create()
        val deleteClickDialog = DeleteRuleDialogItemBinding.inflate(layoutInflater)
        customDialog.setView(deleteClickDialog.root)
        customDialog.setCancelable(false)
        customDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        Glide.with(context).load(rules.imagePath).into(deleteClickDialog.imageOfRule)
        deleteClickDialog.ruleName.text = rules.ruleName

        deleteClickDialog.no.setOnClickListener {
            customDialog.dismiss()
        }

        deleteClickDialog.yes.setOnClickListener {
            appDataBase.Dao().delete(rules)
            loadData()
            Toast.makeText(context, "O'chirildi", Toast.LENGTH_SHORT).show()
            customDialog.dismiss()
        }

        customDialog.show()
    }

    override fun likeClick(rules: Rules) {
        appDataBase.Dao().edit(rules)
        loadData()
    }

    private fun pickImageFromGalleryForEdit() {
        getImageContentForEdit.launch("image/*")
    }

    private val getImageContentForEdit =
        registerForActivityResult(ActivityResultContracts.GetContent()) {
            it ?: return@registerForActivityResult
            editClickDialog.imageOfRule.setImageURI(it)
            val inputStream = context.contentResolver?.openInputStream(it)
            val file = File(context.filesDir, "${LocalDateTime.now()}.jpg")
            val fileOutputStream = FileOutputStream(file)
            inputStream?.copyTo(fileOutputStream)
            inputStream?.close()
            fileOutputStream.close()
            selectedImagePathForEdit = file.absolutePath
        }
}

