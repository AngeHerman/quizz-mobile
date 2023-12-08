package fr.uparis.nzaba.projetmobile2023

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
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
                QuestionButton { navController.navigate("edit") }
            }
            Reponse(answer = question.rep)
        }

    }
    @Composable
    fun QuestionButton(modifyPageRedirect : () -> Unit){
        Button(onClick = {modifyPageRedirect}){
            Text("Modifier")
        }
    }
    @Composable
    fun addQuestionButton(){
        Button(
            onClick = {},
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
                .fillMaxWidth()
                .padding(16.dp))
        {
            Text("Supprimer questions sélectionnés")
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
        println("QUESTIONS TAILLE :" + questions.size)
    }
    @OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
    @Composable
    fun SubjectsDropDownMenu(
        subjectList : List<Sujet>,
        selectedSubject : Sujet,
        changeSelectedSubject : (Sujet) -> Unit){

        var expanded by remember {mutableStateOf(false)}

        Spacer(Modifier.height(5.dp))

        ExposedDropdownMenuBox(
            expanded = expanded, onExpandedChange ={expanded = it},
            ) {
            TextField(
                value = selectedSubject.libelleSujet,
                onValueChange = {},
                readOnly = true)
            ExposedDropdownMenu(expanded = expanded , onDismissRequest = { expanded = false }) {
                subjectList.forEach(){
                    SubjectDropDownItem(selectedOption = {expanded = false;changeSelectedSubject(it);}, text = it.libelleSujet)
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

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ScaffoldQuestion(
        model: GererQuestionViewModel = viewModel(),
    ){
        val navController = rememberNavController()
        val snackbarHostState = remember { SnackbarHostState() }

        val sujets by model.sujetsFlow.collectAsState(listOf())
        var selectedSubject by remember {mutableStateOf(Sujet(3,""))}
        val questions by model.questionFlow.collectAsState(listOf())

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route


        (model::updateSubjectID)(selectedSubject.idSujet)

        Scaffold(
            topBar = {
                MyTopBar()},
            bottomBar = {
                MyBottomBar(
                    navController = navController,
                    navigateTo = {navController.navigate("l")},
                    navigateTo2 = {navController.navigate("edit")},
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

                                    SubjectsDropDownMenu(subjectList = sujets, selectedSubject) {
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
                                println("TEST1")

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
                { navigateTo2 },
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