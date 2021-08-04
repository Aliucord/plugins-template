# The plugins to build, or defaults to all plugins
param([String]$Plugins = "*")

if (-not(Get-Command "d8" -errorAction SilentlyContinue))
{
    Throw "Please add the Android SDK build tools to your path (Android/Sdk/build-tools/SOME_VERSION)"
}

Set-Location ../buildtool
Write-Output "Building plugin..."
./buildtool.exe -p "$plugins"
Set-Location ../buildsPlugins
if (-not(adb devices | findstr "\<device\>")) {
	Throw "No android device found. Connect to your phone via adb first"
}
Write-Output "Pushing plugin zip to device..."
if ($plugins -eq "*") {
	$files = Get-ChildItem . -Filter *.zip
	foreach ($f in $files) {
		adb push -- "$f" /storage/emulated/0/Aliucord/plugins
	}
}
else {
	adb push "$plugins.zip" /storage/emulated/0/Aliucord/plugins
}
Write-Output "Force stopping Aliucord..."
adb shell am force-stop com.aliucord
Write-Output "Launching Aliucord..."
adb shell monkey -p com.aliucord -c android.intent.category.LAUNCHER 1
Set-Location ../plugins
