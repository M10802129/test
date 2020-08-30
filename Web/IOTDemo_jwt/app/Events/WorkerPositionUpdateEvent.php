<?php

namespace App\Events;

use Illuminate\Broadcasting\Channel;
use Illuminate\Broadcasting\InteractsWithSockets;
use Illuminate\Broadcasting\PresenceChannel;
use Illuminate\Broadcasting\PrivateChannel;
use Illuminate\Contracts\Broadcasting\ShouldBroadcast;
use Illuminate\Foundation\Events\Dispatchable;
use Illuminate\Queue\SerializesModels;

class WorkerPositionUpdateEvent implements ShouldBroadcast
{
    use Dispatchable, InteractsWithSockets, SerializesModels;

    public $posX = 0;
    public $posY = 0;
    public $deviceToken = "";
    public $worker = [];

    /**
     * Create a new event instance.
     *
     * @return void
     */
    public function __construct($worker, $posX, $posY, $deviceToken)
    {
        $this->posX = $posX;
        $this->posY = $posY;
        $this->deviceToken = $deviceToken;
        $this->worker = $worker;
    }

    /**
     * Get the channels the event should broadcast on.
     *
     * @return \Illuminate\Broadcasting\Channel|array
     */
    public function broadcastOn()
    {
        // return new PrivateChannel('worker_position');
        return new Channel('worker_position');
    }
}
