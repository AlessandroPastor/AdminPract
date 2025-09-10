package com.example.adminmovile.presentation.navigation

object Routes {
    // Rutas base
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"

    const val ONBOARDING = "onboarding"
    const val DEVICE_INFO = "device_info"
    const val LAND_PAGE = "landpage"
    const val EXPLORATE = "explorate"

    const val UPDATE_PERFIL = "update_perfil"
    const val SERVICES = "services"
    const val PLACES = "places"
    const val PROGRESS = "estadistica"
    const val PRACTICE = "practicas"
    const val EVENTS = "events"
    const val RECOMMENDATIONS = "recommendations"


    object HomeScreen {
        private const val HOME_PREFIX = "/homeScreen"

        // Hijos de Configuracion (01)
        object Setup {
            private const val SETUP = "$HOME_PREFIX/setup"
            const val USUARIOS = "$SETUP/users"
            const val ROLE = "$SETUP/roles"
            const val SEPTIONS = "$SETUP/config"
            const val MODULE = "$SETUP/module"
            const val PARENT_MODULE = "$SETUP/parent-module"
        }

        // Hijos de Documentos y Trámite (02)
        object Docs {
            private const val DOCS = "$HOME_PREFIX/docs"
            const val INBOX = "$DOCS/inbox"
            const val TRAMITES = "$DOCS/tramites"
        }
        // Hijos de Seguimiento y Evaluación (03)
        object Seguimiento {
            private const val SEGUIMIENTO = "$HOME_PREFIX/seguimiento"
            const val INDICADORES = "$SEGUIMIENTO/indicadores"
            const val EVALUACIONES = "$SEGUIMIENTO/evaluaciones"
        }
    }
}