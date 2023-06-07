package Services

import Authentication.LoginFragment
import Authentication.RegisterFragment
import ProjectForm.ProjectForm
import com.example.opsc7311_poe.ViewProject

class FragmentFactory {
    fun getProjectForm():ProjectForm {
        return ProjectForm.newInstance()
    }
    fun getTaskForm() {

    }

    fun getProjectView(): ViewProject {
        return ViewProject.newInstance()
    }

    fun getLogin(): LoginFragment {
        return LoginFragment.newInstance()
    }

    fun getRegister(): RegisterFragment {
        return  RegisterFragment.newInstance()
    }
}