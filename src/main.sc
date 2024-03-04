require: functions.js
require: slotfilling/slotFilling.sc
  module = sys.zb-common
theme: /

    state: Start
        q!: $regex</start>
        a: Начнём.

    state: Hello
        intent!: /привет
        a: Привет привет

    state: Bye
        intent!: /пока
        a: Пока пока

    state: GetWeather
        intent!: /geo
        script:
            var city = $caila.inflect($parseTree._geo, ["nomn"]);
            openWeatherCurrentMap("metric", "ru", city).then(function (res) {
                    if (res && res.weather) {
                        $reactions.answer("Сегодня в городе " + capitalize(city) + " " + res.weather[0].description + ", " + Math.round(res.main.temp) + "°C");
                        if (res.weather[0].main == 'Rain' || res.weather[0].main == 'Drizzle' || res.weather[0].main == 'CLouds') {
                            $reactions.answer("Советую захватить с собой зонтик!");
                        }
                        else if (res.wather[0].temp < 0){
                            $reactions.answer("Брр, Ну и мороз! Одевайтесь теплее!");
                        }
                    }
                    else {
                        $reactions.answer("Что-то сервер барахлит. Не могу узнать погоду");
                    }
                }
            ).catch(function (err) {
                    $reactions.answer("Что-то сервер барахлит. Не могу узнать погоду");
                }
            );

    state: NoMatch
        event!: noMatch
        a: Я не понял. Вы сказали: {{$request.query}}

    state: Match
        event!: match
        a: {{$context.intent.answer}}