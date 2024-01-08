package fr.uparis.nzaba.projetmobile2023.model

import android.app.Application
import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.pow
import kotlin.math.sqrt

class GererQuestionViewModel (private val application: Application) : AndroidViewModel(application) {

    private val dao = (application as JeuxApplication).database.jeuxDao()

    var questionField = mutableStateOf("")
    var repField = mutableStateOf("")
    var qcmInt = mutableStateOf(0)
    var statutInt = mutableStateOf(0)
    var nextDate = mutableStateOf("")
    var subjectID = mutableStateOf(0)

    var choiceText = mutableStateOf("")
    var choiceList = mutableStateListOf<Choix>()

    var error = mutableStateOf(false)

    var selectedQuestion = mutableStateListOf<Question>()
    var selectedAnswers = mutableStateListOf<Choix>()

    var idsujet = mutableStateOf(2)
    var sujetsFlow = dao.loadSujet()
    var questionFlow = dao.loadQuestion(idsujet.value)
    var questionID = mutableStateOf(1)
    var choiceFlow = dao.loadChoixReponse(questionID.value)


    var questionList = mutableListOf<Question>()
    var answerList = mutableListOf<Choix>()


    fun retrieveChoices(idQuestion: Int){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                choiceFlow =  dao.loadChoixReponse(idQuestion)
            }catch (e: SQLiteConstraintException){
                println("Erreur DB !")
            }
        }
    }

    @Composable
    fun setQuestionList(){
        questionList = questionFlow.collectAsState(listOf()).value.filter {
            val day =
                SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).parse(it.nextDate)
            val today = Calendar.getInstance().time
            println("Boolean :" + (day <= today))
            day <= today
        }.shuffled().toMutableList()

    }

    fun updateSubjectID(updated : Int){
        idsujet.value = updated
    }

    fun updateSubjectIDCreation(updated : Int){
        subjectID.value = updated
    }

    fun addQuestionToSelected(question: Question){
        selectedQuestion.add(question)
    }
    fun removeQuestionFromSelected(question: Question){
        selectedQuestion.remove(question)
    }

    fun addToChoiceList(choice : Choix){
        choiceList.add(choice)
    }


    private fun changeQuestionStatus(question: Question,status : Int){

        var newStatus = when{
            status > 0 -> status
            else -> 1
        }
        val currDate = Calendar.getInstance()
        //2.0.pow(newStatus - 1.toDouble()).toInt()
        currDate.add(Calendar.DATE, 0)

        val date = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)
        val newDateStr = date.format(currDate.time)

        val updatedQuestion = question.copy(statut = newStatus, nextDate = newDateStr)
        addQuestion(updatedQuestion)
    }
    fun checkAnswer(idQuestion: Int,rep : String): Boolean{
        var bool = false
        val q = questionList.find {it.idQuestion == idQuestion}?.apply {
            var status = this.statut

            if (this.qcm == 0) {
                if (rep.lowercase().replace(" ", "") == this.rep.lowercase().replace(" ", "")) {
                    status++
                    bool = true
                }else{
                    status--
                }
            } else {
                val answers = answerList.filter { it.bon }
                if(selectedAnswers.containsAll(answers) && selectedAnswers.size == answers.size){
                    status++
                    bool = true
                }else{
                    status--

                }
            }
            changeQuestionStatus(this,status)
        }
        return bool
    }
    fun fillDBwithChoiceList() {
        var r = viewModelScope.launch {
            val listchoix = choiceList.toList()
            for (choice in listchoix) {
                try {
                    val res = async {dao.insertChoix(choice)}
                    error.value = (res.await()== -1L)

                    if(!error.value){
                        choiceList.remove(choice)
                    }
                } catch (e: SQLiteConstraintException) {
                    println("Erreur avec la base de donnée : fillDBwithChoiceList")
                }
            }
        }

    }
    fun createQuestionChoice(idQuestion: Int,rightChoice : Boolean){
        viewModelScope.launch(Dispatchers.IO){
            try {
                val res = dao.insertChoix(Choix(
                    texte = choiceText.value,
                    bon = rightChoice,
                    idQuestion = idQuestion))
            } catch (e: SQLiteConstraintException){

            }
        }
    }
    fun clearFields(){
        questionField.value =""
        repField.value = ""
        qcmInt.value = 0
        statutInt.value = 0
        nextDate.value = ""
        subjectID.value = 0
    }

    fun addQuestion(q : Question) {
        println(" idQuestion : " + q.idQuestion  )

        viewModelScope.launch(Dispatchers.IO) {

            try {
                val res = async {
                    dao.insertQuestion(q)
                }
                println(" res : " + res  )
                val result = res.await()
                error.value = (result == -1L)

                if (error.value) {
                    questionID.value = -1
                } else {
                    questionID.value = result.toInt()
                }


            } catch (e: SQLiteConstraintException) {

                println("Erreur !")

            }


        }
    }

    fun deleteSingleQuestion(idQuestion: Int){
        viewModelScope.launch(Dispatchers.IO){
            try{
                dao.deleteQuestion(idQuestion)
            }catch (e: SQLiteConstraintException){

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

    fun reloadQuestions()  {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                questionFlow = dao.loadQuestion(idsujet.value)
                println("SUCCESS")
            }
        } catch (e: SQLiteConstraintException) {
            println("FAILURE")
        }
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