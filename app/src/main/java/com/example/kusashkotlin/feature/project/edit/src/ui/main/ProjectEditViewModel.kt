package com.example.kusashkotlin.feature.project.edit.src.ui.main

import com.example.kusashkotlin.data.model.RoleModel
import com.example.kusashkotlin.data.model.SpecializationModel
import com.example.kusashkotlin.data.repo.PreferencesRepository
import com.example.kusashkotlin.feature.project.edit.src.mvi.MviViewModel
import com.example.kusashkotlin.feature.project.edit.src.ui.main.mvi.ProjectEditAction
import com.example.kusashkotlin.feature.project.edit.src.ui.main.mvi.ProjectEditFeatureFactory
import com.example.kusashkotlin.feature.project.edit.src.ui.main.mvi.ProjectEditState

class ProjectEditViewModel(factory: ProjectEditFeatureFactory) : MviViewModel<ProjectEditState, ProjectEditAction>(factory) {

    val allSpecializations: List<SpecializationModel> = PreferencesRepository().getAllSpecializations()!!

    var allBelbinRoles: List<RoleModel> = PreferencesRepository().getAllRoles()!!

    fun getBelbinListByIndex(belbinIndexes: List<Int>): MutableList<String> {
        var belbinRoles = MutableList<String>(0) { i -> "" }
        var belbinCnt = belbinIndexes.size
        for (i in 0 until belbinCnt) {
            for (j in allBelbinRoles.indices) {
                if (allBelbinRoles[j].id == belbinIndexes.get(i)) {
                    belbinRoles.add(allBelbinRoles[j].role)
                }
            }
        }
        return belbinRoles
    }

    fun getSpecializationsListByIndex(specializationIndexes: List<Int>): MutableList<String> {
        var specializations = MutableList<String>(0) { i -> "" }
        var specializationCnt = specializationIndexes.size
        for (i in 0 until specializationCnt) {
            for (j in allSpecializations.indices) {
                if (allSpecializations[j].id == specializationIndexes.get(i)) {
                    specializations.add(allSpecializations[j].name)
                }
            }
        }
        return specializations
    }



}