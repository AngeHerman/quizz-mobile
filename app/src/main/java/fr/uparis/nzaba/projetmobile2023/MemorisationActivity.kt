package fr.uparis.nzaba.projetmobile2023

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import fr.uparis.nzaba.projetmobile2023.data.Choix
import fr.uparis.nzaba.projetmobile2023.model.ReglerNotifViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.timer


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

        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Veuillez sélectionner un sujet !")
            Spacer(Modifier.height(15.dp))
            SubjectsDropDownMenu(
                subjectList = subjectList,
                selectedSubject = selectedSubject,
                alignement = Alignment.Center,
                changeSelectedSubject = selectSubjectToStudy
            )
            Spacer(Modifier.height(25.dp))
            Button(onClick = navigateTo){
                Text("Commencer l'entraînement !")
            }


        }



}
@SuppressLint("UnrememberedMutableState")
@Composable
fun QcmQuestion(
    answer : Choix,
    selectedAnswers: List<Choix>,
    addMethod : (Choix) -> Unit,
    deleteFromList : (Choix) -> Unit

    ){
        Card(
            modifier = Modifier
                .padding(horizontal = 50.dp, vertical = 15.dp)
                .background(Color.Transparent)
                .fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(2.dp, Color.Black)
        ) {
            Row {
                Text(text = answer.texte, modifier = Modifier.padding(15.dp))
                Column(horizontalAlignment = Alignment.End) {
                    Row{
                        SelectElementCheckBox(
                            element = answer,
                            selectedElements = selectedAnswers,
                            addMethod = addMethod,
                            deleteFromList = deleteFromList
                        )
                    }

                }


            }
        }
    }




