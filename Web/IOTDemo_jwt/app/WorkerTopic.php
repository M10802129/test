<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class WorkerTopic extends Model
{
        /**
     * @var string
     */
    protected $table = 'worker_topics';
    /**
     * @var array
     */
    protected $guarded = [];
    protected $fillable = [
        'worker_type_id',
        'topic_name',
        'topic_path',
        'topic_description'
    ];
    protected $hidden = [
        'laravel_through_key', 'created_at', 'updated_at'
    ];
    public function workers()
    {
        return $this->hasMany('App\Worker', 'worker_type_id', 'worker_type_id');
    }
}
