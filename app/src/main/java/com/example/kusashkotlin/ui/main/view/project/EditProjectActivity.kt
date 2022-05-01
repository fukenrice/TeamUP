package com.example.kusashkotlin.ui.main.view.project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AbsListView
import com.example.kusashkotlin.R
import kotlinx.android.synthetic.main.activity_edit_project.*

class EditProjectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_project)
        projectEditBelbinListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL)
    }
}