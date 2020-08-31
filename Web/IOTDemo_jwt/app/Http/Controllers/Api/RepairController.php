<?php

namespace App\Http\Controllers\Api;

use Kreait\Firebase\Messaging\CloudMessage;
use Kreait\Firebase\Messaging\Notification;
use Illuminate\Http\Request;
use App\Http\Controllers\Controller;

class RepairController extends Controller
{
        /**
    * @param Request $request
    * @return \Illuminate\Http\JsonResponse
    * @throws \Illuminate\Validation\ValidationException
    */
    protected $messaging;
    
    public function __construct()
    {
        $this->messaging = app('firebase.messaging'); 
    }

    public function message($repairType)
    {
        $topic = "device-".$repairType;


        $message = CloudMessage::withTarget('topic', $topic)
                ->withNotification(Notification::fromArray([
                    'title' => 'deviceTopicNotification',
                    'body' => $topic." 發送了通知!",
                    'data' => []
                ]));
        
        $this->messaging->send($message);
        return response()->json([
            'success' => true,
            'topic' => $topic
        ]);
    }
}
