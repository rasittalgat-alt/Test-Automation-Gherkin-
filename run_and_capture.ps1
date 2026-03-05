param(
  [switch]$Headless = $true
)

$ErrorActionPreference = 'Stop'
$projectRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $projectRoot

if (-not $env:JAVA_HOME) {
  $jdk = Get-ChildItem 'C:\Program Files\Eclipse Adoptium' -Directory -ErrorAction SilentlyContinue |
    Where-Object { $_.Name -like 'jdk-11*' } |
    Sort-Object Name -Descending |
    Select-Object -First 1
  if ($jdk) {
    $env:JAVA_HOME = $jdk.FullName
  }
}

if (-not $env:JAVA_HOME) {
  throw 'JAVA_HOME is not set. Install Java 11 first.'
}

if ($env:Path -notlike "*$($env:JAVA_HOME)\bin*") {
  $env:Path = "$($env:JAVA_HOME)\bin;" + $env:Path
}

Write-Host "Using JAVA_HOME: $env:JAVA_HOME"

$gradleArgs = @('clean', 'test')
if ($Headless) {
  $gradleArgs += '-Dheadless=true'
}

& .\gradlew.bat @gradleArgs

$report = Join-Path $projectRoot 'build\reports\cucumber\cucumber.html'
$screens = Join-Path $projectRoot 'build\reports\screenshots'

Write-Host "\nDone."
Write-Host "Cucumber report: $report"
Write-Host "Screenshots dir: $screens"
