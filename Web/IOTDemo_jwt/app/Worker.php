<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Worker extends Model
{
    /**
     * @var string
     */
    protected $table = 'workers';
    protected $hidden = [
        'created_at', 'updated_at'
    ];
    /**
     * @var array
     */
    protected $guarded = [];

    public function topics()
    {
        return $this->hasManyThrough(
            'App\WorkerTopic', 
            'App\Worker',
            'worker_type_id',
            'worker_type_id',
            'id'
        );
    }

    public function workerTypeInfo()
    {
        return $this->hasOne('App\WorkerTypeInfo', 'id', 'worker_type_id');
    }
}
