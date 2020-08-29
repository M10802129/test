<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class WorkerTypeInfo extends Model
{
    protected $table = 'worker_type_infos';
    protected $hidden = [
        'created_at', 'updated_at'
    ];
    protected $guarded = [];
}
