include(":HelloWorld")
rootProject.name = "AliucordPlugins"

include(":DiscordStubs")
project(":DiscordStubs").projectDir = File("../repo/DiscordStubs")

include(":Aliucord")
project(":Aliucord").projectDir = File("../repo/Aliucord")