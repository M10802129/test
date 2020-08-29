<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class LoginInfo extends Model
{
    protected $table = 'login_infos';
    protected $fillable = [
        'device_token',
        'user_id'
    ];
}
