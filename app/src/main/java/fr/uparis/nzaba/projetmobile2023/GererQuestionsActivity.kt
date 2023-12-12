package fr.uparis.nzaba.projetmobile2023

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import fr.uparis.nzaba.projetmobile2023.data.Choix
import fr.uparis.nzaba.projetmobile2023.data.Question
import fr.uparis.nzaba.projetmobile2023.data.Sujet
import fr.uparis.nzaba.projetmobile2023.model.GererQuestionViewModel
import fr.uparis.nzaba.projetmobile2023.ui.theme.Projetmobile2023Theme

class GererQuestionsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Projetmobile2023Theme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    EcranGererQuestions()

                }
            }
        }
    }

    @Composable
    fun EcranGererQuestions(model: GererQuestionViewModel = viewModel()){

        ScaffoldQuestion()

    }




    @Composable
    fun UneQuestion(question : Question,navController: NavHostController){
        Card(
            Modifier
                .fillMaxSize()
                .padding(10.dp)
        ){
            QuestionContent(question,navController)
        }
    }

    @Composable
    fun QuestionContent(question: Question, navController: NavHostController){
        Column{

            Row {
                Text(question.texte)
                Box(Modifier.fillMaxWidth(),contentAlignment = Alignment.CenterEnd) {
                    deleteQuestionButton()
                }


            }

            Box(
                contentAlignment = Alignment.BottomStart
            ) {
                Reponse(answer = question.rep)

            }
        }

    }

    @Composable
    fun Reponse(answer : String){
        Card {
            Text("Réponse :")
            Spacer(Modifier.width(5.dp))
            Text(answer,Modifier.padding(1.dp))
        }
    }

    @Composable
    fun addQuestionButton(addQuestiontoDB : () -> Unit){
        Button(
            onClick = addQuestiontoDB,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp))
        {
            Text("Ajouter Question")
        }
    }

    @Composable
    fun deleteQuestionButton(){
        Button(
            onClick = {},
            modifier = Modifier
                .padding(16.dp))
        {
            Icon(Icons.Default.Delete, "delete")

        }
    }


    @Composable
    fun ListeQuestions(questions : List<Question>,navController: NavHostController) {

        LazyColumn(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(1f)) {
            itemsIndexed(questions) {
                //index,item -> Text(item.texte)
                index,item -> UneQuestion(question = item,navController)
            }
        }
    }
    @OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
    @Composable
    fun SubjectsDropDownMenu(
        subjectList : List<Sujet>,
        selectedSubject : Sujet,
        alignement: Alignment,
        changeSelectedSubject : (Sujet) -> Unit){

        var expanded by remember {mutableStateOf(false)}

        Spacer(Modifier.height(5.dp))
        Box(Modifier.fillMaxWidth(),
            contentAlignment = alignement
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded, onExpandedChange = { expanded = it },
            ) {
                TextField(
                    value = selectedSubject.libelleSujet,
                    onValueChange = {},
                    readOnly = true
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    subjectList.forEach() {
                        SubjectDropDownItem(selectedOption = {
                            expanded = false;changeSelectedSubject(it);
                        }, text = it.libelleSujet)
                    }
                }
            }
        }
    }
    @Composable
    fun SubjectDropDownItem(selectedOption: () -> Unit, text: String){
        DropdownMenuItem(onClick =  selectedOption ) {
            Text(text)
        }
    }
    @Composable
    fun SubjectDropDownMenuForCreation(
        subjectList:List<Sujet>,
        selectedSubject: Sujet,
        changeSelectedSubject: (Sujet) -> Unit
    ){
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = "Sujet : ",Modifier.padding(horizontal = 10.dp))
        Box(
            Modifier
                .padding(horizontal = 10.dp)
                .fillMaxWidth()) {
            SubjectsDropDownMenu(
                subjectList = subjectList,
                selectedSubject = selectedSubject,
                changeSelectedSubject = changeSelectedSubject,
                alignement = Alignment.TopStart
            )
        }
    }
    @SuppressLint("UnrememberedMutableState")
    @Composable
    fun CreationPage(
        subjectList:List<Sujet>,
        selectedSubject: Sujet,
        answer : String,
        editAnswer : (String) -> Unit,
        question : String,
        editQuestionText : (String) -> Unit,
        answerList : MutableList<Choix>,
        addQuestiontoDB: () -> Unit,
        changeSelectedSubject: (Sujet) -> Unit
      ){

        var display by remember { mutableStateOf(false) }
        var count by remember { mutableStateOf(0) }

        LazyColumn{
            item(1) {
                SubjectDropDownMenuForCreation(subjectList, selectedSubject, changeSelectedSubject)
                TextfieldQuestion(text = "Question : ", addToString = editQuestionText ,question)
                CheckBoxQCM(display = display, changeDisplay = { display = !display })
            }

            item(2){
            if(!display){
                TextfieldQuestion(text = "Réponse : " , addToString = editAnswer,answer)
            }else{
                AnswerforQCM(count,answerList) { count++ }
                }
            }
            item(3) {
                Spacer(modifier = Modifier.height(5.dp))
                addQuestionButton(addQuestiontoDB)
            }

        }

    }
    @SuppressLint("UnrememberedMutableState")
    @Composable
    fun AnswerforQCM(
        count : Int,
        choiceList : MutableList<Choix>,
        increaseCount : () -> Unit,
        ){

        var b = mutableStateOf(false)
        Column {
                for (i in 1..count ){

                    var t = mutableStateOf("")

                    Row(){
                       TextfieldQuestion(text = "Réponse ${i}: " , addToString = {t.value = it},t.value)
                        //CheckBoxAnswerQCM(rightAnswer =, changeRightAnswer = {b })
                    }

                    var c = Choix(texte = t.value,bon = 1, idQuestion = 0)


                }
        }
        Spacer(modifier = Modifier.width(5.dp))
        Button(onClick = increaseCount) {
            Icon(Icons.Default.Add, "add")
        }


    }
    fun addtoAnswerList(
        choiceList : MutableList<Choix>,
        c : Choix
        ){
        choiceList.find { it.idChoix == c.idChoix }.apply {
            if(this != null) choiceList.remove(this)
        }
        choiceList.add(c)
    }
    @Composable
    fun CheckBoxQCM(display : Boolean, changeDisplay : ((Boolean) -> Unit)?){
        Row(Modifier.padding(10.dp)) {
            Text("QCM : ")
            Spacer(modifier = Modifier.width(5.dp))
            Checkbox(checked = display, onCheckedChange = changeDisplay)
        }
    }

    @Composable
    fun CheckBoxAnswerQCM(rightAnswer : Boolean, changeRightAnswer : ((Boolean) -> Unit)?){
        Row(Modifier.padding(10.dp)) {
            Spacer(modifier = Modifier.width(5.dp))
            Checkbox(checked = rightAnswer, onCheckedChange = changeRightAnswer)
        }
    }
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun DatePickerField(){

        //val context = LocalContext.current
        //DatePicker(context)

    }
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TextfieldQuestion(text : String, addToString: (String) -> Unit,value : String){
        Spacer(modifier = Modifier.height(5.dp))
        Box(
            Modifier
                .padding(10.dp)
                .fillMaxWidth()){

            Column {
                Text(text)
                Spacer(Modifier.width(5.dp))
                TextField(value = value, onValueChange = addToString)
            }
        }
    }

    @SuppressLint("UnrememberedMutableState")
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ScaffoldQuestion(
        model: GererQuestionViewModel = viewModel(),
    ){
        val navController = rememberNavController()
        val snackbarHostState = remember { SnackbarHostState() }

        val sujets by model.sujetsFlow.collectAsState(listOf())
        var selectedSubject by remember {mutableStateOf(Sujet(3,""))}
        var selectedSubject2 by remember {mutableStateOf(Sujet(3,""))}

        val questions by model.questionFlow.collectAsState(listOf())
        var answerList = mutableStateListOf<Choix>()

        var answer by remember { mutableStateOf("") }
        var questionText by remember { mutableStateOf("")}



        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route


        (model::updateSubjectID)(selectedSubject.idSujet)

        Scaffold(
            topBar = {
                MyTopBar()},
            bottomBar = {
                MyBottomBar(
                    navController = navController,
                    navigateTo = {navController.navigate("l"){launchSingleTop = true }},
                    navigateTo2 = {navController.navigate("edit"){popUpTo("l") }},
                    currentRoute = currentRoute
                )
            },

                snackbarHost = { SnackbarHost(snackbarHostState) })
                    { paddingV ->

                    NavHost(
                        navController = navController,
                        startDestination = "l",
                        modifier = Modifier.padding(paddingV)){

                            composable("l"){

                                Column {

                                    SubjectsDropDownMenu(subjectList = sujets, selectedSubject,Alignment.TopCenter) {
                                        selectedSubject = it;
                                        (model::updateSubjectID)(selectedSubject.idSujet)
                                    }

                                    (model.reloadQuestions().collectAsState(listOf()).also {
                                        val questions by it
                                        ListeQuestions(questions = questions, navController = navController)
                                    })

                                    ListeQuestions(questions = questions, navController = navController)

                                }

                            }

                            composable("edit"){
                                CreationPage(
                                    sujets,
                                    selectedSubject2,
                                    answer,
                                    {
                                        answer = it;
                                        (model::addAnswer)(answer);
                                    },
                                    questionText,
                                    {
                                        questionText = it;
                                        (model::addQuestionText)(questionText)
                                    },
                                    answerList, {

                                        (model::addQuestion)()

                                        for (i in 0..answerList.size){

                                            (model::createQuestionChoice)(model.questionID.value)
                                        }

                                    },{
                                    selectedSubject2 = it;
                                    (model::updateSubjectIDCreation)(selectedSubject2.idSujet)})

                            }
                    }


        }
    }

    @Composable
    fun MyTopBar(){
        TopAppBar(title = {
            Text("Gérer les questions",
                style = MaterialTheme.typography.displayMedium) },
                backgroundColor = MaterialTheme.colorScheme.primaryContainer)
    }

    @Composable
    fun MyBottomBar(
        navController: NavHostController,
        navigateTo: () -> Unit,
        navigateTo2: () -> Unit,
        currentRoute: String?){
        BottomAppBar {
            MyBottomBarItem(
                navigateTo,
                currentRoute == "l",
                { Icon(Icons.Default.List, "liste") },
                this)
            MyBottomBarItem(
                 navigateTo2 ,
                currentRoute == "edit",
                { Icon(Icons.Default.Add, "ajouter") },
                this)
        }

    }
    @Composable
    fun MyBottomBarItem(
        navigateTo: () -> Unit,
        routeCheck: Boolean,
        icon: @Composable ()->Unit,
        rowScope: RowScope
    ){
        rowScope.BottomNavigationItem(
            selected = routeCheck,
            onClick = navigateTo ,
            icon = icon)

    }
}