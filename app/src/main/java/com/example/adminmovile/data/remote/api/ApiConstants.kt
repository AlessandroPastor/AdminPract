package com.example.adminmovile.data.remote.api

object ApiConstants {
    //const val BASE_URL = "http://172.22.8.17:8000"
    const val BASE_URL = "http://192.168.1.40:8000" //PC PASTOR
    //const val BASE_URL = "http://192.172.2.101:8000" //PC JAMIL
    //const val BASE_URL = "http://192.177.1.13:8000" //PC CRISTIAN

    object Configuration {

        // ROLES
        const val ROLES = "$BASE_URL/api/role"
        const val ROLE_BY_ID = "$BASE_URL//apirole/{id}"
        const val ROLE_BY_MODULE = "$BASE_URL/api/role/module"

        // PARENTMODULE
        const val GET_PARENT_MODULE_BY_ID = "$BASE_URL/api/parent-module/{id}"
        const val UPDATE_PARENT_MODULE = "$BASE_URL/api/parent-module/{id}"
        const val DELETE_PARENT_MODULE = "$BASE_URL/api/parent-module/{id}"
        const val GET_PARENT_MODULE = "$BASE_URL/api/parent-module"
        const val CREATE_PARENT_MODULE = "$BASE_URL/api/parent-module"
        const val GET_PARENT_MODULE_LIST = "$BASE_URL/api/parent-module/list"
        const val GET_PARENT_MODULE_DETAIL_LIST = "$BASE_URL/api/parent-module/list-detail-module-list"

        // MODULES
        const val MODULES = "$BASE_URL/api/module"
        const val MODULE_BY_ID = "$BASE_URL/api/module/{id}"
        const val MODULE_SELECTED = "$BASE_URL/api/module/modules-selected/roleId/{roleId}/parentModuleId/{parentModuleId}"
        const val MENU_ENDPOINT = "$BASE_URL/api/module/menu"


        // AUTH
        const val LOGIN_ENDPOINT = "$BASE_URL/api/login"
        const val REGISTER_ENDPOINT = "$BASE_URL/api/register"
        const val UPDATE_PROFILE_ENDPOINT = "$BASE_URL/api/update-profile"
        const val UPLOAD_PHOTO_ENDPOINT = "$BASE_URL/api/upload-photo"

        //SERVICE
        const val SERVICE_ENDPOINT = "$BASE_URL/service"
        const val SERVICE_GET_BYID = "$BASE_URL/service/{id}"
        const val SERVICE_POST = "$BASE_URL/service"
        const val SERVICE_PUT = "$BASE_URL/service/{id}"
        const val SERVICE_DELETE = "$BASE_URL/service/{id}"

    }

}
