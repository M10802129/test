<?php

namespace App\Http\Controllers\Api;

use App\User;
use App\LoginInfo;
use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use Tymon\JWTAuth\Facades\JWTAuth;
use Tymon\JWTAuth\Exceptions\JWTException;
use App\Http\Requests\RegisterFormRequest;
use Illuminate\Support\Facades\Auth;

class AuthController extends Controller
{
    /**
     * @var bool
     */
    public $loginAfterSignUp = true;

    public function __construct()
    {
        $this->middleware('auth:api', [
            'except' => [
                'register',
                'login',
                'loginDevice'
            ]
        ]);
    }
    public function login(Request $request)
    {
        $input = $request->only('email', 'password');
        $token = null;

        if (!$token = auth('api')->attempt($input)) {
            return response()->json([
                'success' => false,
                'message' => 'Invalid Email or Password',
            ], 401);
        }

        return $this->respondWithToken($token);
    }
    public function loginDevice(Request $request)
    {
        $input = $request->only('email', 'password');
        $deviceToken = $request->input('device_token');
        // error_log('device token '.$deviceToken);
        $token = null;

        if (!$token = auth('api')->attempt($input)) {
            return response()->json([
                'success' => false,
                'message' => 'Invalid Email or Password',
            ], 401);
        }
        
        $user = auth('api')->user();  

        //先找是否原本有登入的使用者
        $loginInfo = LoginInfo::where("device_token", $deviceToken)->first();
        if($loginInfo !== null){
            if($loginInfo->user_id !== $user->id){
                $loginInfo->device_token = "";
                $loginInfo->save();
            }
        }

        $loginInfo = LoginInfo::where("user_id", $user->id)->first();
        if($loginInfo !== null){
            $loginInfo->device_token = $deviceToken;
            $loginInfo->save();
        }else{
            LoginInfo::updateOrCreate([
                'user_id' => $user->id,
                'device_token' => $deviceToken,
            ]);
        }
        $resp = [
            'success' => true,
            'access_token' => $token,
            'token_type' => 'bearer',
            'expires_in' => auth('api')->factory()->getTTL() * 60,
            'user' => $user,
            'device_token' => $deviceToken
        ];
        if($user->worker){
            // $resp['worker'] = $user->worker
            // $user->worker->workerTypeInfo();
            error_log($user->worker->workerTypeInfo);
            error_log($user->worker->topics);
        }
        return response()->json($resp);
    }
    /**
     * @param Request $request
     * @return \Illuminate\Http\JsonResponse
     * @throws \Illuminate\Validation\ValidationException
     */
    public function logout()
    {
        try {
            // JWTAuth::invalidate($request->token);
            auth('api')->logout();
            return response()->json([
                'success' => true,
                'message' => 'User logged out successfully'
            ]);
        } catch (JWTException $exception) {
            return response()->json([
                'success' => false,
                'message' => 'Sorry, the user cannot be logged out'
            ], 500);
        }
    }

    /**
     * @param RegisterFormRequest $request
     * @return \Illuminate\Http\JsonResponse
     */
    public function register(RegisterFormRequest $request)
    {
        $user = new User();
        $user->name = $request->name;
        $user->email = $request->email;
        $user->password = bcrypt($request->password);
        $user->save();

        if ($this->loginAfterSignUp) {
            return $this->login($request);
        }

        return response()->json([
            'success'   =>  true,
            'data'      =>  $user
        ], 200);
    }

    public function refresh()
    {
        if(!$token = auth('api')->refresh()){
            return response()->json([
                'success' => false,
                'message' => 'cant refresh token',
            ], 401);
        }
        return $this->respondWithToken($token);
    }

    /**
     * Get the authenticated User.
     *
     * @return \Illuminate\Http\JsonResponse
     */
    public function me()
    {
        return response()->json(auth('api')->user());
    }

    protected function respondWithToken($token)
    {
        return response()->json([
            'success' => true,
            'access_token' => $token,
            'token_type' => 'bearer',
            'expires_in' => auth('api')->factory()->getTTL() * 60,
            'user' => auth('api')->user()
        ]);
    }
}