@Composable
fun SimpleQuestion(
    answer: String,
    changeAnswer: (String) -> Unit
){
        Box(contentAlignment = Alignment.Center) {
            Column {
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
@Composable
fun QuestionDisplay(question: Question,
                    answer: String,
                    changeAnswer : (String) -> Unit,
                    time : Int,
                    number: Int,
                    nextQuestion: () -> Unit,
                    passQuestion: () -> Unit,
                    answerList: List<Choix>,
                    selectedAnswers: List<Choix>,
                    addMethod : (Choix) -> Unit,
                    deleteFromList : (Choix) -> Unit){


    LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
        item {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
                Text(
                    "Question n°$number",
                    modifier = Modifier.padding(10.dp),
                    textAlign = TextAlign.Center
                )
            }

        }
        item{
            Spacer(modifier = Modifier.padding(10.dp))
            Card(
                modifier = Modifier
                    .padding(15.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(1.dp),
                border = BorderStroke(1.dp, Color.Black)

            ) {
                Text(
                    question.texte,
                    modifier = Modifier.padding(10.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
        item {
            if (question.qcm == 0) SimpleQuestion(
                answer = answer,
                changeAnswer = changeAnswer
            )
        }
        if (question.qcm == 1 && answerList.isNotEmpty()) {
            itemsIndexed(answerList) {
                index,item ->
                QcmQuestion(
                    answer = item,
                    selectedAnswers = selectedAnswers,
                    addMethod = addMethod,
                    deleteFromList = deleteFromList
                )
            }
        }

        item {
            Row {
                Spacer(modifier = Modifier.width(200.dp))
                Button(modifier = Modifier.padding(10.dp), onClick = nextQuestion) {
                    Text(text = "Prochaine question")
                }
            }

        }
        item {
            BasicCountdownTimer(passQuestion = passQuestion,time = time)
        }
    }

}

@Composable
fun BasicCountdownTimer(passQuestion: () -> Unit,
                        time : Int) {
    var timeLeft by remember {
        mutableIntStateOf(time)
    }
    LaunchedEffect(key1 = timeLeft) {
        while (timeLeft > 0) {
            delay(1000L)
            timeLeft--
        }
    }
    if(timeLeft == 0) passQuestion()
    Text("$timeLeft",Modifier.padding(10.dp))
}
@Composable
fun PageStatistique(correctAnswers : Int,
                    questionsNumber : Int){
    Column(Modifier.padding(10.dp)) {
        Text("Resultat ", modifier = Modifier.padding(15.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Nombres de bonnes réponses !")
            Row {
                Text("$correctAnswers/$questionsNumber", modifier = Modifier.padding(10.dp))
            }
        }

        Row{
            //Text("Temps de réponse moyen : $avgTime",modifier = Modifier.padding(10.dp))
        }
    }
}

@Composable
fun BlankPage(){
    Column(horizontalAlignment = Alignment.CenterHorizontally){
        Text("Veuillez repasser dans quelques jours\n pour vous entrainer",Modifier.padding(15.dp))
        Text("Ou ajouter d'autres questions !",Modifier.padding(15.dp))
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun ScaffoldMemorisation( model: GererQuestionViewModel = viewModel(),
                          model2: ReglerNotifViewModel = viewModel(),
){
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }

    var selectedSubject by remember { mutableStateOf(Sujet(-1, "")) }
    var questions: MutableList<Question>

    var selectedAnswerList = remember { model.selectedAnswers }

    var answer by remember {
        mutableStateOf("")
    }
    var index by remember {
        mutableStateOf(0)
    }

    var questionRetrieve by remember {
        mutableStateOf(false)
    }
    var nextQuestion by remember { mutableStateOf(false) }
    val time = runBlocking {
        model2.defaultConfig.first()
    }

    var goodAnswers by remember {
        mutableStateOf(0)
    }



    Scaffold(
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
                    navigateTo = { navController.navigate("questions/$index"){popUpTo("start")}})
            }
            composable("questions/{ind}") {

                questions = model.questionList
                questionRetrieve = false

                val ind = it.arguments?.getString("ind")!!.toInt()

                if(questions.isNotEmpty() && questions.size > 1 && ind <= questions.size - 1){
                    if(questions[ind].qcm == 1){
                        model.questionID.value = questions[ind].idQuestion
                        model.retrieveChoices(questions[ind].idQuestion)
                        model.answerList = model.choiceFlow.collectAsState(listOf()).value.toMutableList()
                    }
                    QuestionDisplay(
                        question = questions[ind],
                        answer = answer,
                        changeAnswer = {answer =it},
                        time = time,
                        number = index + 1,
                        nextQuestion = {
                            if(model.checkAnswer(questions[ind].idQuestion,answer))
                                goodAnswers++
                            if(index <= questions.size - 1) {
                                NextQuestion(
                                    clearAnswer = { answer = "" },
                                    increaseIndex = { index++ },
                                    navigateTo = {
                                        navController.navigate("questions/$index") {
                                            popUpTo("start")
                                        }
                                    }
                                )
                            }
                        },
                        passQuestion = {nextQuestion = true},
                        answerList = model.answerList,
                        selectedAnswers = selectedAnswerList,
                        addMethod = {selectedAnswerList.add(it)},
                        deleteFromList = {selectedAnswerList.remove(it)}
                        )
                }else if(ind > questions.size - 1){
                    navController.navigate("stat")
                } else{
                    navController.navigate("noQuestions")
                }
            }
            composable("stat"){
                PageStatistique(goodAnswers,model.questionList.size)

            }
            composable("noQuestions"){
                BlankPage()
                }
            }
        }
    if(questionRetrieve) model.setQuestionList()

    if(nextQuestion){
        if(index <= model.questionList.size - 1) {
            NextQuestion(
                clearAnswer = { answer = "" },
                increaseIndex = { index++ },
                navigateTo = {
                    navController.navigate("questions/$index") {
                        popUpTo("start")
                    }
                }
            )
            println("INDEX : $index")
        }
        nextQuestion = false
    }

}


fun NextQuestion(
    clearAnswer : () -> Unit,
    increaseIndex : () -> Unit,
    navigateTo: () -> Unit){

    clearAnswer()
    increaseIndex()
    navigateTo()

}

@Preview(showBackground = true)
@Composable
fun SimpleQuestionPreview() {
    Projetmobile2023Theme {
        /*SimpleQuestion(
            "Android",
            "",
            {},
            1,
            {}
            )*/
    }
}