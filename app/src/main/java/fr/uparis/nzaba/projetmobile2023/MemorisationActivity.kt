package fr.uparis.nzaba.projetmobile2023

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fr.uparis.nzaba.projetmobile2023.data.Sujet
import fr.uparis.nzaba.projetmobile2023.data.Question
import fr.uparis.nzaba.projetmobile2023.model.GererQuestionViewModel
import fr.uparis.nzaba.projetmobile2023.ui.theme.Projetmobile2023Theme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList


class MemorisationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Projetmobile2023Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ScaffoldMemorisation()
                }
            }
        }
    }
}

@Composable
fun StartingPage(subjectList: List<Sujet>,
                 selectedSubject: Sujet,
                 selectSubjectToStudy : (Sujet) -> Unit,
                 navigateTo : () -> Unit
                 ){
    Box(contentAlignment = Alignment.Center){
        Column {
            SubjectsDropDownMenu(
                subjectList = subjectList,
                selectedSubject = selectedSubject,
                alignement = Alignment.Center,
                changeSelectedSubject = selectSubjectToStudy
            )
            Spacer(Modifier.padding(10.dp))
            Button(onClick = navigateTo){
                Text("Commencer l'entraînement !")
            }

        }

    }

}
fun QcmQuestion(){

}
@Composable
fun SimpleQuestion(
    questionText: String,
    answer: String,
    changeAnswer: (String) -> Unit,
    number: Int,
    nextQuestion: () -> Unit
){
    LazyColumn {
        item {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
                Text(
                    "Question n°$number",
                    modifier = Modifier.padding(10.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
        item {
            Box(contentAlignment = Alignment.Center) {
                Column {

                    Card(
                        modifier = Modifier
                            .padding(5.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(1.dp),
                        border = BorderStroke(1.dp, Color.Black)

                    ) {
                        Text(
                            questionText,
                            modifier = Modifier.padding(10.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                    Spacer(modifier = Modifier.padding(10.dp))
                    Card(
                        modifier = Modifier
                            .padding(5.dp)
                            .background(Color.Transparent),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(2.dp, Color.Black)
                    ) {
                        TextField(
                            value = answer,
                            onValueChange = changeAnswer,
                            modifier = Modifier.size(1000.dp, 300.dp),
                            placeholder = { Text("Veuillez écrire votre réponse !") }
                        )
                    }

                }

            }
        }
        item {
            Spacer(modifier = Modifier.padding(5.dp))
            Button(onClick = nextQuestion) {
                Text(text = "Prochaine question")

            }
        }
    }

}
@Composable
fun QuestionDisplay(question: Question,
                    answer: String,
                    changeAnswer : (String) -> Unit,
                    number: Int,
                    nextQuestion: () -> Unit){

    if(question.qcm == 0) SimpleQuestion(
        questionText = question.texte,
        answer = answer,
        changeAnswer = changeAnswer,
        number,
        nextQuestion)



}

@SuppressLint("UnrememberedMutableState")
@Composable
fun ScaffoldMemorisation( model: GererQuestionViewModel = viewModel()){
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }

    var selectedSubject by remember { mutableStateOf(Sujet(-1, "")) }
    var questions: MutableList<Question>

    var answer by remember {
        mutableStateOf("")
    }
    var index by remember {
        mutableStateOf(0)
    }

    var questionRetrieve by remember {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {},
        bottomBar = {},
        snackbarHost = { SnackbarHost(snackbarHostState) }) {
            paddingV ->
        NavHost(
            navController = navController,
            startDestination = "start",
            modifier = Modifier.padding(paddingV)
        ){
            composable("start"){
                StartingPage(
                    subjectList = model.sujetsFlow.collectAsState(listOf()).value,
                    selectedSubject = selectedSubject,
                    selectSubjectToStudy =
                    {
                        selectedSubject = it
                        (model.updateSubjectID(selectedSubject.idSujet))
                        (model.reloadQuestions())
                        questionRetrieve = true

                    },
                    navigateTo = { navController.navigate("questions"){popUpTo("start")}})
            }
            composable("questions") {
                    questions = model.questionList

                    if(questions.size > 1 && index <= questions.size - 1){
                        QuestionDisplay(
                            question = questions[index],
                            answer = answer,
                            changeAnswer = {answer =it},
                            number = index + 1,
                            nextQuestion = {
                                if(questions[index].rep.lowercase() == answer.lowercase()){
                                    println("Bon")
                                }else{
                                    println("Faux")
                                }
                                answer = ""
                                index++}
                            )
                    }


                }


            }
        }
    if(questionRetrieve){
        model.setQuestionList()
        }
    }



@Composable
fun GetQuestions(model:GererQuestionViewModel,
                 id : Int,
                 backToFalse : (List<Question>) -> Unit) {



    val questions = model.questionFlow.collectAsState(listOf()).value.filter {
        val day =
            SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).parse(it.nextDate)
        val today = Calendar.getInstance().time
        println("Boolean :" + (day <= today))
        day <= today
    }

    backToFalse(questions.shuffled())

}
@Composable
fun Greeting2(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun SimpleQuestionPreview() {
    Projetmobile2023Theme {
        SimpleQuestion(
            "Android",
            "",
            {},
            1,
            {}
            )
    }
}