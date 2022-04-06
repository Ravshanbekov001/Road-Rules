package com.example.yolharakatiqoidalari.fragments

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.example.yolharakatiqoidalari.R
import com.example.yolharakatiqoidalari.adapters.ItemClick
import com.example.yolharakatiqoidalari.adapters.ShowRulesAdapter
import com.example.yolharakatiqoidalari.adapters.ViewPagerAdapter
import com.example.yolharakatiqoidalari.database.AppDataBase
import com.example.yolharakatiqoidalari.database.Rules
import com.example.yolharakatiqoidalari.databinding.*
import com.google.android.material.tabs.TabLayoutMediator
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
class HomeFragment : Fragment(), ItemClick {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var bindingDialog: AddRuleDialogItemBinding
    private lateinit var editClickDialog: EditRuleDialogItemBinding
    private lateinit var context: FragmentActivity
    private lateinit var arrayAdapter: ArrayAdapter<String>
    private lateinit var selectedImagePath: String
    private lateinit var selectedImagePathForEdit: String
    lateinit var appDataBase: AppDataBase
    lateinit var list: ArrayList<Rules>
    lateinit var list2: ArrayList<Rules>
    lateinit var list3: ArrayList<Rules>
    lateinit var list4: ArrayList<Rules>
    private val tabTitles =
        arrayListOf("Ogohlantiruvchi", "Imtiyozli", "Ta'qiqlovchi", "Buyuruvchi")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        selectedImagePath = ""
        selectedImagePathForEdit = ""
        context = requireActivity()
        appDataBase = AppDataBase.getInstance(context)
        loadData()

        binding.addRule.setOnClickListener {
            addRule()
        }
    }

    private fun loadData() {
        list = ArrayList()
        list2 = ArrayList()
        list3 = ArrayList()
        list4 = ArrayList()
        val mlist = appDataBase.Dao().getAllRules()

        mlist.forEach {
            when (it.typeOfRule) {
                "Ogohlantiruvchi" -> list.add(it)
                "Imtiyozli" -> list2.add(it)
                "Ta'qiqlovchi" -> list3.add(it)
                "Buyuruvchi" -> list4.add(it)
            }
        }

        binding.viewPager.adapter = ViewPagerAdapter(list, list2, list3, list4, this)

        TabLayoutMediator(binding.tabLayout, binding.viewPager)
        { tab, position ->
            tab.text = tabTitles[position]
        }.attach()

        for (i in 0 until binding.tabLayout.tabCount) {
            val textView = LayoutInflater.from(requireContext())
                .inflate(R.layout.tab_item, null, false) as TextView
            binding.tabLayout.getTabAt(i)?.customView = textView
        }
    }

    private fun loadSpinnerData(bindingDialog: AddRuleDialogItemBinding) {
        arrayAdapter = ArrayAdapter(context, R.layout.spinner_item, tabTitles)
        bindingDialog.ACTVTypeOfRule.setAdapter(arrayAdapter)
    }

    private fun addRule() {
        val customDialog = AlertDialog.Builder(context).create()
        bindingDialog = AddRuleDialogItemBinding.inflate(layoutInflater)
        customDialog.setView(bindingDialog.root)
        customDialog.setCancelable(false)
        customDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        closeDialog(customDialog, bindingDialog)
        save(customDialog, bindingDialog)
        addRuleImage(bindingDialog)
        loadSpinnerData(bindingDialog)

        customDialog.show()
    }

    private fun closeDialog(customDialog: AlertDialog, bindingDialog: AddRuleDialogItemBinding) {
        bindingDialog.closeDialog.setOnClickListener {
            customDialog.dismiss()
        }
    }

    private fun save(customDialog: AlertDialog, bindingDialog: AddRuleDialogItemBinding) {
        bindingDialog.save.setOnClickListener {
            if (
                bindingDialog.TIENameOfRule.text!!.isNotEmpty() &&
                bindingDialog.TIEAboutOfRule.text!!.isNotEmpty() &&
                bindingDialog.ACTVTypeOfRule.text.isNotEmpty()
            ) {
                val ruleName = bindingDialog.TIENameOfRule.text.toString().trim()
                val aboutRule = bindingDialog.TIEAboutOfRule.text.toString().trim()
                val typeOfRule = bindingDialog.ACTVTypeOfRule.text.toString().trim()

                val rules = Rules(ruleName, aboutRule, typeOfRule, selectedImagePath)
                appDataBase.Dao().add(rules)
                loadData()
                Toast.makeText(context, "Saqlandi", Toast.LENGTH_SHORT).show()
                customDialog.dismiss()
            } else {
                Toast.makeText(context, "Berilgan maydonlarni to'ldiring", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun addRuleImage(bindingDialog: AddRuleDialogItemBinding) {
        bindingDialog.imageOfRule.setOnClickListener {
            pickImageFromGallery()
        }
    }

    private fun pickImageFromGallery() {
        getImageContent.launch("image/*")
    }

    private fun pickImageFromGalleryForEdit() {
        getImageContentForEdit.launch("image/*")
    }


    private val getImageContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) {
            it ?: return@registerForActivityResult
            bindingDialog.imageOfRule.setImageURI(it)
            val inputStream = context.contentResolver?.openInputStream(it)
            val file = File(context.filesDir, "${LocalDateTime.now()}.jpg")
            val fileOutputStream = FileOutputStream(file)
            inputStream?.copyTo(fileOutputStream)
            inputStream?.close()
            fileOutputStream.close()
            selectedImagePath = file.absolutePath
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
            if (selectedImagePathForEdit == "") {
                rules.imagePath = rules.imagePath
            } else {
                rules.imagePath = selectedImagePathForEdit
            }

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
    }
}


// Old method
//    private fun pickImageFromGallery() {
//        startActivityForResult(
//            Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
//                addCategory(Intent.CATEGORY_OPENABLE)
//                type = "image/*"
//            },
//            1
//        )
//}

// Old method
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
//            val uri = data?.data ?: return
//            bindingDialog.imageOfRule.setImageURI(uri)
//            val inputStream = context.contentResolver?.openInputStream(uri)
//            val file = File(context.filesDir, "image.jpg")
//            val fileOutputStream = FileOutputStream(file)
//            inputStream?.copyTo(fileOutputStream)
//            inputStream?.close()
//            fileOutputStream.close()
//            val absolutePath = file.absolutePath
//            Toast.makeText(context, absolutePath, Toast.LENGTH_SHORT).show()
//        }
//    }
