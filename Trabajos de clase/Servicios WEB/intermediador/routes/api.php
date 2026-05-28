<?php

use App\Http\Controllers\IntermediadorController;
use Illuminate\Support\Facades\Route;

Route::post('/login', [IntermediadorController::class, 'login']);
Route::post('/transaccion', [IntermediadorController::class, 'transaccion']);