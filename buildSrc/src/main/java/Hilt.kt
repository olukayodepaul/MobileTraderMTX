object Hilt {

    private const val hilt_dagger_version = "2.28-alpha"
    const val hilt_dagger = "com.google.dagger:hilt-android:${hilt_dagger_version}"

    private const val hilt_compiler_versioin = "2.28-alpha"
    const val hilt_compiler = "com.google.dagger:hilt-android-compiler:${hilt_compiler_versioin}"

    private const val hilt_lifecycle_viewmodel = "1.0.0-alpha01"
    const val hilt_viewModel = "androidx.hilt:hilt-lifecycle-viewmodel:${hilt_lifecycle_viewmodel}"
    const val hilt_viewModel_kapt = "androidx.hilt:hilt-compiler:$hilt_lifecycle_viewmodel"

    //classPath
    private const val hilt_version = "2.28-alpha"
    const val hilt_gradle_plugin = "com.google.dagger:hilt-android-gradle-plugin:${hilt_version}"

}