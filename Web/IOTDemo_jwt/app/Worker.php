<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Worker extends Model
{
    /**
     * @var string
     */
    protected $table = 'workers';

    /**
     * @var array
     */
    protected $guarded = [];
}
