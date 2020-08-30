
<!DOCTYPE html>
<html lang="{{ str_replace('_', '-', app()->getLocale()) }}">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>

        <title>Laravel</title>
    </head>
    <body>
        <div id="container" style="display:flex">
            <div id="map_container">
                <canvas id="map_canvas"></canvas>
            </div>
            <div>
                通知維修類型A
                <br>
                <button onclick="askRepair('A')">通知</button>
                <br>
                通知維修類型B
                <br>
                <button onclick="askRepair('B')">通知</button>
                <br>
                通知維修類型C
                <br>
                <button onclick="askRepair('C')">通知</button>
                <br>
            </div>
        </div>
        <div id="worker_info"></div>
        <script src="{{ asset('/js/app.js') }}"></script>

        <script>
            var myView = document.getElementById('map_canvas');
            var canvas_container = document.getElementById('map_container');
            // 創一個 Render 自動判斷是否有 webGL
            const app = new PIXI.Application({
                width: 512, height: 512,
                resolution: window.devicePixelRatio || 1,
                view:myView
            });
            console.log(app.view)
            // 新增至頁面
            canvas_container.html = app.view;

            const container = new PIXI.Container();
            app.stage.addChild(container);

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
                    });
                    if(workerIndex !== -1){
                        workers[workerIndex] = e;
                        workers[workerIndex].expire_at = getTimestamp() + 10;
                    }else{
                        e.expire_at = getTimestamp() + 10;
                        workers.push(e);
                    }
                    // renderMap();
                }); 
            function renderMap() {
                //移除以前存在但現在不存在的worker
                var childRemove = [];
                container.children.forEach(function (s) {
                    var user_id = s.user_id;
                    
                    if(!user_id){
                        container.removeChild(s)
                    }
                    else if(!workers.find(el => el.worker.user_id === user_id)){
                        container.removeChild(s)
                    }
                });

                //移動或新增
                workers.forEach(function (worker) {
                    var x = parseInt(worker.posX);
                    var y = parseInt(worker.posY);
                    var sprite = container.children.find(el => el.user_id === worker.worker.user_id);
                    if(sprite){//移動
                        sprite.x = x;
                        sprite.y = y;
                    }else{
                        var color = getRandomColor()

                        var style = new PIXI.TextStyle({
                            fontSize: 16,
                            fill: color
                        });
                        var text = new PIXI.Text(worker.worker.name, style);
                        var bound = text.getLocalBounds()
                        text.x = -bound.width/2 + 5;
                        text.y = -20;
                        // text.user_id = worker.worker.user_id;


                        var texture = getNewTexture(color);
                        var circle = new PIXI.Sprite(texture);
                        circle.x = x;
                        circle.y = y;
                        circle.user_id = worker.worker.user_id;

                        //mouseover
                        circle.interactive = true;
                        circle.hitArea = new PIXI.Circle(0, 0, 20);
                        circle.mouseover = function(mouseData) {
                            setWorkerInfo(worker);
                        }
                        circle.mouseout = function(mouseData) {
                            setWorkerInfo();
                        }
                        circle.addChild(text);
                        container.addChild(circle);

                    }
                });
                
            }
            app.ticker.add((delta) => {
                var now = getTimestamp();
                workers.forEach(function(item, index, object) {
                    if (item.expire_at < now ) {
                        object.splice(index, 1);
                    }
                });
                renderMap();
            });

            function update(obj/*, …*/) {
                for (var i=1; i<arguments.length; i++) {
                    for (var prop in arguments[i]) {
                        var val = arguments[i][prop];
                        if (typeof val == "object") // this also applies to arrays or null!
                            update(obj[prop], val);
                        else
                            obj[prop] = val;
                    }
                }
                return obj;
            }

            function removeAllChild(){
                while(container.children[0]){
                    container.removeChild(container.children[0]); 
                }
            }
            function getTimestamp(){
                return Math.floor(Date.now() / 1000);
            }
            function getNewTexture(color) {
                var graphics = new PIXI.Graphics();
                // Set the fill color
                graphics.beginFill(color); // Red
                // Draw a circle
                graphics.drawCircle(0, 0, 5); // drawCircle(x, y, radius)
                // Applies fill to lines and shapes since the last call to beginFill.
                graphics.endFill();
                return app.renderer.generateTexture(graphics);
            }
            function getRandomColor() {
                return Math.random() * 0xFFFFFF << 0;
            }

            function setWorkerInfo(worker) {
                var workerInfo = $('#worker_info');
                if(worker){
                    // console.log(workerInfo)
                    var text = "worker:" + worker.worker.name + 
                               "<br>worker_type:" + worker.worker.worker_type_info.name + 
                               "<br>可維修項目:<br>";
                    var topics = worker.worker.topics
                    topics.forEach(function(el,i){
                        text += i + "." + el.topic_name + " ";
                    });
                    workerInfo.html(text);
                }else{
                    workerInfo.html("");
                }
            }
            
        </script>

    </body>
</html>