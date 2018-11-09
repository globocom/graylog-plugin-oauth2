#!/bin/bash

@echo off
for %%F in (*.java) do (
  echo Adding header text to C file %%F
  type gpl.txt "%%F" > "%%~nF.temp"
  rename "%%F" "%%~nF.bak"
  rename "%%~nF.temp" "%%F"
)
