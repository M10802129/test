<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateWorkerTopicsTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('worker_topics', function (Blueprint $table) {
            $table->bigIncrements('id');
            $table->unsignedBigInteger('worker_type_id');
                
            $table->string('topic_name');
            $table->string('topic_path');
            $table->string('topic_description')->nullable();
            $table->unique([
                'worker_type_id',
                'topic_name'
            ]);
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('worker_topics');
    }
}
