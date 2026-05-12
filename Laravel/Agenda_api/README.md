## nose
php artisan install:api

php artisan make:model Persona -mcrsf --api

composer require firebase/php-jwt

JWT_SECRET=secret
JWT_ALGORITHM=HS256

php artisan make:middleware JwtMiddleware


php artisan make:controller LoginController

DB_CONNECTION=mysql
DB_HOST=127.0.0.1
DB_PORT=3306
DB_DATABASE=bd_agenda_api
DB_USERNAME=root
DB_PASSWORD=

php artisan key:generate

php artisan migrate --seed
php artisan migrate:fresh --seed
