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
    var error = mutableStateOf(false)
    var selectedQuestion = mutableStateListOf<Question>()
    var idsujet = mutableStateOf(2)
    var sujetsFlow = dao.loadSujet()
    var questionFlow = dao.loadQuestion(idsujet.value)
    var questionList = listOf<Question>()


    fun updateSubjectID(updated : Int){
        idsujet.value = updated
    }
    fun addQuestion() {

        viewModelScope.launch(Dispatchers.IO) {
            val res = async {
                dao.insertQuestion(
                    Question(
                        qcm = 0,
                        texte = questionField.value,
                        rep = repField.value,
                        statut = 0,
                        nextDate = "",
                        idSujet = idsujet.value
                    )
                )
            }
            error.value = (res.await() == -1L)

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