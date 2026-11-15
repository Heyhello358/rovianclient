@echo off
title Rovian Client - setup and run
where node >nul 2>nul || (echo Install Node.js 20+ from https://nodejs.org & pause & exit /b 1)
where cargo >nul 2>nul || (echo Install Rust from https://rustup.rs & pause & exit /b 1)
if not exist node_modules call npm install || (pause & exit /b 1)
if not exist src-tauri\icons\icon.ico call npx tauri icon public\rovian-mark.svg
call npm run tauri:dev
pause
