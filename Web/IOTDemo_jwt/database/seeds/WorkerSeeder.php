<?php

use Illuminate\Database\Seeder;
use App\User;
use App\Worker;
class WorkerSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        $user = new User();
        $user->name = "worker1";
        $user->email = "w1@mail.com";
        $user->password = bcrypt('password');
        $user->save();

        $worker = new Worker();
        $worker->user_id = $user->id;
        $worker->name = "維修人員1";
        $worker->worker_type_id = 1;
        $user->worker()->save($worker);

        $user = new User();
        $user->name = "worker2";
        $user->email = "w2@mail.com";
        $user->password = bcrypt('password');
        $user->save();

        $worker = new Worker();
        $worker->user_id = $user->id;
        $worker->name = "維修人員2";
        $worker->worker_type_id = 2;
        $user->worker()->save($worker);

    }
}
