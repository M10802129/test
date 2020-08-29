<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;

use Tymon\JWTAuth\Facades\JWTAuth;
/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/

Route::post('login', 'Api\AuthController@login');
Route::post('register', 'Api\AuthController@register');

Route::group(['middleware' => 'auth.jwt'], function () {
    Route::get('logout', 'Api\AuthController@logout');

    Route::post('/worker/set', 'Api\WorkerController@store');
    Route::get('/worker', 'Api\WorkerController@index');

});

// Route::middleware('auth:api')->get('/user', function (Request $request) {
//     return $request->user();
// });
