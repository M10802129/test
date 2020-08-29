<?php

use Illuminate\Database\Seeder;
use App\WorkerTopic;
class WorkerTopicSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        $topic = new WorkerTopic();
        $topic->worker_type_id = 1;
        $topic->topic_name = "維修類型A";
        $topic->topic_path = "device/A";
        $topic->topic_description = "工人類型A/維修類型A 推播topic";
        $topic->save();

        $topic = new WorkerTopic();
        $topic->worker_type_id = 1;
        $topic->topic_name = "維修類型B";
        $topic->topic_path = "device/B";
        $topic->topic_description = "工人類型A/維修類型B 推播topic";
        $topic->save();

        $topic = new WorkerTopic();
        $topic->worker_type_id = 2;
        $topic->topic_name = "維修類型B";
        $topic->topic_path = "device/B";
        $topic->topic_description = "工人類型B/維修類型B 推播topic";
        $topic->save();

        $topic = new WorkerTopic();
        $topic->worker_type_id = 2;
        $topic->topic_name = "維修類型C";
        $topic->topic_path = "device/C";
        $topic->topic_description = "工人類型B/維修類型C 推播topic";
        $topic->save();
    }
}
