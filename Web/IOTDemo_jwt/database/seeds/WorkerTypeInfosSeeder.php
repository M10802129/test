<?php

use Illuminate\Database\Seeder;
use App\WorkerTypeInfo;
class WorkerTypeInfosSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        $workerType = new WorkerTypeInfo();
        $workerType->name = "類型A";
        $workerType->description = "類型A的工人";
        $workerType->save();

        $workerType = new WorkerTypeInfo();
        $workerType->name = "類型B";
        $workerType->description = "類型B的工人";
        $workerType->save();
    }
}
