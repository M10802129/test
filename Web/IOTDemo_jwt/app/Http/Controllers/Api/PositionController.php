<?php

namespace App\Http\Controllers\Api;


use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use Tymon\JWTAuth\Facades\JWTAuth;
use Tymon\JWTAuth\Exceptions\JWTException;
use App\Events\WorkerPositionUpdateEvent;

class PositionController extends Controller
{
    /**
     * @var
     */
    protected $user;

    /**
     * WorkerController constructor.
     */
    public function __construct()
    {
        $this->user = JWTAuth::parseToken()->authenticate();
    }

        /**
    * @param Request $request
    * @return \Illuminate\Http\JsonResponse
    * @throws \Illuminate\Validation\ValidationException
    */
    public function store(Request $request)
    {
        $this->validate($request, [
            'device_token' => 'required',
            'pos_x' => 'required',
            'pos_y' => 'required'
        ]);
        
        $worker = $this->user->worker;
        $worker->workerTypeInfo;
        $worker->topics;
        $posX = $request->input('pos_x');
        $posY = $request->input('pos_y');
        $deviceToken = $request->input('device_token');
        // error_log($this->user->worker);
        event(new WorkerPositionUpdateEvent($worker, $posX, $posY, $deviceToken));
        return response()->json([
            'success' => true
        ]);

    }
}
