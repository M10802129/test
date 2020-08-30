
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
        Echo.channel('laravel_database_worker_position')
            .listen('WorkerPositionUpdateEvent', e => {
                console.log(e)
            })
    </script>

    </body>
</html>