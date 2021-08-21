// include(":MyFirstPlugin")

include(":CatApi")
project(":CatApi").projectDir = File("./ExamplePlugins/CatApi")

include(":HelloWorld")
project(":HelloWorld").projectDir = File("./ExamplePlugins/HelloWorld")

include(":HelloWorldAdvanced")
project(":HelloWorldAdvanced").projectDir = File("./ExamplePlugins/HelloWorldAdvanced")

include(":MyFirstPatch")
project(":MyFirstPatch").projectDir = File("./ExamplePlugins/MyFirstPatch")

rootProject.name = "AliucordPlugins"
