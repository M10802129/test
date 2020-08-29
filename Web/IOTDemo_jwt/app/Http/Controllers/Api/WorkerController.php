<?php

namespace App\Http\Controllers\Api;

use App\Worker;
use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use Tymon\JWTAuth\Facades\JWTAuth;
use Tymon\JWTAuth\Exceptions\JWTException;


class WorkerController extends Controller
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
    * @return mixed
    */
    public function index()
    {
        $tasks = $this->user->worker()->get(['worker_type', 'name'])->toArray();

        return $tasks;
    }

    /**
    * @param $id
    * @return \Illuminate\Http\JsonResponse
    */
    public function show($id)
    {
        $worker = $this->user->worker()->find($id);

        if (!$worker) {
            return response()->json([
                'success' => false,
                'message' => 'Sorry, task with id ' . $id . ' cannot be found.'
            ], 400);
        }

        return $worker;
    }

    /**
    * @param Request $request
    * @return \Illuminate\Http\JsonResponse
    * @throws \Illuminate\Validation\ValidationException
    */
    public function store(Request $request)
    {
        $this->validate($request, [
            'name' => 'required',
            'worker_type' => 'required',
        ]);

        $worker = new Worker();
        $worker->name = $request->name;
        $worker->worker_type = $request->worker_type;
        if($request->description){
            $worker->description = $request->description;
        }

        if ($this->user->worker()->save($worker))
            return response()->json([
                'success' => true,
                'worker' => $worker
            ]);
        else
            return response()->json([
                'success' => false,
                'message' => 'Sorry, worker could not be added.'
            ], 500);
    }

    /**
    * @param Request $request
    * @param $id
    * @return \Illuminate\Http\JsonResponse
    */
    public function update(Request $request, $id)
    {
        $worker = $this->user->worker()->find($id);

        if (!$worker) {
            return response()->json([
                'success' => false,
                'message' => 'Sorry, worker with id ' . $id . ' cannot be found.'
            ], 400);
        }

        $updated = $worker->fill($request->all())->save();

        if ($updated) {
            return response()->json([
                'success' => true
            ]);
        } else {
            return response()->json([
                'success' => false,
                'message' => 'Sorry, worker could not be updated.'
            ], 500);
        }
    }

    /**
    * @param $id
    * @return \Illuminate\Http\JsonResponse
    */
    public function destroy($id)
    {
        $worker = $this->user->worker()->find($id);

        if (!$worker) {
            return response()->json([
                'success' => false,
                'message' => 'Sorry, worker with id ' . $id . ' cannot be found.'
            ], 400);
        }

        if ($worker->delete()) {
            return response()->json([
                'success' => true
            ]);
        } else {
            return response()->json([
                'success' => false,
                'message' => 'Worker could not be deleted.'
            ], 500);
        }
    }
}
