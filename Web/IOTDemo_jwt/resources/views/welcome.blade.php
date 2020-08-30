
<!DOCTYPE html>
<html lang="{{ str_replace('_', '-', app()->getLocale()) }}">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">


        <title>Laravel</title>
    </head>
    <body>
        <script src="{{ asset('/js/app.js') }}"></script>

        <script>
            // 創一個 Render 自動判斷是否有 webGL
            var renderer = PIXI.autoDetectRenderer(512, 512, {
                antialias: true
            });

            // 新增至頁面
            document.body.appendChild(renderer.view);

            // 創建 Stage 
            var stage = new PIXI.Container();

            var workers = [];
            var sprites = [];
            Echo.channel('laravel_database_worker_position')
                .listen('WorkerPositionUpdateEvent', e => {
                    var workerIndex = workers.findIndex(item => {
                        if(item.worker){
                            if(item.worker.user_id === e.worker.user_id)
                                return true;
                            else
                                return false;
                        }
                        return false;
                    })
                    if(workerIndex !== -1){
                        workers[workerIndex] = e;
                    }else{
                        workers.push(e);
                    }
                    renderMap();
                }); 
            function renderMap() {
                // stage = new PIXI.Container();
                
                var worker = workers[0]; //debug
                var x = parseInt(worker.posX);
                var y = parseInt(worker.posY);
                console.log(worker['sprite_index'])
                console.log(worker)
                console.log(x, y)
                
                if(worker.sprite){
                    console.log("worker exists!!")

                    sprites[sprite_index].x = x;
                    sprites[sprite_index].y = y;
                }else{
                    console.log("new worker!!")

                    // Initialize the pixi Graphics class
                    var graphics = new PIXI.Graphics();
                    // Set the fill color
                    graphics.beginFill(0xe74c3c); // Red
                    // Draw a circle
                    graphics.drawCircle(x, y, 10); // drawCircle(x, y, radius)
                    // Applies fill to lines and shapes since the last call to beginFill.
                    graphics.endFill();
                    var texture = renderer.generateTexture(graphics);
                    var circle = new PIXI.Sprite(texture);
                    circle.x = x;
                    circle.y = y;
                    // var index = sprites.push(circle) -1 ;
                    worker.sprite = circle;
                    stage.addChild(circle);
                }
                animate();
            }
            function animate() {
                //Render the stage
                renderer.render(stage);
                // requestAnimationFrame(animate);
            }
        </script>

    </body>
</html>