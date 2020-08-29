<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateWorkerTypesTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('worker_type_infos', function (Blueprint $table) {
            $table->bigIncrements('id');
            $table->string('name');
            $table->string('description')->nullable();
            $table->timestamps();
        });
        Schema::table('workers', function (Blueprint $table) {
            $table->foreign('worker_type_id')
                ->references('id')
                ->on('worker_type_infos');
        });
        Schema::table('worker_topics', function (Blueprint $table) {
            $table->foreign('worker_type_id')
                ->references('id')
                ->on('worker_type_infos');
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('worker_types');
    }
}
