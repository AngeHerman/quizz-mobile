package fr.uparis.nzaba.projetmobile2023

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.AndroidViewModel
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
import java.text.SimpleDateFormat
import java.util.Calendar

class GererQuestionsActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Projetmobile2023Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    EcranQuestion(calculateWindowSizeClass(this))
                }
            }
        }
    }
}

@Composable
fun EcranQuestion(size: WindowSizeClass, model: GererQuestionViewModel = viewModel()){
    when(size.widthSizeClass){
        WindowWidthSizeClass.Compact-> PageQuestionPortrait(model)
        else-> PageQuestionLandscape(model)
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageQuestionPortrait(model: GererQuestionViewModel = viewModel()){
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    NavigationDrawer(
        model = model,
        drawerState = drawerState,
        scope = scope,
        customComposable = { model, onMenuIconClick -> ComposableQuestionPortrait(model, onMenuIconClick) }
    )
}

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageQuestionLandscape(model: GererQuestionViewModel = viewModel()){
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    NavigationDrawer(
        model = model,
        drawerState = drawerState,
        scope = scope,
        customComposable = { model, onMenuIconClick -> ComposableQuestionLandscape(model, onMenuIconClick) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComposableQuestionPortrait(model: AndroidViewModel, onMenuIconClick: () -> Unit) {
    EcranGererQuestionsPortrait(model = model as GererQuestionViewModel, onMenuIconClick = onMenuIconClick)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComposableQuestionLandscape(model: AndroidViewModel, onMenuIconClick: () -> Unit) {
    //EcranGererQuestionsLandscape(model = model as GererQuestionViewModel, onMenuIconClick = onMenuIconClick)
    EcranGererQuestionsPortrait(
        model = model as GererQuestionViewModel,
        onMenuIconClick = onMenuIconClick
    )
}


    @SuppressLint("UnrememberedMutableState")
    @Composable
    fun UneQuestion(
        question: Question,
        navController: NavHostController,
        selectedQuestions: List<Question>,
        addMethod: (Question) -> Unit,
        deleteFromList: (Question) -> Unit,
        navigateToEditPage: (Question) -> Unit

    ) {

        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            )

        ) {
            QuestionContent(
                question,
                navController,
                selectedQuestions,
                addMethod,
                deleteFromList,
                navigateToEditPage
            )

        }
    }

    @Composable
    fun QuestionContent(
        question: Question,
        navController: NavHostController,
        selectedQuestions: List<Question>,
        addMethod: (Question) -> Unit,
        deleteFromList: (Question) -> Unit,
        navigateToEditPage: (Question) -> Unit
    ) {

        Column {
            Row {
                Text(text = "Question : \n ${question.texte}",modifier = Modifier.padding(5.dp),textAlign = TextAlign.Start)
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {

                        Row {
                            QuestionButton(
                                { navigateToEditPage(question) },
                                Modifier.padding(5.dp),
                                Icons.Default.Edit,
                                ""
                            )
                            Spacer(Modifier.width(5.dp))
                            SelectQuestionCheckBox(
                                question,
                                selectedQuestions,
                                addMethod,
                                deleteFromList
                            )
                        }
                }
            }
            Spacer(Modifier.padding(5.dp))
            Row {
                if(question.qcm == 0) DisplayText(text1= "Réponse :",text2 = question.rep)
                DisplayText(text1 = "Statut :", text2 = question.statut.toString() )
                DisplayText(text1 = "Prochaine date :", text2 =question.nextDate)

            }


        }

    }

    fun addQuestionToSelectedList(
        addToSelection: Boolean,
        question: Question,
        addMethod: (Question) -> Unit,
        deleteFromList: (Question) -> Unit
    ) {
        if (addToSelection) {
            addMethod(question)
        } else {
            deleteFromList(question)
        }
    }

    @Composable
    fun DisplayText(text1: String,text2 : String) {
            Text("$text1\n$text2",Modifier.padding(5.dp),textAlign = TextAlign.Start)
    }


    @Composable
    fun SelectQuestionCheckBox(
        question: Question,
        selectedQuestions: List<Question>,
        addMethod: (Question) -> Unit,
        deleteFromList: (Question) -> Unit
    ) {
        Checkbox(
            checked = selectedQuestions.contains(question),
            onCheckedChange = {
                addQuestionToSelectedList(
                    it,
                    question,
                    addMethod,
                    deleteFromList
                )
            });

    }

    @Composable
    fun QuestionButton(
        methodForButton: () -> Unit,
        modifier: Modifier,
        img: ImageVector,
        str: String
    ) {
        Button(
            onClick = methodForButton,
            modifier = modifier
        ) {
            Text(str)
            Icon(img, "icon")
        }
    }


    @Composable
    fun ListeQuestions(
        questions: List<Question>,
        navController: NavHostController,
        selectedQuestions: List<Question>,
        addMethod: (Question) -> Unit,
        deleteFromList: (Question) -> Unit,
        navigateToEditPage: (Question) -> Unit
    ) {

        LazyColumn(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(1f)
        ) {
            itemsIndexed(questions) {
                //index,item -> Text(item.texte)
                    index, item ->
                    UneQuestion(
                        question = item,
                        navController,
                        selectedQuestions,
                        addMethod,
                        deleteFromList,
                        navigateToEditPage
                    )


            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
    @Composable
    fun SubjectsDropDownMenu(
        subjectList: List<Sujet>,
        selectedSubject: Sujet,
        alignement: Alignment,
        changeSelectedSubject: (Sujet) -> Unit
    ) {

        var expanded by remember { mutableStateOf(false) }

        Spacer(Modifier.height(5.dp))
        Box(
            Modifier.fillMaxWidth(),
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
                            expanded = false;
                            changeSelectedSubject(it);
                        }, text = it.libelleSujet)
                    }
                }
            }
        }
    }

    @Composable
    fun SubjectDropDownItem(selectedOption: () -> Unit, text: String) {
        DropdownMenuItem(onClick = selectedOption) {
            Text(text)
        }
    }

    @Composable
    fun SubjectDropDownMenuForCreation(
        subjectList: List<Sujet>,
        selectedSubject: Sujet,
        changeSelectedSubject: (Sujet) -> Unit
    ) {
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = "Sujet : ", Modifier.padding(horizontal = 10.dp))
        Box(
            Modifier
                .padding(horizontal = 10.dp)
                .fillMaxWidth()
        ) {
            SubjectsDropDownMenu(
                subjectList = subjectList,
                selectedSubject = selectedSubject,
                changeSelectedSubject = changeSelectedSubject,
                alignement = Alignment.TopStart
            )
        }
    }

    @Composable
    fun EditingPage(
        qID : Int,
        qcm : Int,
        editQuestionText: (String) -> Unit,
        question: String,
        answer: String,
        editAnswer: (String) -> Unit,
        editQuestionFromDB: (Int) -> Unit
    ) {
        println("QUESTION IDDD : $qID")
        LazyColumn {
            item {
                TextfieldQuestion(text = "Question : ", addToString = editQuestionText, question)
                Spacer(modifier = Modifier.height(5.dp))
                if(qcm == 0) {
                    TextfieldQuestion(text = "Réponse : ", addToString = editAnswer, answer)
                    Spacer(modifier = Modifier.height(5.dp))
                }
                QuestionButton(
                    { editQuestionFromDB(qID) },
                    Modifier.padding(16.dp),
                    Icons.Default.Edit,
                    "Modifier la question"
                )
            }
        }

    }

    @Composable
    fun addQuestionButton(addQuestiontoDB: () -> Unit, addString: String) {
        Button(
            onClick = addQuestiontoDB,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(addString)
        }
    }

    @SuppressLint("UnrememberedMutableState")
    @Composable
    fun CreationPage(
        subjectList: List<Sujet>,
        selectedSubject: Sujet,
        answer: String,
        editAnswer: (String) -> Unit,
        question: String,
        editQuestionText: (String) -> Unit,
        addQuestiontoDB: () -> Unit,
        goToChoiceCreationPage: () -> Unit,
        changeSelectedSubject: (Sujet) -> Unit
    ) {

        var display by remember { mutableStateOf(false) }

        LazyColumn {
            item(1) {
                SubjectDropDownMenuForCreation(subjectList, selectedSubject, changeSelectedSubject)
                TextfieldQuestion(text = "Question : ", addToString = editQuestionText, question)
                CheckBoxQCM(display = display, changeDisplay = { display = !display })
            }

            if (!display) {
                item(2) {
                    TextfieldQuestion(text = "Réponse : ", addToString = editAnswer, answer)
                    Spacer(modifier = Modifier.height(5.dp))
                    addQuestionButton(addQuestiontoDB, "Ajouter Question")
                }
            } else {
                item(3) {
                    Spacer(modifier = Modifier.height(5.dp))

                    QuestionButton(
                        methodForButton = goToChoiceCreationPage,
                        modifier = Modifier
                            .padding(5.dp)
                            .fillMaxWidth(),
                        img = Icons.Default.Add, str = "Ajouter reponses"
                    )
                }
            }
        }
    }



    @Composable
    fun CheckBoxQCM(display: Boolean, changeDisplay: ((Boolean) -> Unit)?) {
        Row(Modifier.padding(10.dp)) {
            Text("QCM : ")
            Spacer(modifier = Modifier.width(5.dp))
            Checkbox(checked = display, onCheckedChange = changeDisplay)
        }
    }

    @Composable
    fun CheckBoxAnswerQCM(rightAnswer: Boolean, changeRightAnswer: ((Boolean) -> Unit)?) {
        Row(Modifier.padding(10.dp)) {
            Spacer(modifier = Modifier.width(5.dp))
            Checkbox(checked = rightAnswer, onCheckedChange = changeRightAnswer)
        }
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


    @Composable
    fun CreateChoicePage(
        createChoiceLeave: () -> Unit,
        createChoiceContinue: () -> Unit,
        editAnswer: (String) -> Unit,
        answer: String,
        rightAnswer: Boolean,
        changeRightAnswer: ((Boolean) -> Unit)?
    ) {
        Column {
            Box {
                Text("Question", Modifier.padding(5.dp))
            }
            Spacer(Modifier.height(5.dp))
            Row {
                Box {
                    TextfieldQuestion(text = "Réponse : ", addToString = editAnswer, answer)
                }
                Spacer(modifier = Modifier.height(5.dp))

            }
            Row {
                Text("Bonne réponse ?", Modifier.padding(5.dp), textAlign = TextAlign.Start)
                Box {
                    CheckBoxAnswerQCM(
                        rightAnswer = rightAnswer,
                        changeRightAnswer = changeRightAnswer
                    )
                }
            }
            Spacer(modifier = Modifier.height(5.dp))
            Row {
                QuestionButton(
                    methodForButton = createChoiceLeave,
                    modifier = Modifier.padding(1.dp),
                    img = Icons.Default.Done,
                    str = "Finir d'ajouter des réponses"
                )
                QuestionButton(
                    methodForButton = createChoiceContinue,
                    modifier = Modifier.padding(1.dp),
                    img = Icons.Default.Add,
                    str = "Ajouter réponse et continuer"
                )
            }

        }

    }

    @SuppressLint("UnrememberedMutableState")
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun EcranGererQuestionsPortrait(model: GererQuestionViewModel = viewModel(),onMenuIconClick: () -> Unit){
        val navController = rememberNavController()
        val snackbarHostState = remember { SnackbarHostState() }

        val sujets by model.sujetsFlow.collectAsState(listOf())
        var selectedSubject by remember { mutableStateOf(Sujet(-1, "")) }
        var selectedSubject2 by remember { mutableStateOf(Sujet(3, "")) }

        val questions by model.questionFlow.collectAsState(listOf())
        var qList: List<Question>

        var answer by remember { mutableStateOf("") }
        var questionText by remember { mutableStateOf("") }

        var selectedQuestions = model.selectedQuestion

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        var choiceAnswer by remember { mutableStateOf("") }
        var rightAnswer by remember { mutableStateOf(false) }

        var questionID by remember { mutableStateOf(0) }
        var questionAdd by remember {mutableStateOf(false)}
        var questionEdit by remember {mutableStateOf(false)}
        var questionDel by remember {mutableStateOf(false)}

        var choiceAdd by remember { mutableStateOf(false) }
        var continueAdd by remember { mutableStateOf(false) }

        var navigateTo by remember { mutableStateOf({})}
        var qcm by remember{ mutableStateOf(0)}
        var show by remember{ mutableStateOf(false) }

        (model::updateSubjectID)(selectedSubject.idSujet)

        Scaffold(
            topBar = {
                TopBarOther("Gérer les questions", onMenuIconClick)
            },
            bottomBar = {
                MyBotBar(
                    navController = navController,
                    navigateTo = { navController.navigate("l") { launchSingleTop = true } },
                    navigateTo2 = { navController.navigate("create") { popUpTo("l") } },
                    currentRoute = currentRoute
                )
            },

            snackbarHost = { SnackbarHost(snackbarHostState) })
        { paddingV ->

            NavHost(
                navController = navController,
                startDestination = "l",
                modifier = Modifier.padding(paddingV)
            ) {

                composable("l") {

                    Column {

                        SubjectsDropDownMenu(
                            subjectList = sujets,
                            selectedSubject,
                            Alignment.TopCenter
                        ) {
                            selectedSubject = it;
                            (model::updateSubjectID)(selectedSubject.idSujet)
                            (model.reloadQuestions())
                            show = true
                        }
                        QuestionButton(
                            { questionDel = true },
                            Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            Icons.Default.Delete,
                            "Supprimer les selectionnées"
                        )

                        qList = model.questionFlow.collectAsState(listOf()).value
                        if(show){
                            ListeQuestions(
                                questions = qList,
                                navController = navController,
                                selectedQuestions = selectedQuestions,
                                addMethod = { model.addQuestionToSelected(it) },
                                deleteFromList = { model.removeQuestionFromSelected(it) },
                                navigateToEditPage = {
                                    navController.navigate("modif/${it.idQuestion}/${it.qcm}") {
                                        popUpTo(
                                            "l"
                                        )
                                    }
                                })
                        }
                    }
                }

                composable("create") {
                    CreationPage(
                        sujets,
                        selectedSubject2,
                        answer,
                        { answer = it; },
                        questionText,
                        { questionText = it; },{
                            questionAdd = true
                            navigateTo = { navController.navigate("l"){launchSingleTop = true } }
                        }, {
                            questionAdd = true
                            qcm = 1
                            navigateTo = {navController.navigate("createChoice") { popUpTo("l") }}
                        }) {
                        selectedSubject2 = it
                        (model::updateSubjectIDCreation)(selectedSubject2.idSujet)
                    }
                }
                composable("modif/{id}/{qcm}") {

                    val id = it.arguments?.getString("id")!!.toInt()
                    val qcmv = it.arguments?.getString("qcm")!!.toInt()

                    EditingPage(
                        qID = id,
                        qcm = qcm,
                        editQuestionText = { questionText = it },
                        question = questionText,
                        answer = answer,
                        editAnswer = { answer = it }
                    ) {
                        when (qcm) {
                            1 -> navigateTo = {navController.navigate("createChoice") { popUpTo("l")}}
                            0 -> navigateTo = {navController.navigate("l") { launchSingleTop = true }}
                        }
                        questionID = id
                        qcm = qcmv
                        questionEdit = true
                    }
                }
                composable("createChoice") {
                    CreateChoicePage(
                        createChoiceLeave = {
                            choiceAdd = true
                            continueAdd = false
                        },
                        createChoiceContinue = {
                            choiceAdd = true
                            continueAdd = true
                        },
                        editAnswer = { choiceAnswer = it },
                        answer = choiceAnswer,
                        rightAnswer = rightAnswer,
                        changeRightAnswer = { rightAnswer = !rightAnswer })
                }
            }
        }

        if(questionDel){
            DeleteQuestion(
                model = model,
                backtoFalse = { questionDel = false },
                text ="Etes vous sûr de vouloir supprimer ?"
            )
        }

        if(questionEdit){
            model.deleteSingleQuestion(questionID)
            val q = Question(
                idQuestion = questionID,
                qcm = qcm,
                texte = questionText,
                rep = answer,
                statut = 1,
                nextDate = SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().time),
                idSujet = selectedSubject.idSujet
            )
            AddQuestion(
                model = model,
                navigateTo = navigateTo,
                q = q,
                displayText = "Question modifié",
                backtoFalse = {
                    questionEdit= false
                    questionID = it
                },
                clearFields = {
                    questionText = ""
                    answer = ""
                }
            )
        }
        if(questionAdd){
            val q = Question(
                qcm = qcm,
                texte = questionText,
                rep = answer,
                statut = 1,
                nextDate =SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().time),
                idSujet = selectedSubject2.idSujet
            )
            AddQuestion(
                model = model,
                navigateTo = navigateTo,
                q = q,
                displayText = "Question ajouté",
                backtoFalse = {
                    questionAdd = false
                    questionID = it
                },
                clearFields = {
                    questionText = ""
                    answer = ""
                }
            )
        }
        if(choiceAdd){
            AddChoice(
                model = model,
                navigateTo = {navController.navigate("l") { launchSingleTop = true }},
                choiceAnswer = choiceAnswer,
                rightAnswer = rightAnswer,
                continueAdd = continueAdd,
                clearFields = { choiceAnswer = ""
                    rightAnswer = false}

            ) {
                choiceAdd = false
            }
        }


    }

    @Composable
    fun DeleteQuestion(model : GererQuestionViewModel,
                       backtoFalse: () -> Unit,
                       text : String){
            ShowAlertDialog(
                text = text,
                confirmAlert = {
                    model.deleteQuestion();
                    backtoFalse()

                },
                cancelAlert = {
                    backtoFalse()
                })

    }
    @Composable
    fun AddChoice(
        model: GererQuestionViewModel,
        navigateTo: () -> Unit,
        choiceAnswer: String,
        rightAnswer: Boolean,
        continueAdd: Boolean,
        clearFields: () -> Unit,
        backtoFalse: () -> Unit
    ) {

        val context = LocalContext.current
        val choix = Choix(
            texte = choiceAnswer,
            bon = rightAnswer,
            idQuestion = model.questionID.value
        )

        if (choiceAnswer.isNotEmpty()) {
            model.addToChoiceList(choix)
            clearFields()
        }
        backtoFalse()

        if (!continueAdd) {
            model.fillDBwithChoiceList()
            println("SIZE : ${model.choiceList.toList().size} ")
            if (model.choiceList.toList().isEmpty()) {
                Toast.makeText(context, "Les choix ont été ajoutés !", Toast.LENGTH_SHORT).show()
                navigateTo()
            }

        }
    }
    @SuppressLint("UnrememberedMutableState")
    @Composable
    fun AddQuestion(
        model: GererQuestionViewModel,
        navigateTo: () -> Unit,
        q : Question,
        displayText : String,
        backtoFalse: (Int) -> Unit,
        clearFields: () -> Unit

        ) {

        val context = LocalContext.current
        var text = "..."

        if (q.rep == "" && q.qcm == 0 || q.texte == "") {
            text = "Veuillez remplir les champs"
            backtoFalse(-1)
        } else {
            model.addQuestion(q)

            if (model.questionID.value != -1) {
                text = displayText
                backtoFalse(q.idQuestion)
                clearFields()
                navigateTo()

            }
        }
        val toast = Toast.makeText(context, text, Toast.LENGTH_SHORT)
        toast.show()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ShowAlertDialog(
        text: String,
        confirmAlert: () -> Unit,
        cancelAlert: () ->  Unit
    ) {
            AlertDialog(
                onDismissRequest = cancelAlert,
                confirmButton = { Button(onClick = confirmAlert ){ Text("Confirmer") }},
                dismissButton = { Button(onClick = cancelAlert ){ Text("Annuler") }},
                text = { Text(text) }
            )


    }

    @Composable
    fun TopBar() {
        TopAppBar(
            title = {
                Text(
                    "Gérer les questions",
                    style = MaterialTheme.typography.displayMedium
                )
            },
            backgroundColor = MaterialTheme.colorScheme.primaryContainer
        )
    }

    @Composable
    fun MyBotBar(
        navController: NavHostController,
        navigateTo: () -> Unit,
        navigateTo2: () -> Unit,
        currentRoute: String?
    ) {
        BottomAppBar {
            MyBottomBarItem(
                navigateTo,
                currentRoute == "l",
                { Icon(Icons.Default.List, "liste") },
                this
            )
            MyBottomBarItem(
                navigateTo2,
                currentRoute == "edit",
                { Icon(Icons.Default.Add, "ajouter") },
                this
            )
        }

    }

    @Composable
    fun MyBottomBarItem(
        navigateTo: () -> Unit,
        routeCheck: Boolean,
        icon: @Composable () -> Unit,
        rowScope: RowScope
    ) {
        rowScope.BottomNavigationItem(
            selected = routeCheck,
            onClick = navigateTo,
            icon = icon
        )

    }



