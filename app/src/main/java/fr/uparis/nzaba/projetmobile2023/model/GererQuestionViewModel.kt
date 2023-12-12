package fr.uparis.nzaba.projetmobile2023.model

import android.app.Application
import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import fr.uparis.nzaba.projetmobile2023.JeuxApplication
import fr.uparis.nzaba.projetmobile2023.data.Choix
import fr.uparis.nzaba.projetmobile2023.data.Sujet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import fr.uparis.nzaba.projetmobile2023.data.Question
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList

class GererQuestionViewModel (private val application: Application) : AndroidViewModel(application) {

    private val dao = (application as JeuxApplication).database.jeuxDao()

    var questionField = mutableStateOf("")
    var repField = mutableStateOf("")
    var qcmInt = mutableStateOf(0)
    var statutInt = mutableStateOf(0)
    var nextDate = mutableStateOf("")
    var subjectID = mutableStateOf(0)

    var choiceText = mutableStateOf("")
    var rightChoice = mutableStateOf(0)


    var error = mutableStateOf(false)
    var selectedQuestion = mutableStateListOf<Question>()
    var idsujet = mutableStateOf(2)
    var sujetsFlow = dao.loadSujet()
    var questionFlow = dao.loadQuestion(idsujet.value)
    var questionID = mutableStateOf(0)


    fun addStatutInt(statut : Int){
        statutInt.value = statut
    }
    fun addQcmInt(qcm : Int){
        qcmInt.value = qcm
    }
    fun addAnswer(answer : String){
        repField.value = answer
    }

    fun addQuestionText(question : String){
        questionField.value = question
    }

    fun updateSubjectID(updated : Int){
        idsujet.value = updated
    }

    fun updateSubjectIDCreation(updated : Int){
        subjectID.value = updated
    }

   // fun u

    fun createQuestionChoice(texte : String, bon : Int, idQuestion: Int){
        viewModelScope.launch(Dispatchers.IO){
            try {
                val res = dao.insertChoix(Choix(
                    texte = texte,
                    bon = bon,
                    idQuestion = idQuestion))
            } catch (e: SQLiteConstraintException){

            }
        }
    }
    fun addQuestion(){
        viewModelScope.launch(Dispatchers.IO) {
            println("AAAAAAAAAAAAAAAAAAAA\n")
            val q =  Question(
                qcm = qcmInt.value,
                texte = questionField.value,
                rep = repField.value,
                statut = statutInt.value,
                nextDate = "",
                idSujet = subjectID.value
            )
            val res = async {
                dao.insertQuestion(q)
            }

            error.value = (res.await() == -1L)

            if(error.value) {  }
            else{
                questionID.value = q.idQuestion
            }

        }
    }

    fun deleteQuestion() {
        for (question in selectedQuestion.toList()) {
            try {
                viewModelScope.launch(Dispatchers.IO) {
                    dao.deleteSelectedQuestion(question = question)
                    selectedQuestion.remove(question)

                }
            } catch (e: SQLiteConstraintException) {

            }
        }
    }

    fun reloadQuestions() : Flow<List<Question>> {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                questionFlow = dao.loadQuestion(idsujet.value)
                println("SUCCESS")
            }
        } catch (e: SQLiteConstraintException) {
            println("FAILURE")
        }
        return questionFlow
    }
}
/*
    fun retrieveAssociatedSubject(idSubject :Int ){
        try {
            viewModelScope.launch(Dispatchers.IO) {
                var subj = dao.selectSubject(idSubject)
                sujet.value = subj
            }
        } catch(e: SQLiteConstraintException){

        }

        if(questionFlow == null){
          //  questionFlow = Flow<List<Question>>()
        }


    }

}*/