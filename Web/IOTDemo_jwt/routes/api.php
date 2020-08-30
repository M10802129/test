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

Route::post('/auth/register', 'Api\AuthController@register');

Route::group([
    'middleware' => 'api',
    'prefix' => 'auth'
], function () {
    Route::post('login', 'Api\AuthController@login');
    Route::post('logindevice', 'Api\AuthController@loginDevice');
    Route::post('logout', 'Api\AuthController@logout');
    Route::post('refresh', 'Api\AuthController@refresh');
    Route::post('user', 'Api\AuthController@me');


});

Route::group([
    'middleware' => 'api',
    'prefix' => 'worker'
], function () {
    Route::get('', 'Api\WorkerController@index');
    Route::post('set', 'Api\WorkerController@store');
    Route::group([
        'prefix' => 'position'
    ], function () {
        Route::post('set', 'Api\PositionController@store');
        
    });
});
